package com.maxlength.aggregate.service;


import com.maxlength.spec.vo.Wallet;

public interface WalletService {

    Wallet.response createWallet(Wallet.create request) throws Exception;

}
