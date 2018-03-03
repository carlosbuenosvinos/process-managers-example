package com.example.dddrestaurant.application;

import com.example.dddrestaurant.utils.BaseMessage;
import com.example.dddrestaurant.utils.Command;
import com.example.dddrestaurant.utils.Message;

import java.time.LocalDateTime;

public class SendToMeInCommand extends BaseMessage implements Command
{
    private final LocalDateTime when;
    private final com.example.dddrestaurant.utils.Message backMessage;

    public SendToMeInCommand(LocalDateTime when, com.example.dddrestaurant.utils.Message backMessage, String correlationId, String causationMessageId) {
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
