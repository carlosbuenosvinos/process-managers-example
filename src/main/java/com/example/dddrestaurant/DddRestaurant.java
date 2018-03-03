package com.example.dddrestaurant;

import com.example.dddrestaurant.application.*;
import com.example.dddrestaurant.application.SendToMeInCommand;
import com.example.dddrestaurant.domain.model.AssistanceManager;
import com.example.dddrestaurant.domain.model.Cashier;
import com.example.dddrestaurant.domain.model.MidgetHouse;
import com.example.dddrestaurant.domain.model.Waiter;
import com.example.dddrestaurant.domain.model.cook.Cook;
import com.example.dddrestaurant.domain.model.order.*;
import com.example.dddrestaurant.handlers.*;
import com.example.dddrestaurant.utils.*;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DddRestaurant {
    public static void main(String[] args) {

        double percentageOfDroppedMessages = 0.2;
        double percentageOfDuplicatedMessages = 0.2;

        // The problem talks about a Restaurant
        // In this restaurant we have a Waiter, multiple Chefs cooking in Parallel,
        // an Assistance Manager that prices all orders and a Cashier that takes
        // the payment. Our restaurant also has to support two flows, customers paying
        // first and eating then, and customers eating first and eating afterwards.
        // That's why we'll go for a Process Manager approach. Same actors with different
        // process managers can handle both flows (at the same time).

        // Steps in the exercises
        // - Write the main Actors
        // - Every Actor connects to the other actor (heavily coupled)
        // - Adding ThreadHandler, Multiplexer, Round Robin
        // - Introducing Bus
        // - Actors publishing and listening to events
        // - Introduce Process Manager (listening to Events, firing Commands and sometimes Events also)
        // - Actors listening to Commands, and firing Events
        // - Add Handlers to Drop messages and Duplicate messages
        // - Fix duplication with Idempotency
        // - Fix loosing messages with Timeout
        // - Enjoy our restaurant!! Thanks Greg!

        // PART ONE - BUILDING ACTORS AND WIRING

        // This is our Message Bus (used for Commands and Events)
        TopicBasedPubSub pubSub = new TopicBasedPubSub();

        // This is our collection of Process Managers
        // It's wrapped into a Threaded Handler that will behave as
        // the message inbox of an Actor.
        MidgetHouse midgetHouse = new MidgetHouse(pubSub);
        ThreadedHandler<Message> threadedMidgetHouse = new ThreadedHandler<Message>(midgetHouse);

        // This is our Cashier who will take payments
        ThreadedHandler cashier = buildCashier(pubSub);

        // This is our Assistance Manager who will price orders
        ThreadedHandler assistanceManager = buildAssistanceManager(pubSub);

        // This is our Waiter. It doesn't matter if it's Threaded or not
        Waiter waiter = new Waiter(pubSub);

        // Let's build our troupe of Chefs!
        // They are three receiving commands on a Round Robin style.
        // We will like to simulate that chefs loose messages and also receive duplicate messages.
        // Duplication has to be solved using idempotency checks based on messages (not orders)
        // Loosing messages will be solved with a timeout approach

        // We have a list of Orders Cooked that we'll use for debugging
        ConcurrentLinkedQueue<Order> ordersCooked = new ConcurrentLinkedQueue<Order>();
        ThreadedHandler<CookFoodCommand> threadedCook1 = buildCook(pubSub, "Ferran Adrià", ordersCooked);
        ThreadedHandler<CookFoodCommand> threadedCook2 = buildCook(pubSub, "Albert Adrià", ordersCooked);
        ThreadedHandler<CookFoodCommand> threadedCook3 = buildCook(pubSub, "Joan Roca", ordersCooked);

        ConcurrentLinkedQueue<ThreadedHandler<CookFoodCommand>> cookers = new ConcurrentLinkedQueue<ThreadedHandler<CookFoodCommand>>();
        cookers.add(threadedCook1);
        cookers.add(threadedCook2);
        cookers.add(threadedCook3);
        ThreadedHandler<CookFoodCommand> cookersMultiplexer = new ThreadedHandler<CookFoodCommand>(
            new FairRoundRobin(cookers)
        );

        IdempotentHandler idempotentMultiplexedCookers = new IdempotentHandler(cookersMultiplexer);
        Handles realCookers = new ScrewThingsUpDropHandler(
            new ScrewThingsUpDuplicationHandler(
                idempotentMultiplexedCookers,
                percentageOfDuplicatedMessages
            ),
            percentageOfDroppedMessages
        );

        // This is responsible for handling TimeOuts
        TimerService timerService = new TimerService(pubSub);

        // Subscribing Listeners
        // @TODO: every actor has to be subscribed to the Commands it understands
        pubSub.subscribe(CookFoodCommand.class, realCookers);
        pubSub.subscribe(PriceOrderCommand.class, assistanceManager);
        pubSub.subscribe(TakePaymentCommand.class, cashier);
        pubSub.subscribe(SendToMeInCommand.class, timerService);

        // @TODO: the MidgetHouse only has to be subscribed to:
        // - OrderPlaced to start the process (create a MidgetPayLast)
        // - OrderCompleted to finish the process (destroy the MidgetPayLast)
        pubSub.subscribe(OrderPlaced.class, threadedMidgetHouse);
        pubSub.subscribe(OrderCompleted.class, threadedMidgetHouse);

        // PART TWO - STARTING ACTORS

        cashier.start();
        assistanceManager.start();
        threadedCook1.start();
        threadedCook2.start();
        threadedCook3.start();
        cookersMultiplexer.start();
        threadedMidgetHouse.start();
        timerService.start();

        (new SizeObserver("MidgetHouse", midgetHouse)).start();
        (new CollectionObserver("Total orders cooked", ordersCooked)).start();

        // PART THREE - RUNNING OUR PROGRAM PASSING FIRST MESSAGES

        for (int i = 0; i < 100; i++) {
            Order newOrder;
            // You can elaborate more the messages (more fields, lines, quantities, status flags, etc.)

            // 50% of Orders will be of customers that want to pay first
            // other 50% will pay last. This is handle by a flag. The MidgetHouse contains
            // a factory that will instance it a pay first midget (PM) or pay last.
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
