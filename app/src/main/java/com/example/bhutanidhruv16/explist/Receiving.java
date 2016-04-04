package com.example.bhutanidhruv16.explist;

/**
 * Created by bhutanidhruv16 on 13-Apr-16.
 */

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Receiving extends Activity {

    TextView output;
    public static String loginURL = "https://pratyush.pythonanywhere.com/hoggers/customers";
    String data = "";

    RequestQueue requestQueue;

    public void get() {
        requestQueue = Volley.newRequestQueue(this);
//        output = (TextView) findViewById(R.id.jsonData);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, loginURL, "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray ja = response.getJSONArray("customers");
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject jsonObject = ja.getJSONObject(i);
                                String username = jsonObject.getString("username");
                                String orderstring = jsonObject.getString("orderstring");
                                String calendar_date = jsonObject.getString("calendar_date");
                                String calendar_time = jsonObject.getString("calendar_time");
                                data += "Blog Number " + (i + 1) + " Blog Name= " + username + " URL= " + calendar_date + " \n ";
                            }

                            output.setText(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                }
        );
        requestQueue.add(jor);
    }

//    public static void postNewComment(Context context, final String username, final Calendar cal, final String orderhashstring) {
////        mPostCommentResponse.requestStarted();
//        RequestQueue queue = Volley.newRequestQueue(context);
//
//        String calender_date = cal.get(Calendar.DAY_OF_MONTH) + "/" + String.valueOf(cal.get(Calendar.MONTH) + 1) + "/" + String.valueOf(cal.get(Calendar.YEAR));
//        String calender_time = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
//
//        JSONObject json = new JSONObject();
//        try {
//            json.put("username", username);
//            json.put("orderstring", orderhashstring);
//            json.put("calender_date", calender_date);
//            json.put("calendar_time", calender_time);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, loginURL, json, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
////                mPostCommentResponse.requestCompleted();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
////                mPostCommentResponse.requestEndedWithError(error);
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json;charset=utf-8");
//                return headers;
//            }
//        };
//        queue.add(sr);
//    }

}