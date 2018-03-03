package com.example.dddrestaurant.handlers;

import com.example.dddrestaurant.utils.Message;
import com.example.dddrestaurant.utils.Sizable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadedHandler<T extends Message> extends Thread implements Handles<T>, Sizable
{
    private Handles<T> handler;
    private ConcurrentLinkedQueue<T> inboxQueue;

    public ThreadedHandler(Handles<T> handler) {
        this.handler = handler;
        this.inboxQueue = new ConcurrentLinkedQueue<T>();
    }

    public void handle(T message) {
        this.inboxQueue.add(message);
    }

    public int size() {
        return this.inboxQueue.size();
    }

    public void run()
    {
        while (true) {
            T message = inboxQueue.poll();
            if (message != null) {
                this.handler.handle(message);
            }
        }
    }
}
