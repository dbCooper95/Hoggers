package com.example.bhutanidhruv16.explist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bhutanidhruv16.explist.Utils.Constants;
import com.example.bhutanidhruv16.explist.db.FoodItem;
import com.example.bhutanidhruv16.explist.db.GroupItem;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;

/**
 * Created by bhutanidhruv16 on 14-Feb-16.
 */
public class Dialog_EditMenuFoodItem extends Dialog {

    EditText mod_name, mod_price, mod_refno, mod_alias;
    Button modify, delete, vegtype;
    FoodItem foodItem;

    public Dialog_EditMenuFoodItem(Context context, FoodItem foodItem) {
        super(context);
        this.foodItem = foodItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_editmenufooditem);

        mod_name = (EditText) findViewById(R.id.modify_name);
        mod_price = (EditText) findViewById(R.id.modify_price);
        mod_alias = (EditText) findViewById(R.id.modify_alias);
        mod_refno = (EditText) findViewById(R.id.modify_refNo);

        mod_name.setText(foodItem.itemName);
        mod_refno.setText(foodItem.refNo + "");
        mod_alias.setText(foodItem.alias);
        mod_price.setText(foodItem.cost + "");

        modify = (Button) findViewById(R.id.modify_modify);
        delete = (Button) findViewById(R.id.modify_remove);

        vegtype = (Button) findViewById(R.id.mod_itemtype);

        if (foodItem.foodtype.equals(Constants.NONVEG)) {
            vegtype.setText(Constants.NONVEG);
            vegtype.setBackgroundResource(R.color.red_l_chestnut);
        } else {
            vegtype.setText(Constants.VEG);
            vegtype.setBackgroundResource(R.color.green_l_jade);
        }

        vegtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodItem.foodtype.equals(Constants.VEG)) {
                    vegtype.setText(Constants.NONVEG);
                    vegtype.setBackgroundResource(R.color.red_l_chestnut);
                    foodItem.foodtype = Constants.NONVEG;
                } else {
                    vegtype.setText(Constants.VEG);
                    vegtype.setBackgroundResource(R.color.green_l_jade);
                    foodItem.foodtype = Constants.VEG;
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete this item?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        foodItem.delete();
                        dialog.dismiss();
                        dismiss();
                    }

                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodItem.itemName = mod_name.getText().toString();
                foodItem.foodtype = vegtype.getText().toString().trim();
                foodItem.cost = Integer.parseInt(mod_price.getText().toString());
                foodItem.alias = mod_alias.getText().toString();
                foodItem.refNo = Integer.parseInt(mod_refno.getText().toString());

                if (foodItem.itemName.equals("") || foodItem.foodtype.equals("") || mod_alias.getText().toString().equals("") || foodItem.alias.equals("") || mod_refno.getText().toString().equals(""))
                    Toast.makeText(getContext(), "Enter all details!!",
                            Toast.LENGTH_SHORT).show();
                else {
                    int alreadyExists = 0;
                    ArrayList<FoodItem> food = (ArrayList<FoodItem>) FoodItem.listAll(FoodItem.class);
                    for (int i = 0; i < food.size(); i++) {
                        if (foodItem.itemName.equals(food.get(i).itemName))
                            alreadyExists += 1;
                        else if (mod_alias.getText().toString().equals(food.get(i).alias))
                            alreadyExists += 1;
                        else if (foodItem.refNo == (food.get(i).refNo))
                            alreadyExists += 1;
                    }
//                    System.out.println("Check Here "+alreadyExists);
                    if (alreadyExists >= 2) {
                        Toast.makeText(getContext(), "Already Exists!!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        foodItem.save();
                    }
                }
                dismiss();
            }
        });
        this.setCancelable(true);
    }
}
