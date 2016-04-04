package com.example.bhutanidhruv16.explist.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bhutanidhruv16.explist.R;
import com.example.bhutanidhruv16.explist.db.GroupItem;

import java.util.ArrayList;

public class OrderGroupIconAdapter extends BaseAdapter {

    Context context;
    public ArrayList<GroupItem> dataGroup;
    LayoutInflater inflater;
    GroupSelectedListener groupSelectedListener;

    public OrderGroupIconAdapter(Context context, ArrayList<GroupItem> dataGroup, GroupSelectedListener groupSelectedListener) {
        this.dataGroup = dataGroup;
        this.context = context;
        this.groupSelectedListener = groupSelectedListener;
    }

    @Override
    public int getCount() {
        return dataGroup.size();
    }

    @Override
    public Object getItem(int position) {
        return dataGroup.get(position);
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
            vi = inflater.inflate(R.layout.element_order_group_icon, null);
            holder = new ViewHolder();
            holder.layout_groupicon = (LinearLayout) vi.findViewById(R.id.layout_group_icon);
            holder.group_image = (ImageView) vi.findViewById(R.id.order_group_image);
            holder.group_name = (TextView) vi.findViewById(R.id.order_groupname);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (dataGroup.size() <= 0) {
            holder.group_name.setText("No Data");
        } else {
            final GroupItem group = dataGroup.get(position);
            holder.group_name.setText(group.name);

            if(group.name.equals("Wrap")){
                holder.group_image.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.wraps));
            }
            if(group.name.equals("Pasta")){
                holder.group_image.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.pasta));
            }
            if(group.name.equals("Momos")){
                holder.group_image.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.momos));
            }
            if(group.name.equals("Noodles")){
                holder.group_image.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.noodles));
            }

            holder.layout_groupicon.setOnClickListener(new View.OnClickListener() {       // holder.group_image
                @Override
                public void onClick(View v) {
                    holder.layout_groupicon.setBackgroundResource(R.color.Selected);
                    groupSelectedListener.groupSelected(group, position);
                    notifyDataSetChanged();                                 // also notify data change for foot item list
                    sendMessage();                                          //  not working???
                }
            });
        }
        return vi;
    }

    public static class ViewHolder {
        LinearLayout layout_groupicon;
        ImageView group_image;
        TextView group_name;
    }

    public interface GroupSelectedListener {
        void groupSelected(GroupItem groupItem, int position);
    }

    private void sendMessage() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("message", "This is my message!");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}