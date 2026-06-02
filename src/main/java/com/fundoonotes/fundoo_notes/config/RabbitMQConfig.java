package com.fundoonotes.fundoo_notes.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Queue naam
    public static final String EMAIL_QUEUE = "email.queue";

    // Exchange naam
    public static final String EMAIL_EXCHANGE = "email.exchange";   // exchange - this is path for route meassgae.. it decide message kis queue m jayega..

    // Routing key
    public static final String EMAIL_ROUTING_KEY = "email.routing.key";          // it provide address of the queue..


//      Queue banaya..
//      durable = true → RabbitMQ restart hone pe bhi queue rahegi

    @Bean
    public Queue emailQueue() {

        return new Queue(EMAIL_QUEUE, true);
    }


//      Direct Exchange = exact routing key match hone pe hi message us queue mein jaata hai.

    @Bean
    public DirectExchange emailExchange() {

        return new DirectExchange(EMAIL_EXCHANGE);
    }

//      Queue aur Exchange ko bind krega
//      Routing key se message route hoga..
    @Bean
    public Binding emailBinding(Queue emailQueue, DirectExchange emailExchange) {
        return BindingBuilder
                .bind(emailQueue)    // choose queue..
                .to(emailExchange)    // exchnage choose
                .with(EMAIL_ROUTING_KEY);   // routing key attach..
    }


//      Message converter — Java Object → JSON -> bytes

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


//     RabbitTemplate —Actual Message bhejne ka tool

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);    // rabbitmq server connection..
        template.setMessageConverter(messageConverter());
        return template;
    }
}