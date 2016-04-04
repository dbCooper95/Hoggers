package com.example.bhutanidhruv16.explist.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bhutanidhruv16.explist.DialogConfirmBill;
import com.example.bhutanidhruv16.explist.Dialog_addon;
import com.example.bhutanidhruv16.explist.OrderSelector;
import com.example.bhutanidhruv16.explist.R;
import com.example.bhutanidhruv16.explist.Utils.Constants;
import com.example.bhutanidhruv16.explist.Utils.Printer;
import com.example.bhutanidhruv16.explist.db.CurrentOrder;
import com.example.bhutanidhruv16.explist.db.FoodItem;
import com.example.bhutanidhruv16.explist.db.Order;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddOrderAdapter extends BaseAdapter {
    Context context;
    ArrayList<Order> currentOrders;
    LayoutInflater inflater;

    public AddOrderAdapter(Context context, ArrayList<Order> currentOrders) {
        this.context = context;
        this.currentOrders = currentOrders;
    }

    @Override
    public int getCount() {
        return currentOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return currentOrders.get(position);
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
            vi = inflater.inflate(R.layout.element_addorder, null);
            holder = new ViewHolder();
            holder.order_summary = (LinearLayout) vi.findViewById(R.id.ordersummary);

            holder.additemtype = (LinearLayout) vi.findViewById(R.id.additemtype);
            holder.additem = (Button) vi.findViewById(R.id.addnewitem);
            holder.addparcelitem = (Button) vi.findViewById(R.id.addnewparcelitem);
            holder.checkbox = (CheckBox) vi.findViewById(R.id.addorder_checkbox);                 // make changes acc to items delivered

            holder.orderDetails = (LinearLayout) vi.findViewById(R.id.element_orders_ll);
            holder.orderno = (TextView) vi.findViewById(R.id.orderno);
            holder.price = (TextView) vi.findViewById(R.id.totalprice);
            vi.setTag(holder);

        } else
            holder = (ViewHolder) vi.getTag();

        holder.checkbox.setClickable(false);                            // ADDED

        int bill_amt = 0;
        HashMap<FoodItem, Integer> orderhm2 = currentOrders.get(position).foodItemHashMap;
        for (Map.Entry<FoodItem, Integer> entry : orderhm2.entrySet()) {
            bill_amt += entry.getKey().cost * entry.getValue();
        }
        holder.price.setText(context.getString(R.string.Rs) + "" + bill_amt);

        // System.out.println(bill_amt + "this is the bill");

        holder.orderno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.orderDetails.getVisibility() == View.GONE) {
                    holder.additemtype.setVisibility(View.VISIBLE);
                    holder.orderDetails.setVisibility(View.VISIBLE);
                } else {
                    holder.additemtype.setVisibility(View.GONE);
                    holder.orderDetails.setVisibility(View.GONE);
                }
            }
        });

        final int finalBill_amt = bill_amt;
        holder.price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("AddOrderAdapter"+" ConfirmBillId: "+Integer.parseInt(holder.orderno.getText().toString().replace('#',' ').trim()));
                Dialog confirmBill = new DialogConfirmBill(context, finalBill_amt, Long.parseLong(holder.orderno.getText().toString().replace('#', ' ').trim()));                          // made changes
                confirmBill.show();

                confirmBill.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // notifyDataSetChanged();
                        Intent intent = new Intent(Constants.REFRESH_ADAPTERS);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        // send broadcast
//                        Intent intent2 = new Intent(context,MainActivity.class);
//                        intent2.putExtra("fragmentNumber", 1);
//                        context.startActivity(intent2);
                    }
                });
            }
        });


        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);         // to check whether parcel or not
        final SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("parcel", false);

        holder.additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentOrder.foodItemHashMap = currentOrders.get(position).foodItemHashMap;
                Printer.PrintHashMap(CurrentOrder.foodItemHashMap);
                CurrentOrder.orderid = currentOrders.get(position).orderid;

                editor.putBoolean("parcel", false);
                editor.commit();

                Intent intent = new Intent(context, OrderSelector.class);        // how to send order no. for that item
                context.startActivity(intent);
            }
        });

        holder.addparcelitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentOrder.foodItemHashMap = currentOrders.get(position).foodItemHashMap;
                Printer.PrintHashMap(CurrentOrder.foodItemHashMap);
                CurrentOrder.orderid = currentOrders.get(position).orderid;

                editor.putBoolean("parcel", true);
                editor.commit();

                Intent intent = new Intent(context, OrderSelector.class);        //  how to send order no. for that item
                context.startActivity(intent);                                   //  parcel item in currentOrders database
                //       System.out.println("Open new activity");
            }
        });

        // calculation of holder.price using orders database
        // orderDetails to be added using orders database

        if (position % 2 == 0)
            holder.order_summary.setBackgroundResource(R.color.PrimaryBackground);
        else
            holder.order_summary.setBackgroundResource(R.color.SecondaryBackground);

        holder.orderno.setText("#" + (currentOrders.get(position).orderid));

        boolean check = true;
        for (Map.Entry<FoodItem, Integer> entry : currentOrders.get(position).foodItemHashMap.entrySet()) {
            check = check && entry.getKey().pending;
            //  System.out.println("Outside onClick " + entry.getKey().pending);
        }
        holder.checkbox.setChecked(check);

