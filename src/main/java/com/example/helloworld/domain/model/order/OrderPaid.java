package com.example.helloworld.domain.model.order;

public class OrderPaid extends OrderBaseEvent
{
    public OrderPaid(String correlationId, String causationMessageId, Order order) {
        super(correlationId, causationMessageId, order);
    }
}
