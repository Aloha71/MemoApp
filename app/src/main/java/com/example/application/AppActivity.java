package com.example.application;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AppActivity extends Activity {
    TextView tvurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        Button button = this.findViewById(R.id.person_button);
        tvurl = this.findViewById(R.id.tvurl);

        tvurl.setOnClickListener(new View.OnClickListener() {//实现开发者网址跳转
            @Override
            public void onClick(View view) {
                Uri url = Uri.parse(tvurl.getText().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW,url);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}