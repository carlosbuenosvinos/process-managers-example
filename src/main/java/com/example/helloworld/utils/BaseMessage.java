package com.example.helloworld.utils;

import java.util.UUID;

public class BaseMessage implements Message
{
    protected String messageId;
    protected String correlationMessageId;
    protected String causationMessageId;

    public BaseMessage(String correlationId, String causationMessageId)
    {
        this.messageId = UUID.randomUUID().toString();
        this.correlationMessageId = correlationId;
        this.causationMessageId = causationMessageId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getCorrelationMessageId() {
        return correlationMessageId;
    }

    public String getCausationMessageId() {
        return causationMessageId;
    }
}
