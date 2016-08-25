package com.example.shoplist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoplist.model.DatabaseHelper;
import com.example.shoplist.model.ShoppingItemDao;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;

/**
 * Created by O10 on 24.08.2016.
 */

public class ItemsListFragment extends Fragment {
    static String ARG_TAB_NUM = "tab_num";

    private int tabNumer;
    private boolean archivedItemsFragment;
    private ShoppingItemDao shoppingItemDao;

    private RecyclerView recyclerView;
    private ItemsListAdapter itemsListAdapter;

    static ItemsListFragment newInstance(int num) {
        ItemsListFragment instance = new ItemsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TAB_NUM, num);
        instance.setArguments(args);
        return instance;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabNumer = getArguments() != null ? getArguments().getInt(ARG_TAB_NUM) : 0;
        if (tabNumer > 0) {
            archivedItemsFragment = true;
        }
        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(getContext(), DatabaseHelper.class);
        try {
            shoppingItemDao = databaseHelper.getShoppingItemDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View topView = inflater.inflate(R.layout.fragment_items_list, container, false);
        recyclerView = (RecyclerView) topView.findViewById(R.id.items_list_recycle_view);
        return topView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!archivedItemsFragment) {
            itemsListAdapter = new ItemsListAdapter(shoppingItemDao.getShoppingItems(false));
        } else {
            itemsListAdapter = new ItemsListAdapter(shoppingItemDao.getShoppingItems(true));
        }
        recyclerView.setAdapter(itemsListAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (archivedItemsFragment) {
            itemsListAdapter.updateShoppingItems(shoppingItemDao.getShoppingItems(true));
        } else {
            itemsListAdapter.updateShoppingItems(shoppingItemDao.getShoppingItems(false));
        }
    }
}