package com.maxlength.rest;

import com.maxlength.aggregate.service.OwnerService;
import com.maxlength.spec.common.BaseResponse;
import com.maxlength.spec.enums.Confirm;
import com.maxlength.spec.enums.Flag;
import com.maxlength.spec.vo.Owner;
import com.maxlength.spec.vo.Token;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/ethereum/contracts/owners")
@Api(tags = "[블록체인] - 관리자 API", protocols = "http", produces = "application/json", consumes = "appliction/json")
public class OwnerResource {

    private final OwnerService ownerService;

    public OwnerResource(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    /**
     * 서명요청 / 서명취소요청 하기
     * @param request
     * 0: 토큰전송, 1: 토큰발행, 2: 토큰소각, 3: 서명자추가, 4: 서명자제거
     * @return
     */
    @PostMapping("/requestConfirm")
    @ApiOperation(value = "서명요청 하기", notes = "서명요청 하기")
    public ResponseEntity<Token.transactionReceipt> requestConfirm(@RequestBody Owner.requestConfirm request) throws Exception {

        return BaseResponse.ok(ownerService.requestConfirm(request, Flag.REQUEST));
    }

    /**
     * 서명취소 요청 하기
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/requestCancel")
    @ApiOperation(value = "서명취소 요청 하기", notes = "서명요청 하기")
    public ResponseEntity<Token.transactionReceipt> requestCancel(@RequestBody Owner.requestConfirm request) throws Exception {

        return BaseResponse.ok(ownerService.requestConfirm(request, Flag.CANCEL));
    }


    /**
     * 서명완료한 서명자 가져오기
     * @param address
     * @return
     */
    @GetMapping("/getConfirmSigners")
    @ApiOperation(value = "서명요청 하기", notes = "서명요청 하기")
    public ResponseEntity<List<Owner.getSigners>> getConfirmSigners(
        @ApiParam(value = "지갑주소", required = true) @RequestParam(name= "address") String address,
        @ApiParam(value = "요청타입 (TRANSFER, MINT, BURN, ADD, REMOVE)", required = true) @RequestParam(name= "confirmType") String confirmType) throws Exception {

        return BaseResponse.ok(ownerService.getConfirmSigners(address, Confirm.valueOf(confirmType)));
    }

    /**
     * 토큰 발행
     * @param request
     * @return
     */
    @PostMapping("/mint")
    @ApiOperation(value = "토큰 발행", notes = "토큰 발행")
    public ResponseEntity<Token.transactionReceipt> mint(@RequestBody Owner.requestMintBurn request) throws Exception {

        return BaseResponse.ok(ownerService.mintBurn(request, Confirm.MINT));
    }

    /**
     * 토큰 소각
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/burn")
    @ApiOperation(value = "토큰 소각", notes = "토큰 소각")
    public ResponseEntity<Token.transactionReceipt> burn(@RequestBody Owner.requestMintBurn request) throws Exception {

        return BaseResponse.ok(ownerService.mintBurn(request, Confirm.BURN));
    }


    /**
     * 전체 서명자목록 가져오기
     * @param address
     * @return
     */
    @GetMapping("/getSigners")
    @ApiOperation(value = "전체 서명자목록 가져오기", notes = "전체 서명자목록 가져오기")
    public ResponseEntity<List<Owner.getSigners>> getSigners(
        @ApiParam(value = "지갑주소", required = true) @RequestParam(name= "address") String address) throws Exception {

        return BaseResponse.ok(ownerService.getSigners(address));
    }
}
