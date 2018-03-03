package com.example.dddrestaurant.domain.model;

import com.example.dddrestaurant.domain.model.order.*;
import com.example.dddrestaurant.utils.Publisher;

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
