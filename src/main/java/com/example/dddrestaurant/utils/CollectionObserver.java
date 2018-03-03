package com.example.dddrestaurant.utils;

import com.example.dddrestaurant.domain.model.order.Order;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CollectionObserver extends Thread
{
    private final Collection<Order> collection;
    private final String name;

    public CollectionObserver(String name, ConcurrentLinkedQueue<Order> collection) {
        this.name = name;
        this.collection = collection;
    }

    public void run() {
        while (true) {
            System.out.println("Size of " + name + " is: " + collection.size());
//            for (Order order: collection) {
//                System.out.println("- Order " + order.id() + " by cook " + order.getCookName());
//            }

            try {
                Thread.sleep(1000);
            } catch(Exception e) {
            }
        }
    }
}
