package com.example.bhutanidhruv16.explist.db;

import com.orm.SugarRecord;

public class Login extends SugarRecord<FoodItem> {

    public String username;
    public String password;
    public long loginid;

    public Login() {

    }

    public Login(String username, String password, long loginid) {
        this.username = username;
        this.password = password;
        this.loginid = loginid;
    }
}