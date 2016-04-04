package com.example.bhutanidhruv16.explist.Adapters;

/**
 * Created by bhutanidhruv16 on 21-Mar-16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bhutanidhruv16.explist.R;

import java.util.ArrayList;

public class LoginDetailsAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<String> username;
    ArrayList<Long> amount;

    public LoginDetailsAdapter(Context context, ArrayList<String> username, ArrayList<Long> amount) {
        this.context = context;
        this.username = username;
        this.amount = amount;
    }

    @Override
    public int getCount() {
        return username.size();
    }

    @Override
    public Object getItem(int position) {
        return username.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            vi = inflater.inflate(R.layout.element_logindetails, null);
            holder = new ViewHolder();
            holder.name = (TextView) vi.findViewById(R.id.logindetails_username);
            holder.bill = (TextView) vi.findViewById(R.id.logindetails_amount);
            vi.setTag(holder);

        } else
            holder = (ViewHolder) vi.getTag();

        holder.name.setText(username.get(position));
        holder.bill.setText(context.getString(R.string.Rs) + amount.get(position) + "");

        return vi;
    }

    public static class ViewHolder {
        TextView name, bill;
    }
}

