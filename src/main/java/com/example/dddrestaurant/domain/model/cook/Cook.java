package com.example.dddrestaurant.domain.model.cook;

import com.example.dddrestaurant.application.CookFoodCommand;
import com.example.dddrestaurant.domain.model.order.Order;
import com.example.dddrestaurant.domain.model.order.OrderCooked;
import com.example.dddrestaurant.handlers.Handles;
import com.example.dddrestaurant.utils.Message;
import com.example.dddrestaurant.utils.Publisher;
import com.example.dddrestaurant.utils.Sizable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Cook implements Handles<CookFoodCommand>, Sizable
{
    private final String name;
    private final ConcurrentHashMap<String, Boolean> messagesSeen;
    private final ConcurrentLinkedQueue<Order> ordersCooked;
    private Publisher publisher;

    public Cook(Publisher publisher, String name, ConcurrentLinkedQueue<Order> ordersCooked) {
        this.publisher = publisher;
        this.name = name;
        this.ordersCooked = ordersCooked;
        this.messagesSeen = new ConcurrentHashMap<String, Boolean>();
    }

    public void handle(CookFoodCommand message) {
        message.order.addCookName(this.name);

        try {
            Thread.sleep(1000);
        } catch(Exception e) {

        }
        message.order.cook();
        ordersCooked.add(message.order);

        this.publisher.publish(new OrderCooked(
            message.getCorrelationMessageId(),
            message.getMessageId(),
            message.order
        ));
    }

    public int size()
    {
        return messagesSeen.size();
    }
}
