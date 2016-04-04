package com.example.bhutanidhruv16.explist.Adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.bhutanidhruv16.explist.R;
import com.example.bhutanidhruv16.explist.Utils.Printer;
import com.example.bhutanidhruv16.explist.db.FoodItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderFoodItemListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    public ArrayList<FoodItem> dataFood;
    HashMap<FoodItem, Integer> alreadyOrderedHM;

    public OrderFoodItemListAdapter(Context context, ArrayList<FoodItem> foodItems, HashMap<FoodItem, Integer> alreadyOrdered) {
        this.dataFood = foodItems;                  // all food items within that category
        this.context = context;
        this.alreadyOrderedHM = alreadyOrdered;     // fooditems with quantity
    }

    @Override
    public int getCount() {
        return dataFood.size();
    }

    @Override
    public Object getItem(int position) {
        return dataFood.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            vi = inflater.inflate(R.layout.element_order_fooditem, null);
            holder = new ViewHolder();
            holder.item_name = (TextView) vi.findViewById(R.id.order_itemname);
            holder.plus = (Button) vi.findViewById(R.id.order_itemplus);
            holder.subt = (Button) vi.findViewById(R.id.order_itemsubt);
            holder.quantity = (TextView) vi.findViewById(R.id.order_itemquantity);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();


        if (holder != null) {
            final FoodItem foodItem = dataFood.get(position);

            final SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
            final SharedPreferences.Editor editor = pref.edit();

            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = Integer.parseInt(holder.quantity.getText().toString());
                    holder.quantity.setText("" + (count + 1));

                    int checkpending = 0;
                    for (Map.Entry<FoodItem, Integer> entry : alreadyOrderedHM.entrySet()) {
                        if (entry.getKey().itemName.equals(foodItem.itemName) && entry.getKey().parcel == pref.getBoolean("parcel", false)) {
                            if (entry.getKey().pending == false) {
                                alreadyOrderedHM.put(entry.getKey(), count + 1);
                                return;
                            } else
                                checkpending = 1;
                        }
                    }

                    // Log.wtf("ORDERFOODITEM ADAPTER","ARE YOU HERE "+pref.getBoolean("parcel",false));

                    if (checkpending == 0) {
                        FoodItem temp = new FoodItem(foodItem.itemName, foodItem.cost, foodItem.groupName, foodItem
                                .refNo, foodItem.alias, foodItem.foodtype, foodItem.pending, pref.getBoolean("parcel", false));
                        alreadyOrderedHM.put(temp, 1);
                        System.out.println("new entry");

                    } else {
                        FoodItem temp = new FoodItem(foodItem.itemName, foodItem.cost, foodItem.groupName, foodItem
                                .refNo, foodItem.alias, foodItem.foodtype, false, pref.getBoolean("parcel", false));
                        alreadyOrderedHM.put(temp, 1);
                    }
                }
            });

            holder.subt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = Integer.parseInt(holder.quantity.getText().toString());

                    if (count != 0) {
                        holder.quantity.setText("" + (--count));
                        if (count == 0) {
                            for (Map.Entry<FoodItem, Integer> entry : alreadyOrderedHM.entrySet()) {
                                if (entry.getKey().itemName.equals(foodItem.itemName) && entry.getKey().parcel == pref.getBoolean("parcel", false) && entry.getKey().pending == false) {
                                    alreadyOrderedHM.remove(entry.getKey());
                                    return;
                                }
                            }
                        } else {
                            for (Map.Entry<FoodItem, Integer> entry : alreadyOrderedHM.entrySet()) {
                                if (entry.getKey().itemName.equals(foodItem.itemName) && entry.getKey().parcel == pref.getBoolean("parcel", false) && entry.getKey().pending == false) {
                                    alreadyOrderedHM.put(entry.getKey(), count);
                                    return;
                                }
                            }
                        }
                    }
                }
            });

//            System.out.println("In OrderFoodItemListAdapter => ");
//            Printer.PrintHashMap(alreadyOrderedHM);
//            System.out.println(foodItem.itemName);
//            System.out.println(".get(fooditem)");

            holder.quantity.setText("0");
            for (Map.Entry<FoodItem, Integer> entry : alreadyOrderedHM.entrySet()) {
                if (entry.getKey().itemName.equals(foodItem.itemName) && entry.getKey().parcel == pref.getBoolean("parcel", false) && entry.getKey().pending == false) {             // change here
                    holder.quantity.setText("" + (entry.getValue()));
                }
            }

            holder.item_name.setText(dataFood.get(position).itemName);
        }
        return vi;
    }

    public static class ViewHolder {
        TextView item_name;
        Button plus, subt;
        TextView quantity;
    }
}
