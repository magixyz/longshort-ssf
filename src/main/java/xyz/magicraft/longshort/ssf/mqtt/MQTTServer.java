package xyz.magicraft.longshort.ssf.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;

@Component
public class MQTTServer {


	private NioEventLoopGroup bossGroup;

	private NioEventLoopGroup workGroup;
	
	@Autowired
	private MQTTInboundHandler mqttInboundHandler;
	

	/**
	 * 	启动服务
	 */
	public void startup(int port) {

			try {
				bossGroup = new NioEventLoopGroup(1);
				workGroup = new NioEventLoopGroup();

				ServerBootstrap bootstrap = new ServerBootstrap();
				bootstrap.group(bossGroup, workGroup);
				bootstrap.channel(NioServerSocketChannel.class);

				bootstrap.option(ChannelOption.SO_REUSEADDR, true)
						.option(ChannelOption.SO_BACKLOG, 1024)
						.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
						.option(ChannelOption.SO_RCVBUF, 10485760);

				bootstrap.childOption(ChannelOption.TCP_NODELAY, true)
						.childOption(ChannelOption.SO_KEEPALIVE, true)
						.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

				bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
							protected void initChannel(SocketChannel ch) {
								ChannelPipeline channelPipeline = ch.pipeline();
								// 设置读写空闲超时时间
								channelPipeline.addLast(new IdleStateHandler(600, 600, 1200));
								channelPipeline.addLast("encoder", MqttEncoder.INSTANCE);
								channelPipeline.addLast("decoder", new MqttDecoder());
//								channelPipeline.addLast(inbounds.myBean());
								channelPipeline.addLast(mqttInboundHandler);
								
								
								
//								 BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) ServerApplication.context.getBeanFactory();
//								 beanFactory.registerBeanDefinition(null, null);
							}
						});
				ChannelFuture f = bootstrap.bind(port).sync();
				if(f.isSuccess()){
					System.out.println("startup success port = " + port);
//				f.channel().closeFuture().sync();
				} else {
					System.out.println("startup fail port = " + port);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

 

	}

	/**
	 * 	关闭服务
	 */
	public void shutdown() throws InterruptedException {
		if (workGroup != null && bossGroup != null) {
			bossGroup.shutdownGracefully().sync();
			workGroup.shutdownGracefully().sync();
			System.out.println("shutdown success");
		}
	}
	
}
