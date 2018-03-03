package com.example.helloworld.domain.model;

import com.example.helloworld.application.PriceOrderCommand;
import com.example.helloworld.domain.model.order.OrderPriced;
import com.example.helloworld.handlers.Handles;
import com.example.helloworld.utils.Publisher;

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
