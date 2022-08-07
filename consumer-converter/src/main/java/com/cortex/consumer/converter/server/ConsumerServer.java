package com.cortex.consumer.converter.server;

import com.cortex.consumer.converter.service.ConsumerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import consts.RabbitMQConsts;
import dto.ConversionDTO;
import dto.RequestDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServer {

    private RabbitTemplate rabbitTemplate;
    private ConsumerService converterService;
    private ObjectMapper objectMapper;

    public ConsumerServer(RabbitTemplate rabbitTemplate, ConsumerService converterService) {
        this.rabbitTemplate = rabbitTemplate;
        this.converterService = converterService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    /**
     * Responsible for listening to the queue processing
     * @param requestDTO
     * @throws JsonProcessingException
     */
    @RabbitListener(queues = RabbitMQConsts.RPC_QUEUE)
    public void conversorCurrency(RequestDTO requestDTO) throws JsonProcessingException {
        ConversionDTO conversionDTO = this.converterService.convertCurrency(requestDTO);
        String conversionDTOString = this.objectMapper.writeValueAsString(conversionDTO);
        rabbitTemplate.convertSendAndReceive(RabbitMQConsts.EXCHANGE, RabbitMQConsts.RPC_REPLY_CONVERSION_QUEUE, conversionDTOString);
    }
}
