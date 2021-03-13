package com.example.mqttdemo.model;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class BusniessHandle {


    public void handleBusiness(String topic, String message) {
        log.info("处理业务逻辑... topic:[{}],message:[{}]",topic,message);
    }
}
