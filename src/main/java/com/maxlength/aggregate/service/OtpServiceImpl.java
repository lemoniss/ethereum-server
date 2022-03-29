package com.maxlength.aggregate.service;

import com.maxlength.aggregate.entity.AccountEntity;
import com.maxlength.aggregate.entity.OtpEntity;
import com.maxlength.aggregate.repository.AccountRepository;
import com.maxlength.aggregate.repository.OtpRepository;
import com.maxlength.spec.common.BaseException;
import com.maxlength.spec.enums.Yesno;
import com.maxlength.spec.vo.Otp;
import com.maxlength.spec.vo.Otp.OtpInfoResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Optional;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import de.taimos.totp.TOTP;

@Service
@Transactional
public class OtpServiceImpl implements OtpService {

    private final AccountRepository accountRepository;
    private final OtpRepository otpRepository;

    public OtpServiceImpl(AccountRepository accountRepository, OtpRepository otpRepository) {
        this.accountRepository = accountRepository;
        this.otpRepository = otpRepository;
    }

    @Value("${otp.google-url}")
    private String googleUrl;

    @Value("${otp.issuer}")
    private String issuer;


    @Override
    public Otp.OtpRegResponse generateSecretKey(Otp.GenerateSecretKeyRequest request) {

        Optional<AccountEntity> accountEntity = accountRepository.findByUsernameAndDelYn(request.getUsername(), Yesno.N);

        if(accountEntity.isPresent()) {

            Optional<OtpEntity> otpEntity = otpRepository.findByUserId(accountEntity.get().getUserEntity().getId());

            if(otpEntity.isPresent()) {
                return Otp.OtpRegResponse.builder()
                    .secretKey(otpEntity.get().getSecretkey())
                    .barCodeUrl(otpEntity.get().getBarcodeUrl())
                    .build();
            } else {
                otpEntity = Optional.of(new OtpEntity());
                SecureRandom random = new SecureRandom();
                byte[] bytes = new byte[20];
                random.nextBytes(bytes);
                Base32 base32 = new Base32();
                String secretKey =  base32.encodeToString(bytes);

                otpEntity.get().setUserId(accountEntity.get().getUserEntity().getId());
                otpEntity.get().setSecretkey(secretKey);
                otpEntity.get().setBarcodeUrl(getGoogleAuthenticatorBarcode(secretKey, request.getUsername(), issuer));
                OtpEntity entity = otpRepository.save(otpEntity.get());

                return Otp.OtpRegResponse.builder()
                    .secretKey(entity.getSecretkey())
                    .barCodeUrl(entity.getBarcodeUrl())
                    .build();
            }
        } else {
            throw new BaseException("등록이 안된 사용자입니다.");
        }
    }

    @Override
    public boolean otpValidate(Otp.OtpValidateRequest request) {

        Optional<AccountEntity> accountEntity = accountRepository.findByUsernameAndDelYn(request.getUsername(), Yesno.N);

        if(accountEntity.isPresent()) {

            Optional<OtpEntity> otpEntity = otpRepository.findByUserId(accountEntity.get().getUserEntity().getId());

            if(otpEntity.isPresent()) {
                return request.getInputOtpNumber().equals(getTOTPCode(otpEntity.get().getSecretkey()));
            } else {
                throw new BaseException("등록된 SecretKey가 없습니다.");
            }
        } else {
            throw new BaseException("등록이 안된 사용자입니다.");
        }
    }

    @Override
    public boolean removeSecretKey(Otp.GenerateSecretKeyRequest request) {
        Optional<AccountEntity> accountEntity = accountRepository.findByUsernameAndDelYn(request.getUsername(), Yesno.N);

        if(accountEntity.isPresent()) {

            Optional<OtpEntity> otpEntity = otpRepository.findByUserId(accountEntity.get().getUserEntity().getId());

            if(otpEntity.isPresent()) {
                otpRepository.delete(otpEntity.get());
                return true;
            } else {
                return false;
            }
        } else {
            throw new BaseException("등록이 안된 사용자입니다.");
        }
    }

    @Override
    public boolean checkRegist(Otp.OtpCheckRequest request) {
        Optional<AccountEntity> accountEntity = accountRepository.findByUsernameAndDelYn(request.getUsername(), Yesno.N);

        if (accountEntity.isPresent()) {
            Optional<OtpEntity> otpEntity = otpRepository.findByUserId(accountEntity.get().getUserEntity().getId());
            return otpEntity.isPresent();
        }else {
            throw new BaseException("등록이 안된 사용자입니다.");
        }

    }

    @Override
    public Otp.OtpInfoResponse getSecretKey(Otp.OtpInfoRequest request){
        Optional<AccountEntity> accountEntity = accountRepository.findByUsernameAndDelYn(request.getUsername(), Yesno.N);

        if (accountEntity.isPresent()) {
            Optional<OtpEntity> otpEntity = otpRepository.findByUserId(accountEntity.get().getUserEntity().getId());

            Otp.OtpInfoResponse res = new OtpInfoResponse();
            res.setSecretKey(otpEntity.get().getSecretkey());

            return res;
        } else {
            throw new BaseException("등록이 안된 사용자입니다.");
        }
    }

    private String getGoogleAuthenticatorBarcode(String secretKey, String account, String issuer) {
        return googleUrl + "otpauth://totp/"
            + URLEncoder.encode(issuer + ":" + account, StandardCharsets.UTF_8).replace("+", "%20") + "?secret="
            + URLEncoder.encode(secretKey, StandardCharsets.UTF_8).replace("+", "%20") + "&issuer="
            + URLEncoder.encode(issuer, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

}

