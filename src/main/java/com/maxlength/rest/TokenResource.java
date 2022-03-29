package com.maxlength.rest;

import com.maxlength.aggregate.service.TokenService;
import com.maxlength.spec.common.BaseResponse;
import com.maxlength.spec.vo.Token;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ethereum/contracts/users")
@Api(tags = "[블록체인] - 사용자 API", protocols = "http", produces = "application/json", consumes = "appliction/json")
public class TokenResource {

    private final TokenService tokenService;

    public TokenResource(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * 토큰 전송
     * @param request
     * @return
     */
    @PostMapping("/transfer")
    @ApiOperation(value = "토큰 전송", notes = "토큰 전송")
    public ResponseEntity<Token.transactionReceipt> transfer(@RequestBody Token.transferRequest request) throws Exception {

        return BaseResponse.ok(tokenService.transfer(request));
    }

    /**
     * 토큰 전송 (수수료대납)
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/delegateTransfer")
    @ApiOperation(value = "토큰 전송 (수수료대납)", notes = "토큰 전송 (수수료대납)")
    public ResponseEntity<Token.transactionReceipt> delegateTransfer(@RequestBody Token.transferRequest request) throws Exception {

        return BaseResponse.ok(tokenService.delegateTransfer(request));
    }

    /**
     * 토큰 잔액 조회
     * @param address
     * @param scale
     * @return
     * @throws Exception
     */
    @GetMapping("/balanceOf")
    @ApiOperation(value = "잔액조회", notes = "잔액조회")
    public ResponseEntity<Token.balanceOf> getBalanceOf(
        @ApiParam(value = "지갑주소", required = true) @RequestParam(name= "address") String address,
        @ApiParam(value = "소수점자리수(기본: 0)") @RequestParam(name= "scale", required = false, defaultValue = "0") int scale) throws Exception {

        return BaseResponse.ok(tokenService.getBalanceOf(address, scale));
    }

}
