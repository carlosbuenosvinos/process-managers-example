package com.example.helloworld.utils;

public abstract class BaseEvent extends BaseMessage implements Event
{
    public BaseEvent(String correlationId, String causationMessageId) {
        super(correlationId, causationMessageId);
    }
}
