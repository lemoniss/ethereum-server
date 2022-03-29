package com.maxlength.rest;

import com.maxlength.aggregate.service.SignerService;
import com.maxlength.spec.common.BaseResponse;
import com.maxlength.spec.enums.Confirm;
import com.maxlength.spec.vo.Signer;
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
@RequestMapping("/api/ethereum/contracts/signers")
@Api(tags = "[블록체인] - 서명자 API", protocols = "http", produces = "application/json", consumes = "appliction/json")
public class SignerResource {

    private final SignerService signerService;

    public SignerResource(SignerService signerService) {
        this.signerService = signerService;
    }

    /**
     * 서명요청 가져오기
     * @param address
     * @param confirmType
     * @return
     * @throws Exception
     */
    @GetMapping("/getRequestConfirm")
    @ApiOperation(value = "서명요청 가져오기", notes = "서명요청 가져오기")
    public ResponseEntity<Signer.getRequestConfirm> getRequestConfirm(
        @ApiParam(value = "지갑주소", required = true) @RequestParam(name= "address") String address,
        @ApiParam(value = "요청타입 (TRANSFER, MINT, BURN, ADD, REMOVE)", required = true) @RequestParam(name= "confirmType") String confirmType) throws Exception {

        return BaseResponse.ok(signerService.getRequestConfirm(address, Confirm.valueOf(confirmType)));
    }

    /**
     * 서명 하기
     * @param request
     * @return
     */
    @PostMapping("/agreeSign")
    @ApiOperation(value = "서명 하기", notes = "서명 하기")
    public ResponseEntity<Token.transactionReceipt> agreeSign(@RequestBody Signer.agreeSign request) throws Exception {

        return BaseResponse.ok(signerService.agreeSign(request));
    }

}
