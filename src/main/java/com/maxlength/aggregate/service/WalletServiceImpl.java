package com.maxlength.aggregate.service;

import com.maxlength.aggregate.entity.AccountEntity;
import com.maxlength.aggregate.entity.WalletEntity;
import com.maxlength.aggregate.repository.AccountRepository;
import com.maxlength.aggregate.repository.WalletRepository;
import com.maxlength.component.TransUtils;
import com.maxlength.spec.common.BaseException;
import com.maxlength.spec.vo.Wallet;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WalletServiceImpl implements WalletService {

    private final TransUtils transUtils;
    private final AccountRepository accountRepository;
    private final WalletRepository walletRepository;

    public WalletServiceImpl(TransUtils transUtils, AccountRepository accountRepository, WalletRepository walletRepository) {
        this.transUtils = transUtils;
        this.accountRepository = accountRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    public Wallet.response createWallet(Wallet.create request) throws Exception {

        Optional<AccountEntity> accountEntity = accountRepository.findById(request.getAccountId());

        if(accountEntity.isPresent()) {
            if(null == accountEntity.get().getUserEntity().getWalletEntity()) {

                Wallet.makeWallet makeWallet = transUtils.makeWallet();

                WalletEntity walletEntity = WalletEntity.builder()
                    .address(makeWallet.getAddress())
                    .privateKey(makeWallet.getPrivateKey())
                    .userEntity(accountEntity.get().getUserEntity())
                    .build();

                walletRepository.save(walletEntity);

                return Wallet.response.builder()
                    .address(makeWallet.getAddress())
                    .build();
            } else {
                throw new BaseException("wallet already exist");
            }
        } else {
            throw new BaseException("account not exist");
        }
    }
}

