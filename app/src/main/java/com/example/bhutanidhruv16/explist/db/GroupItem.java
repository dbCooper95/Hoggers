package com.example.bhutanidhruv16.explist.db;

import com.orm.SugarRecord;

public class GroupItem extends SugarRecord<GroupItem> {
    public String name;

    public GroupItem() {

    }

    public GroupItem(String name) {
        this.name = name;
    }
}
