package com.example.shoplist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by O10 on 23.08.2016.
 * Main helper used for accessing and managing SQLite database
 * For getting an instance use OpenHelperManager.getHelper(getContext(), DatabaseHelper.class)
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "shopping_list_database.db";
    private static final int DATABASE_VERSION = 1;

    private ShoppingItemDao shoppingItemDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ShoppingItem.class);
            initDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, ShoppingItem.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ShoppingItemDao getShoppingItemDao() throws SQLException {
        if (shoppingItemDao == null) {
            this.shoppingItemDao = DaoManager.createDao(getConnectionSource(), ShoppingItem.class);
        }
        return shoppingItemDao;
    }

    /*
    Function initializes database with some random entries
     */
    private void initDatabase() throws SQLException {
        final ShoppingItemDao shoppingItemDao = getShoppingItemDao();

        final Calendar calendar = Calendar.getInstance();

        ShoppingItem shoppingItem = new ShoppingItem();
        shoppingItem.setItemName("Sony Bravia TV");
        shoppingItem.setComments("This might be expensive");
        shoppingItem.setPriority(ShoppingItem.ShoppingPriority.LOW);
        calendar.set(2015, 0, 20);
        shoppingItem.setCreationDate(calendar.getTime());
        calendar.set(2020, 0, 21);
        shoppingItem.setValidUntilDate(calendar.getTime());
        shoppingItemDao.create(shoppingItem);

        shoppingItem = new ShoppingItem();
        shoppingItem.setItemName("Bread");
        shoppingItem.setComments("One big two smalls");
        shoppingItem.setPriority(ShoppingItem.ShoppingPriority.HIGH);
        calendar.set(2016, 5, 20);
        shoppingItem.setCreationDate(calendar.getTime());
        calendar.set(2016, 5, 21);
        shoppingItem.setValidUntilDate(calendar.getTime());
        shoppingItemDao.create(shoppingItem);

        shoppingItem = new ShoppingItem();
        shoppingItem.setItemName("Onions");
        shoppingItem.setComments("30 kg");
        shoppingItem.setPriority(ShoppingItem.ShoppingPriority.NORMAL);
        calendar.set(2016, 5, 20);
        shoppingItem.setCreationDate(calendar.getTime());
        calendar.set(2016, 5, 21);
        shoppingItem.setValidUntilDate(calendar.getTime());
        shoppingItemDao.create(shoppingItem);

        shoppingItem = new ShoppingItem();
        shoppingItem.setItemName("Potatoes");
        shoppingItem.setComments("15 kg");
        shoppingItem.setPriority(ShoppingItem.ShoppingPriority.NORMAL);
        calendar.set(2016, 5, 2);
        shoppingItem.setCreationDate(calendar.getTime());
        calendar.set(2016, 5, 5);
        shoppingItem.setValidUntilDate(calendar.getTime());
        shoppingItemDao.create(shoppingItem);


        shoppingItem = new ShoppingItem();
        shoppingItem.setItemName("Pulp Fiction");
        shoppingItem.setComments("This is an example of a long comment,his is an example of a long comment,his is an example of a long comment,his is an example of a long comment,his is an example of a long comment,");
        shoppingItem.setPriority(ShoppingItem.ShoppingPriority.NORMAL);
        calendar.set(2016, 4, 20);
        shoppingItem.setCreationDate(calendar.getTime());
        calendar.set(2022, 5, 25);
        shoppingItem.setValidUntilDate(calendar.getTime());
        shoppingItemDao.create(shoppingItem);

        shoppingItem = new ShoppingItem();
        shoppingItem.setItemName("Long gone item");
        shoppingItem.setComments("This is is long gone");
        shoppingItem.setArchived(true);
        shoppingItem.setPriority(ShoppingItem.ShoppingPriority.NORMAL);
        calendar.set(2014, 4, 20);
        shoppingItem.setCreationDate(calendar.getTime());
        calendar.set(2015, 5, 25);
        shoppingItem.setValidUntilDate(calendar.getTime());
        shoppingItemDao.create(shoppingItem);

        shoppingItem = new ShoppingItem();
        shoppingItem.setItemName("Long gone item without comment");
        shoppingItem.setPriority(ShoppingItem.ShoppingPriority.NORMAL);
        shoppingItem.setArchived(true);
        calendar.set(2012, 4, 20);
        shoppingItem.setCreationDate(calendar.getTime());
        calendar.set(2012, 5, 25);
        shoppingItem.setValidUntilDate(calendar.getTime());
        shoppingItemDao.create(shoppingItem);


    }

}
