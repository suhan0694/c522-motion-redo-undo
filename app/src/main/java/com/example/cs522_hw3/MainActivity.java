package com.example.cs522_hw3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cs522_hw3.TextViewUndoRedo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button undoBtn = findViewById(R.id.button);
        Button redoBtn = findViewById(R.id.button2);
        EditText editTxt = (EditText) findViewById(R.id.editText);

        TextViewUndoRedo helper = new TextViewUndoRedo(editTxt);

        undoBtn.setOnClickListener(view -> {
            //Toast.makeText(this,"Hello", Toast.LENGTH_LONG).show();
            helper.undo();
        });

        redoBtn.setOnClickListener(view -> {
            helper.redo();
        });


    }
}
