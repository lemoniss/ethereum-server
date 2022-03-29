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
//public class TokenMessageProducer {
//
//    @Autowired
//    private KafkaTemplate<String, BlockChainReceive> tokenKafkaTemplate;
//
//    public TokenMessageProducer(KafkaTemplate<String, BlockChainReceive> tokenKafkaTemplate) {
//        this.tokenKafkaTemplate = tokenKafkaTemplate;
//    }
//
//    public void send(BlockChainReceive message) {
//        tokenKafkaTemplate.send("tokenDepisitBatchResponse-in-0", message);
//    }
//}
