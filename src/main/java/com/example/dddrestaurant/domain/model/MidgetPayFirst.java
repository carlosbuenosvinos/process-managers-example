package com.example.dddrestaurant.domain.model;

import com.example.dddrestaurant.application.CookFoodCommand;
import com.example.dddrestaurant.application.PriceOrderCommand;
import com.example.dddrestaurant.application.TakePaymentCommand;
import com.example.dddrestaurant.domain.model.order.*;
import com.example.dddrestaurant.handlers.Handles;
import com.example.dddrestaurant.utils.Message;
import com.example.dddrestaurant.utils.Publisher;

public class MidgetPayFirst implements Handles<Message>, Midget
{
    private final Publisher publisher;

    public MidgetPayFirst(Publisher publisher) {
        this.publisher = publisher;
    }

    public void handle(Message message) {
        if (message instanceof OrderPlaced) {
            this.publisher.publish(
                new PriceOrderCommand(
                    message.getCorrelationMessageId(),
                    message.getMessageId(),
                    ((OrderPlaced) message).getOrder()
                )
            );
        } else if(message instanceof OrderCooked) {
            this.publisher.publish(
                new OrderCompleted(
                    message.getCorrelationMessageId(),
                    message.getMessageId(),
                    ((OrderCooked) message).getOrder()
                )
            );
        } else if(message instanceof OrderPriced) {
            this.publisher.publish(
                new TakePaymentCommand(
                    message.getCorrelationMessageId(),
                    message.getMessageId(),
                    ((OrderPriced) message).getOrder()
                )
            );
        } else if(message instanceof OrderPaid) {
            this.publisher.publish(
                new CookFoodCommand(
                    message.getCorrelationMessageId(),
                    message.getMessageId(),
                    ((OrderPaid) message).getOrder()
                )
            );
        }
    }
}
