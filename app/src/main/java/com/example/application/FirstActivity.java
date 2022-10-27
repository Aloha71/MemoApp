package com.example.application;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class FirstActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Button button = this.findViewById(R.id.Loading);
        SharedPreferences user = FirstActivity.this.getSharedPreferences("user",MODE_PRIVATE);
        String username = user.getString("username",null);
        //实现自动加载对应用户界面
        if(username != null){
            Intent intent = new Intent(FirstActivity.this,MainActivity.class);
            intent.putExtra("username",username);
            startActivity(intent);
            FirstActivity.this.finish();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                    startActivity(intent);
                    FirstActivity.this.finish();
            }
        });
    }


}