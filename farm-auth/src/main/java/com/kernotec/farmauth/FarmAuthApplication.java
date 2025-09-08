package com.kernotec.farmauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.kernotec.core", "com.kernotec.farmauth"})
public class FarmAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmAuthApplication.class, args);
    }

}
