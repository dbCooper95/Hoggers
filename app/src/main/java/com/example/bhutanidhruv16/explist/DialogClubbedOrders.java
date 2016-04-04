package com.example.bhutanidhruv16.explist;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bhutanidhruv16.explist.Adapters.ClubbedOrderAdapter;
import com.example.bhutanidhruv16.explist.db.FoodItem;
import com.example.bhutanidhruv16.explist.db.Order;
import com.example.bhutanidhruv16.explist.db.OrderSugar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DialogClubbedOrders extends Dialog {

    ListView lv_cluborders;
    ArrayList<Order> currentOrders;
    EditText clubnum;
    Button modify_clubnum;

    public DialogClubbedOrders(Context context, ArrayList<Order> currentOrders) {
        super(context);
        this.currentOrders = currentOrders;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Clubbed Orders");

        setContentView(R.layout.dialog_clubbedorders);
        this.setCancelable(true);

        lv_cluborders = (ListView) findViewById(R.id.lv_clubbedorders);

        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0);
        final SharedPreferences.Editor editor = pref.edit();

        int count = 0;
        ArrayList<String> distinctNames = new ArrayList<>();
        ArrayList<FoodItem> foodItem = new ArrayList<>();
        ArrayList<Long> ordernum = new ArrayList<>();
        ArrayList<Integer> quantity = new ArrayList<>();

        for (int i = 0; i < currentOrders.size(); i++) {
            if (count == pref.getInt("clubnum", 0))
                break;
            Order order = currentOrders.get(i);
            HashMap<FoodItem, Integer> orderHashMap = order.foodItemHashMap;
//            System.out.println("IDHAR DEKH BHAI CLUB ORDDER "+order.pending);

            int include = 0;
            for (Map.Entry<FoodItem, Integer> entry : orderHashMap.entrySet()) {
                if (entry.getKey().pending == false) {
                    include = 1;
                }
            }

            if (include == 1) {
                for (Map.Entry<FoodItem, Integer> entry : orderHashMap.entrySet()) {
                    if (entry.getKey().pending == false) {
                        if (!distinctNames.contains(entry.getKey().itemName))
                            distinctNames.add(entry.getKey().itemName);
                        foodItem.add(entry.getKey());
                        ordernum.add(order.orderid);
                        quantity.add(entry.getValue());
                    }
                }
                count++;
            }

        }


        lv_cluborders.setAdapter(new ClubbedOrderAdapter(getContext(), distinctNames, foodItem, ordernum, quantity));

        clubnum = (EditText) findViewById(R.id.numberClubbedorders);
        modify_clubnum = (Button) findViewById(R.id.modify_clubnum);

        clubnum.setText(pref.getInt("clubnum", 0) + "");

        modify_clubnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = clubnum.getText().toString();
                if (!number.equals("")) {
                    int num = Integer.parseInt(number);
                    editor.putInt("clubnum", num);
                    editor.commit();
                    dismiss();
                } else
                    Toast.makeText(getContext(), "Enter valid number", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
