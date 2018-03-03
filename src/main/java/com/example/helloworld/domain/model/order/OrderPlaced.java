package com.example.helloworld.domain.model.order;

public class OrderPlaced extends OrderBaseEvent
{
    public OrderPlaced(String correlationId, String causationMessageId, Order order) {
        super(correlationId, causationMessageId, order);
    }
}
