package com.example.helloworld.handlers;

import com.example.helloworld.utils.Message;

public class ScrewThingsUpHandler<T extends Message> implements Handles<T>
{
    private final Handles<T> handler;

    public ScrewThingsUpHandler(Handles<T> handler)
    {
        this.handler = handler;
    }

    public void handle(T message)
    {
        double random = Math.random();
        if (random < 0.2) {
            // Skip SendToMeInCommand (network issue)
            System.out.println("Dropped message: " + message.getClass().getName());
        } else if(random > 0.8) {
            // Double publication (network issue)
            System.out.println("Doble publication of message: " + message.getClass().getName());
            this.handler.handle(message);
            this.handler.handle(message);
        } else {
            this.handler.handle(message);
        }
    }
}
