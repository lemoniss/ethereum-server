package com.maxlength.spec.vo;

import com.maxlength.spec.enums.Confirm;
import io.swagger.annotations.ApiModel;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@ApiModel(description = "Signer")
public class Signer {


    @Getter
    @ApiModel(description = "서명하기")
    public static class agreeSign {
        private String address;

        @Enumerated(EnumType.STRING)
        private Confirm confirmType;

        private String targetAddress;
    }

    @Getter
    @Builder
    @ApiModel(description = "서명요청 목록")
    public static class getRequestConfirm {

        @Enumerated(EnumType.STRING)
        private List<Confirm> confirmTypes;

        private String removeAddress;
    }
}
