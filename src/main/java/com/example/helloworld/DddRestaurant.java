package com.example.helloworld;

import com.example.helloworld.application.*;
import com.example.helloworld.application.SendToMeInCommand;
import com.example.helloworld.domain.model.AssistanceManager;
import com.example.helloworld.domain.model.Cashier;
import com.example.helloworld.domain.model.MidgetHouse;
import com.example.helloworld.domain.model.Waiter;
import com.example.helloworld.domain.model.cook.Cook;
import com.example.helloworld.domain.model.order.*;
import com.example.helloworld.handlers.*;
import com.example.helloworld.utils.*;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DddRestaurant {
    public static void main(String[] args) {

        TopicBasedPubSub pubSub = new TopicBasedPubSub();

        MidgetHouse midgetHouse = new MidgetHouse(pubSub);
        ThreadedHandler<Message> threadedMidgetHouse = new ThreadedHandler<Message>(midgetHouse);

        ConcurrentLinkedQueue<Order> ordersCooked = new ConcurrentLinkedQueue<Order>();

        ThreadedHandler<CookFoodCommand> threadedCook1 = buildCook(pubSub, "Vanessa", ordersCooked);
        ThreadedHandler<CookFoodCommand> threadedCook2 = buildCook(pubSub, "Valentina", ordersCooked);
        ThreadedHandler<CookFoodCommand> threadedCook3 = buildCook(pubSub, "Gabriela", ordersCooked);
        ThreadedHandler assistanceManager = buildAssistanceManager(pubSub);
        ThreadedHandler cashier = buildCashier(pubSub);

        ArrayList<ThreadedHandler> cookersList = new ArrayList<ThreadedHandler>();
        cookersList.add(threadedCook1);
        cookersList.add(threadedCook2);
        cookersList.add(threadedCook3);
        ThreadedHandler<CookFoodCommand> cookersMultiplexer = new ThreadedHandler<CookFoodCommand>(
            new RoundRobin<CookFoodCommand>(cookersList)
        );

        IdempotencyHandler cookers = new IdempotencyHandler(cookersMultiplexer);

        TimerService timerService = new TimerService(pubSub);

        // Subscribing Listeners
        // @TODO: every actor has to be subscribed to the Commands it understands
        pubSub.subscribe(CookFoodCommand.class, cookers);
        pubSub.subscribe(PriceOrderCommand.class, assistanceManager);
        pubSub.subscribe(TakePaymentCommand.class, cashier);
        pubSub.subscribe(SendToMeInCommand.class, timerService);

        // @TODO: the MidgetHouse only has to be subscribed to:
        // - OrderPlaced to start the process (create a MidgetPayLast)
        // - OrderCompleted to finish the process (destroy the MidgetPayLast)
        pubSub.subscribe(OrderPlaced.class, threadedMidgetHouse);
        pubSub.subscribe(OrderCompleted.class, threadedMidgetHouse);

        // Starting Actors
        cashier.start();
        assistanceManager.start();
        threadedCook1.start();
//        threadedCook2.start();
//        threadedCook3.start();
        cookersMultiplexer.start();
        threadedMidgetHouse.start();
        timerService.start();

        // Observers - debugging
        (new SizeObserver("MidgetHouse", midgetHouse)).start();
        (new CollectionObserver("Total orders cooked", ordersCooked)).start();

        // Typical order
        // 1. Build Everything
        // 2. Start Processes
        // 3. Start passing Messages

        Waiter waiter = new Waiter(pubSub);
        for (int i = 0; i < 50; i++) {
            Order newOrder;
//            if (Math.random() > 0.5) {
//                newOrder = Order.fromJson("{\"waiter\": \"Joe\",\"payFirst\": true}");
//            } else {
//                newOrder = Order.fromJson("{\"waiter\": \"Joe\",\"payFirst\": false}");
//            }

            newOrder = Order.fromJson("{\"waiter\": \"Joe\",\"payFirst\": false}");
            newOrder.changeId(String.valueOf(i));

            waiter.handle(new PlaceOrderCommand(
                UUID.randomUUID().toString(),
                "-1",
                newOrder
            ));
        }
    }

    private static ThreadedHandler<TakePaymentCommand> buildCashier(TopicBasedPubSub pubSub) {
        return new ThreadedHandler<TakePaymentCommand>(new Cashier(pubSub));
    }

    private static ThreadedHandler<PriceOrderCommand> buildAssistanceManager(TopicBasedPubSub pubSub) {
        return new ThreadedHandler<PriceOrderCommand>(new AssistanceManager(pubSub));
    }

    private static ThreadedHandler<CookFoodCommand> buildCook(TopicBasedPubSub pubSub, String cookName, ConcurrentLinkedQueue<Order> ordersCooked) {
        return new ThreadedHandler<CookFoodCommand>(
            new Cook(pubSub, cookName, ordersCooked)
        );
    }
}