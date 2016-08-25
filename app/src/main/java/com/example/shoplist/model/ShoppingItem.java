package com.example.shoplist.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by O10 on 23.08.2016.
 */

@DatabaseTable(tableName = ShoppingItem.ITEMS_TABLE_NAME, daoClass = ShoppingItemDao.class)
public class ShoppingItem {
    public static final String ITEMS_TABLE_NAME = "items";
    public static final String ID_COLUMN_NAME = "_id";
    public static final String ITEM_NAME_COLUMN_NAME = "item_name";
    public static final String CREATION_DATE_COLUMN_NAME = "creat_date";
    public static final String VALID_UNTIL_DATE_COLUMN_NAME = "valid_until";
    public static final String COMMNENTS_COLUMN_NAME = "comments";
    public static final String IS_ARCHIVED_COLUMN_NAME = "is_archived";
    public static final String PRIORITy_COLUMN_NAME = "priority";
    public static final String IMAGE_URI_COLUMN_NAME = "image_uri";

    public static enum ShoppingPriority {
        LOW,
        NORMAL,
        HIGH;
    }

    @DatabaseField(columnName = ID_COLUMN_NAME, generatedId = true)
    private long itemID;
    @DatabaseField(columnName = ITEM_NAME_COLUMN_NAME, canBeNull = false)
    private String itemName;
    @DatabaseField(columnName = CREATION_DATE_COLUMN_NAME, canBeNull = false)
    private Date creationDate;
    @DatabaseField(columnName = VALID_UNTIL_DATE_COLUMN_NAME)
    private Date validUntilDate;
    @DatabaseField(columnName = COMMNENTS_COLUMN_NAME)
    private String comments;
    @DatabaseField(columnName = IS_ARCHIVED_COLUMN_NAME)
    private boolean archived;
    @DatabaseField(columnName = PRIORITy_COLUMN_NAME, dataType = DataType.ENUM_INTEGER)
    private ShoppingPriority priority;
    @DatabaseField(columnName = IMAGE_URI_COLUMN_NAME)
    private String imageURI;

    public long getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getValidUntilDate() {
        return validUntilDate;
    }

    public void setValidUntilDate(Date validUntilDate) {
        this.validUntilDate = validUntilDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public ShoppingPriority getPriority() {
        return priority;
    }

    public void setPriority(ShoppingPriority priority) {
        this.priority = priority;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }
}
