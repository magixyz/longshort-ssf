package xyz.magicraft.longshort.ssf.mqtt;

import java.util.ArrayList;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttConnAckVariableHeader;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttConnectVariableHeader;
import io.netty.handler.codec.mqtt.MqttFixedHeader;

import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttSubAckMessage;
import io.netty.handler.codec.mqtt.MqttSubAckPayload;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import io.netty.handler.codec.mqtt.MqttUnsubscribeMessage;
import io.netty.handler.codec.mqtt.MqttUnsubAckMessage;

@Component
public class MQTTMessageBacker {

	private static final Logger log =  LoggerFactory.getLogger(MQTTMessageBacker.class);
	
	@Autowired
	IBizHandler bizHandler;

	/**
	 * 	确认连接请求
	 * @param channel
	 * @param mqttMessage
	 */
	public void connack (Channel channel, MqttMessage mqttMessage) {
		MqttConnectMessage mqttConnectMessage = (MqttConnectMessage) mqttMessage;
		MqttFixedHeader mqttFixedHeaderInfo = mqttConnectMessage.fixedHeader();
		MqttConnectVariableHeader mqttConnectVariableHeaderInfo = mqttConnectMessage.variableHeader();
		
		//	构建返回报文， 可变报头
		MqttConnAckVariableHeader mqttConnAckVariableHeaderBack = new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, mqttConnectVariableHeaderInfo.isCleanSession());
		//	构建返回报文， 固定报头
		MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.CONNACK,mqttFixedHeaderInfo.isDup(), MqttQoS.AT_MOST_ONCE, mqttFixedHeaderInfo.isRetain(), 0x02);
		//	构建CONNACK消息体
		MqttConnAckMessage connAck = new MqttConnAckMessage(mqttFixedHeaderBack, mqttConnAckVariableHeaderBack);
		log.info("back--"+connAck.toString());
		channel.writeAndFlush(connAck);
	}
	
	/**
	 * 	根据qos发布确认
	 * @param channel
	 * @param mqttMessage
	 */
	public void puback (Channel channel, MqttMessage mqttMessage) {
		MqttPublishMessage mqttPublishMessage = (MqttPublishMessage) mqttMessage;
		
		
		MqttFixedHeader mqttFixedHeaderInfo = mqttPublishMessage.fixedHeader();
		MqttQoS qos = (MqttQoS) mqttFixedHeaderInfo.qosLevel();
        
		bizHandler.publish(mqttPublishMessage);

        



        
//        System.out.println(influxDB.queryGps()); 
        

        switch (qos) {
	        case AT_MOST_ONCE: 		//	至多一次
	            break;
	        case AT_LEAST_ONCE:		//	至少一次
	    		//	构建返回报文， 可变报头
	    		MqttMessageIdVariableHeader mqttMessageIdVariableHeaderBack = MqttMessageIdVariableHeader.from(mqttPublishMessage.variableHeader().packetId());
	    		//	构建返回报文， 固定报头
	    		MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.PUBACK,mqttFixedHeaderInfo.isDup(), MqttQoS.AT_MOST_ONCE, mqttFixedHeaderInfo.isRetain(), 0x02);
	    		//	构建PUBACK消息体
	    		MqttPubAckMessage pubAck = new MqttPubAckMessage(mqttFixedHeaderBack, mqttMessageIdVariableHeaderBack);
	    		log.info("back--"+pubAck.toString());
	    		channel.writeAndFlush(pubAck);
	            break;
	        case EXACTLY_ONCE:		//	刚好一次
	            //	构建返回报文， 固定报头
	        	MqttFixedHeader mqttFixedHeaderBack2 = new MqttFixedHeader(MqttMessageType.PUBREC,false, MqttQoS.AT_LEAST_ONCE,false,0x02);
	            //	构建返回报文， 可变报头
	            MqttMessageIdVariableHeader mqttMessageIdVariableHeaderBack2 = MqttMessageIdVariableHeader.from(mqttPublishMessage.variableHeader().packetId());
	            MqttMessage mqttMessageBack = new MqttMessage(mqttFixedHeaderBack2,mqttMessageIdVariableHeaderBack2);
	    		log.info("back--"+mqttMessageBack.toString());
	    		channel.writeAndFlush(mqttMessageBack);
	            break;
			default:
				break;
        }
	}
	
	/**
	 * 	发布完成 qos2 
	 * @param channel
	 * @param mqttMessage
	 */
	public void pubcomp (Channel channel, MqttMessage mqttMessage) {
        MqttMessageIdVariableHeader messageIdVariableHeader = (MqttMessageIdVariableHeader) mqttMessage.variableHeader();       
        //	构建返回报文， 固定报头
    	MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.PUBCOMP,false, MqttQoS.AT_MOST_ONCE,false,0x02);
        //	构建返回报文， 可变报头
        MqttMessageIdVariableHeader mqttMessageIdVariableHeaderBack = MqttMessageIdVariableHeader.from(messageIdVariableHeader.messageId());
        MqttMessage mqttMessageBack = new MqttMessage(mqttFixedHeaderBack,mqttMessageIdVariableHeaderBack);
		log.info("back--"+mqttMessageBack.toString());
		channel.writeAndFlush(mqttMessageBack);
	}
	
	/**
	 * 	订阅确认
	 * @param channel
	 * @param mqttMessage
	 */
	public void suback(Channel channel, MqttMessage mqttMessage) {
		MqttSubscribeMessage mqttSubscribeMessage = (MqttSubscribeMessage) mqttMessage;
		MqttMessageIdVariableHeader messageIdVariableHeader = mqttSubscribeMessage.variableHeader(); 
		//	构建返回报文， 可变报头
		MqttMessageIdVariableHeader variableHeaderBack = MqttMessageIdVariableHeader.from(messageIdVariableHeader.messageId());
		Set<String> topics = mqttSubscribeMessage.payload().topicSubscriptions().stream().map(mqttTopicSubscription -> mqttTopicSubscription.topicName()).collect(Collectors.toSet());
		
		log.info(topics.toString());
		
		bizHandler.subscribe(channel,topics);
		
		
		List<Integer> grantedQoSLevels = new ArrayList<>(topics.size());
		for (int i = 0; i < topics.size(); i++) {
			grantedQoSLevels.add(mqttSubscribeMessage.payload().topicSubscriptions().get(i).qualityOfService().value());
		}
		//	构建返回报文	有效负载
		MqttSubAckPayload payloadBack = new MqttSubAckPayload(grantedQoSLevels);
		//	构建返回报文	固定报头
		MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_MOST_ONCE, false, 2+topics.size());
		//	构建返回报文	订阅确认
		MqttSubAckMessage subAck = new MqttSubAckMessage(mqttFixedHeaderBack,variableHeaderBack, payloadBack);
		log.info("back--"+subAck.toString());
		channel.writeAndFlush(subAck);
	}
	
	/**
	 * 	取消订阅确认
	 * @param channel
	 * @param mqttMessage
	 */
	public void unsuback(Channel channel, MqttMessage mqttMessage) {
		
		MqttUnsubscribeMessage mqttUnsubscribeMessage = (MqttUnsubscribeMessage) mqttMessage;
		
		MqttMessageIdVariableHeader messageIdVariableHeader = (MqttMessageIdVariableHeader) mqttMessage.variableHeader(); 
		//	构建返回报文	可变报头
		MqttMessageIdVariableHeader variableHeaderBack = MqttMessageIdVariableHeader.from(messageIdVariableHeader.messageId());
		//	构建返回报文	固定报头
		MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.UNSUBACK, false, MqttQoS.AT_MOST_ONCE, false, 2);
		//	构建返回报文	取消订阅确认
		MqttUnsubAckMessage unSubAck = new MqttUnsubAckMessage(mqttFixedHeaderBack,variableHeaderBack);
		log.info("back--"+unSubAck.toString());
		channel.writeAndFlush(unSubAck);
		
		
		List<String> topics = mqttUnsubscribeMessage.payload().topics();
		
		
		bizHandler.unsubscribe(channel, topics);
		
		
	}
	
	/**
	 * 	心跳响应
	 * @param channel
	 * @param mqttMessage
	 */
	public void pingresp (Channel channel, MqttMessage mqttMessage) {
		//	心跳响应报文	11010000 00000000  固定报文
		MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0);
		MqttMessage mqttMessageBack = new MqttMessage(fixedHeader);
		log.info("back--"+mqttMessageBack.toString());
		channel.writeAndFlush(mqttMessageBack);
	}
	

}
