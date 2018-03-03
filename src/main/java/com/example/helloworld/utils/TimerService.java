package com.example.helloworld.utils;

import com.example.helloworld.application.SendToMeInCommand;
import com.example.helloworld.handlers.Handles;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TimerService extends Thread implements Handles<SendToMeInCommand>
{
    private final TopicBasedPubSub bus;
    private final ConcurrentLinkedQueue<SendToMeInCommand> timeouts;

    public TimerService(TopicBasedPubSub bus)
    {
        this.bus = bus;
        this.timeouts = new ConcurrentLinkedQueue<SendToMeInCommand>();
    }

    public void handle(SendToMeInCommand message)
    {
        this.timeouts.add(message);
//        System.out.println(timeouts.size());
    }

    public void run()
    {
        while (true) {
            LocalDateTime now = LocalDateTime.now();
            for(SendToMeInCommand c: timeouts) {
                if (now.isAfter(c.getWhen())) {
                    bus.publish(
                        c.getBackMessage().getCorrelationMessageId(),
                        c.getBackMessage()
                    );

                    timeouts.remove(c);
                }
            }

            try {
                Thread.sleep(500);
            } catch (Exception e) {

            }
        }
    }
}
