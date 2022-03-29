package com.maxlength.spec.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorVo {

    private static final Logger log = LoggerFactory.getLogger(ErrorVo.class);
    public static final String SUCCESS = "00";
    public static final String FAIL = "10";
    public static final String ACCESSDENIED = "403";
    public static final String UNAUTHORIZED = "401";
    public static final String BADREQUEST = "400";

    @ApiModelProperty(
        value = "성공/에러코드",
        example = "00"
    )
    private String code = "00";
    @ApiModelProperty(
        value = "메세지 코드",
        example = "ERROR.SYS.999"
    )
    private String messageCode = "";
    @ApiModelProperty(
        value = "에러의 경우 에러메시지",
        example = ""
    )
    private String message = "";

    public ErrorVo() {
    }

    public ErrorVo(String code) {
        this.code = code;
        this.messageCode = "";
        this.message = "";
    }

    public ErrorVo(String code, String messageCode) {
        this.code = code;
        this.messageCode = messageCode;
        this.message = "";
    }

    public ErrorVo(String code, String messageCode, String message) {
        this.code = code;
        this.messageCode = messageCode;
        this.message = message;
    }

    public String toJson() {
        String json = "";
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            json = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException var4) {
            log.error(var4.getMessage(), var4);
        }

        return json;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessageCode() {
        return this.messageCode;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setMessageCode(final String messageCode) {
        this.messageCode = messageCode;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String toString() {
        return "ErrorVo(code=" + this.getCode() + ", messageCode=" + this.getMessageCode() + ", message=" + this.getMessage() + ")";
    }
}
