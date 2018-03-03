package com.example.dddrestaurant.utils;

public class SizeObserver extends Thread
{
    private final Sizable sizable;
    private final String name;

    public SizeObserver(String name, Sizable sizeable) {
        this.name = name;
        this.sizable = sizeable;
    }

    public void run() {
        while (true) {
            System.out.println("Size of " + name + " is: " + sizable.size());
            try {
                Thread.sleep(1000);
            } catch(Exception e) {
            }
        }
    }
}
