package com.skytakeaway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@EnableScheduling
public class SkyTakeawayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkyTakeawayApplication.class, args);
    }

}
