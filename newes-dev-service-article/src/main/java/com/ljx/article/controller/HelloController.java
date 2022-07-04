package com.ljx.article.controller;

import com.ljx.api.config.RabbitMQDelayConfig;
import com.ljx.grace.result.GraceJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("producer")
public class HelloController  {

    final static Logger logger = LoggerFactory.getLogger(HelloController.class);
    //@Autowired
    //private RabbitTemplate rabbitTemplate;
    @GetMapping("/hello")
    public GraceJSONResult hello(){
        return GraceJSONResult.ok();
    }


    @GetMapping("/delay")
    public Object delay() {
//
//    MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
//        @Override
//        public Message postProcessMessage(Message message) throws AmqpException {
//            // 设置消息的持久
//            message.getMessageProperties()
//                    .setDeliveryMode(MessageDeliveryMode.PERSISTENT);
//            // 设置消息延迟的时间，单位ms毫秒
//            message.getMessageProperties()
//                    .setDelay(5000);
//            return message;
//        }
//    };
//
//    rabbitTemplate.convertAndSend(
//            RabbitMQDelayConfig.EXCHANGE_DELAY,
//            "publish.delay.do",
//            "这是一条延迟消息~~",
//            messagePostProcessor);
//
//    System.out.println("生产者发送的延迟消息：" + new Date());

    return "OK";
}

}
