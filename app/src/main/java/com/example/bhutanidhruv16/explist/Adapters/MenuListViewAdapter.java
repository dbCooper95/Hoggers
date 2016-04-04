package com.example.bhutanidhruv16.explist.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bhutanidhruv16.explist.DialogInputItem;
import com.example.bhutanidhruv16.explist.Dialog_EditMenuFoodItem;
import com.example.bhutanidhruv16.explist.Fragments.FragmentMenuMaker;
import com.example.bhutanidhruv16.explist.R;
import com.example.bhutanidhruv16.explist.Utils.Constants;
import com.example.bhutanidhruv16.explist.db.FoodItem;
import com.example.bhutanidhruv16.explist.db.GroupItem;

import java.util.ArrayList;

public class MenuListViewAdapter extends BaseAdapter {

    Context context;
    public ArrayList<FoodItem> dataFood;
    public ArrayList<GroupItem> dataGroup;
    LayoutInflater inflater;
    DialogInputItem.ItemAddedListener itemAddedListener;

    public MenuListViewAdapter(Context context, ArrayList<FoodItem> dataFood, ArrayList<GroupItem> dataGroup, DialogInputItem.ItemAddedListener listener) {
        this.dataFood = dataFood;
        this.dataGroup = dataGroup;
        this.itemAddedListener = listener;
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {     //  parameters ??
        View vi = convertView;
        final ViewHolder holder;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);    //??

        if (convertView == null) {
            vi = inflater.inflate(R.layout.element_menu, null);
            holder = new ViewHolder();
            holder.food_items = (LinearLayout) vi.findViewById(R.id.element_menu_listview);
            holder.group_name = (Button) vi.findViewById(R.id.element_menu_button);
            holder.add_food_item = (Button) vi.findViewById(R.id.add_food_tem);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (dataGroup.size() <= 0) {
            holder.group_name.setText("No Data");
        } else {
            final GroupItem group = dataGroup.get(position);
            holder.group_name.setText(group.name);

            holder.group_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.food_items.getVisibility() == View.GONE) {
                        holder.food_items.setVisibility(View.VISIBLE);
                        holder.add_food_item.setVisibility(View.VISIBLE);
                    } else {
                        holder.food_items.setVisibility(View.GONE);
                        holder.add_food_item.setVisibility(View.GONE);
                    }
                }
            });

            holder.group_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this group?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            group.delete();
                            ArrayList<FoodItem> foodItemArrayList = (ArrayList<FoodItem>) FoodItem.listAll(FoodItem.class);
                            for (int i = 0; i < foodItemArrayList.size(); i++) {
                                if (foodItemArrayList.get(i).groupName.equals(holder.group_name.getText().toString())) {
                                    for (int j = 0; j < dataFood.size(); j++) {
                                        if (dataFood.get(j).itemName.equals(foodItemArrayList.get(i).itemName)) {
                                            dataFood.remove(j);
                                        }
                                    }
                                    foodItemArrayList.get(i).delete();
                                }
                            }
                            dataGroup.remove(dataGroup.get(position));
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                    return true;
                }
            });

            holder.add_food_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DialogInputItem(context, group, itemAddedListener).show();
                }
            });

            holder.food_items.removeAllViews();

            for (final FoodItem foodItem : dataFood) {

                if (foodItem.groupName.equals(group.name)) {

                    LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View inflatedView = inflater1.inflate(R.layout.element_menu_food_item, null, false);

                    TextView name = (TextView) inflatedView.findViewById(R.id.element_food_item_name);
                    TextView refno = (TextView) inflatedView.findViewById(R.id.element_food_item_refno);
                    TextView alias = (TextView) inflatedView.findViewById(R.id.element_food_item_alias);
                    TextView price = (TextView) inflatedView.findViewById(R.id.element_food_item_price);
                    TextView vegnonveg = (TextView) inflatedView.findViewById(R.id.element_food_item_vegnonveg);
                    Button edit_fooditem_menu = (Button) inflatedView.findViewById(R.id.edit_menu_fooditem);

                    name.setText("" + foodItem.itemName);
                    alias.setText("" + foodItem.alias);
                    price.setText("" + foodItem.cost);
                    refno.setText("" + foodItem.refNo);

                    if (foodItem.foodtype.equals(Constants.VEG))
                        vegnonveg.setBackgroundResource(R.color.green_l_jade);
                    if (foodItem.foodtype.equals(Constants.NONVEG))
                        vegnonveg.setBackgroundResource(R.color.red_l_chestnut);

                    edit_fooditem_menu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Dialog dialog = new Dialog_EditMenuFoodItem(context, foodItem);
                            dialog.show();

                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    itemAddedListener.ItemAdded();
                                }
                            });
                        }
                    });
                    holder.food_items.addView(inflatedView);
                }
            }
        }
        return vi;
    }

    public static class ViewHolder {
        Button add_food_item, group_name;
        LinearLayout food_items;
    }
}