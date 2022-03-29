package com.maxlength.rest;

import com.maxlength.aggregate.service.OtpService;
import com.maxlength.spec.common.BaseResponse;
import com.maxlength.spec.vo.Otp;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ethereum/otp")
@Api(tags = {"[OTP] - OTP API"}, protocols = "http", produces = "application/json", consumes = "application/json")
public class OtpResource {

    private final OtpService otpService;

    public OtpResource(OtpService otpService) {
        this.otpService = otpService;
    }

    /**
     * 등록
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<Otp.OtpRegResponse> generateSecretKey(@RequestBody Otp.GenerateSecretKeyRequest request) {
        return BaseResponse.ok(otpService.generateSecretKey(request));
    }

    /**
     * 검증
     * @param request
     * @return
     */
    @PostMapping("/validate")
    public boolean otpValidate(@RequestBody Otp.OtpValidateRequest request) {
        return otpService.otpValidate(request);
    }

    /**
     * 삭제
     * @param request
     * @return
     */
    @DeleteMapping
    public boolean removeSecretKey(@RequestBody Otp.GenerateSecretKeyRequest request) {
        return otpService.removeSecretKey(request);
    }


    /**
     * 등록 유무 확인
     * @param request
     * @return
     */
    @PostMapping("/check")
    public boolean checkRegist(@RequestBody Otp.OtpCheckRequest request){ return otpService.checkRegist(request);}

    /**
     * username으로 시크릿 키 조회
     * @param request
     * @return
     */
    @PostMapping("/secret")
    public Otp.OtpInfoResponse getSecretKey(@RequestBody Otp.OtpInfoRequest request){ return otpService.getSecretKey(request);}
}
