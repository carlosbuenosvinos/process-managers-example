package com.example.helloworld.handlers;

import com.example.helloworld.utils.Message;

public class PrinterHandler implements Handles<Message>
{
    private final String topic;

    public PrinterHandler(String topic)
    {
        this.topic = topic;
    }

    public void handle(Message o) {
        System.out.println(o.getClass().toString() + ", id:" + o.getMessageId() + ", correlationId:" + o.getCorrelationMessageId() + ", causationId:" + o.getCausationMessageId());
    }
}
