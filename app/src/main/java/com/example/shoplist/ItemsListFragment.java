package com.example.shoplist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.shoplist.database.DatabaseHelper;
import com.example.shoplist.database.ShoppingItem;
import com.example.shoplist.database.ShoppingItemDao;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE_MODAL;

/**
 * Created by O10 on 24.08.2016.
 * View Pager Fragment showing list of current or archived shopping items.
 */

public class ItemsListFragment extends Fragment {
    static String ARG_TAB_NUM = "tab_num";

    private boolean archivedItemsFragment;
    private ShoppingItemDao shoppingItemDao;

    private ListView shoppingItemsList;
    private ItemsListAdapter itemsListAdapter;
    private ListFragmentInterface listFragmentInterface;

    static ItemsListFragment newInstance(int num) {
        ItemsListFragment instance = new ItemsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TAB_NUM, num);
        instance.setArguments(args);
        return instance;
    }

    /**
     * Interface used for communication with activity, allowing to disable / enable ui elements when CAB is up or down
     */
    interface ListFragmentInterface {
        void onContextualUp();

        void onContextualDown();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listFragmentInterface = (ListFragmentInterface) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int tabNumer = getArguments() != null ? getArguments().getInt(ARG_TAB_NUM) : 0;
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
        shoppingItemsList = (ListView) topView.findViewById(R.id.items_list_view);
        return topView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!archivedItemsFragment) {
            itemsListAdapter = new ItemsListAdapter(shoppingItemDao.getShoppingItems(false));
        } else {
            itemsListAdapter = new ItemsListAdapter(shoppingItemDao.getShoppingItems(true));
        }
        shoppingItemsList.setAdapter(itemsListAdapter);
        shoppingItemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ShoppingItem clickedItem = itemsListAdapter.getItem(position);
                Intent intent = new Intent(getContext(), SingleItemActivity.class);
                intent.putExtra(SingleItemActivity.EDITED_ITEM_ID, clickedItem.getItemID());
                startActivity(intent);
            }
        });
        shoppingItemsList.setChoiceMode(CHOICE_MODE_MULTIPLE_MODAL);
        shoppingItemsList.setMultiChoiceModeListener(new CabModeListener());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        requestShoppingItemsUpdate();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ShoppingListUpdateEvent event) {
        requestShoppingItemsUpdate();
    }

    private void requestShoppingItemsUpdate() {
        if (archivedItemsFragment) {
            itemsListAdapter.updateShoppingItems(shoppingItemDao.getShoppingItems(true));
        } else {
            itemsListAdapter.updateShoppingItems(shoppingItemDao.getShoppingItems(false));
        }
    }

    /**
     * Contextual Action Bar Listener used for deleting multiple items on the shopping list
     */
    private class CabModeListener implements AbsListView.MultiChoiceModeListener {
        List<ShoppingItem> clickedItems = new ArrayList<>();

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            if (checked) {
                clickedItems.add(itemsListAdapter.getItem(position));
            } else {
                clickedItems.remove(itemsListAdapter.getItem(position));
            }
            if (clickedItems.size() == 1) {
                mode.setTitle(clickedItems.size() + " " + getString(R.string.item_sel));
            } else {
                mode.setTitle(clickedItems.size() + " " + getString(R.string.items_sel));
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_contextual_list, menu);
            listFragmentInterface.onContextualUp();
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_delete_many_items:
                    shoppingItemDao.deleteItemsOrMoveToArchived(clickedItems);
                    EventBus.getDefault().post(new ShoppingListUpdateEvent());
                    mode.finish();
                    return true;

            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            clickedItems.clear();
            listFragmentInterface.onContextualDown();
        }
    }

}