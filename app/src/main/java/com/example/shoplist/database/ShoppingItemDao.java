package com.example.shoplist.database;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by O10 on 23.08.2016.
 * Implementation of Database Access Object for Shopping Item Table.
 * All shopping items reads/writes should be defined here and then used by accessing Dao instance
 */

public class ShoppingItemDao extends BaseDaoImpl<ShoppingItem, Long> implements ShoppingItemDaoInterface {
    protected ShoppingItemDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, ShoppingItem.class);
    }

    /**
     * Returns all shopping items
     *
     * @param archived true if searched items should be archived
     * @return list containing all the shopping items
     */
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

    /**
     * Creates new shopping item or updates one if the id is already definied in the database
     *
     * @param shoppingItem shopping item to be saved or updated
     * @return true if item successfully saved/updated, false otherwise
     */
    public boolean createOrUpdateItem(ShoppingItem shoppingItem) {
        try {
            createOrUpdate(shoppingItem);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes shopping item
     *
     * @param itemID id of item to be deleted
     * @return true if item successfully saved/updated, false otherwise
     */
    public boolean deleteShoppingItem(long itemID) {
        try {
            deleteById(itemID);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes shopping item if it is archived or marks as archived otherwise
     *
     * @param id id of item to be deleted
     * @return true if item was delted, false if moved to archived
     */
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

    /**
     * Deletes shopping items if they are archived or marks them as archived otherwise
     *
     * @param shoppingItems list of items do be deleted or archived
     * @return true
     */
    public boolean deleteItemsOrMoveToArchived(List<ShoppingItem> shoppingItems) {
        for (ShoppingItem shoppingItem : shoppingItems) {
            deleteItemOrMoveToArchived(shoppingItem.getItemID());
        }
        return true;
    }

    /**
     * Return shopping item with given id
     *
     * @param itemID id of shopping item
     * @return shopping item with given id
     */
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
