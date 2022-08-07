package com.cortex.producer.converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProducerConverterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerConverterApplication.class, args);
    }

}
