package com.maxlength.spec.vo;

import com.maxlength.spec.enums.Confirm;
import io.swagger.annotations.ApiModel;
import java.math.BigDecimal;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@ApiModel(description = "Owner")
public class Owner {


    @Getter
    @Builder
    @ApiModel(description = "서명자 지갑주소 목록")
    public static class getSigners {
        private String signerAddress;
    }

    @Getter
    @ApiModel(description = "서명 요청")
    public static class requestConfirm {
        private String address;

        @Enumerated(EnumType.STRING)
        private Confirm confirmType;
    }

    @Getter
    @ApiModel(description = "서명자 추가/삭제 요청")
    public static class requestSignerAddRemove {
        private String address;

        @Enumerated(EnumType.STRING)
        private Confirm confirmType;

        private String targetAddress;
    }

    @Getter
    @ApiModel(description = "토큰 발행/소각 요청")
    public static class requestMintBurn {
        private String address;

        private String targetAddress;

        private BigDecimal amount;
    }

}
