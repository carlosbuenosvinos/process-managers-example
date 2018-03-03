package com.example.helloworld.domain.model;

import com.example.helloworld.domain.model.order.*;
import com.example.helloworld.utils.Publisher;

public class MidgetFactory
{
    private final Publisher publisher;

    public MidgetFactory(Publisher publisher) {
        this.publisher = publisher;
    }

    public Midget fromOrder(Order order) {
        if (order.isPayFirst()) {
            return new MidgetPayFirst(publisher);
        } else {
            return new MidgetPayLast(publisher);
        }
    }
}
