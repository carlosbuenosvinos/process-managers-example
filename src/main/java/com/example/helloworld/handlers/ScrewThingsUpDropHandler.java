package com.example.helloworld.handlers;

import com.example.helloworld.utils.Message;

public class ScrewThingsUpDropHandler<T extends Message> implements Handles<T>
{
    private final Handles<T> handler;
    private final double percentageOfDroppedPackages;

    public ScrewThingsUpDropHandler(Handles<T> handler, double percentageOfDroppedMessages)
    {
        this.handler = handler;
        this.percentageOfDroppedPackages = percentageOfDroppedMessages;
    }

    public void handle(T message)
    {
        double random = Math.random();
        if (random < percentageOfDroppedPackages) {
            // Drop message
        } else {
            this.handler.handle(message);
        }
    }
}
