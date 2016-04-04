package com.example.bhutanidhruv16.explist.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bhutanidhruv16.explist.R;
import com.example.bhutanidhruv16.explist.db.CompletedOrders;
import com.example.bhutanidhruv16.explist.db.FoodItem;
import com.example.bhutanidhruv16.explist.db.Login;
import com.example.bhutanidhruv16.explist.db.Order;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bhutanidhruv16 on 13-Feb-16.
 */

public class DailySaleAdapter extends BaseAdapter {

    Context context;
    ArrayList<CompletedOrders> orderList;
    LayoutInflater inflater;

    public DailySaleAdapter(Context context, ArrayList<CompletedOrders> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
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
            vi = inflater.inflate(R.layout.element_dailysale, null);
            holder = new ViewHolder();
            holder.order_summary = (LinearLayout) vi.findViewById(R.id.dailysale_ordersummary);

            holder.orderDetails = (LinearLayout) vi.findViewById(R.id.dailysale_element_orders_ll);
            holder.orderno = (TextView) vi.findViewById(R.id.dailysale_orderno);
            holder.date = (TextView) vi.findViewById(R.id.dailysale_date);
            holder.price = (TextView) vi.findViewById(R.id.dailysale_totalprice);
            vi.setTag(holder);

        } else
            holder = (ViewHolder) vi.getTag();

        holder.date.setText(orderList.get(position).username+"\n"+orderList.get(position).cal.get(Calendar.DAY_OF_MONTH) + "/" + String.valueOf(orderList.get(position).cal.get(Calendar.MONTH) + 1) + "/" + orderList.get(position).cal.get(Calendar.YEAR) + "\n" +
                orderList.get(position).cal.get(Calendar.HOUR_OF_DAY) + ":" + orderList.get(position).cal.get(Calendar.MINUTE) + ":" + orderList.get(position).cal.get(Calendar.SECOND));

        int bill_amt = 0;
        final HashMap<FoodItem, Integer> orderhm2 = orderList.get(position).stringToHashMap(orderList.get(position).hashmapString);
        for (Map.Entry<FoodItem, Integer> entry : orderhm2.entrySet()) {
            bill_amt += entry.getKey().cost * entry.getValue();
        }
        holder.price.setText(context.getString(R.string.Rs)+""+ bill_amt);

        holder.order_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.orderDetails.getVisibility() == View.GONE) {
                    holder.orderDetails.setVisibility(View.VISIBLE);
                } else {
                    holder.orderDetails.setVisibility(View.GONE);
                }
            }
        });

        holder.order_summary.setBackgroundResource(R.color.SecondaryBackground);

        holder.orderno.setText("#" + (orderList.get(position).orderid));

        holder.orderDetails.removeAllViews();

        for (final Map.Entry<FoodItem, Integer> foodItem : orderList.get(position).stringToHashMap(orderList.get(position).hashmapString).entrySet()) {

            LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View inflatedView = inflater1.inflate(R.layout.element_dailysale_fooditem, null, false);

            TextView name = (TextView) inflatedView.findViewById(R.id.completedorder_fooditem_title);
            TextView qty = (TextView) inflatedView.findViewById(R.id.completedorder_fooditem_qty);
            TextView price = (TextView) inflatedView.findViewById(R.id.completedorder_fooditem_price);

            name.setText("" + foodItem.getKey().itemName);
            qty.setText("" + foodItem.getValue());
            price.setText("" + foodItem.getKey().cost);

            holder.orderDetails.addView(inflatedView);
        }
        return vi;
    }

    public static class ViewHolder {
        LinearLayout order_summary;
        TextView orderno, price;
        LinearLayout orderDetails;
        TextView date;
    }
}

