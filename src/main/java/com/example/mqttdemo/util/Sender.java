package com.example.mqttdemo.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class Sender {

    private final MqttUtil mqttUtil;

    public void send(String clientId, String topicName, String msg) {
        try {
            MqttConnectOptions options = mqttUtil.getConnection();
            MqttClient client = mqttUtil.getClient(clientId);
            MqttTopic topic = client.getTopic(topicName);
            client.connect(options);
            MqttMessage message = buildMessage(msg);
            MqttDeliveryToken token = topic.publish(message);
            token.waitForCompletion();
            log.info("send success ,clientId:[{}], topic:[{}], msg: [{}] ", clientId, topic, msg);

            client.disconnect();
            client.close();
        } catch (Exception e) {
            log.info("fail to send ,clientId:[{}], topic:[{}], msg:[{}] ", clientId, topicName, msg, e);
        }
    }

    public MqttMessage buildMessage(String msg) {
        MqttMessage message = new MqttMessage();
        message.setQos(1);
        message.setRetained(false);
        message.setPayload(msg.getBytes());
        return message;
    }
}
