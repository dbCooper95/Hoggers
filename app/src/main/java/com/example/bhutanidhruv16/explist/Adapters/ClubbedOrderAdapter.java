package com.example.bhutanidhruv16.explist.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bhutanidhruv16.explist.R;
import com.example.bhutanidhruv16.explist.db.FoodItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bhutanidhruv16 on 18-Feb-16.
 */
public class ClubbedOrderAdapter extends BaseAdapter {
    Context context;
    ArrayList<FoodItem> foodItem;
    ArrayList<Integer> quantity;
    ArrayList<Long> ordernum;
    ArrayList<String> distinctNames;
    ArrayList<HashMap<Long, Integer>> club;
    LayoutInflater inflater;

    public ClubbedOrderAdapter(Context context, ArrayList<String> distinctNames, ArrayList<FoodItem> foodItem, ArrayList<Long> ordernum, ArrayList<Integer> quantity) {
        this.context = context;
        this.foodItem = foodItem;
        this.quantity = quantity;
        this.ordernum = ordernum;
        this.distinctNames = distinctNames;

    }

    @Override
    public int getCount() {
        return distinctNames.size();
    }

    @Override
    public Object getItem(int position) {
        return distinctNames.get(position);
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
            vi = inflater.inflate(R.layout.element_clubbedorder, null);
            holder = new ViewHolder();
            holder.cluborder_itemname = (TextView) vi.findViewById(R.id.cluborder_itemName);
            holder.ll_cluborder = (LinearLayout) vi.findViewById(R.id.ll_cluborders);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        holder.cluborder_itemname.setText("" + distinctNames.get(position) + ":");
        // holder.cluborder_itemname.setPaintFlags(holder.cluborder_itemname.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        holder.ll_cluborder.removeAllViews();

        int count_parcel = 0;
        for (int i = 0; i < foodItem.size(); i++) {

            if (foodItem.get(i).itemName.equals(distinctNames.get(position)) && foodItem.get(i).parcel == false) {
                LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View inflatedView = inflater1.inflate(R.layout.element_club_orders, null, false);

                final TextView textorderno = (TextView) inflatedView.findViewById(R.id.element_cluborderno);
                TextView textquantity = (TextView) inflatedView.findViewById(R.id.element_clubquantity);
                final LinearLayout ll_orderno = (LinearLayout) inflatedView.findViewById(R.id.ll_text_orderno);

                textorderno.setText("#" + ordernum.get(i));
                textquantity.setText(quantity.get(i) + "");

                ll_orderno.setBackgroundColor(Color.parseColor("#fc6a74"));
                textorderno.setTextColor(Color.parseColor("#fc6a74"));

                final int finalI = i;
                textorderno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (foodItem.get(finalI).pending == false) {
                            foodItem.get(finalI).pending = true;
                            ll_orderno.setBackgroundColor(Color.parseColor("#4CAF50"));
                            textorderno.setTextColor(Color.parseColor("#4CAF50"));
                        } else {
                            foodItem.get(finalI).pending = false;
                            ll_orderno.setBackgroundColor(Color.parseColor("#fc6a74"));
                            textorderno.setTextColor(Color.parseColor("#fc6a74"));
                        }
                        System.out.println("CLICKED HERE IN CLUBBED ORDERS");
                    }
                });

                holder.ll_cluborder.addView(inflatedView);
            }
        }

        for (int i = 0; i < foodItem.size(); i++) {

            if (foodItem.get(i).itemName.equals(distinctNames.get(position)) && foodItem.get(i).parcel == true) {
                LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View inflatedView = inflater1.inflate(R.layout.element_club_orders, null, false);

                final TextView textorderno = (TextView) inflatedView.findViewById(R.id.element_cluborderno);
                TextView textquantity = (TextView) inflatedView.findViewById(R.id.element_clubquantity);
                final LinearLayout ll_orderno = (LinearLayout) inflatedView.findViewById(R.id.ll_text_orderno);

                textorderno.setText("#" + ordernum.get(i));
                textquantity.setText(quantity.get(i) + "");

                ll_orderno.setBackgroundColor(Color.parseColor("#FFC107"));
                textorderno.setTextColor(Color.parseColor("#FFC107"));

                final int finalI = i;
                textorderno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (foodItem.get(finalI).pending == false) {
                            foodItem.get(finalI).pending = true;
                            ll_orderno.setBackgroundColor(Color.parseColor("#4CAF50"));
                            textorderno.setTextColor(Color.parseColor("#4CAF50"));
                        } else {
                            foodItem.get(finalI).pending = false;
                            ll_orderno.setBackgroundColor(Color.parseColor("#FFC107"));
                            textorderno.setTextColor(Color.parseColor("#FFC107"));
                        }
                        System.out.println("CLICKED HERE IN CLUBBED ORDERS");
                    }
                });

                holder.ll_cluborder.addView(inflatedView);
            }
        }


        return vi;
    }

    public static class ViewHolder {
        TextView cluborder_itemname;
        LinearLayout ll_cluborder;
    }
}

