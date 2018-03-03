package com.example.dddrestaurant.domain.model;

import com.example.dddrestaurant.application.PriceOrderCommand;
import com.example.dddrestaurant.domain.model.order.OrderPriced;
import com.example.dddrestaurant.handlers.Handles;
import com.example.dddrestaurant.utils.Publisher;

public class AssistanceManager implements Handles<PriceOrderCommand>
{
    private Publisher publisher;

    public AssistanceManager(Publisher publisher) {
        this.publisher = publisher;
    }

    public void handle(PriceOrderCommand message) {
        try {
            Thread.sleep(100);
        } catch (Exception e) {

        }

        this.publisher.publish(new OrderPriced(
            message.getCorrelationMessageId(),
            message.getMessageId(),
            message.order
        ));
    }
}
