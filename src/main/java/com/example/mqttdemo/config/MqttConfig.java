package com.example.mqttdemo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class MqttConfig {

    @Value ("${mqtt.host}")
    public String host;
    @Value ("${mqtt.username}")
    public String username;
    @Value ("${mqtt.password}")
    public String password;
    @Value ("${mqtt.connectionTimeout:10}")
    public int connectionTimeout;
    @Value ("${mqtt.keepAliveInterval:20}")
    public int keepAliveInterval;
    @Value ("${mqtt.cleanSession:true}")
    public Boolean cleanSession;
    @Value ("${mqtt.automaticReconnect:true}")
    public Boolean automaticReconnect;


}
