package com.example.bhutanidhruv16.explist;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.example.bhutanidhruv16.explist.Adapters.OrderFoodItemListAdapter;
import com.example.bhutanidhruv16.explist.Adapters.OrderGroupIconAdapter;
import com.example.bhutanidhruv16.explist.Utils.Constants;
import com.example.bhutanidhruv16.explist.db.CurrentOrder;
import com.example.bhutanidhruv16.explist.db.FoodItem;
import com.example.bhutanidhruv16.explist.db.GroupItem;
import com.example.bhutanidhruv16.explist.db.Order;
import com.example.bhutanidhruv16.explist.db.OrderSugar;
import com.orm.StringUtil;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OrderSelector extends AppCompatActivity implements OrderGroupIconAdapter.GroupSelectedListener {
    ListView lv_group_icons;
    ListView lv_itemlist;
    ArrayList<GroupItem> groups;
    ArrayList<FoodItem> fooditems;
    OrderGroupIconAdapter groupAdapter;
    OrderFoodItemListAdapter itemAdapter;
    FloatingActionButton floatbutton;
    Button vegtype;
    HashMap<FoodItem, Integer> hm_order;

    String[] string_items;
    int currentGroupItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_selector);

        groups = (ArrayList<GroupItem>) GroupItem.listAll(GroupItem.class);
        hm_order = CurrentOrder.foodItemHashMap;

//         System.out.println("Printing In Order Selector");
//         Printer.PrintHashMap(hm_order);

        lv_group_icons = (ListView) findViewById(R.id.listview_group_icons);
        lv_itemlist = (ListView) findViewById(R.id.order_items);
        vegtype = (Button) findViewById(R.id.vegtype);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        final SharedPreferences.Editor editor = pref.edit();

//        lv_group_icons.getChildAt(currentGroupItem).setBackgroundResource(R.color.Selected);    ERROR??

        vegtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("SELECTED POSITION = " + currentGroupItem)
                lv_group_icons.getChildAt(currentGroupItem).setBackgroundResource(R.color.Selected);
                if (vegtype.getText().toString().equals(Constants.VEG)) {
                    vegtype.setText(Constants.NONVEG);                     // remove veg and add non veg from foodItems hashmap
                    vegtype.setBackgroundResource(R.color.red);

                    groups = (ArrayList<GroupItem>) GroupItem.listAll(GroupItem.class);
//
//                    groupAdapter = new OrderGroupIconAdapter(OrderSelector.this, groups, OrderSelector.this);
//                    lv_group_icons.setAdapter(groupAdapter);
//                    groupAdapter.notifyDataSetChanged();

                    fooditems = (ArrayList<FoodItem>) Select.from(FoodItem.class).where(
                            Condition.prop(StringUtil.toSQLName("foodtype")).eq(Constants.NONVEG),
                            Condition.prop(StringUtil.toSQLName("groupName")).eq(groups.get(currentGroupItem).name)).list();

                    Log.wtf("OrderSelector", "Listing FoodItems " + fooditems);

                    itemAdapter = new OrderFoodItemListAdapter(getApplicationContext(), fooditems, hm_order);
                    lv_itemlist.setAdapter(itemAdapter);
                    itemAdapter.notifyDataSetChanged();

                } else {
                    vegtype.setText(Constants.VEG);
                    vegtype.setBackgroundResource(R.color.green_l_jade);

                    groups = (ArrayList<GroupItem>) GroupItem.listAll(GroupItem.class);

//                    groupAdapter = new OrderGroupIconAdapter(OrderSelector.this, groups, OrderSelector.this);
//                    lv_group_icons.setAdapter(groupAdapter);
//                    groupAdapter.notifyDataSetChanged();

                    fooditems = (ArrayList<FoodItem>) Select.from(FoodItem.class).where(
                            Condition.prop(StringUtil.toSQLName("foodtype")).eq(Constants.VEG),
                            Condition.prop(StringUtil.toSQLName("groupName")).eq(groups.get(currentGroupItem).name)).list();

                    //  Condition.prop(StringUtil.toSQLName("parcel")).eq(String.valueOf(true)) working

                    // Log.wtf("OrderSelector", "Listing FoodItems " + fooditems);

                    itemAdapter = new OrderFoodItemListAdapter(getApplicationContext(), fooditems, hm_order);
                    lv_itemlist.setAdapter(itemAdapter);
                    itemAdapter.notifyDataSetChanged();
                }
            }
        });

        floatbutton = (FloatingActionButton) findViewById(R.id.floating_button);
        floatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // now all elements are stored in hm_order.
                // once this activity closes, save the selected order
                CurrentOrder.foodItemHashMap = hm_order;
                int neworder = 0;
                for (Map.Entry<FoodItem, Integer> entry : hm_order.entrySet()) {
                    if (entry.getValue() > 0) {
                        neworder = 1;
                    }
                }

                //    Log.wtf("OrderSelector", "order id" + CurrentOrder.orderid);

                //if (neworder == 1) {         // if atleast one fooditem with non zero quantity then form a new order
                if (CurrentOrder.orderid != -1) {
                    System.out.println("Already Exists");
                    Order order = new Order(CurrentOrder.orderid, CurrentOrder.userid, CurrentOrder.foodItemHashMap, true);
                    order.save();
                } else {
                    System.out.println("New Order");
                    SharedPreferences pref = getApplication().getSharedPreferences("MyPref", 0);   // record non parcel items
                    SharedPreferences.Editor editor = pref.edit();

                    Order order = new Order(pref.getLong("orderid", 0), CurrentOrder.userid, CurrentOrder.foodItemHashMap, true);
                    order.save();

                    long temp = pref.getLong("orderid", 0);
                    editor.putLong("orderid", temp + 1).apply();
                }


                //}

