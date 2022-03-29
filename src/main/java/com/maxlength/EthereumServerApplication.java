package com.maxlength;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@EnableScheduling
//@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
@EnableSwagger2
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class EthereumServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EthereumServerApplication.class, args);
    }
}
