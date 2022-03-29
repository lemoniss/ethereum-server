package com.maxlength.aggregate.service;

import com.maxlength.component.TransUtils;
import com.maxlength.spec.vo.Event;
import com.maxlength.spec.vo.Token;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    private TransUtils transUtils;

    public EventServiceImpl(TransUtils transUtils) {
        this.transUtils = transUtils;
    }

    @Override
    public Mono<Token.transactionHash> write(Event.writeRequest request, String contractAddress) throws Exception {

        Token.txCountResponse txCount = transUtils.txCount(transUtils.corpCredentials().getAddress());

        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(transUtils.corpCredentials().getAddress()));
        inputParameters.add(new Utf8String(request.getContents()));

        List<TypeReference<?>> outputParameters = new ArrayList<>();
        TypeReference<Bool> typeReferenceBool = new TypeReference<>() {};
        outputParameters.add(typeReferenceBool);

        Token.txRequest txData = Token.txRequest.builder()
            .address(transUtils.corpCredentials().getAddress())
            .nonce(txCount.getNonce())
            .functionName(request.getFunctionName())
            .inputParameters(inputParameters)
            .outputParameters(outputParameters)
            .build();

        return Mono.just(transUtils.ethSendSignedTx(txData));
    }

    @Override
    public Token.transactionReceipt receipt(String txHash) throws Exception {
        return transUtils.txReceipt(txHash);
    }

    @Override
    public List<Event.getEventList> list(String eventName, String contractAddress, BigInteger blockNumber) {
        return transUtils.ethEventList(eventName, contractAddress, blockNumber);
    }

    @Override
    public Event.getEventDetail detail(Event.detailRequest request) {
        return transUtils.ethEventDetail(request);
    }

}

