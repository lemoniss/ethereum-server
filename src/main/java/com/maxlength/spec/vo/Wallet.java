package com.maxlength.spec.vo;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@ApiModel(description = "Wallet VO")
public class Wallet {

    @Getter
    @ApiModel(description = "지갑 생성 요청")
    public static class create {
        @NotNull
        private Long accountId;
    }

    @Getter
    @Builder
    @ApiModel(description = "지갑 응답")
    public static class response {
        private String address;
    }

    @Getter
    @Builder
    @ApiModel(description = "지갑생성")
    public static class makeWallet {
        private String address;
        private String privateKey;
    }
}
