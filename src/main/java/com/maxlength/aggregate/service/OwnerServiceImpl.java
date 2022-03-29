package com.maxlength.aggregate.service;

import com.maxlength.component.TransUtils;
import com.maxlength.spec.common.BaseException;
import com.maxlength.spec.enums.Confirm;
import com.maxlength.spec.enums.Flag;
import com.maxlength.spec.vo.Owner;
import com.maxlength.spec.vo.Token;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;

@Service
@Transactional
public class OwnerServiceImpl implements OwnerService {

    private TransUtils transUtils;

    public OwnerServiceImpl(TransUtils transUtils) {
        this.transUtils = transUtils;
    }

    @Override
    public List<Owner.getSigners> getSigners(String address) throws Exception {

        Token.txCountResponse txCount = transUtils.txCount(address);

        List<Type> inputParameters = new ArrayList<>();

        List<TypeReference<?>> outputParameters = new ArrayList<>();
        TypeReference<DynamicArray<Address>> typeReference = new TypeReference<>() {};
        outputParameters.add(typeReference);

        String functionName = "getSigners";

        Token.txRequest txData = Token.txRequest.builder()
            .address(address)
            .nonce(txCount.getNonce())
            .functionName(functionName)
            .inputParameters(inputParameters)
            .outputParameters(outputParameters)
            .build();

        List<Type> res = transUtils.ethCallTx(txData);

        List<Owner.getSigners> returnList = new ArrayList<>();

        for(int i= 0; i< res.size(); i++) {

            for(Address ad: (List<Address>) res.get(i).getValue()) {
                Owner.getSigners obj = Owner.getSigners.builder()
                    .signerAddress(ad.getValue())
                    .build();
                returnList.add(obj);
            }
        }

        return returnList;

    }

    @Override
    public List<Owner.getSigners> getConfirmSigners(String address, Confirm confirmType) throws Exception {

        Token.txCountResponse txCount = transUtils.txCount(address);

        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Uint8(confirmType.getConfirmValue()));

        List<TypeReference<?>> outputParameters = new ArrayList<>();
        TypeReference<DynamicArray<Address>> typeReference = new TypeReference<>() {};
        outputParameters.add(typeReference);

        String functionName = "getConfirmSigners";

        Token.txRequest txData = Token.txRequest.builder()
            .address(address)
            .nonce(txCount.getNonce())
            .functionName(functionName)
            .inputParameters(inputParameters)
            .outputParameters(outputParameters)
            .build();

        List<Type> res = transUtils.ethCallTx(txData);

        List<Owner.getSigners> returnList = new ArrayList<>();

        for(int i= 0; i< res.size(); i++) {

            for(Address ad: (List<Address>) res.get(i).getValue()) {
                Owner.getSigners obj = Owner.getSigners.builder()
                    .signerAddress(ad.getValue())
                    .build();
                returnList.add(obj);
            }
        }


        return returnList;
    }

    @Override
    public Token.transactionReceipt mintBurn(Owner.requestMintBurn request, Confirm confirmType) throws Exception {

        Token.txCountResponse txCount = transUtils.txCount(request.getAddress());
        BigInteger sendBalance = transUtils.multiplyTokenDecimals(request.getAmount());

        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(request.getTargetAddress()));
        inputParameters.add(new Uint256(sendBalance));

        List<TypeReference<?>> outputParameters = new ArrayList<>();

        String functionName = confirmType.name().toLowerCase();

        Token.txRequest txData = Token.txRequest.builder()
            .address(request.getAddress())
            .nonce(txCount.getNonce())
            .functionName(functionName)
            .inputParameters(inputParameters)
            .outputParameters(outputParameters)
            .build();

        return transUtils.ethSendTx(txData);
    }

    @Override
    public Token.transactionReceipt requestConfirm(Owner.requestConfirm request, Flag flag) throws Exception {

        Token.txCountResponse txCount = transUtils.txCount(request.getAddress());

        String functionName;

        if(flag.equals(Flag.REQUEST)) {
            functionName = "requestConfirm";
        } else if(flag.equals(Flag.CANCEL)) {
            functionName = "cancelConfirm";
        } else {
            throw new BaseException("서명요청, 서명취소요청만 실행할 수 있습니다.");
        }

        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Uint8(request.getConfirmType().getConfirmValue()));

        List<TypeReference<?>> outputParameters = new ArrayList<>();

        Token.txRequest txData = Token.txRequest.builder()
            .address(request.getAddress())
            .nonce(txCount.getNonce())
            .functionName(functionName)
            .inputParameters(inputParameters)
            .outputParameters(outputParameters)
            .build();

        return transUtils.ethSendTx(txData);
    }

}

