package com.codepath.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT ="item_text";
    public static final String KEY_ITEM_POSITION ="item_position";
    public static final int EDIT_TEXT_CODE = 20;
    List<String> items;

    Button btnAdd;
    EditText edItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        edItem = findViewById(R.id.edItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();
        /*items = new ArrayList<>();
        items.add("Buy milk");
        items.add("Go to the gym");
        items.add("Have fun");*/

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position){
                //Delete the item from the model
                items.remove(position);
                //Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position){
                Log.d("Main Activity", "Single click at position" + position);
                //Create the new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                //Pass the data being edited
                i.putExtra(KEY_ITEM_TEXT,items.get(position));
                i.putExtra(KEY_ITEM_POSITION,items.get(position));
                //Display activity
                startActivityForResult(i,EDIT_TEXT_CODE);
            }
        };

        final ItemsAdapter itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = edItem.getText().toString();
                //Add to the model
                items.add(todoItem);
                //Notify adapter that an item is inserted
                itemsAdapter.notifyItemChanged(items.size()-1);
                edItem.setText("");
                Toast.makeText(getApplicationContext(),"Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

    }

    //handle the edit activity
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RESULT_OK) {
            //Retrieve updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //Extract original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            //Update the model with the new item text
            items.set(position, itemText);
            //notify the adapter
            itemsAdapter.notifyItemChanged(position);
            //persist changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
        //super.onActivityResult(requestCode, resultCode, data);
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    //This function will load items by reading every line of the data file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e){
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    //This function saves items by writing them nto the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        }catch (IOException e){
            Log.e("MainActivity", "Error writing items",e);
        }
    }

}