package com.example.AddressBook.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue userRegistrationQueue() {
        return new Queue("user.registration.queue", false);
    }

    @Bean
    public Queue addressBookQueue() {
        return new Queue("address.book.queue", false);
    }
}