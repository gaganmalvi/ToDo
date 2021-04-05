package com.malviscape.todo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.malviscape.todo.R.string.*;
import static org.apache.commons.io.FileUtils.writeLines;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView ListItems;
    private CoordinatorLayout mCLayout;
    private Typeface mTypeface;
    private Context mContext;
    private Activity mActivity;

    public void Toolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
    }

    private void readToDo() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo_contents.cartel");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    private void saveToDo() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo_contents.cartel");
        try {
            writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mActivity = MainActivity.this;
        mCLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/and_black.ttf");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListItems = findViewById(R.id.ListItems);
        items = new ArrayList<>();
        readToDo();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position, convertView, parent);

                // Set the typeface/font for the current item
                item.setTypeface(mTypeface);

                // Set the font size.
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);

                // return the view
                return item;

            }

        };
        ListItems.setAdapter(itemsAdapter);
        setupListViewListener();
        Toolbar();
    }
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ImageView emptyView  = findViewById(R.id.animationView);
        ListView ListItems = (ListView) findViewById(R.id.ListItems);
        ListItems.setEmptyView(emptyView);
    }

    public void fab(View v) {
        EditText editToDo = findViewById(R.id.editToDo);
        String itemText = editToDo.getText().toString();
        if (editToDo.getText().toString().trim().length() <= 0) {
            Toast.makeText(MainActivity.this, toast_empty, Toast.LENGTH_SHORT).show();
        }
        else {
            itemsAdapter.add("- "+itemText);
            editToDo.setText("");
        }
        saveToDo();
    }

    private void setupListViewListener() {
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        ListItems,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    items.remove(position);
                                    itemsAdapter.notifyDataSetChanged();
                                    saveToDo();

                                }


                            }
                        });
        ListItems.setOnTouchListener(touchListener);

        // Add long click to remove too.
        ListItems.setOnItemLongClickListener(
                (adapter, item, pos, id) -> {
                    items.remove(pos);
                    itemsAdapter.notifyDataSetChanged();
                    saveToDo();
                    return true;
                });

    }

}

