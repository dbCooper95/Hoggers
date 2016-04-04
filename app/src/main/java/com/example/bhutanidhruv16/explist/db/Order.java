package com.example.bhutanidhruv16.explist.db;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.bhutanidhruv16.explist.Adapters.AddOrderAdapter;
import com.example.bhutanidhruv16.explist.Utils.Printer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Order {

    public long orderid;
    public int userid;
    public HashMap<FoodItem, Integer> foodItemHashMap;
    public boolean pending;

    public Order(long orderid, int userid, HashMap<FoodItem, Integer> foodItemHashMap, boolean pending) {
        this.orderid = orderid;
        this.userid = userid;
        this.foodItemHashMap = foodItemHashMap;
        this.pending = pending;
    }

    public void save() {
        String map = mapToString(this.foodItemHashMap);
        // System.out.println(this.userid + " " + map + " " + this.pending);

        ArrayList<OrderSugar> all_orders = (ArrayList<OrderSugar>) OrderSugar.listAll(OrderSugar.class);
        OrderSugar orderSugar = null;
        for (int i = 0; i < all_orders.size(); i++) {
            if (all_orders.get(i).orderid == orderid) {
                orderSugar = all_orders.get(i);
            }
        }

        // System.out.println("is order sugar null : " + orderSugar == null);
        // Printer.PrintHashMap(this.foodItemHashMap);

        //System.out.println(this.userid + " " + map + " " + this.pending);

        if (orderSugar == null) {
            System.out.println("IS NULL, saving new");
            OrderSugar newOrder = new OrderSugar(this.orderid, this.userid, map, this.pending);
            newOrder.save();
        } else {
            System.out.println("Exists, updating");

            System.out.println(this.userid + " " + map + " " + this.pending);
            //    System.out.println("CHECK WITH THIS"+orderSugar.hashmapString);

            orderSugar.orderid = orderid;
            orderSugar.hashmapString = map;
            System.out.println(orderSugar.hashmapString);       // same as map
            orderSugar.userid = this.userid;
            orderSugar.pending = this.pending;
            orderSugar.save();

            ArrayList<OrderSugar> orders = (ArrayList<OrderSugar>) OrderSugar.listAll(OrderSugar.class);
//            for (OrderSugar order : orders) {
//                System.out.println("After the change" + order.hashmapString);
//            }
        }
        // Printer.PrintDatabase();
    }

    public String mapToString(HashMap<FoodItem, Integer> map) {
        String finalans = "";
        StringBuilder ans = new StringBuilder();
        ans.append("[");
        for (Map.Entry<FoodItem, Integer> val : map.entrySet()) {
            ans.append("{\"itemName\":\"");
            ans.append(val.getKey().itemName);
            ans.append("\",");

            ans.append("\"cost\":\"");
            ans.append(val.getKey().cost);
            ans.append("\",");

            ans.append("\"groupName\":\"");
            ans.append(val.getKey().groupName);
            ans.append("\",");

            ans.append("\"refNo\":\"");
            ans.append(val.getKey().refNo);
            ans.append("\",");

            ans.append("\"alias\":\"");
            ans.append(val.getKey().alias);
            ans.append("\",");

            ans.append("\"foodtype\":\"");
            ans.append(val.getKey().foodtype);
            ans.append("\",");

            ans.append("\"pending\":\"");                       // ADDED
            ans.append(val.getKey().pending);
            ans.append("\",");

            ans.append("\"parcel\":\"");
            ans.append(val.getKey().parcel);
            ans.append("\",");

            ans.append("\"quantity\":\"");
            ans.append(val.getValue());
            ans.append("\"},");
        }
        finalans = ans.toString();
        finalans = finalans.substring(0, finalans.length() - 1);
        finalans += "]";
        return finalans;
    }

    public HashMap<FoodItem, Integer> stringToHashMap(String s) {
        HashMap<FoodItem, Integer> map = new HashMap<>();
        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {      // CHANGED
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                FoodItem foodItem = new FoodItem(
                        jsonObject.getString("itemName"), Integer.parseInt(jsonObject.getString("cost")),
                        jsonObject.getString("groupName"), Integer.parseInt(jsonObject.getString("refNo")),
                        jsonObject.getString("alias"), jsonObject.getString("foodtype"),
                        jsonObject.getString("pending").equals("true"), jsonObject.getString("parcel").equals("true")); // or get boolean
                map.put(foodItem, Integer.parseInt(jsonObject.getString("quantity")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        for (Map.Entry<FoodItem, Integer> val : map.entrySet()) {
//            System.out.println(val.getKey().a + " " + val.getValue());
//        }

        return map;
    }
}