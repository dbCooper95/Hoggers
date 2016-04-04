package com.example.bhutanidhruv16.explist;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bhutanidhruv16.explist.Utils.Constants;
import com.example.bhutanidhruv16.explist.db.FoodItem;
import com.example.bhutanidhruv16.explist.db.GroupItem;
import com.example.bhutanidhruv16.explist.db.OrderSugar;

import java.util.ArrayList;

public class DialogInputItem extends Dialog {

    EditText name, price, refno, alias;
    Button done;
    GroupItem groupItem;
    ItemAddedListener itemAddedListener;
    Button vegType;

    public DialogInputItem(Context context, GroupItem groupItem, ItemAddedListener listener) {
        super(context);
        this.groupItem = groupItem;
        itemAddedListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_newitem);

        this.setCancelable(true);

        name = (EditText) findViewById(R.id.input_item_name);
        vegType = (Button) findViewById(R.id.input_item_type);
        price = (EditText) findViewById(R.id.input_item_price);
        refno = (EditText) findViewById(R.id.input_item_RefNo);
        alias = (EditText) findViewById(R.id.input_item_alias);
        done = (Button) findViewById(R.id.input_item_done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem_name = name.getText().toString();
                String newItem_price = price.getText().toString();
                String newItem_refno = refno.getText().toString();
                String newItem_alias = alias.getText().toString();

                if (newItem_name.equals("") || newItem_alias.equals("") || newItem_refno.equals("") || newItem_name.equals(""))
                    Toast.makeText(getContext(), "Enter all details!!",
                            Toast.LENGTH_SHORT).show();
                else {
                    int alreadyExists = 0;

                    ArrayList<FoodItem> food = (ArrayList<FoodItem>) FoodItem.listAll(FoodItem.class);
                    for (int i = 0; i < food.size(); i++) {
                        if (newItem_name.equals(food.get(i).itemName))
                            alreadyExists = 1;
                        if (newItem_alias.equals(food.get(i).alias))
                            alreadyExists = 1;
                        if (Integer.parseInt(newItem_refno) == (food.get(i).refNo))
                            alreadyExists = 1;
                    }

                    if (alreadyExists == 1) {
                        Toast.makeText(getContext(), "Already Exists!!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // added group name to item
                        FoodItem foodItem = new FoodItem(newItem_name+" "+groupItem.name, Integer.parseInt(newItem_price), groupItem.name, Integer.parseInt(newItem_refno), newItem_alias, vegType.getText().toString(), false, false);
                        foodItem.save();
                        itemAddedListener.ItemAdded();
                        Toast.makeText(getContext(), "Successfully Added",
                                Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
            }
        });

        if (vegType.getText().toString().equals(Constants.NONVEG)) {
            vegType.setText(Constants.NONVEG);
            vegType.setBackgroundResource(R.color.red_l_chestnut);
        } else {
            vegType.setText(Constants.VEG);
            vegType.setBackgroundResource(R.color.green_l_jade);
        }

        vegType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vegType.getText().toString().equals(Constants.VEG)) {
                    vegType.setText(Constants.NONVEG);
                    vegType.setBackgroundResource(R.color.red_l_chestnut);
                } else {
                    vegType.setText(Constants.VEG);
                    vegType.setBackgroundResource(R.color.green_l_jade);
                }
            }
        });


    }

    public interface ItemAddedListener {
        void ItemAdded();
    }
}
