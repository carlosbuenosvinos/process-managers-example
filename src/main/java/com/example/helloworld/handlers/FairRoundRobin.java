package com.example.helloworld.handlers;

import com.example.helloworld.utils.Message;

import java.util.ArrayList;

public class FairRoundRobin<T extends Message> implements Handles<T>
{
    private ArrayList<ThreadedHandler<Message>> handleOrders;

    public FairRoundRobin(ArrayList<ThreadedHandler<Message>> handlers)
    {
         this.handleOrders = (ArrayList<ThreadedHandler<Message>>) handlers.clone();
    }

    public void handle(T message)
    {
        boolean delivered = false;
        while (!delivered) {
            for (ThreadedHandler ho: this.handleOrders) {
                if (ho.size() < 5) {
                    ho.handle(message);
                    delivered = true;
                    break;
                }
            }
        }
    }
}
