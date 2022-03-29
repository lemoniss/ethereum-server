package com.maxlength.aggregate.service;

import com.maxlength.spec.enums.Confirm;
import com.maxlength.spec.enums.Flag;
import com.maxlength.spec.vo.Owner;
import com.maxlength.spec.vo.Owner.getSigners;
import com.maxlength.spec.vo.Token;
import java.util.List;

public interface OwnerService {

    List<getSigners> getSigners(String address) throws Exception;

    List<getSigners> getConfirmSigners(String address, Confirm confirmType) throws Exception;

    Token.transactionReceipt mintBurn(Owner.requestMintBurn request, Confirm confirmType) throws Exception;

    Token.transactionReceipt requestConfirm(Owner.requestConfirm request, Flag flag) throws Exception;
}
