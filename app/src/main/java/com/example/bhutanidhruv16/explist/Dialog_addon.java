package com.example.bhutanidhruv16.explist;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.example.bhutanidhruv16.explist.db.FoodItem;
import com.example.bhutanidhruv16.explist.db.Order;
import com.example.bhutanidhruv16.explist.db.OrderSugar;

/**
 * Created by bhutanidhruv16 on 24-Mar-16.
 */

public class Dialog_addon extends Dialog {

    String items[] = {"Extra Cheese", "Without Onion", "Extra Capsicum"};

    ArrayAdapter<String> adapter;
    EditText quant1, quant2, quant3;
    Button modify;

    MultiAutoCompleteTextView acTextView[];
    int item_id[] = {R.id.options1, R.id.options2, R.id.options3};

    FoodItem foodItem;
    int quantity;

    public Dialog_addon(Context context, FoodItem foodItem, int quantity) {
        super(context);
        this.foodItem = foodItem;
        this.quantity = quantity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_addon);

        quant1 = (EditText) findViewById(R.id.addon_quant1);
        quant2 = (EditText) findViewById(R.id.addon_quant2);
        quant3 = (EditText) findViewById(R.id.addon_quant3);

        acTextView = new MultiAutoCompleteTextView[3];

        for (int i = 0; i < 3; i++) {
            adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, items);
            acTextView[i] = (MultiAutoCompleteTextView) findViewById(item_id[i]);
            final int finalI = i;
            acTextView[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        acTextView[finalI].showDropDown();
                    }
                }
            });
            acTextView[i].setAdapter(adapter);
            acTextView[i].setThreshold(1);
            acTextView[i].setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        }

        modify = (Button) findViewById(R.id.addon_modify);

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = 0;
                String temp = "";
                if (!quant1.getText().toString().equals("") && !acTextView[0].getText().toString().equals("")) {
                    counter += Integer.parseInt(quant1.getText().toString());
                    temp += quant1.getText().toString() + ":" + acTextView[0].getText().toString() + ";";
                }
                if (!quant2.getText().toString().equals("") && !acTextView[1].getText().toString().equals("")) {
                    counter += Integer.parseInt(quant2.getText().toString());
                    temp += quant1.getText().toString() + ":" + acTextView[1].getText().toString() + ";";
                }
                if (!quant3.getText().toString().equals("") && !acTextView[2].getText().toString().equals("")) {
                    counter += Integer.parseInt(quant3.getText().toString());
                    temp += quant1.getText().toString() + ":" + acTextView[2].getText().toString();
                }

                if (counter > quantity) {
                    Toast.makeText(getContext(), "Not Possible", Toast.LENGTH_LONG).show();
                } else if (counter > 0) {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_LONG).show();
                    Log.d("DialogAddon", temp);
                }
            }
        });
    }
}
