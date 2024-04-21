package xyz.magicraft.longshort.ssf.mqtt;

import java.util.List;
import java.util.Set;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

public interface IBizHandler {
	void subscribe(Channel channel,Set<String> topics);
	
	void unsubscribe(Channel channel,List<String> topics);
	
	void publish(MqttPublishMessage mqttPublishMessage);

}
