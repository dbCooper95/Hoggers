package com.example.bhutanidhruv16.explist;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bhutanidhruv16.explist.db.FoodItem;
import com.example.bhutanidhruv16.explist.db.GroupItem;

import java.util.ArrayList;


public class DialogInputGroup extends Dialog {

    EditText name, id;
    Button done;
    GroupAddedListener groupAddedListener;

    public DialogInputGroup(Context context, GroupAddedListener listener) {
        super(context);
        groupAddedListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_newgroup);

        this.setCancelable(true);

        name = (EditText) findViewById(R.id.input_group_name);
        done = (Button) findViewById(R.id.input_group_done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem_name = name.getText().toString();
                GroupItem groupItem = new GroupItem(newItem_name);
                int check = 0;
                ArrayList<GroupItem> groupItems = (ArrayList<GroupItem>) GroupItem.listAll(GroupItem.class);
                for (int i = 0; i < groupItems.size(); i++) {
                    if (groupItems.get(i).name.equals(newItem_name)) {
                        check = 1;
                    }
                }

                if (newItem_name.equals("")) {
                    check = 1;
                }

                if (check == 0) {
                    groupItem.save();
                    groupAddedListener.groupAdded();
                } else {
                    Toast.makeText(getContext(), "Already exists!", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });
    }

    public interface GroupAddedListener {
        void groupAdded();
    }
}
