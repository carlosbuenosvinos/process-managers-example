package com.example.dddrestaurant.domain.model;

import com.example.dddrestaurant.application.CookFoodCommand;
import com.example.dddrestaurant.application.PriceOrderCommand;
import com.example.dddrestaurant.application.SendToMeInCommand;
import com.example.dddrestaurant.application.TakePaymentCommand;
import com.example.dddrestaurant.domain.model.cook.CookTimedOut;
import com.example.dddrestaurant.domain.model.order.*;
import com.example.dddrestaurant.handlers.Handles;
import com.example.dddrestaurant.utils.Message;
import com.example.dddrestaurant.utils.Publisher;

import java.time.LocalDateTime;

public class MidgetPayLast implements Handles<com.example.dddrestaurant.utils.Message>, Midget
{
    private final Publisher publisher;

    public MidgetPayLast(Publisher publisher) {
        this.publisher = publisher;
    }

    public void handle(com.example.dddrestaurant.utils.Message message) {
        if (message instanceof OrderPlaced) {
            handleOrderPlaced(message);
        } else if(message instanceof OrderCooked) {
            handleOrderCooked(message);
        } else if(message instanceof OrderPriced) {
            handleOrderPriced(message);
        } else if(message instanceof OrderPaid) {
            handleOrderPaid(message);
        } else if(message instanceof CookTimedOut) {
            handleCookTimedOut((CookTimedOut) message);
        } else {
//            System.out.println(message.getClass().getSimpleName() + " received, but I don't know what to do???");
        }
    }

    private void handleCookTimedOut(CookTimedOut message) {
        CookFoodCommand cookFoodCommand = new CookFoodCommand(
            message.getCausationMessageId(),
            message.getCorrelationMessageId(),
            message.getMessageId(),
            message.getOrder()
        );

        this.publisher.publish(cookFoodCommand);

        this.publisher.publish(
            new SendToMeInCommand(
                LocalDateTime.now().plusNanos(5 * 1000),
                new CookTimedOut(
                    message.getCorrelationMessageId(),
                    cookFoodCommand.getMessageId(),
                    message.getOrder()
                ),
                message.getCorrelationMessageId(),
                cookFoodCommand.getMessageId()
            )
        );
    }

    private void handleOrderPlaced(Message message) {
        CookFoodCommand cookFoodCommand = new CookFoodCommand(
            message.getCorrelationMessageId(),
            message.getMessageId(),
            ((OrderBaseEvent) message).getOrder()
        );

        this.publisher.publish(cookFoodCommand);

        this.publisher.publish(
            new SendToMeInCommand(
                LocalDateTime.now().plusNanos(5 * 1000),
                new CookTimedOut(
                    message.getCorrelationMessageId(),
                    cookFoodCommand.getMessageId(),
                    ((OrderPlaced) message).getOrder()
                ),
                message.getCorrelationMessageId(),
                cookFoodCommand.getMessageId()
            )
        );
    }

    private void handleOrderCooked(Message message) {
        this.publisher.publish(
                new PriceOrderCommand(
                        message.getCorrelationMessageId(),
                        message.getMessageId(),
                        ((OrderCooked) message).getOrder()
                )
        );
    }

    private void handleOrderPriced(Message message) {
        this.publisher.publish(
                new TakePaymentCommand(
                        message.getCorrelationMessageId(),
                        message.getMessageId(),
                        ((OrderPriced) message).getOrder()
                )
        );
    }

    private void handleOrderPaid(Message message) {
        this.publisher.publish(
            new OrderCompleted(
                message.getCorrelationMessageId(),
                message.getMessageId(),
                ((OrderPaid) message).getOrder()
            )
        );
    }
}
