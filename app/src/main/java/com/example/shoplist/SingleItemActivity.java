package com.example.shoplist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shoplist.model.DatabaseHelper;
import com.example.shoplist.model.ShoppingItem;
import com.example.shoplist.model.ShoppingItemDao;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by O10 on 24.08.2016.
 */

public class SingleItemActivity extends AppCompatActivity {
    public static final String EDITED_ITEM_ID = "edit_id";
    public static final String DATE_FORMAT = "dd-MM-yyyy";

    private EditText itemName, itemComments, currentDate, validDate;
    private Spinner prioritySpinner;
    private ImageView priorityIndicator;
    private DatePickerDialog creationDatePicker;
    private DatePickerDialog validDatePicker;

    private ShoppingItemDao shoppingItemDao;
    private ShoppingItem editedItem;
    private Calendar creationCalendar = Calendar.getInstance();
    private Calendar validCalendar = Calendar.getInstance();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO This layout need some serious improvements
        setContentView(R.layout.activity_single_item);

        itemName = (EditText) findViewById(R.id.edit_item_name);
        itemComments = (EditText) findViewById(R.id.edit_item_comments);
        currentDate = (EditText) findViewById(R.id.text_creation_date);
        validDate = (EditText) findViewById(R.id.text_valid_date);
        prioritySpinner = (Spinner) findViewById(R.id.priority_spinner);
        priorityIndicator = (ImageView) findViewById(R.id.priority_indicator);

        try {
            shoppingItemDao = OpenHelperManager.getHelper(this, DatabaseHelper.class).getShoppingItemDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long editedID = getIntent().getLongExtra(EDITED_ITEM_ID, -1);

        if (editedID > 0) {
            initAsEditItem(editedID);
        } else {
            initAsNewItem();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_single_item_activity, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        final MenuItem menuItem = menu.findItem(R.id.menu_save_item);
        final MenuItem itemDelete = menu.findItem(R.id.menu_delete_item);
        if (editedItem.isArchived()) {
            menuItem.setEnabled(false);
            menuItem.setVisible(false);
        }
        if (editedItem.getItemID() < 1) {
            itemDelete.setEnabled(false);
            itemDelete.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_save_item:
                saveUiData();
                shoppingItemDao.createOrUpdateItem(editedItem);
                Toast.makeText(this, getString(R.string.item_save), Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.menu_delete_item:
                shoppingItemDao.deleteItemOrMoveToArchived(editedItem.getItemID());
                if (editedItem.isArchived()) {
                    Toast.makeText(this, getString(R.string.item_del), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.move_arch), Toast.LENGTH_SHORT).show();
                }
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initAsNewItem() {
        editedItem = new ShoppingItem();
        getSupportActionBar().setTitle(getString(R.string.new_item));
        currentDate.setText(simpleDateFormat.format(creationCalendar.getTime()));
        editedItem.setCreationDate(creationCalendar.getTime());
        validCalendar.add(Calendar.DATE, 1);
        validDate.setText(simpleDateFormat.format(validCalendar.getTime()));
        editedItem.setValidUntilDate(validCalendar.getTime());
        editedItem.setPriority(ShoppingItem.ShoppingPriority.LOW);
    }

    private void initAsEditItem(long id) {
        editedItem = shoppingItemDao.getShoppingItem(id);
        getSupportActionBar().setTitle(editedItem.getItemName());
        itemName.setText(editedItem.getItemName());
        creationCalendar.setTime(editedItem.getCreationDate());
        currentDate.setText(simpleDateFormat.format(creationCalendar.getTime()));
        validCalendar.setTime(editedItem.getValidUntilDate());
        validDate.setText(simpleDateFormat.format(validCalendar.getTime()));
        if (editedItem.getComments() != null) {
            itemComments.setText(editedItem.getComments());
        }
        if (editedItem.isArchived()) {
            handleArchivedItem();
        }
    }

    private void handleArchivedItem() {
        itemName.setEnabled(false);
        itemComments.setEnabled(false);
        currentDate.setEnabled(false);
        validDate.setEnabled(false);
        prioritySpinner.setEnabled(false);
        invalidateOptionsMenu();
    }

    private void initViews() {
        currentDate.setKeyListener(null);
        validDate.setKeyListener(null);
        creationDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                creationCalendar.set(year, monthOfYear, dayOfMonth);
                editedItem.setCreationDate(creationCalendar.getTime());
                currentDate.setText(simpleDateFormat.format(creationCalendar.getTime()));
            }
        }, creationCalendar.get(Calendar.YEAR), creationCalendar.get(Calendar.MONTH), creationCalendar.get(Calendar.DAY_OF_MONTH));


        validDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                validCalendar.set(year, monthOfYear, dayOfMonth);
                editedItem.setValidUntilDate(validCalendar.getTime());
                validDate.setText(simpleDateFormat.format(validCalendar.getTime()));
            }
        }, validCalendar.get(Calendar.YEAR), validCalendar.get(Calendar.MONTH), validCalendar.get(Calendar.DAY_OF_MONTH));


        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (v == currentDate) {
                        creationDatePicker.show();
                    } else {
                        validDatePicker.show();
                    }
                }
            }
        };
        currentDate.setOnFocusChangeListener(onFocusChangeListener);
        validDate.setOnFocusChangeListener(onFocusChangeListener);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priorities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                      @Override
                                                      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                          switch (position) {
                                                              case 0:
                                                                  priorityIndicator.setImageResource(R.drawable.priority_drawable_green);
                                                                  editedItem.setPriority(ShoppingItem.ShoppingPriority.LOW);
                                                                  break;
                                                              case 1:
                                                                  priorityIndicator.setImageResource(R.drawable.priority_drawable_yellow);
                                                                  editedItem.setPriority(ShoppingItem.ShoppingPriority.NORMAL);
                                                                  break;
                                                              case 2:
                                                              default:
                                                                  priorityIndicator.setImageResource(R.drawable.priority_drawable_red);
                                                                  editedItem.setPriority(ShoppingItem.ShoppingPriority.HIGH);
                                                                  break;
                                                          }
                                                      }

                                                      @Override
                                                      public void onNothingSelected(AdapterView<?> parent) {

                                                      }
                                                  }
        );

        switch (editedItem.getPriority()) {
            case LOW:
                prioritySpinner.setSelection(0);
                priorityIndicator.setImageResource(R.drawable.priority_drawable_green);
                break;
            case NORMAL:
                prioritySpinner.setSelection(1);
                priorityIndicator.setImageResource(R.drawable.priority_drawable_yellow);
                break;
            case HIGH:
            default:
                prioritySpinner.setSelection(2);
                priorityIndicator.setImageResource(R.drawable.priority_drawable_red);
        }
    }

    private void saveUiData() {
        editedItem.setItemName(String.valueOf(itemName.getText()));
        editedItem.setCreationDate(creationCalendar.getTime());
        editedItem.setValidUntilDate(validCalendar.getTime());
        editedItem.setComments(String.valueOf(itemComments.getText()));
    }

}
