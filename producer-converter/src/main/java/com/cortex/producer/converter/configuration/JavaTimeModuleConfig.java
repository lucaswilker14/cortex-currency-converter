package com.cortex.producer.converter.configuration;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaTimeModuleConfig {

    @Bean
    public JavaTimeModule dateTimeModule(){
        return new JavaTimeModule();
    }
}
