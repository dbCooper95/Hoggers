package com.example.bhutanidhruv16.explist;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bhutanidhruv16.explist.Adapters.AddOrderAdapter;
import com.example.bhutanidhruv16.explist.Utils.Constants;
import com.example.bhutanidhruv16.explist.db.FoodItem;
import com.example.bhutanidhruv16.explist.db.OrderSugar;
import com.orm.StringUtil;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dialog_search extends Dialog {

    FoodItem foodItem;
    HashMap<FoodItem, Integer> hm_order;

    public Dialog_search(Context context, FoodItem foodItem, HashMap<FoodItem, Integer> alreadyOrdered) {
        super(context);
        this.foodItem = foodItem;
        this.hm_order = alreadyOrdered;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_search);
        this.setCancelable(true);

        Button plus = (Button) findViewById(R.id.search_order_itemplus);
        Button subt = (Button) findViewById(R.id.search_order_itemsubt);
        final TextView quantity = (TextView) findViewById(R.id.search_order_itemquantity);
        TextView name = (TextView) findViewById(R.id.search_order_itemname);

        name.setText(foodItem.itemName);

        final SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0);
        final SharedPreferences.Editor editor = pref.edit();

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(quantity.getText().toString());
                quantity.setText("" + (count + 1));

                int checkpending = 0;
                for (Map.Entry<FoodItem, Integer> entry : hm_order.entrySet()) {
                    if (entry.getKey().itemName.equals(foodItem.itemName) && entry.getKey().parcel == pref.getBoolean("parcel", false)) {
                        if (entry.getKey().pending == false) {
                            hm_order.put(entry.getKey(), count + 1);
                            return;
                        } else
                            checkpending = 1;
                    }
                }

                // Log.wtf("ORDERFOODITEM ADAPTER","ARE YOU HERE "+pref.getBoolean("parcel",false));

                if (checkpending == 0) {
                    FoodItem temp = new FoodItem(foodItem.itemName, foodItem.cost, foodItem.groupName, foodItem
                            .refNo, foodItem.alias, foodItem.foodtype, foodItem.pending, pref.getBoolean("parcel", false));
                    hm_order.put(temp, 1);
                    System.out.println("new entry");

                } else {
                    FoodItem temp = new FoodItem(foodItem.itemName, foodItem.cost, foodItem.groupName, foodItem
                            .refNo, foodItem.alias, foodItem.foodtype, false, pref.getBoolean("parcel", false));
                    hm_order.put(temp, 1);
                }
            }
        });

        subt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(quantity.getText().toString());

                if (count != 0) {
                    quantity.setText("" + (--count));
                    if (count == 0) {
                        for (Map.Entry<FoodItem, Integer> entry : hm_order.entrySet()) {
                            if (entry.getKey().itemName.equals(foodItem.itemName) && entry.getKey().parcel == pref.getBoolean("parcel", false) && entry.getKey().pending == false) {
                                hm_order.remove(entry.getKey());
                                return;
                            }
                        }
                    } else {
                        for (Map.Entry<FoodItem, Integer> entry : hm_order.entrySet()) {
                            if (entry.getKey().itemName.equals(foodItem.itemName) && entry.getKey().parcel == pref.getBoolean("parcel", false) && entry.getKey().pending == false) {
                                hm_order.put(entry.getKey(), count);
                                return;
                            }
                        }
                    }
                }
            }
        });

        quantity.setText("0");
        for (Map.Entry<FoodItem, Integer> entry : hm_order.entrySet()) {
            if (entry.getKey().itemName.equals(foodItem.itemName) && entry.getKey().parcel == pref.getBoolean("parcel", false) && entry.getKey().pending == false) {             // change here
                quantity.setText("" + (entry.getValue()));
            }
        }
    }
}
