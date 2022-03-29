package com.maxlength.aggregate.service;

import com.maxlength.component.TransUtils;
import com.maxlength.spec.vo.Token;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;

@Service
public class TokenServiceImpl implements TokenService {

    private TransUtils transUtils;

    public TokenServiceImpl(TransUtils transUtils) {
        this.transUtils = transUtils;
    }

    @Override
    public Token.transactionHash transfer(Token.transferRequest request) throws Exception {
        return transUtils.erc20Transfer(request);
    }

    @Override
    public Token.transactionReceipt delegateTransfer(Token.transferRequest request) throws Exception {
        return transUtils.erc20DelegateTransfer(request);
    }

    @Override
    public Token.balanceOf getBalanceOf(String address, int scale) throws Exception {

        Token.txCountResponse txCount = transUtils.txCount(address);

        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(address));

        List<TypeReference<?>> outputParameters = new ArrayList<>();
        TypeReference<Uint256> typeReferenceUint256 = new TypeReference<>() {};
        outputParameters.add(typeReferenceUint256);

        String functionName = "balanceOf";

        Token.txRequest txData = Token.txRequest.builder()
            .address(address)
            .nonce(txCount.getNonce())
            .functionName(functionName)
            .inputParameters(inputParameters)
            .outputParameters(outputParameters)
            .build();

        List<Type> res = transUtils.ethCallTx(txData);

        return Token.balanceOf.builder()
            .amount(transUtils.devideTokenDecimals((BigInteger) res.get(0).getValue(), scale))
            .build();
    }

}

