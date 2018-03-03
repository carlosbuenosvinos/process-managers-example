package com.example.helloworld.domain.model.cook;

import com.example.helloworld.domain.model.order.Order;
import com.example.helloworld.domain.model.order.OrderBaseEvent;

public class CookTimedOut extends OrderBaseEvent
{
    public CookTimedOut(String correlationId, String causationMessageId, Order order) {
        super(correlationId, causationMessageId, order);
    }
}
