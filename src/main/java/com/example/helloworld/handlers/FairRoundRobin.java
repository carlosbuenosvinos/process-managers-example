package com.example.helloworld.handlers;

import com.example.helloworld.utils.Message;

import java.util.concurrent.ConcurrentLinkedQueue;

public class FairRoundRobin<T extends Message> implements Handles<T>
{
    private ConcurrentLinkedQueue<ThreadedHandler<Message>> handlers;

    public FairRoundRobin(ConcurrentLinkedQueue<ThreadedHandler<Message>> handlers)
    {
        this.handlers = handlers;
    }

    public void handle(T message)
    {
        boolean delivered = false;
        while (!delivered) {
            for (ThreadedHandler ho: handlers) {
                if (ho.size() < 5) {
                    ho.handle(message);
                    delivered = true;
                    break;
                }
            }
        }
    }
}
