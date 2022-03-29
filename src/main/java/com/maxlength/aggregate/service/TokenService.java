package com.maxlength.aggregate.service;


import com.maxlength.spec.vo.Token;

public interface TokenService {

    Token.transactionHash transfer(Token.transferRequest request) throws Exception;

    Token.transactionReceipt delegateTransfer(Token.transferRequest request) throws Exception;

    Token.balanceOf getBalanceOf(String address, int scale) throws Exception;
}
