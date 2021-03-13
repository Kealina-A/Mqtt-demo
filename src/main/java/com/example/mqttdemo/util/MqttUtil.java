package com.example.mqttdemo.util;

import com.example.mqttdemo.config.MqttConfig;
import lombok.AllArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MqttUtil {

    private final MqttConfig mqttConfig;

    public MqttConnectOptions getConnection () {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(mqttConfig.getCleanSession());
        options.setUserName(mqttConfig.getUsername());
        options.setPassword(mqttConfig.getPassword().toCharArray());
        options.setConnectionTimeout(mqttConfig.getConnectionTimeout());
        options.setKeepAliveInterval(mqttConfig.getKeepAliveInterval());
        options.setAutomaticReconnect(mqttConfig.getAutomaticReconnect());
        try {
            if (mqttConfig.getHost().startsWith("ssl")) {
                options.setSocketFactory(SslUtil.getSocketFactory());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return options;
    }

    public MqttClient getClient(String clientId) throws MqttException {
        return new MqttClient(mqttConfig.host, clientId, new MemoryPersistence());
    }



}
