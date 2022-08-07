package com.cortex.producer.converter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import consts.RabbitMQConsts;
import dto.ConversionDTO;
import dto.RequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProducerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper;

    public ProducerService(){
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    @Cacheable(cacheNames = "currency_converter")
    public ConversionDTO sendMessageConversion(String dataCotacao, String moedaOrigem, String moedaFinal, Double valorDesejado) throws JsonProcessingException {
        RequestDTO requestDTO = new RequestDTO(dataCotacao, moedaOrigem, moedaFinal, valorDesejado);

        String conversionDTO = (String) rabbitTemplate.convertSendAndReceive(RabbitMQConsts.EXCHANGE, RabbitMQConsts.RPC_QUEUE, requestDTO);

        log.info("Salvando Cache - CURRENCY-QUOTATION::Redis");
        return this.objectMapper.readValue(conversionDTO, ConversionDTO.class);
    }
}
