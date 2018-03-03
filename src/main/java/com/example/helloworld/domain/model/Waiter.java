package com.example.helloworld.domain.model;

import com.example.helloworld.application.PlaceOrderCommand;
import com.example.helloworld.domain.model.order.OrderPlaced;
import com.example.helloworld.handlers.Handles;
import com.example.helloworld.utils.Publisher;

public class Waiter implements Handles<PlaceOrderCommand> {
    private Publisher publisher;

    public Waiter(Publisher publisher) {
        this.publisher = publisher;
    }

    public void handle(PlaceOrderCommand message) {
        try {
            Thread.sleep(100);
        } catch (Exception e) {

        }

        this.publisher.publish(new OrderPlaced(
                message.getCorrelationMessageId(),
                message.getMessageId(),
                message.order
        ));
    }
}
