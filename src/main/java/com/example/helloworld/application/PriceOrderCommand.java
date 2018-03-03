package com.example.helloworld.application;

import com.example.helloworld.domain.model.order.Order;
import com.example.helloworld.utils.BaseMessage;
import com.example.helloworld.utils.Command;

public class PriceOrderCommand extends BaseMessage implements Command
{
    public Order order;

    public PriceOrderCommand(String correlationId, String causationMessageId, Order order)
    {
        super(correlationId, causationMessageId);
        this.order = order;
    }
}
