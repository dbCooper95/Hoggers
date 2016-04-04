package com.example.bhutanidhruv16.explist.db;

import android.util.Log;

import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;


public class CompletedOrders extends SugarRecord<OrderSugar> {

    public Long orderid;
    public int userid;
    public String hashmapString;
    public boolean pending;
    public Calendar cal;
    public int bill;
    public String username;

    public CompletedOrders() {
    }

    public CompletedOrders(long orderid,Calendar cal,int userid, String hashmapString, boolean pending,int bill,String username) {
        this.orderid = orderid;
        this.cal = cal;
        this.userid = userid;
        this.hashmapString = hashmapString;
        this.pending = pending;
        this.bill = bill;
        this.username = username;
    }

    public HashMap<FoodItem, Integer> stringToHashMap(String s) {
        HashMap<FoodItem, Integer> map = new HashMap<>();
        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {              // CHANGED
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                FoodItem foodItem = new FoodItem(
                        jsonObject.getString("itemName"), Integer.parseInt(jsonObject.getString("cost")),
                        jsonObject.getString("groupName"), Integer.parseInt(jsonObject.getString("refNo")),
                        jsonObject.getString("alias"), jsonObject.getString("foodtype"),
//                        jsonObject.getString("pending").equals("true"), jsonObject.getString("parcel").equals("true"));
                        jsonObject.getBoolean("pending"), jsonObject.getBoolean("parcel"));
                Log.wtf("OrderSugar", "Adding FoodItem to Hashmap " + foodItem.toString());
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
