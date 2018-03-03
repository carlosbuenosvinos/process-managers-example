package com.example.dddrestaurant.domain.model.order;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Order
{
    private JsonObject jsonObject;
    private boolean payFirst;

    private Order(String jsonString)
    {
        jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
    }

    public static Order fromJson(String jsonString)
    {
        return new Order(jsonString);
    }

    public void pay() {
        jsonObject.addProperty("paid", true);
    }

    public String id() {
        return jsonObject.get("id").getAsString();
    }

    public void changeId(String id) {
        jsonObject.addProperty("id", id);
    }

    public void addCookName(String name) {
        jsonObject.addProperty("cookName", name);
    }

    public void cook() {
        jsonObject.addProperty("cooked", true);
    }

    public boolean isPayFirst() {
        return jsonObject.has("payFirst") && jsonObject.get("payFirst").getAsBoolean();
    }

    public String getCookName() {
        return jsonObject.get("cookName").getAsString();
    }
}
