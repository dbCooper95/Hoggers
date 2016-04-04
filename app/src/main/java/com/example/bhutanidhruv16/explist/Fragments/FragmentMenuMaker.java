package com.example.bhutanidhruv16.explist.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.bhutanidhruv16.explist.Adapters.MenuListViewAdapter;
import com.example.bhutanidhruv16.explist.DialogInputGroup;
import com.example.bhutanidhruv16.explist.DialogInputItem;
import com.example.bhutanidhruv16.explist.R;
import com.example.bhutanidhruv16.explist.db.FoodItem;
import com.example.bhutanidhruv16.explist.db.GroupItem;

import java.util.ArrayList;

public class FragmentMenuMaker extends Fragment implements DialogInputItem.ItemAddedListener, DialogInputGroup.GroupAddedListener {

    public FragmentMenuMaker() {
    }

    ListView lv_categories;
    ArrayList<GroupItem> groups = new ArrayList<>();
    ArrayList<FoodItem> items = new ArrayList<>();
    MenuListViewAdapter adapter;
    Button add_group;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_menu_maker, container, false);
        add_group = (Button) rootView.findViewById(R.id.add_group);
        lv_categories = (ListView) rootView.findViewById(R.id.listview_category);

        add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogInputGroup(getActivity(), FragmentMenuMaker.this).show();
            }
        });

        groups = (ArrayList<GroupItem>) GroupItem.listAll(GroupItem.class);

        items = (ArrayList<FoodItem>) FoodItem.listAll(FoodItem.class);

        adapter = new MenuListViewAdapter(getActivity(), items, groups, this);
        lv_categories.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return rootView;
    }

    @Override
    public void ItemAdded() {
        items = (ArrayList<FoodItem>) FoodItem.listAll(FoodItem.class);
        adapter = new MenuListViewAdapter(getActivity(), items, groups, this);
        lv_categories.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void groupAdded() {
        groups = (ArrayList<GroupItem>) GroupItem.listAll(GroupItem.class);
        adapter = new MenuListViewAdapter(getActivity(), items, groups, this);
        lv_categories.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
