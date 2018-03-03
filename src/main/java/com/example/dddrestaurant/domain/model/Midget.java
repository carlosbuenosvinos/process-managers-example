package com.example.dddrestaurant.domain.model;

import com.example.dddrestaurant.domain.model.cook.CookTimedOut;
import com.example.dddrestaurant.domain.model.order.OrderCooked;
import com.example.dddrestaurant.domain.model.order.OrderPaid;
import com.example.dddrestaurant.domain.model.order.OrderPlaced;
import com.example.dddrestaurant.domain.model.order.OrderPriced;
import com.example.dddrestaurant.handlers.Handles;
import com.example.dddrestaurant.utils.Message;
import com.example.dddrestaurant.utils.Publisher;

public abstract class Midget implements Handles<Message>
{
    protected final Publisher publisher;

    public Midget(Publisher publisher) {
        this.publisher = publisher;
    }

    public void handle(com.example.dddrestaurant.utils.Message message) {
        if (message instanceof OrderPlaced) {
            handleOrderPlaced((OrderPlaced) message);
        } else if(message instanceof OrderCooked) {
            handleOrderCooked((OrderCooked) message);
        } else if(message instanceof OrderPriced) {
            handleOrderPriced((OrderPriced) message);
        } else if(message instanceof OrderPaid) {
            handleOrderPaid((OrderPaid) message);
        } else if(message instanceof CookTimedOut) {
            handleCookTimedOut((CookTimedOut) message);
        } else {
//            System.out.println(message.getClass().getSimpleName() + " received, but I don't know what to do???");
        }
    }

    protected abstract void handleCookTimedOut(CookTimedOut message);

    protected abstract void handleOrderPaid(OrderPaid message);

    protected abstract void handleOrderPriced(OrderPriced message);

    protected abstract void handleOrderCooked(OrderCooked message);

    protected abstract void handleOrderPlaced(OrderPlaced message);

}
