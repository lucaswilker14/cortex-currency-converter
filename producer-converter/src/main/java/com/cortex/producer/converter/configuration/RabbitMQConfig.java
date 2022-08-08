package com.cortex.producer.converter.configuration;

import com.cortex.producer.converter.exceptions.ServerOfflineHandler;
import consts.RabbitMQConsts;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(RabbitMQConsts.EXCHANGE);
    }

    /**
     * method to share more complex objects on the exchange
     * @return Jackson2JsonMessageConverter
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Base method to create model, convert message, server reply queue
     * @param connectionFactory
     * @param messageConverter
     * @return RabbitTemplate
     */
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                  Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        template.setReplyAddress(RabbitMQConsts.RPC_REPLY_CONVERSION_QUEUE);
        template.setReplyTimeout(6000);
        return template;
    }

    /**
     * listen for server response over queue RPC_REPLY_CONVERSION_QUEUE
     * @param connectionFactory
     * @param messageConverter
     * @return SimpleMessageListenerContainer
     */
    @Bean
    SimpleMessageListenerContainer replyContainer(ConnectionFactory connectionFactory,
                                                  Jackson2JsonMessageConverter messageConverter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(RabbitMQConsts.RPC_REPLY_CONVERSION_QUEUE);
        container.setMessageListener(rabbitTemplate(connectionFactory, messageConverter));
        container.setErrorHandler(new ServerOfflineHandler());
        return container;
    }

}
