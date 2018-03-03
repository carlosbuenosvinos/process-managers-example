package com.example.dddrestaurant.handlers;

import com.example.dddrestaurant.utils.Message;

public interface Handles<T extends Message> {
    void handle(T message);
}
