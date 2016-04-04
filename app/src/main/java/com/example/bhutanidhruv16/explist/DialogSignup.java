package com.example.bhutanidhruv16.explist;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

public class DialogSignup extends Dialog {

    EditText username, pass, confirmpass, adminpass;
    Button adduser;

    public DialogSignup(Context context) {
        super(context);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Create Account");
        setContentView(R.layout.dialog_signup);

        username = (EditText) findViewById(R.id.newuser);
        pass = (EditText) findViewById(R.id.newuserpass);
        confirmpass = (EditText) findViewById(R.id.newuserconfirmpass);

        adminpass = (EditText) findViewById(R.id.adminconfirmation);

        adduser = (Button) findViewById(R.id.addnewuser);

        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pas = pass.getText().toString();
                String confirmpas = confirmpass.getText().toString();
                String adminpas = adminpass.getText().toString();

                if (user.equals("") || pas.equals("") || confirmpas.equals("") || adminpas.equals("")) {
                    Toast.makeText(getContext(), "Enter all details", Toast.LENGTH_SHORT).show();
                } else {
                    int checkusername = 0;
                    String actualadminpass = "";
                    ArrayList<Login> loginrecords = (ArrayList<Login>) Login.listAll(Login.class);
                    for (int i = 0; i < loginrecords.size(); i++) {
                        if (loginrecords.get(i).username.equals(user)) {
                            checkusername = 1;
                        }
                        if (loginrecords.get(i).username.equals("admin")) {
                            actualadminpass = loginrecords.get(i).password;
                        }
                    }
                    if (checkusername == 0) {
                        if (pas.equals(confirmpas) && adminpas.equals(actualadminpass)) {
                            SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0);
                            final SharedPreferences.Editor editor = pref.edit();
                            Login newuser = new Login(user, pas, pref.getLong("nextloginid", 0));
                            newuser.save();
                            long temp = pref.getLong("nextloginid", 0);
                            editor.putLong("nextloginid", temp + 1);
                            editor.commit();
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), "Details donot match", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Choose a different username", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // check for username
        // update id no. from shared preferences

    }
}