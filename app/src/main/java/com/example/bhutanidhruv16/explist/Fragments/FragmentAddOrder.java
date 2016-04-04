package com.example.bhutanidhruv16.explist.Fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.INotificationSideChannel;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.bhutanidhruv16.explist.Adapters.AddOrderAdapter;
import com.example.bhutanidhruv16.explist.DialogClubbedOrders;
import com.example.bhutanidhruv16.explist.Dialog_EditMenuFoodItem;
import com.example.bhutanidhruv16.explist.Dialog_clubinput;
import com.example.bhutanidhruv16.explist.OrderSelector;
import com.example.bhutanidhruv16.explist.R;
import com.example.bhutanidhruv16.explist.Utils.Constants;
import com.example.bhutanidhruv16.explist.db.CurrentOrder;
import com.example.bhutanidhruv16.explist.db.FoodItem;
import com.example.bhutanidhruv16.explist.db.Order;
import com.example.bhutanidhruv16.explist.db.OrderSugar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentAddOrder extends Fragment {
    AddOrderAdapter adapter;
    ListView lv_add_order;
    Button addorder, cluborder;
    public ArrayList<Order> currentOrders;

    public FragmentAddOrder() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_add_order, container, false);

        final SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);   // record non parcel items
        final SharedPreferences.Editor editor = pref.edit();

        addorder = (Button) rootView.findViewById(R.id.add_order_button);
        addorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("parcel", false);
                editor.commit();

                CurrentOrder.userid = 12;
                CurrentOrder.orderid = -1;
                CurrentOrder.foodItemHashMap = new HashMap<FoodItem, Integer>();

                Intent intent = new Intent(getActivity(), OrderSelector.class);
                startActivity(intent);
            }
        });

        cluborder = (Button) rootView.findViewById(R.id.club_order_button);
        cluborder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog club = new DialogClubbedOrders(getActivity(), currentOrders);
                int previousclubnum = pref.getInt("clubnum", 0);
                club.show();

                club.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        lv_add_order = (ListView) rootView.findViewById(R.id.lv_addorder);
                        adapter = new AddOrderAdapter(getActivity(), currentOrders);
                        lv_add_order.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        for (int i = 0; i < currentOrders.size(); i++) {            // to save changes in order sugar
                            currentOrders.get(i).save();
                        }
//
//                        if (pref.getInt("clubnum", 0) != previousclubnum) {
//                            previousclubnum = pref.getInt("clubnum", 0);
//                            club.show();
//                        }
                    }
                });
            }
        });

        currentOrders = new ArrayList<>();
        ArrayList<OrderSugar> orders = (ArrayList<OrderSugar>) OrderSugar.listAll(OrderSugar.class);
        Log.wtf("FragAddOrder", "CHECKING : " + orders.size());
        for (OrderSugar order : orders) {
            //     Log.wtf("FragAddOrder", "OrderString : " + order.hashmapString);
            HashMap<FoodItem, Integer> orderHashMap = order.stringToHashMap(order.hashmapString);
//            for (Map.Entry<FoodItem, Integer> entry : orderHashMap.entrySet()) {
//                System.out.println(entry.getKey().pending + " Pradeet chodu hai");
//            }
            currentOrders.add(new Order(order.orderid, order.userid, orderHashMap, order.pending));
        }

        Log.wtf("FragAddOrder", "CHECKING COUNTER : " + orders.size());
//        for (int i = 0; i < currentOrders.size(); i++) {
//            HashMap<FoodItem, Integer> orderhm2 = currentOrders.get(i).foodItemHashMap;
//            Log.i("FragAddOrder", "Printing Hashmap : " + currentOrders.get(i).mapToString(currentOrders.get(i).foodItemHashMap));
//            for (Map.Entry<FoodItem, Integer> entry : orderhm2.entrySet()) {
//                System.out.println(entry.getKey().pending + " Rohan chutiya hai");
//            }
//        }

        lv_add_order = (ListView) rootView.findViewById(R.id.lv_addorder);
        adapter = new AddOrderAdapter(getActivity(), currentOrders);
        lv_add_order.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(refreshAdapter, new IntentFilter(Constants.REFRESH_ADAPTERS));

        return rootView;
    }

    private BroadcastReceiver refreshAdapter = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Log.wtf("Fragment addOrder", "Broadcast reciever entered");
            currentOrders.clear();

            ArrayList<OrderSugar> orders = (ArrayList<OrderSugar>) OrderSugar.listAll(OrderSugar.class);
            for (OrderSugar order : orders) {
                //      Log.wtf("FragAddOrder", "OrderString : " + order.hashmapString);
                HashMap<FoodItem, Integer> orderHashMap = order.stringToHashMap(order.hashmapString);

//                for (Map.Entry<FoodItem, Integer> entry : orderHashMap.entrySet()) {
//                    System.out.println(entry.getKey().pending + " Pradeet chodu hai");
//                }

                currentOrders.add(new Order(order.orderid, order.userid, orderHashMap, order.pending));
            }
            adapter.notifyDataSetChanged();
        }
    };
}
