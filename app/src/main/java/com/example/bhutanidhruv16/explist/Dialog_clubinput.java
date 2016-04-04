package com.example.bhutanidhruv16.explist;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bhutanidhruv16.explist.Adapters.AddOrderAdapter;
import com.example.bhutanidhruv16.explist.db.OrderSugar;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Dialog_clubinput extends Dialog {

    EditText clubnum;
    Button proceed;

    public Dialog_clubinput(Context context) {
        super(context);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_clubinput);
        this.setCancelable(true);

        final EditText clubnum = (EditText) findViewById(R.id.clubnum);
        Button proceed = (Button) findViewById(R.id.proceed_cluborder);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(clubnum.getText().toString());
                Log.d("ClubInputDialog","number "+number);


                // new Dialog or activity for clubbing screen
            }
        });
    }
}