//                ArrayList<OrderSugar> orders = (ArrayList<OrderSugar>) OrderSugar.listAll(OrderSugar.class);
//                for (OrderSugar temporder : orders) {
//                    HashMap<FoodItem, Integer> orderHashMap = temporder.stringToHashMap(temporder.hashmapString);
//                    System.out.println(temporder.hashmapString + "  Printing in order SELECTOR ");
//                }

                Intent intent = new Intent(Constants.REFRESH_ADAPTERS);                             // to change current orders
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                finish();
            }
        });

        groups = (ArrayList<GroupItem>) GroupItem.listAll(GroupItem.class);

        groupAdapter = new OrderGroupIconAdapter(OrderSelector.this, groups, OrderSelector.this);
        lv_group_icons.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();

        fooditems = (ArrayList<FoodItem>) Select.from(FoodItem.class).where(
                Condition.prop(StringUtil.toSQLName("foodtype")).eq(Constants.VEG),
                Condition.prop(StringUtil.toSQLName("groupName")).eq(groups.get(currentGroupItem).name)).list();

        itemAdapter = new OrderFoodItemListAdapter(getApplicationContext(), fooditems, hm_order);
        lv_itemlist.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

        final ArrayList<FoodItem> foodItemArrayList = (ArrayList<FoodItem>) FoodItem.listAll(FoodItem.class);
        string_items = new String[2 * foodItemArrayList.size()];

        for (int i = 0; i < foodItemArrayList.size(); i++) {
            string_items[i] = foodItemArrayList.get(i).refNo + " " + foodItemArrayList.get(i).itemName;
            string_items[i + foodItemArrayList.size()] = foodItemArrayList.get(i).alias + " " + foodItemArrayList.get(i).itemName;
        }

        final AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.orderactivity_searchbar);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, string_items);
        actv.setAdapter(adapter);
        actv.setThreshold(1);

        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = 0;
                String checkstring = "";
                String inp[] = actv.getText().toString().split(" ");

                for (int i = 1; i < inp.length; i++) {
                    checkstring = checkstring + inp[i] + " ";
                }
                checkstring = checkstring.trim();
                for (int i = 0; i < foodItemArrayList.size(); i++) {
                    if (foodItemArrayList.get(i).itemName.equals(checkstring)) {
                        index = i;
                    }
                }
//                System.out.println("CHECK HERE" + foodItemArrayList.get(index).itemName + " " + index + " " + id + " " + checkstring);
                Dialog search = new Dialog_search(OrderSelector.this, foodItemArrayList.get(index), hm_order);
                search.show();
                search.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        actv.setText("");
                        itemAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void groupSelected(GroupItem groupItem, int position) {
        System.out.println("SELECTED POSITION = " + position);
        this.currentGroupItem = position;

        ArrayList<FoodItem> groupFoodItems = new ArrayList<>();
        for (FoodItem foodItem : fooditems) {
            if (foodItem.groupName.equals(groupItem.name)) {
                groupFoodItems.add(foodItem);
            }
        }
        itemAdapter = new OrderFoodItemListAdapter(getApplicationContext(), groupFoodItems, hm_order);
        lv_itemlist.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();

        for (int i = 0; i < lv_group_icons.getChildCount(); i++) {
            lv_group_icons.getChildAt(i).setBackgroundResource(R.color.UNSELECTED);
        }
        lv_group_icons.getChildAt(position).setBackgroundResource(R.color.Selected);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);

            final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
            final SharedPreferences.Editor editor = pref.edit();

            if (vegtype.getText().toString().equals(Constants.NONVEG)) {               // form the food item list again

                fooditems = (ArrayList<FoodItem>) Select.from(FoodItem.class).where(
                        Condition.prop(StringUtil.toSQLName("foodtype")).eq(Constants.NONVEG),
                        Condition.prop(StringUtil.toSQLName("groupName")).eq(groups.get(currentGroupItem).name)).list();

                itemAdapter = new OrderFoodItemListAdapter(getApplicationContext(), fooditems, hm_order);
                lv_itemlist.setAdapter(itemAdapter);
                itemAdapter.notifyDataSetChanged();

            } else {
                fooditems = (ArrayList<FoodItem>) Select.from(FoodItem.class).where(
                        Condition.prop(StringUtil.toSQLName("foodtype")).eq(Constants.VEG),
                        Condition.prop(StringUtil.toSQLName("groupName")).eq(groups.get(currentGroupItem).name)).list();

                itemAdapter = new OrderFoodItemListAdapter(getApplicationContext(), fooditems, hm_order);
                lv_itemlist.setAdapter(itemAdapter);
                itemAdapter.notifyDataSetChanged();
            }
        }
    };
}