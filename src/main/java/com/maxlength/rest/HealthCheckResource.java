package com.maxlength.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ethereum/healthcheck")
public class HealthCheckResource {

    @GetMapping
    public String healthcheck() {
        return "healthcheck";
    }
}
