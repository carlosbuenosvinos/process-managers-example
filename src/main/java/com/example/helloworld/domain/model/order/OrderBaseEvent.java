package com.example.helloworld.domain.model.order;

import com.example.helloworld.utils.BaseEvent;

public abstract class OrderBaseEvent extends BaseEvent
{
    protected Order order;

    public OrderBaseEvent(String correlationId, String causationMessageId, Order order)
    {
        super(correlationId, causationMessageId);
        this.order = order;
    }

    public Order getOrder()
    {
        return order;
    }
}
