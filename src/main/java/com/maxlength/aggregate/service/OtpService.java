package com.maxlength.aggregate.service;


import com.maxlength.spec.vo.Otp;

public interface OtpService {

    Otp.OtpRegResponse generateSecretKey(Otp.GenerateSecretKeyRequest request);

    boolean otpValidate(Otp.OtpValidateRequest request);

    boolean removeSecretKey(Otp.GenerateSecretKeyRequest request);

    boolean checkRegist(Otp.OtpCheckRequest request);

    Otp.OtpInfoResponse getSecretKey(Otp.OtpInfoRequest request);

}
