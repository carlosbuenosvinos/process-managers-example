package com.example.dddrestaurant.application;

import com.example.dddrestaurant.utils.BaseMessage;
import com.example.dddrestaurant.domain.model.order.Order;
import com.example.dddrestaurant.utils.Command;

public class CookFoodCommand extends BaseMessage implements Command
{
    public Order order;

    public CookFoodCommand(String messageId, String correlationId, String causationMessageId, Order order)
    {
        super(correlationId, causationMessageId);
        this.messageId = messageId;
        this.order = order;
    }

    public CookFoodCommand(String correlationId, String causationMessageId, Order order)
    {
        super(correlationId, causationMessageId);
        this.order = order;
    }
}
