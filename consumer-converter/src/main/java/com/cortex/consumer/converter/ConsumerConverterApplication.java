package com.cortex.consumer.converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ConsumerConverterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerConverterApplication.class, args);
    }

}
