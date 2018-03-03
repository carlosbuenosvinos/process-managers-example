package com.example.dddrestaurant.utils;

public interface Message
{
    String getMessageId();
    String getCorrelationMessageId();
    String getCausationMessageId();
}
