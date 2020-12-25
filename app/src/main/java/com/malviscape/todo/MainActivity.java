package com.malviscape.todo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private android.widget.ArrayAdapter<String> itemsAdapter;
    private android.widget.ListView ListItems;
    private android.widget.EditText editToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListItems = (android.widget.ListView) findViewById(R.id.ListItems);
        items = new ArrayList<String>();
        itemsAdapter = new android.widget.ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        ListItems.setAdapter(itemsAdapter);
        items.add("First Item");
    }

    public void fab(View v) {
        android.widget.EditText editToDo = (android.widget.EditText) findViewById(R.id.editToDo);
        String itemText = editToDo.getText().toString();
        itemsAdapter.add(itemText);
        editToDo.setText("");
    }
}

