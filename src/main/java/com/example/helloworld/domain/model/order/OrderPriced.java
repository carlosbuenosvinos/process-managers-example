package com.example.helloworld.domain.model.order;

public class OrderPriced extends OrderBaseEvent
{
    public OrderPriced(String correlationId, String causationMessageId, Order order) {
        super(correlationId, causationMessageId, order);
    }
}
