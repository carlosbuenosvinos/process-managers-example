package com.example.helloworld.handlers;

import com.example.helloworld.utils.Message;

public interface Handles<T extends Message> {
    void handle(T message);
}
