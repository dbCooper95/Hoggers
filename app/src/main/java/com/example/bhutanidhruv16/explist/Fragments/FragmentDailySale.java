package com.example.bhutanidhruv16.explist.Fragments;

/**
 * Created by bhutanidhruv16 on 13-Feb-16.
 */

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bhutanidhruv16.explist.Adapters.DailySaleAdapter;
import com.example.bhutanidhruv16.explist.Adapters.DailySaleAdapterItemList;
import com.example.bhutanidhruv16.explist.R;
import com.example.bhutanidhruv16.explist.Utils.Constants;
import com.example.bhutanidhruv16.explist.db.CompletedOrders;
import com.example.bhutanidhruv16.explist.db.FoodItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FragmentDailySale extends Fragment {
    DailySaleAdapter adapter;                           // for orderlist
    DailySaleAdapterItemList adapter2;                  // for itemlist
    ListView lv_dailysale;
    ArrayList<CompletedOrders> orders;                  // list of all completed orders
    ArrayList<CompletedOrders> shortlisted;             // shortlisted orders
    ArrayList<String> itemname;
    ArrayList<Integer> quantity;
    Button from, to;
    TextView totalbill;
    TextView fromtext, totext;
    TextView orderlist, itemlist;
    int which = 0;                  // to =0,from=1
    int list = 0;                   // 0 for orderlist , 1 for itemlist

    public FragmentDailySale() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_daily_sale, container, false);

        orders = (ArrayList<CompletedOrders>) CompletedOrders.listAll(CompletedOrders.class);
        shortlisted = new ArrayList<>();
        itemname = new ArrayList<>();
        quantity = new ArrayList<>();

        lv_dailysale = (ListView) rootView.findViewById(R.id.lv_dailysale);
        from = (Button) rootView.findViewById(R.id.date_from);
        to = (Button) rootView.findViewById(R.id.date_to);
        fromtext = (TextView) rootView.findViewById(R.id.from_text);
        totext = (TextView) rootView.findViewById(R.id.to_text);
        totalbill = (TextView) rootView.findViewById(R.id.totalsale);

        orderlist = (TextView) rootView.findViewById(R.id.dailysale_orderlist_text);                // 0 for order list
        itemlist = (TextView) rootView.findViewById(R.id.dailysale_fooditems_text);                 // 1 for item list

        itemlist.setOnClickListener(new View.OnClickListener() {                // set adapter
            @Override
            public void onClick(View v) {
                list = 1;
                orderlist.setBackgroundColor(Color.parseColor("#ffffff"));
                itemlist.setBackgroundColor(Color.parseColor("#b9e9e9e9"));

                adapter2 = new DailySaleAdapterItemList(getActivity(), itemname, quantity);
                lv_dailysale.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();
            }
        });

        orderlist.setOnClickListener(new View.OnClickListener() {              //  set adapter
            @Override
            public void onClick(View v) {
                list = 0;
                orderlist.setBackgroundColor(Color.parseColor("#b9e9e9e9"));
                itemlist.setBackgroundColor(Color.parseColor("#ffffff"));

                adapter = new DailySaleAdapter(getActivity(), shortlisted);
                lv_dailysale.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String preday = "";
        String premonth = "";

        if (day < 10) {
            preday = "0";
        }
        if (month < 9) {
            premonth = "0";
        }

        totext.setText(preday + day + "/" + premonth + (month + 1) + "/" + String.valueOf(year).substring(2));
        fromtext.setText(preday + day + "/" + premonth + (month + 1) + "/" + String.valueOf(year).substring(2));

        int sum = 0;
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).cal.get(Calendar.YEAR) == year && orders.get(i).cal.get(Calendar.MONTH) == month && orders.get(i).cal.get(Calendar.DAY_OF_MONTH) == day) {
                shortlisted.add(orders.get(i));
                System.out.println(orders.get(i).bill);
                sum = sum + orders.get(i).bill;

                HashMap<FoodItem, Integer> orderHashMap = orders.get(i).stringToHashMap(orders.get(i).hashmapString);
                for (Map.Entry<FoodItem, Integer> entry : orderHashMap.entrySet()) {
                    if (itemname.contains(entry.getKey().itemName)) {
                        int position = itemname.indexOf(entry.getKey().itemName);
                        quantity.set(position, quantity.get(position) + entry.getValue());
                    } else {
                        itemname.add(entry.getKey().itemName);
                        quantity.add(entry.getValue());
                    }
                }
            }
        }
        totalbill.setText(getString(R.string.Rs) + sum + "");

        if (list == 0) {
            adapter = new DailySaleAdapter(getActivity(), shortlisted);
            lv_dailysale.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {       // fooditem adapter
        }

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inp[] = fromtext.getText().toString().split("/");
                inp[2] = "20" + inp[2];
                int year = Integer.parseInt(inp[2]);
                int month = Integer.parseInt(inp[1]) - 1;
                int day = Integer.parseInt(inp[0]);
                which = 1;
                new DatePickerDialog(getActivity(), pickerListener, year, month, day).show();
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which = 0;
                String inp[] = totext.getText().toString().split("/");
                inp[2] = "20" + inp[2];
                int year = Integer.parseInt(inp[2]);
                int month = Integer.parseInt(inp[1]) - 1;
                int day = Integer.parseInt(inp[0]);

                new DatePickerDialog(getActivity(), pickerListener, year, month, day).show();
            }
        });

        return rootView;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            String preday = "";
            String premonth = "";

            if (day < 10) {
                preday = "0";
            }
            if (month < 9) {
                premonth = "0";
            }

            if (which == 0) {
                totext.setText(preday + day + "/" + premonth + (month + 1) + "/" + String.valueOf(year).substring(2));
            } else {
                fromtext.setText(preday + day + "/" + premonth + (month + 1) + "/" + String.valueOf(year).substring(2));
            }

            shortlisted = new ArrayList<>();
            itemname = new ArrayList<>();
            quantity = new ArrayList<>();

            int sum = 0;
            String fdate[] = fromtext.getText().toString().split("/");
            fdate[2] = "20" + fdate[2];
            String tdate[] = totext.getText().toString().split("/");
            tdate[2] = "20" + tdate[2];

            int fromdatenumber = Integer.parseInt(fdate[2] + fdate[1] + fdate[0]);
            int todatenumber = Integer.parseInt(tdate[2] + tdate[1] + tdate[0]);

            for (int i = 0; i < orders.size(); i++) {
                preday = "";
                premonth = "";

                if (orders.get(i).cal.get(Calendar.DAY_OF_MONTH) < 10) {
                    preday = "0";
                }
                if ((orders.get(i).cal.get(Calendar.MONTH) + 1) < 10) {
                    premonth = "0";
                }

                int check = Integer.parseInt(orders.get(i).cal.get(Calendar.YEAR) + "" + premonth + (orders.get(i).cal.get(Calendar.MONTH) + 1) + "" + preday + orders.get(i).cal.get(Calendar.DAY_OF_MONTH));
                // System.out.println(fromdatenumber + " " + check + " " + todatenumber);
                if (check >= fromdatenumber && check <= todatenumber) {
                    shortlisted.add(orders.get(i));
                    sum = sum + orders.get(i).bill;

                    // for each order create a fooditem list

                    HashMap<FoodItem, Integer> orderHashMap = orders.get(i).stringToHashMap(orders.get(i).hashmapString);
                    for (Map.Entry<FoodItem, Integer> entry : orderHashMap.entrySet()) {
                        if (itemname.contains(entry.getKey().itemName)) {
                            int position = itemname.indexOf(entry.getKey().itemName);
                            quantity.set(position, quantity.get(position) + entry.getValue());
                        } else {
                            itemname.add(entry.getKey().itemName);
                            quantity.add(entry.getValue());
                        }
                    }
                }
            }

            for (int i = 0; i < itemname.size(); i++) {
                System.out.println(itemname.get(i) + " " + quantity.get(i));
            }

            totalbill.setText(getString(R.string.Rs) + sum + "");

            if (list == 0) {
                adapter = new DailySaleAdapter(getActivity(), shortlisted);
                lv_dailysale.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {   // fooditem adapter
                adapter2 = new DailySaleAdapterItemList(getActivity(), itemname, quantity);
                lv_dailysale.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();
            }
        }
    };

    public boolean isGreater(String a, String b) {            // a is from and b is to date
        String fdate[] = a.split("/");
        String tdate[] = b.split("/");

        for (int i = 2; i >= 0; i--) {
            if (Integer.parseInt(fdate[i]) > Integer.parseInt(tdate[i]))
                return false;
            else if (Integer.parseInt(fdate[i]) < Integer.parseInt(tdate[i]))
                return true;
        }
        return true;
    }
}
