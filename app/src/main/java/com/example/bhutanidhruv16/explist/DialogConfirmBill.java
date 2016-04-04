package com.example.bhutanidhruv16.explist;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhutanidhruv16.explist.db.CompletedOrders;
import com.example.bhutanidhruv16.explist.db.CurrentOrder;
import com.example.bhutanidhruv16.explist.db.FoodItem;
import com.example.bhutanidhruv16.explist.db.Login;
import com.example.bhutanidhruv16.explist.db.Order;
import com.example.bhutanidhruv16.explist.db.OrderSugar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DialogConfirmBill extends Dialog {

    Button paid, send;
    Button cancel;
    TextView finalbill, balance_amt;
    EditText discount, phonenum, amountpaid;
    int bill;
    long ordernum;
    CheckBox receive;
    LinearLayout ll_send;

    public DialogConfirmBill(Context context, int bill, long ordernum) {
        super(context);
        this.bill = bill;
        this.ordernum = ordernum;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm_bill);

        this.setCancelable(true);

        paid = (Button) findViewById(R.id.billpaid);
        cancel = (Button) findViewById(R.id.cancel);
        finalbill = (TextView) findViewById(R.id.finalbilldisplay);
        discount = (EditText) findViewById(R.id.discount);
        receive = (CheckBox) findViewById(R.id.checkbox_receive_msg);
        phonenum = (EditText) findViewById(R.id.phonenumber);
        send = (Button) findViewById(R.id.sendmessage);
        ll_send = (LinearLayout) findViewById(R.id.ll_send);

        finalbill.setText("" + bill);
        discount.addTextChangedListener(mTextEditorWatcher);


        amountpaid = (EditText) findViewById(R.id.amountpaid);
        balance_amt = (TextView) findViewById(R.id.amount_balance);

        amountpaid.addTextChangedListener(mTextEditorWatcher2);

        receive.setChecked(false);

        receive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    ll_send.setVisibility(View.VISIBLE);
                } else
                    ll_send.setVisibility(View.GONE);
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSMessage();
            }
        });

        paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change bill amount to finalbilldisplay.getText();
                //  move order to completed orders

                Log.d("DiaologConfirmBill ", ordernum + "");

                if (amountpaid.getText().toString().equals("") || (!amountpaid.getText().toString().equals("") && Integer.parseInt(amountpaid.getText().toString()) >= bill)) {
                    ArrayList<OrderSugar> orders = (ArrayList<OrderSugar>) OrderSugar.listAll(OrderSugar.class);
                    for (OrderSugar order : orders) {
                        // Find order
                        //System.out.println("Printing ordernum "+order.getId());
                        if (order.orderid == ordernum) {
                            order.pending = false;
                            Log.d("DiaologConfirmBill", ordernum + " ENTERED");
                            order.save();

                            // System.out.println(order.getId()+"  "+order.userid+" "+order.hashmapString);
                            SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0);
                            final SharedPreferences.Editor editor = pref.edit();

                            String username = "";
                            ArrayList<Login> loginrecords = (ArrayList<Login>) Login.listAll(Login.class);
                            for (int i = 0; i < loginrecords.size(); i++) {
                                if (loginrecords.get(i).loginid == pref.getLong("loggedin", 0)) {
                                    username = loginrecords.get(i).username;
                                }
                            }
                            CompletedOrders completeorder = new CompletedOrders(order.orderid, Calendar.getInstance(), order.userid, order.hashmapString, true, bill, username);
                            completeorder.save();

                            order.delete();
                            break;
                        }
                    }
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Collect full amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {  // do nothing
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            if (!String.valueOf(s).equals("")) {
                double d = bill - Float.parseFloat(String.valueOf(s)) / 100 * bill;
                DecimalFormat f = new DecimalFormat("##.0");
                finalbill.setText("" + f.format(d));              // make changes once the bill is calculated

            } else
                finalbill.setText("" + bill);
        }
    };

    private final TextWatcher mTextEditorWatcher2 = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            if (!String.valueOf(s).equals("")) {
                double d = Integer.parseInt(amountpaid.getText().toString()) - Float.parseFloat(finalbill.getText().toString());
                DecimalFormat f = new DecimalFormat("##.0");
                balance_amt.setText("" + f.format(d));              // make changes once the bill is calculated
            } else
                balance_amt.setText("0");
        }
    };

    protected void sendSMSMessage() {

        String message = "";

        HashMap<FoodItem, Integer> orderhm2 = new HashMap<>();

        ArrayList<OrderSugar> orders = (ArrayList<OrderSugar>) OrderSugar.listAll(OrderSugar.class);

        for (OrderSugar order : orders) {
            if (order.orderid == ordernum) {                                // to be changed order.getID
                orderhm2 = order.stringToHashMap(order.hashmapString);
            }
        }

        message += "Order number: " + ordernum + "\n";
        int count = 1;

        for (Map.Entry<FoodItem, Integer> entry : orderhm2.entrySet()) {
            message += count + ") " + entry.getKey().itemName + " " + entry.getKey().groupName + ", price " + entry.getKey().cost + ", quantity " + entry.getValue() + "\n";
            count++;
        }

        message = message + "Total bill: " + bill + "\n";

        message += "Hoggers Den";
        Log.d("MESSAGE", "" + message);
        String phoneNo = phonenum.getText().toString();
        if (phoneNo.length() != 10) {
            Toast.makeText(getContext(), "Phone number not valid", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}

// Using order no. create bill ... check for mobile numbers length