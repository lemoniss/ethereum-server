package com.maxlength.spec.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import java.math.BigInteger;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(description = "Read Write VO")
public class Event {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(description = "기록 요청")
    public static class writeRequest {
        @NotNull
        private String functionName;

        @NotNull
        private String contents;
    }

    @Getter
    @Builder
    @ApiModel(description = "목록 요청")
    public static class listRequest {
        private BigInteger  blockNumber;
        private String      transactionHash;
        private String      eventName;
    }

    @Getter
    @Builder
    @ApiModel(description = "목록 응답")
    public static class getEventList {
        private BigInteger  blockNumber;
        private String      transactionHash;

        private String  address;
        private String  contents;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime regDt;
    }

    @Getter
    @Builder
    @ApiModel(description = "상세 요청")
    public static class detailRequest {
        private BigInteger  blockNumber;
        private String      transactionHash;
        private String      eventName;
    }

    @Getter
    @Builder
    @ApiModel(description = "상세 가져오기")
    public static class getEventDetail {
        private BigInteger  blockNumber;
        private String      transactionHash;

        private String  address;
        private String  contents;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime regDt;
    }
}
