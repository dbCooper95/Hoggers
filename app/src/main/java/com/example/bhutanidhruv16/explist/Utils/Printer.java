package com.example.bhutanidhruv16.explist.Utils;

import android.widget.ListView;

import com.example.bhutanidhruv16.explist.db.FoodItem;
import com.example.bhutanidhruv16.explist.db.OrderSugar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Printer {

    public static void PrintHashMap(HashMap<FoodItem, Integer> hashMap) {
        System.out.println("Printing HashMap");
        for (Map.Entry<FoodItem, Integer> entry : hashMap.entrySet()) {
            System.out.println(entry.getKey().itemName + " " + entry.getValue());
        }
        System.out.println("---");
    }

    public static void PrintDatabase() {
        List<OrderSugar> orderSugars = OrderSugar.listAll(OrderSugar.class);
        for (OrderSugar orderSugar : orderSugars) {
            System.out.println(orderSugar.getId() + " ");
            PrintHashMap(orderSugar.stringToHashMap(orderSugar.hashmapString));
        }
    }

}
