package com.example.dddrestaurant.domain.model;

import com.example.dddrestaurant.application.CookFoodCommand;
import com.example.dddrestaurant.application.PriceOrderCommand;
import com.example.dddrestaurant.application.SendToMeInCommand;
import com.example.dddrestaurant.application.TakePaymentCommand;
import com.example.dddrestaurant.domain.model.cook.CookTimedOut;
import com.example.dddrestaurant.domain.model.order.*;
import com.example.dddrestaurant.handlers.Handles;
import com.example.dddrestaurant.utils.Publisher;
import java.time.LocalDateTime;

public class MidgetPayLast extends Midget implements Handles<com.example.dddrestaurant.utils.Message>
{
    public MidgetPayLast(Publisher publisher) {
        super(publisher);
    }

    protected void handleCookTimedOut(CookTimedOut message) {
        publishCookFoodCommand(new CookFoodCommand(
            message.getCausationMessageId(),
            message.getCorrelationMessageId(),
            message.getMessageId(),
            message.getOrder()
        ), message.getCorrelationMessageId(), message.getOrder());
    }

    protected void handleOrderPlaced(OrderPlaced message) {
        publishCookFoodCommand(new CookFoodCommand(
            message.getCorrelationMessageId(),
            message.getMessageId(),
            message.getOrder()
        ), message.getCorrelationMessageId(), message.getOrder());
    }

    private void publishCookFoodCommand(CookFoodCommand cookFoodCommand, String correlationMessageId, Order order) {
        this.publisher.publish(cookFoodCommand);
        this.publisher.publish(
            new SendToMeInCommand(
                LocalDateTime.now().plusNanos(5 * 1000),
                new CookTimedOut(
                    correlationMessageId,
                    cookFoodCommand.getMessageId(),
                    order
                ),
                correlationMessageId,
                cookFoodCommand.getMessageId()
            )
        );
    }

    protected void handleOrderCooked(OrderCooked message) {
        this.publisher.publish(
            new PriceOrderCommand(
                message.getCorrelationMessageId(),
                message.getMessageId(),
                message.getOrder()
            )
        );
    }

    protected void handleOrderPriced(OrderPriced message) {
        this.publisher.publish(
            new TakePaymentCommand(
                message.getCorrelationMessageId(),
                message.getMessageId(),
                message.getOrder()
            )
        );
    }

    protected void handleOrderPaid(OrderPaid message) {
        this.publisher.publish(
            new OrderCompleted(
                message.getCorrelationMessageId(),
                message.getMessageId(),
                message.getOrder()
            )
        );
    }
}
