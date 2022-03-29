package com.maxlength.event.producer;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class TestMessageProducer {
//
//    @Autowired
//    private KafkaTemplate<String, TestVO> testKafkaTemplate;
//
//    public TestMessageProducer(KafkaTemplate<String, TestVO> testKafkaTemplate) {
//        this.testKafkaTemplate = testKafkaTemplate;
//    }
//
//    public void sendTestVO(TestVO message) {
//
//        testKafkaTemplate.send("tokenDepisitBatchResponse-in-0", message);
//
//    }
//}
