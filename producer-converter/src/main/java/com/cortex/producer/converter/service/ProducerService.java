package com.cortex.producer.converter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import consts.RabbitMQConsts;
import dto.ConversionDTO;
import dto.RequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
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

    public ProducerService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    @Cacheable(cacheNames = "currency_converter")
    public ConversionDTO sendMessageConversion(String dataCotacao, String moedaOrigem, String moedaFinal, Double valorDesejado) throws Exception {
        RequestDTO requestDTO = new RequestDTO(dataCotacao, moedaOrigem, moedaFinal, valorDesejado);

        MessagePostProcessor processor = checkPriority(moedaFinal);

        try {
            String conversionDTO = (String) rabbitTemplate.convertSendAndReceive(RabbitMQConsts.EXCHANGE, RabbitMQConsts.RPC_QUEUE, requestDTO, processor);
            log.info("Salvando Cache - CURRENCY-QUOTATION::Redis");
            return this.objectMapper.readValue(conversionDTO, ConversionDTO.class);
        }catch (Exception e) {
            throw new Exception("Servidor não conseguiu responder.");
        }

    }

    private MessagePostProcessor checkPriority(String moedaFinal) {
        log.info("Checando Prioridades da fila...");
        final int priority = moedaFinal.equalsIgnoreCase("USD") || moedaFinal.equalsIgnoreCase("EUR") ? 5 : 0;

        return message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setPriority(priority);
            return message;
        };
    }
}
