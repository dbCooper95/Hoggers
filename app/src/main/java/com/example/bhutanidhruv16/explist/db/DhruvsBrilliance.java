package com.example.bhutanidhruv16.explist.db;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.bhutanidhruv16.explist.R;
import com.example.bhutanidhruv16.explist.db.FoodItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DhruvsBrilliance extends ActionBarActivity {

    HashMap<FoodItem, Integer> hm;
    String finalans = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        hm = new HashMap<>();
        hm.put(new FoodItem("dhruv", 1), 10);
        hm.put(new FoodItem("Rishabh", 2), 20);
        mapToString(hm);
        stringToMap(finalans);

    }

    class FoodItem {
        String a;
        int b;

        public FoodItem(String a, int b) {
            this.a = a;
            this.b = b;
        }
    }

    private void mapToString(HashMap<FoodItem, Integer> map) {
        StringBuilder ans = new StringBuilder();
        ans.append("[");
        for (Map.Entry<FoodItem, Integer> val : map.entrySet()) {
            ans.append("{\"a\":\"");
            ans.append(val.getKey().a);
            ans.append("\",");

            ans.append("\"b\":\"");
            ans.append(val.getKey().b);
            ans.append("\",");

            ans.append("\"quantity\":\"");
            ans.append(val.getValue());
            ans.append("\"},");
        }
        finalans = ans.toString();
        finalans = finalans.substring(0, finalans.length() - 1);
        finalans += "]";
        //System.out.println(finalans);
    }

    private void stringToMap(String s) {
        HashMap<FoodItem, Integer> map = new HashMap<>();
        try {
            JSONArray jsonArray = new JSONArray(finalans);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                FoodItem foodItem = new FoodItem(jsonObject.getString("a"), Integer.parseInt(jsonObject.getString("b")));
                map.put(foodItem, Integer.parseInt(jsonObject.getString("quantity")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        for (Map.Entry<FoodItem, Integer> val : map.entrySet()) {
//            System.out.println(val.getKey().a + " " + val.getValue());
//        }

    }

}
