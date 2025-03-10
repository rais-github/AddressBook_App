package com.example.AddressBook.service.implimentation;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    @RabbitListener(queues = "user.registration.queue")
    public void receiveUserRegistrationMessage(String message) {
        System.out.println("📩 Received User Registration Event: " + message);
    }

    @RabbitListener(queues = "address.book.queue")
    public void receiveAddressBookMessage(String message) {
        System.out.println("📩 Received Address Book Event: " + message);
    }
}
