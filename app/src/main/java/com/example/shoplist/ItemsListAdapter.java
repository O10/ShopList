package com.example.shoplist;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoplist.model.ShoppingItem;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by O10 on 24.08.2016.
 */

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.ViewHolder> {
    public static final String DATE_LIST_FORMAT = "dd-MM-yyyy";
    private List<ShoppingItem> shoppingItems;
    private SimpleDateFormat dateFormater = new SimpleDateFormat(DATE_LIST_FORMAT, Locale.US);

    public ItemsListAdapter(List<ShoppingItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View topView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_item_list, parent, false);
        return new ViewHolder(topView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShoppingItem currentItem = shoppingItems.get(position);
        holder.itemId = shoppingItems.get(position).getItemID();

        holder.titleText.setText(currentItem.getItemName());
        holder.creationDate.setText(dateFormater.format(currentItem.getCreationDate()));
        holder.comments.setText(currentItem.getComments());
        holder.validUntilDate.setText(dateFormater.format(currentItem.getValidUntilDate()));

        switch (currentItem.getPriority()) {
            case LOW:
                holder.priorityMiniature.setImageResource(R.drawable.priority_drawable_green);
                break;
            case NORMAL:
                holder.priorityMiniature.setImageResource(R.drawable.priority_drawable_yellow);
                break;
            case HIGH:
            default:
                holder.priorityMiniature.setImageResource(R.drawable.priority_drawable_red);
        }

    }

    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }

    public void updateShoppingItems(List<ShoppingItem> newShoppingItems) {
        this.shoppingItems = newShoppingItems;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titleText, creationDate, comments, validUntilDate;
        public ImageView miniature, priorityMiniature;
        public long itemId;

        public ViewHolder(View topView) {
            super(topView);
            titleText = (TextView) topView.findViewById(R.id.item_title);
            creationDate = (TextView) topView.findViewById(R.id.creation_date_text);
            comments = (TextView) topView.findViewById(R.id.comments_text);
            validUntilDate = (TextView) topView.findViewById(R.id.deadline_date_text);
            miniature = (ImageView) topView.findViewById(R.id.miniature_image);
            priorityMiniature = (ImageView) topView.findViewById(R.id.priority_view);
            topView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), SingleItemActivity.class);
            intent.putExtra(SingleItemActivity.EDITED_ITEM_ID, itemId);
            v.getContext().startActivity(intent);
        }
    }
}
