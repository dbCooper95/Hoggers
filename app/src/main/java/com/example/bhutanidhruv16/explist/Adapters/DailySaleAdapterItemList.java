package com.example.bhutanidhruv16.explist.Adapters;

import android.content.Context;
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
import com.example.bhutanidhruv16.explist.db.Order;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bhutanidhruv16 on 13-Feb-16.
 */

public class DailySaleAdapterItemList extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<String> itemname;
    ArrayList<Integer> quantity;

    public DailySaleAdapterItemList(Context context,ArrayList<String> itemname,ArrayList<Integer> quantity) {
        this.context = context;
        this.itemname = itemname;
        this.quantity = quantity;
    }

    @Override
    public int getCount() {
        return itemname.size();
    }

    @Override
    public Object getItem(int position) {
        return itemname.get(position);
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
            vi = inflater.inflate(R.layout.element_dailysaleitemlist, null);
            holder = new ViewHolder();
            holder.item = (TextView) vi.findViewById(R.id.dailysale_itemname);
            holder.quant = (TextView) vi.findViewById(R.id.dailysale_itemquantity);
            vi.setTag(holder);

        } else
            holder = (ViewHolder) vi.getTag();

        holder.item.setText(itemname.get(position));
        holder.quant.setText(quantity.get(position)+"");

        return vi;
    }

    public static class ViewHolder {
        TextView item,quant;
    }
}

