package com.maxlength.aggregate.service;

import com.maxlength.spec.enums.Confirm;
import com.maxlength.spec.vo.Signer;
import com.maxlength.spec.vo.Token;

public interface SignerService {

    Signer.getRequestConfirm getRequestConfirm(String address, Confirm confirmType) throws Exception;

    Token.transactionReceipt agreeSign(Signer.agreeSign request) throws Exception;
}
