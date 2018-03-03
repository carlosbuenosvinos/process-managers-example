package com.example.helloworld.domain.model;

import com.example.helloworld.application.TakePaymentCommand;
import com.example.helloworld.domain.model.order.Order;
import com.example.helloworld.domain.model.order.OrderPaid;
import com.example.helloworld.handlers.Handles;
import com.example.helloworld.utils.Publisher;

import java.util.ArrayList;
import java.util.Iterator;

public class Cashier implements Handles<TakePaymentCommand>
{
    private ArrayList<Order> orders;
    private Publisher publisher;

    public Cashier(Publisher publisher) {
        this.publisher = publisher;
        this.orders = new ArrayList<Order>();
    }

    public void handle(TakePaymentCommand message)
    {
        try {
            Thread.sleep(200);
        } catch(Exception e) {

        }

        this.publisher.publish(new OrderPaid(
            message.getCorrelationMessageId(),
            message.getMessageId(),
            message.order
        ));
    }

    // @TODO: Implement Payment
    public void pay(String orderId)
    {
        Order o;
        Iterator<Order> it = orders.iterator();
        while(it.hasNext()) {
            o = it.next();
            if (o.id().equals(orderId)) {
                o.pay();
            }

            break;
        }
    }

    public ArrayList<Order> getOutstandingOrders()
    {
        return orders;
    }
}
