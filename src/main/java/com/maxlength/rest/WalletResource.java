package com.maxlength.rest;

import com.maxlength.aggregate.service.WalletService;
import com.maxlength.spec.common.BaseResponse;
import com.maxlength.spec.vo.Wallet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/ethereum/wallets")
@Api(tags = "[블록체인] - 기록/열람 API", protocols = "http", produces = "application/json", consumes = "appliction/json")
public class WalletResource {

    private final WalletService walletService;

    public WalletResource(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping()
    @ApiOperation(value = "지갑생성", notes = "지갑생성")
    public ResponseEntity<Wallet.response> createWallet(
        @RequestBody Wallet.create request) throws Exception {

        return BaseResponse.ok(walletService.createWallet(request));
    }

}
