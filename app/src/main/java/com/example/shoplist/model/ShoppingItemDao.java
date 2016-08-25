package com.example.shoplist.model;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by O10 on 23.08.2016.
 */

public class ShoppingItemDao extends BaseDaoImpl<ShoppingItem, Long> implements ShoppingItemDaoInterface {
    protected ShoppingItemDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, ShoppingItem.class);
    }

    public List<ShoppingItem> getShoppingItems(boolean archived) {
        final QueryBuilder<ShoppingItem, Long> shoppingItemQueryBuilder = queryBuilder();
        try {
            shoppingItemQueryBuilder.where().eq(ShoppingItem.IS_ARCHIVED_COLUMN_NAME, archived);
            shoppingItemQueryBuilder.orderBy(ShoppingItem.CREATION_DATE_COLUMN_NAME, true);
            return shoppingItemQueryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean createOrUpdateItem(ShoppingItem shoppingItem) {
        try {
            createOrUpdate(shoppingItem);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteShoppingItem(long itemID) {
        try {
            deleteById(itemID);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteItemOrMoveToArchived(long id) {
        ShoppingItem shoppingItem = getShoppingItem(id);
        if (shoppingItem.isArchived()) {
            deleteShoppingItem(id);
            return true;
        } else {
            shoppingItem.setArchived(true);
            createOrUpdateItem(shoppingItem);
            return false;
        }
    }

    public ShoppingItem getShoppingItem(long itemID) {
        try {
            final List<ShoppingItem> results = queryBuilder().where().idEq(itemID).query();
            if (results != null && results.size() > 0) {
                return results.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
