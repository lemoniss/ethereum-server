package com.maxlength.event.consumer;
//
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.function.Consumer;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.integration.IntegrationMessageHeaderAccessor;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageHeaders;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class TestMessageConsumer {
//
//
//    @Bean
//    public Consumer<Message<TestVO>> test1() {
//        return message -> {
//
//            TestVO testMessage = message.getPayload();
//            MessageHeaders messageHeaders = message.getHeaders();
//            log.info("TestMessage {} \n and  received from bus. topic: {}, partition: {}, offset: {}, deliveryAttempt: {}", testMessage.toString(),
//                messageHeaders.get(KafkaHeaders.RECEIVED_TOPIC, String.class), messageHeaders.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class),
//                messageHeaders.get(KafkaHeaders.OFFSET, Long.class), messageHeaders.get(IntegrationMessageHeaderAccessor.DELIVERY_ATTEMPT, AtomicInteger.class));
//
//        };
//    }
//}
