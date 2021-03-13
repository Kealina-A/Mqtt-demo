package com.example.mqttdemo.model;

import com.example.mqttdemo.util.Sender;
import com.example.mqttdemo.util.Subscriber;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class Controller {

    private final Subscriber subscriber;
    private final Sender sender;

    @GetMapping("/register")
    public void register() {
        subscriber.register("sub_1","topic");
    }

    @GetMapping("/send")
    public void send() {
        sender.send("pub_1","topic","hello world");
    }
}
