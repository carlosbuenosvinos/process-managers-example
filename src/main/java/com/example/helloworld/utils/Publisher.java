package com.example.helloworld.utils;

public interface Publisher<T>
{
    void publish(String topic, T message);
    void publish(T message);
}
