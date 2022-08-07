package com.cortex.consumer.converter.configuration;

import consts.RabbitMQConsts;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    /**
     * Create the queue that will receive the message from the producer
     * @return Queue
     */
    @Bean
    public Queue queue() {
        return new Queue(RabbitMQConsts.RPC_QUEUE, true, false, false);
    }

    /**
     * Create the queue that will return the request response to the producer
     * @return Queue
     */
    @Bean
    public Queue replyQueue() {
        return new Queue(RabbitMQConsts.RPC_REPLY_CONVERSION_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(RabbitMQConsts.EXCHANGE);
    }

    /**
     * method responsible for linking the created queue to the exchange
     * @return Binding
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(RabbitMQConsts.RPC_QUEUE);
    }

    /**
     * method responsible for linking the created reply queue to the exchange
     * @return Binding
     */
    @Bean
    public Binding replyBinding() {
        return BindingBuilder.bind(replyQueue()).to(exchange()).with(RabbitMQConsts.RPC_REPLY_CONVERSION_QUEUE);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                  Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
