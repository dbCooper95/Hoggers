package com.example.bhutanidhruv16.explist;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

public class DialogChangePassword extends Dialog {

    EditText username, oldpass, newpass;
    Button modifypass;

    public DialogChangePassword(Context context) {
        super(context);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Change Password");
        setContentView(R.layout.dialog_change_password);

        username = (EditText) findViewById(R.id.signup_username);
        oldpass = (EditText) findViewById(R.id.oldpassword);
        newpass = (EditText) findViewById(R.id.newpassword);

        modifypass = (Button) findViewById(R.id.modify_password);

        modifypass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String oldpas = oldpass.getText().toString();
                String newpas = newpass.getText().toString();

                ArrayList<Login> loginrecords = (ArrayList<Login>) Login.listAll(Login.class);
                int check_details = 0;
                for (int i = 0; i < loginrecords.size(); i++) {
                    if (loginrecords.get(i).username.equals(user) && loginrecords.get(i).password.equals(oldpas)) {
                        check_details = 1;
                        loginrecords.get(i).password = newpas;
                        loginrecords.get(i).save();
                        Toast.makeText(getContext(), "Password Updated",
                                Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
                if (check_details == 0) {
                    Toast.makeText(getContext(), "Incorrect credentials",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}