package com.example.bhutanidhruv16.explist.db;

import com.orm.SugarRecord;

public class FoodItem extends SugarRecord<FoodItem> {

    public String itemName;
    public int cost;
    public String groupName;
    public int refNo;
    public String alias;
    public String foodtype;                 // veg / non veg
    public boolean parcel;
    public boolean pending;

    public FoodItem() {

    }

    public FoodItem(String itemName, int cost, String groupName, int refNo, String alias, String foodtype, boolean pending, boolean parcel) {
        this.itemName = itemName;
        this.cost = cost;
        this.groupName = groupName;
        this.refNo = refNo;
        this.alias = alias;
        this.foodtype = foodtype;
        this.pending = pending;
        this.parcel = parcel;
    }

    @Override
    public String toString() {
        return this.itemName + " " + this.cost + " " + this.groupName + " " + this.refNo + " " + this.pending + " " + this.parcel;
    }
}