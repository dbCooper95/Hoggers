package com.example.bhutanidhruv16.explist.db;

import com.orm.SugarRecord;

public class User extends SugarRecord<User> {

    public String name;
    public String number;

    public User() {

    }

    public User(String name, String num) {
        this.name = name;
        this.number = num;
    }
}