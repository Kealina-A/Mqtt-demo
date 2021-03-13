package com.example.mqttdemo.util;


import com.example.mqttdemo.model.BusniessHandle;
import com.example.mqttdemo.config.MqttConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class Subscriber {

    private final MqttUtil mqttUtil;
    private final MqttConfig mqttConfig;
    private final BusniessHandle busniessHandle;

    public boolean register(String clientId, String topic) {
        try {
            log.info("注册订阅者,clientId:[{}],topic:[{}]",clientId,topic);
            // MemoryPersistence设置clientid的保存形式，默认为以内存保存
            MqttClient mqttClient = new MqttClient(mqttConfig.getHost(), clientId, new MemoryPersistence());
            MqttConnectOptions options = mqttUtil.getConnection();
            mqttClient.connect(options);
            startSubscribe(mqttClient,clientId,topic);
            return true;
        } catch (Exception e) {
            log.error("",e);
            return false;
        }
    }

    private void startSubscribe(MqttClient mqttClient,String clientId,String topic) {
        // 使用MqttCallbackExtended是为了能处理重新连接mqtt之后的处理，它与MqttCallback的区别就是多了一个connectComplete方法，为了处理重连后的逻辑。
        // 设置了automaticReconnect为true后程序会重新连接，但是之前订阅的客户端帮你重新订阅，所以要自己再执行下重新订阅的逻辑
        mqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectionLost(Throwable throwable) {
                log.error("connectionLost; clientId:[{}],topic:[{}]",clientId,topic,throwable);
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                log.info("接收到消息 Topic: [{}], Message: [{}]",topic,mqttMessage.toString());
                String message = mqttMessage.toString();
                //　这里最好处理下异常，如果有异常就会跳到上面那个方法上
                busniessHandle.handleBusiness(topic,message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                log.info("发送完成；[{}]",iMqttDeliveryToken);
            }

            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                log.info("连接完成，clientId:[{}],topic:[{}]",clientId,topic);
                startSubscribe(mqttClient,clientId,topic);
            }

        });
        log.info("开始订阅 clientId:[{}],topic:[{}]",clientId,topic);
        subscribe(mqttClient,clientId,topic);
    }

    public boolean subscribe(MqttClient mqttClient,String clientId,String topic) {
        try {
            mqttClient.subscribe(topic,1);
            return true;
        } catch (Exception e) {
            log.error("订阅失败 topic:[{}]",topic);
            if (mqttClient.isConnected()) {
                log.info("重新订阅... clientId:[{}],topic:[{}]",clientId,topic);
                startSubscribe(mqttClient,clientId,topic);
            }
        }
        return false;
    }

}
