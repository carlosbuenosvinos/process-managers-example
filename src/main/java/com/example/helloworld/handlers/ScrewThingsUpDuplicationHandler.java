package com.example.helloworld.handlers;

import com.example.helloworld.utils.Message;

public class ScrewThingsUpDuplicationHandler<T extends Message> implements Handles<T>
{
    private final Handles<T> handler;
    private final double percentageOfDuplicatedMessages;

    public ScrewThingsUpDuplicationHandler(Handles<T> handler, double percentageOfDuplicatedMessages)
    {
        this.handler = handler;
        this.percentageOfDuplicatedMessages = percentageOfDuplicatedMessages;
    }

    public void handle(T message)
    {
        double random = Math.random();
        if (random < percentageOfDuplicatedMessages) {
            this.handler.handle(message);
            this.handler.handle(message);
        } else {
            this.handler.handle(message);
        }
    }
}
