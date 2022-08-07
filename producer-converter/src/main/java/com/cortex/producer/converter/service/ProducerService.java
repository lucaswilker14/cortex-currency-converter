package com.cortex.producer.converter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import consts.RabbitMQConsts;
import dto.ConversionDTO;
import dto.RequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProducerService {

    public String sendMessageConversion(String dataCotacao, String moedaOrigem, String moedaFinal, Double valorDesejado) throws JsonProcessingException {
        return "teste";
    }
}
