package com.example.dddrestaurant.domain.model.order;

public class OrderCompleted extends OrderBaseEvent
{
    public OrderCompleted(String correlationId, String causationMessageId, Order order) {
        super(correlationId, causationMessageId, order);
    }
}
