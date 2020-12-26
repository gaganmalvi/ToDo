package com.malviscape.todo;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.apache.commons.io.FileUtils.writeLines;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView ListItems;

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListItems = findViewById(R.id.ListItems);
        items = new ArrayList<>();
        readToDo();
        itemsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);
        ListItems.setAdapter(itemsAdapter);
        setupListViewListener();
        Toolbar();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ImageView emptyView  = findViewById(R.id.emptyimage);
        ListView ListItems = (ListView) findViewById(R.id.ListItems);
        ListItems.setEmptyView(emptyView);
    }

    public void fab(View v) {
        EditText editToDo = findViewById(R.id.editToDo);
        String itemText = editToDo.getText().toString();
        itemsAdapter.add(itemText);
        editToDo.setText("");
        saveToDo();
    }

    private void setupListViewListener() {
        ListItems.setOnItemLongClickListener(
                (adapter, item, pos, id) -> {
                    items.remove(pos);
                    itemsAdapter.notifyDataSetChanged();
                    saveToDo();
                    return true;
                });

    }
}

