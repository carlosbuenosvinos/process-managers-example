package com.example.dddrestaurant.domain.model;

import com.example.dddrestaurant.domain.model.order.OrderCompleted;
import com.example.dddrestaurant.domain.model.order.OrderPlaced;
import com.example.dddrestaurant.handlers.Handles;
import com.example.dddrestaurant.utils.Message;
import com.example.dddrestaurant.utils.Sizable;
import com.example.dddrestaurant.utils.TopicBasedPubSub;

import java.util.HashMap;

public class MidgetHouse implements Handles<Message>, Sizable
{
    // @TODO: The goal is to make easier to debug problems

    private final TopicBasedPubSub publisher;
    private final MidgetFactory midgetFactory;
    private HashMap<String, Midget> midgets;

    public MidgetHouse(TopicBasedPubSub publisher)
    {
        this.publisher = publisher;
        this.midgets = new HashMap<String, Midget>();
        this.midgetFactory = new MidgetFactory(this.publisher);
    }

    public int size()
    {
        return midgets.size();
    }

    public void handle(Message message) {
        if (message instanceof OrderPlaced) {
            handleOrderPlaced((OrderPlaced) message);
        } else if(message instanceof OrderCompleted) {
            handleOrderCompleted((OrderCompleted) message);
        } else {
            handleOtherMessage(message);
        }
    }

    private void handleOrderPlaced(OrderPlaced message)
    {
        Midget midget = midgetFactory.fromOrder(message.getOrder());
        midgets.put(message.getCorrelationMessageId(), midget);

        this.publisher.subscribe(
            message.getCorrelationMessageId(),
            midget
        );

        this.publisher.publish(
            message.getCorrelationMessageId(),
            message
        );
    }

    private void handleOrderCompleted(OrderCompleted message)
    {
        Midget midget = ofId(message);
        if (null != midget) {
            this.publisher.unsubscribe(
                message.getCorrelationMessageId(),
                midget
            );

            midgets.remove(message.getCorrelationMessageId());
        }
    }

    private void handleOtherMessage(Message message)
    {
        // @TODO: Has the House receive all the messages (commands and events) and pass them to the
        // specific midget, or the midget will be listening to such messages directly using the specific topic per process.

//        Midget midget = ofId(message);
//        if (null != midget) {
//            //System.out.println("Did found a MidgetPayLast!");
//            midget.handle(message);
//        } else {
//            //System.out.println("Did NOT found a MidgetPayLast!");
//        }
    }

    private Midget ofId(Message message) {
        return midgets.getOrDefault(message.getCorrelationMessageId(), null);
    }
}
