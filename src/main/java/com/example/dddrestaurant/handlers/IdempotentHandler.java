package com.example.dddrestaurant.handlers;

import com.example.dddrestaurant.utils.Message;

import java.util.concurrent.ConcurrentHashMap;

public class IdempotentHandler<T extends Message> implements Handles<T>
{
    private final Handles<T> handler;
    private final ConcurrentHashMap<String, Boolean> messagesSeen;

    public IdempotentHandler(Handles<T> handler)
    {
        this.handler = handler;
        this.messagesSeen = new ConcurrentHashMap<String, Boolean>();
    }

    public void handle(T message)
    {
        if (null != messagesSeen.putIfAbsent(message.getMessageId(), true)) {
            return;
        }

        this.handler.handle(message);
    }
}
