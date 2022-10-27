package com.example.application;

import android.app.Activity;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends Activity {
    String username;
    Button btadd,btcan;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        btadd = this.findViewById(R.id.add_button);
        btcan = this.findViewById(R.id.cancel_button);
        editText = this.findViewById(R.id.add_text);

        Intent intent = new Intent(AddActivity.this.getIntent());
        username = intent.getStringExtra("username");

        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                DBOpenHelper dbHelper = new DBOpenHelper(AddActivity.this, "Memo", null, 2);
                if(str.length() != 0){
                    if(dbHelper.addMyCollection(str,username)){
                        Toast.makeText(getApplicationContext(), "添加成功,请刷新界面!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btcan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}