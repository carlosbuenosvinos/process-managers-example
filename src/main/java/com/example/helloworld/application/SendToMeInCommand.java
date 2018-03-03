package com.example.helloworld.application;

import com.example.helloworld.utils.BaseMessage;
import com.example.helloworld.utils.Command;
import com.example.helloworld.utils.Message;

import java.time.LocalDateTime;

public class SendToMeInCommand extends BaseMessage implements Command
{
    private final LocalDateTime when;
    private final com.example.helloworld.utils.Message backMessage;

    public SendToMeInCommand(LocalDateTime when, com.example.helloworld.utils.Message backMessage, String correlationId, String causationMessageId) {
        super(correlationId, causationMessageId);
        this.when = when;
        this.backMessage = backMessage;
    }

    public LocalDateTime getWhen() {
        return when;
    }

    public Message getBackMessage() {
        return backMessage;
    }
}
