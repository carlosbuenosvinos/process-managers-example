package com.example.dddrestaurant.handlers;

import com.example.dddrestaurant.utils.Message;

import java.util.concurrent.ConcurrentLinkedQueue;

public class RoundRobin<T extends Message> implements Handles<T>
{
    private ConcurrentLinkedQueue<Handles<Message>> handlers;

    public RoundRobin(ConcurrentLinkedQueue<Handles<Message>> handlers)
    {
        this.handlers = handlers;
    }

    public void handle(T message)
    {
        Handles<Message> handler = this.handlers.remove();
        handler.handle(message);
        this.handlers.add(handler);
    }
}