//        if(check==true)
//            currentOrders.get(position).pending = false;
//        else
//            currentOrders.get(position).pending = true;

        holder.orderDetails.removeAllViews();

        int count_parcel = 0;
        for (final Map.Entry<FoodItem, Integer> foodItem : currentOrders.get(position).foodItemHashMap.entrySet()) {

            //System.out.println("PRINTING ALL FOOITEMS " +foodItem.getKey());

            if (foodItem.getKey().parcel == false) {

                LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View inflatedView = inflater1.inflate(R.layout.element_currentorder_fooditem, null, false);

                TextView name = (TextView) inflatedView.findViewById(R.id.current_order_fooditem_title);
                TextView addon = (TextView) inflatedView.findViewById(R.id.addon);
                TextView qty = (TextView) inflatedView.findViewById(R.id.current_order_fooditem_qty);
                CheckBox orderstatus = (CheckBox) inflatedView.findViewById(R.id.checkbox_itemdelivered);

                // Log.wtf("AddOrderAdapter", "Setting the orderstatus : " + foodItem.getKey().pending);


//                addon.setOnClickListener(new View.OnClickListener() {
//                    @Override
                addon.setText(foodItem.getKey().cost + " x");
//                    public void onClick(View v) {
//                        new Dialog_addon(context,foodItem.getKey(),foodItem.getValue()).show();
//                    }
//                });

                orderstatus.setChecked(foodItem.getKey().pending);

                name.setText("" + foodItem.getKey().itemName);
                qty.setText("" + foodItem.getValue());

                orderstatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            foodItem.getKey().pending = true;                                                  // pending = delivered
                            buttonView.setChecked(true);
                        } else {
                            foodItem.getKey().pending = false;
                            buttonView.setChecked(false);
                        }


                        currentOrders.get(position).save();

                        notifyDataSetChanged();             // No use ??

                        boolean check = true;                                                                 // order checkbox updated
                        HashMap<FoodItem, Integer> orderhm = currentOrders.get(position).foodItemHashMap;
                        for (Map.Entry<FoodItem, Integer> entry : orderhm.entrySet()) {
                            //           System.out.println(entry.getKey().pending + " FUCKING OFF");
                            check = check && entry.getKey().pending;
                        }
                        holder.checkbox.setChecked(check);

//                        if(check==true)
//                            currentOrders.get(position).pending = false;
//                        else
//                            currentOrders.get(position).pending = true;
                    }
                });
                holder.orderDetails.addView(inflatedView);
            } else
                count_parcel++;
        }

        if (count_parcel > 0) {

            LayoutInflater inflater2 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View inflatedView2 = inflater2.inflate(R.layout.element_currentorder_fooditem, null, false);

            TextView name2 = (TextView) inflatedView2.findViewById(R.id.current_order_fooditem_title);
            TextView addon2 = (TextView) inflatedView2.findViewById(R.id.addon);
            TextView qty2 = (TextView) inflatedView2.findViewById(R.id.current_order_fooditem_qty);
            CheckBox orderstatus2 = (CheckBox) inflatedView2.findViewById(R.id.checkbox_itemdelivered);

            inflatedView2.setPadding(5, 10, 0, 10);
            name2.setText("Parcel Items :");
            name2.setTextColor(Color.parseColor("#004D40"));
            name2.setPaintFlags(name2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            qty2.setVisibility(View.GONE);
            addon2.setVisibility(View.GONE);
            orderstatus2.setVisibility(View.GONE);

            holder.orderDetails.addView(inflatedView2);

            for (final Map.Entry<FoodItem, Integer> foodItem : currentOrders.get(position).foodItemHashMap.entrySet()) {

                //System.out.println("PRINTING ALL FOOITEMS " +foodItem.getKey());

                if (foodItem.getKey().parcel == true) {

                    LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View inflatedView = inflater1.inflate(R.layout.element_currentorder_fooditem, null, false);

                    TextView name = (TextView) inflatedView.findViewById(R.id.current_order_fooditem_title);
                    TextView addon = (TextView) inflatedView.findViewById(R.id.addon);
                    TextView qty = (TextView) inflatedView.findViewById(R.id.current_order_fooditem_qty);
                    CheckBox orderstatus = (CheckBox) inflatedView.findViewById(R.id.checkbox_itemdelivered);


                    addon.setText(foodItem.getKey().cost + " x");

                    // Log.wtf("AddOrderAdapter", "Setting the orderstatus : " + foodItem.getKey().pending);

                    orderstatus.setChecked(foodItem.getKey().pending);

                    name.setText("" + foodItem.getKey().itemName);
                    qty.setText("" + foodItem.getValue());

                    orderstatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                foodItem.getKey().pending = true;                                                  // pending = delivered
                                buttonView.setChecked(true);
                            } else {
                                foodItem.getKey().pending = false;
                                buttonView.setChecked(false);
                            }

                            currentOrders.get(position).save();

                            notifyDataSetChanged();             // No use ??

                            boolean check = true;                                                                 // order checkbox updated
                            HashMap<FoodItem, Integer> orderhm = currentOrders.get(position).foodItemHashMap;
                            for (Map.Entry<FoodItem, Integer> entry : orderhm.entrySet()) {
                                //           System.out.println(entry.getKey().pending + " FUCKING OFF");
                                check = check && entry.getKey().pending;
                            }
                            holder.checkbox.setChecked(check);

//                            if(check==true)
//                                currentOrders.get(position).pending = false;
//                            else
//                                currentOrders.get(position).pending = true;

                        }
                    });
                    holder.orderDetails.addView(inflatedView);
                }
            }
        }

        return vi;
    }

    public static class ViewHolder {

        LinearLayout order_summary;
        TextView orderno, price;
        CheckBox checkbox;

        LinearLayout additemtype;
        Button additem;
        Button addparcelitem;

        // cancel order and item using swipe
        // option of editing quantity in item
        // option of dust bin

        LinearLayout orderDetails;
    }
}

