package com.example.bhutanidhruv16.explist.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bhutanidhruv16.explist.R;
import com.example.bhutanidhruv16.explist.Utils.AppController;
import com.example.bhutanidhruv16.explist.db.CurrentOrder;
import com.example.bhutanidhruv16.explist.db.FoodItem;
import com.example.bhutanidhruv16.explist.db.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bhutanidhruv16 on 13-Feb-16.
 */

public class FragmentProbableOrders extends Fragment {

    public FragmentProbableOrders() {

    }

    static Button refresh = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_probableorders, container, false);

        final LinearLayout summary = (LinearLayout) rootView.findViewById(R.id.probable_orders_ll);

        refresh = (Button) rootView.findViewById(R.id.refresh_probable_orders);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNew(getActivity());
                final JSONArray[] array = {null};
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (jsonData != null)
                                array[0] = jsonData.getJSONArray("users");
//                            String temp = array[0].getJSONObject(0).getString("username");
//                            System.out.println(temp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        summary.removeAllViews();

                        if (array[0] != null) {
                            for (int i = 0; i < array[0].length(); i++) {
                                Calendar cal = Calendar.getInstance();
                                String calender_date = cal.get(Calendar.DAY_OF_MONTH) + "/" + String.valueOf(cal.get(Calendar.MONTH) + 1) + "/" + String.valueOf(cal.get(Calendar.YEAR));
                                try {
                                    if (array[0].getJSONObject(i).getString("date").equals(calender_date)) {
                                        LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        View inflatedView = inflater1.inflate(R.layout.element_probable_orders, null, false);

                                        TextView name = (TextView) inflatedView.findViewById(R.id.probable_orders_username);
                                        TextView time = (TextView) inflatedView.findViewById(R.id.probable_orders_date_time);

                                        try {
                                            name.setText(array[0].getJSONObject(i).getString("username"));
                                            time.setText(array[0].getJSONObject(i).getString("date") + "\n" + array[0].getJSONObject(i).getString("time"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        final SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);   // record non parcel items
                                        final SharedPreferences.Editor editor = pref.edit();

                                        final int finalI = i;
                                        inflatedView.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View v) {
                                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        switch (which) {
                                                            case DialogInterface.BUTTON_POSITIVE:
                                                                try {
                                                                    CurrentOrder.foodItemHashMap = stringToHashMap(array[0].getJSONObject(finalI).getString("orderstring"));
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }

                                                                try {
//                                                                System.out.println("https://pratyush.pythonanywhere.com/users/delete/" + array[0].getJSONObject(finalI).getString("id"));
                                                                    remove(getActivity(), "https://pratyush.pythonanywhere.com/users/delete/" + array[0].getJSONObject(finalI).getString("id"));
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }

                                                                Order order = new Order(pref.getLong("orderid", 0), CurrentOrder.userid, CurrentOrder.foodItemHashMap, true);
                                                                order.save();

                                                                long temp = pref.getLong("orderid", 0);
                                                                editor.putLong("orderid", temp + 1).apply();
                                                                break;

                                                            case DialogInterface.BUTTON_NEGATIVE:
                                                                //No button clicked
                                                                break;
                                                        }
                                                    }
                                                };

                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setMessage("Are you sure you want to move this to current orders?").setPositiveButton("Yes", dialogClickListener)
                                                        .setNegativeButton("No", dialogClickListener).show();

                                                return true;
                                            }
                                        });
                                        summary.addView(inflatedView);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }, 5000);
            }
        });
        return rootView;
    }


    public static String loginURL = "https://pratyush.pythonanywhere.com/users";
    public static JSONObject jsonData;

    public static void getNew(Context context) {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, loginURL, "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SendingData", response.toString());
                        jsonData = response;
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("SendingData", "Error: " + error.getMessage());
                Log.wtf("SD", error.getMessage());
                Log.wtf("SD", "" + error.getStackTrace());
                pDialog.hide();
            }
        }) {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public static void remove(Context context, String removeURL) {
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, removeURL, "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RemovingData", response.toString());
                        pDialog.hide();
                        refresh.performClick();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("RemovingData", "Error: " + error.getMessage());
                Log.wtf("SD", error.getMessage());
                Log.wtf("SD", "" + error.getStackTrace());
                pDialog.hide();
            }
        }) {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq);
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
