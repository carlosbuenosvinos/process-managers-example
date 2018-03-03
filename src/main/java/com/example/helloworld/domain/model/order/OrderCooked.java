package com.example.helloworld.domain.model.order;

public class OrderCooked extends OrderBaseEvent
{
    public OrderCooked(String correlationId, String causationMessageId, Order order) {
        super(correlationId, causationMessageId, order);
    }
}
