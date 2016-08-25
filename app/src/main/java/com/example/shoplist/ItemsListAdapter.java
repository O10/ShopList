package com.example.shoplist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoplist.database.ShoppingItem;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by O10 on 24.08.2016.
 * Adapter used by listviews showing all archived and current items in ShopListActivity
 */

public class ItemsListAdapter extends BaseAdapter {
    public static final String DATE_LIST_FORMAT = "dd-MM-yyyy";
    private List<ShoppingItem> shoppingItems;
    private SimpleDateFormat dateFormater = new SimpleDateFormat(DATE_LIST_FORMAT, Locale.US);

    public ItemsListAdapter(List<ShoppingItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
    }


    @Override
    public int getCount() {
        return shoppingItems.size();
    }

    @Override
    public ShoppingItem getItem(int position) {
        return shoppingItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return shoppingItems.get(position).getItemID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShoppingItem currentItem = shoppingItems.get(position);
        ViewHolder viewHolder;

        //TODO Items images and miniatures

        //HOLDER INIT
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.shopping_item_list, parent, false);
            viewHolder.titleText = (TextView) convertView.findViewById(R.id.item_title);
            viewHolder.creationDate = (TextView) convertView.findViewById(R.id.creation_date_text);
            viewHolder.comments = (TextView) convertView.findViewById(R.id.comments_text);
            viewHolder.validUntilDate = (TextView) convertView.findViewById(R.id.deadline_date_text);
            viewHolder.miniature = (ImageView) convertView.findViewById(R.id.miniature_image);
            viewHolder.priorityMiniature = (ImageView) convertView.findViewById(R.id.priority_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //VIEW BINDING
        viewHolder.itemId = shoppingItems.get(position).getItemID();
        viewHolder.titleText.setText(currentItem.getItemName());
        viewHolder.creationDate.setText(dateFormater.format(currentItem.getCreationDate()));
        viewHolder.comments.setText(currentItem.getComments());
        viewHolder.validUntilDate.setText(dateFormater.format(currentItem.getValidUntilDate()));

        if (currentItem.getComments() != null && currentItem.getComments().length() > 0) {
            viewHolder.comments.setVisibility(View.VISIBLE);
        } else {
            viewHolder.comments.setVisibility(View.GONE);
        }

        switch (currentItem.getPriority()) {
            case LOW:
                viewHolder.priorityMiniature.setImageResource(R.drawable.priority_drawable_green);
                break;
            case NORMAL:
                viewHolder.priorityMiniature.setImageResource(R.drawable.priority_drawable_yellow);
                break;
            case HIGH:
            default:
                viewHolder.priorityMiniature.setImageResource(R.drawable.priority_drawable_red);
        }
        return convertView;
    }

    public void updateShoppingItems(List<ShoppingItem> newShoppingItems) {
        this.shoppingItems = newShoppingItems;
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView titleText, creationDate, comments, validUntilDate;
        public ImageView miniature, priorityMiniature;
        public long itemId;
    }

}
