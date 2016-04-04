package com.example.bhutanidhruv16.explist.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bhutanidhruv16.explist.Adapters.LoginDetailsAdapter;
import com.example.bhutanidhruv16.explist.R;
import com.example.bhutanidhruv16.explist.db.CompletedOrders;
import com.example.bhutanidhruv16.explist.db.Login;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by bhutanidhruv16 on 13-Feb-16.
 */

public class FragmentLoginDetails extends Fragment {
    LoginDetailsAdapter adapter;
    ListView lv_logindetails;
    ArrayList<CompletedOrders> orders;                  // list of all completed orders
    ArrayList<String> username;
    ArrayList<Long> amount;
    Button from, to;
    TextView totalbill;
    TextView fromtext, totext;
    int which = 0;

    public FragmentLoginDetails() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_logindetails, container, false);
        orders = (ArrayList<CompletedOrders>) CompletedOrders.listAll(CompletedOrders.class);

        lv_logindetails = (ListView) rootView.findViewById(R.id.lv_logindetails);
        from = (Button) rootView.findViewById(R.id.ld_date_from);
        to = (Button) rootView.findViewById(R.id.ld_date_to);
        fromtext = (TextView) rootView.findViewById(R.id.ld_from_text);
        totext = (TextView) rootView.findViewById(R.id.ld_to_text);
        totalbill = (TextView) rootView.findViewById(R.id.ld_totalsale);

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

        ArrayList<Login> login = (ArrayList<Login>) Login.listAll(Login.class);
        username = new ArrayList<>(login.size());
        amount = new ArrayList<>(login.size());

        for (int i = 0; i < login.size(); i++) {
            username.add("");
            amount.add((long) 0);
        }

        int sum = 0;
        for (int j = 0; j < login.size(); j++) {
            username.set(j, login.get(j).username);
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).cal.get(Calendar.YEAR) == year && orders.get(i).cal.get(Calendar.MONTH) == month && orders.get(i).cal.get(Calendar.DAY_OF_MONTH) == day) {
                    if (login.get(j).username.equals(orders.get(i).username)) {
                        amount.set(j, amount.get(j) + orders.get(i).bill);
                        sum = sum + orders.get(i).bill;
                    }
                }
            }
        }

        totalbill.setText(getString(R.string.Rs) + sum + "");

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

        adapter = new LoginDetailsAdapter(getActivity(), username, amount);
        lv_logindetails.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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

            ArrayList<Login> login = (ArrayList<Login>) Login.listAll(Login.class);
            amount = new ArrayList<>(login.size());
            for (int i = 0; i < login.size(); i++) {
                amount.add((long) 0);
            }

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
                    amount.set(username.indexOf(orders.get(i).username), amount.get(username.indexOf(orders.get(i).username)) + orders.get(i).bill);
                }
            }

            sum = 0;
            for (int i = 0; i < amount.size(); i++) {
                sum += amount.get(i);
                System.out.println(username.get(i) + " " + amount.get(i));
            }

            totalbill.setText(getString(R.string.Rs) + sum + "");

            adapter = new LoginDetailsAdapter(getActivity(), username, amount);
            lv_logindetails.setAdapter(adapter);
            adapter.notifyDataSetChanged();
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
