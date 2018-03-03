package com.example.dddrestaurant.application;

import com.example.dddrestaurant.domain.model.order.Order;
import com.example.dddrestaurant.utils.BaseMessage;
import com.example.dddrestaurant.utils.Command;

public class PlaceOrderCommand extends BaseMessage implements Command
{
    public Order order;

    public PlaceOrderCommand(String correlationId, String causationMessageId, Order order)
    {
        super(correlationId, causationMessageId);
        this.order = order;
    }
}
