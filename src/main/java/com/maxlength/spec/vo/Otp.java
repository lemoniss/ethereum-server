package com.maxlength.spec.vo;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description = "OTP VO")
public class Otp {

    @Getter
    @Setter
    @ApiModel(description = "회원 secretKey 생성 요청")
    public static class GenerateSecretKeyRequest {
        private String username;
    }

    @Getter
    @Builder
    @ApiModel(description = "OTP 등록 응답")
    public static class OtpRegResponse {
        private String secretKey;
        private String barCodeUrl;
    }

    @Getter
    @Setter
    @ApiModel(description = "OTP 인증요청")
    public static class OtpValidateRequest {
        private String username;
        private String inputOtpNumber;
    }

    @Getter
    @Setter
    @ApiModel(description = "OTP 등록 유무 응답")
    public static class OtpCheckRequest {
        private String username;
    }

    @Getter
    @Setter
    @ApiModel(description = "OTP SecretKey 정보 요청")
    public static class OtpInfoRequest {
        private String username;
    }

    @Getter
    @Setter
    @ApiModel(description = "OTP SecretKey 정보 응답")
    public static class OtpInfoResponse {
        private String secretKey;
    }

}
