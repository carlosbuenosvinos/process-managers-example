package com.example.dddrestaurant.domain.model;

import com.example.dddrestaurant.application.PlaceOrderCommand;
import com.example.dddrestaurant.domain.model.order.OrderPlaced;
import com.example.dddrestaurant.handlers.Handles;
import com.example.dddrestaurant.utils.Publisher;

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
