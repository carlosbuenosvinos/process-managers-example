package com.example.helloworld.domain.model;

import com.example.helloworld.application.CookFoodCommand;
import com.example.helloworld.application.PriceOrderCommand;
import com.example.helloworld.application.TakePaymentCommand;
import com.example.helloworld.domain.model.order.*;
import com.example.helloworld.handlers.Handles;
import com.example.helloworld.utils.Message;
import com.example.helloworld.utils.Publisher;

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
