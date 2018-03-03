package com.example.helloworld.utils;

import com.example.helloworld.handlers.Handles;
import java.util.ArrayList;
import java.util.HashMap;

public class TopicBasedPubSub implements Publisher<Message>
{
    private HashMap<String, ArrayList<Handles<Message>>> dictionary;

    public TopicBasedPubSub()
    {
        this.dictionary = new HashMap<String, ArrayList<Handles<Message>>>();
    }

    public void publish(String topic, Message message)
    {
        ArrayList<Handles<Message>> handlers = (ArrayList<Handles<Message>>) this.dictionary.getOrDefault(topic, new ArrayList<Handles<Message>>()).clone();
        if (handlers.isEmpty()) {
            return;
        }

//        System.out.println("✉️ " + message.getClass().getSimpleName() + " published into '" + topic + "(" + handlers.size() + ")' (" + message.getMessageId() + ", " + message.getCorrelationMessageId() + ", " + message.getCausationMessageId());

        for (Handles<Message> handle : handlers) {
            try {
                handle.handle(message);
            } catch(Exception e) {

            }
        }
    }

    public void publish(Message message)
    {
        publish(message.getClass().getSimpleName(), message);
        publish(message.getCorrelationMessageId(), message);
    }

    public <U extends Message> void subscribe(Class messageType, Handles<U> handler)
    {
        subscribe(messageType.getSimpleName(), handler);
    }

    public <U extends Message> void subscribe(String topic, Handles<U> handler)
    {
        ArrayList<Handles<Message>> hos = this.dictionary.getOrDefault(topic, new ArrayList<Handles<Message>>());
        hos.add((Handles<Message>) handler);
        dictionary.put(topic, hos);

//        System.out.println("New handler for topic (" + topic + ")");
    }

    public void unsubscribe(String topic, Handles<Message> handler) {
        ArrayList<Handles<Message>> hos = this.dictionary.getOrDefault(topic, null);
        if (hos != null) {
            hos.remove(handler);
//            System.out.println("Deleted handler for topic (" + topic + ")");
        }
    }
}
