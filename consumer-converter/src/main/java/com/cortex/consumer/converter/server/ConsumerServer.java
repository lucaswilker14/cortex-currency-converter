package com.cortex.consumer.converter.server;


import com.cortex.consumer.converter.service.ConsumerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import consts.RabbitMQConsts;
import dto.ConversionDTO;
import dto.RequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
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
        log.info("Chegou na fila do consumidor...");
        try {
            ConversionDTO conversionDTO = this.converterService.convertCurrency(requestDTO);
            String conversionDTOString = this.objectMapper.writeValueAsString(conversionDTO);
            log.info("Retornando resposta do servidor BCB");
            rabbitTemplate.convertSendAndReceive(RabbitMQConsts.EXCHANGE, RabbitMQConsts.RPC_REPLY_CONVERSION_QUEUE, conversionDTOString);
        }catch (Exception e) {
            rabbitTemplate.convertSendAndReceive(RabbitMQConsts.EXCHANGE, RabbitMQConsts.RPC_REPLY_CONVERSION_QUEUE, e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Dado Inválido.");
        }
    }
}
