package com.maxlength.aggregate.service;

import com.maxlength.spec.vo.Event;
import com.maxlength.spec.vo.Token;
import java.math.BigInteger;
import java.util.List;
import reactor.core.publisher.Mono;

public interface EventService {

    Mono<Token.transactionHash> write(Event.writeRequest request, String contractAddress) throws Exception;

    Token.transactionReceipt receipt(String transactionHash) throws Exception;

    List<Event.getEventList> list(String eventName, String contractAddress, BigInteger blockNumber);

    Event.getEventDetail detail(Event.detailRequest request);
}
