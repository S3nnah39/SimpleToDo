package com.codepath.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText etItem;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etItem = findViewById(R.id.etItem);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit Item");
        etItem.setText(getIntent().getStringExtra(com.codepath.simpletodo.MainActivity.KEY_ITEM_TEXT));

        // When user done editing
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //create an intent containing results
                Intent intent = new Intent();
                //pass the data(results of editing)
                intent.putExtra(com.codepath.simpletodo.MainActivity.KEY_ITEM_TEXT, etItem.getText().toString());
                intent.putExtra(com.codepath.simpletodo.MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(com.codepath.simpletodo.MainActivity.KEY_ITEM_POSITION));

                //set the results of the intent
                setResult(RESULT_OK,intent);
                //Finish activity
                finish();
            }
        });

    }

}