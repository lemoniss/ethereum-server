package com.maxlength.aggregate.service;

import com.maxlength.component.TransUtils;
import com.maxlength.spec.enums.Confirm;
import com.maxlength.spec.vo.Signer;
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
import org.web3j.abi.datatypes.generated.Uint8;

@Service
@Transactional
public class SignerServiceImpl implements SignerService {

    private TransUtils transUtils;

    public SignerServiceImpl(TransUtils transUtils) {
        this.transUtils = transUtils;
    }

    @Override
    public Signer.getRequestConfirm getRequestConfirm(String address, Confirm confirmType) throws Exception {

        Token.txCountResponse txCount = transUtils.txCount(address);

        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        TypeReference<DynamicArray<Uint8>> enabledConfirms = new TypeReference<>() {};
        TypeReference<Address> removeSignerAddr = new TypeReference<>() {};
        outputParameters.add(enabledConfirms);
        outputParameters.add(removeSignerAddr);

        String functionName = "getRequestConfirm";

        Token.txRequest txData = Token.txRequest.builder()
            .address(address)
            .nonce(txCount.getNonce())
            .functionName(functionName)
            .inputParameters(inputParameters)
            .outputParameters(outputParameters)
            .build();

        List<Type> res = transUtils.ethCallTx(txData);

        List<Confirm> requestConfirms = new ArrayList<>();

        List<Uint8> confirmTypes = (List<Uint8>) res.get(0).getValue();

        for(int i= 0; i< confirmTypes.size(); i++) {
            BigInteger confirm = confirmTypes.get(i).getValue();

            switch (confirm.intValue()) {
                case 0: requestConfirms.add(Confirm.TRANSFER);
                    break;
                case 1: requestConfirms.add(Confirm.MINT);
                    break;
                case 2: requestConfirms.add(Confirm.BURN);
                    break;
                case 3: requestConfirms.add(Confirm.ADD);
                    break;
                case 4: requestConfirms.add(Confirm.REMOVE);
                    break;
            }
        }
        return Signer.getRequestConfirm.builder()
            .confirmTypes(requestConfirms)
            .removeAddress(res.get(1).getValue().toString().equals("0x0000000000000000000000000000000000000000") ? "" : res.get(1).getValue().toString())
            .build();

    }

    @Override
    public Token.transactionReceipt agreeSign(Signer.agreeSign request) throws Exception {

        Token.txCountResponse txCount = transUtils.txCount(request.getAddress());

        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Uint8(request.getConfirmType().getConfirmValue()));
        inputParameters.add(new Address(request.getTargetAddress()));

        List<TypeReference<?>> outputParameters = new ArrayList<>();

        String functionName = "agreeSign";

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

