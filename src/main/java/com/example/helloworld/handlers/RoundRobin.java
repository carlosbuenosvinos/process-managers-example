package com.example.helloworld.handlers;

import com.example.helloworld.utils.Message;

import java.util.concurrent.ConcurrentLinkedQueue;

public class RoundRobin<T extends Message> implements Handles<T>
{
    private ConcurrentLinkedQueue<Handles<T>> handlers;

    public RoundRobin(ConcurrentLinkedQueue<Handles<T>> handlers)
    {
        this.handlers = (ConcurrentLinkedQueue<Handles<T>>) handlers.clone();
    }

    public void handle(T message)
    {
        Handles<T> handler = this.handlers.remove();
        handler.handle(message);
        this.handlers.add(handler);
    }
}
