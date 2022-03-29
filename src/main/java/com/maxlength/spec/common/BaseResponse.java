package com.maxlength.spec.common;

import com.maxlength.spec.vo.ErrorVo;
import java.util.HashMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseResponse {

    public static <T> T ok() {
        return of(new ErrorVo(), "application/json", HttpStatus.OK);
    }

    public static <T> T ok(Object object) {
        return of(object, "application/json", HttpStatus.OK);
    }

    public static <T> T of(Object object) {
        return of(object, "application/json", HttpStatus.OK);
    }

    public static <T> T of(Object object, HttpStatus httpStatus) {
        return of(object, "application/json", httpStatus);
    }

    public static <T> T of(Object object, String mediaType, HttpStatus httpStatus) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", mediaType);
        return (T) new ResponseEntity(object, headers, httpStatus);
    }

    public static <T> T error(ErrorVo errorVo) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return error(errorVo, headers, HttpStatus.OK);
    }

    public static <T> T error(ErrorVo errorVo, HttpHeaders headers) {
        return error(errorVo, headers, HttpStatus.OK);
    }

    public static <T> T error(ErrorVo errorVo, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return error(errorVo, headers, status);
    }

    public static <T> T error(ErrorVo errorVo, HttpHeaders headers, HttpStatus status) {
        HashMap<String, Object> error = new HashMap();
        HashMap<String, Object> item = new HashMap();
        item.put("code", errorVo.getCode());
        item.put("messageCode", errorVo.getMessageCode());
        item.put("message", errorVo.getMessage());
        error.put("error", item);
        return (T) new ResponseEntity(error, headers, status);
    }

    public static <T> T error(Object object) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return (T) new ResponseEntity(object, headers, HttpStatus.OK);
    }
}
