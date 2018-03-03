package com.example.helloworld.handlers;

import com.example.helloworld.utils.Message;

import java.util.concurrent.ConcurrentHashMap;

public class IdempotencyHandler<T extends Message> implements Handles<T>
{
    private final Handles<T> handler;
    private final ConcurrentHashMap<String, Boolean> messagesSeen;

    public IdempotencyHandler(Handles<T> handler)
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
