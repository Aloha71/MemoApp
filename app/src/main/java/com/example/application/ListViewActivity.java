package com.example.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ListViewActivity extends Activity {
    EditText text;
    Button button_up,button_delete,button_re;

    int position;
    Intent intent;
    String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        text = this.findViewById(R.id.listView_text);
        button_delete = this.findViewById(R.id.listView_delete);
        button_up = this.findViewById(R.id.listView_updata);
        button_re = this.findViewById(R.id.btnre);
        Bundle b = getIntent().getExtras();
        String username = getIntent().getStringExtra("username");
        if( b != null) {
            str=b.getString("title");
            //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            text.setText(str.toCharArray(), 0, str.length());
            position = b.getInt("position");
        }
        button_delete.setOnClickListener(new View.OnClickListener() {//delete
            @Override
            public void onClick(View v) {
                DBOpenHelper dbHelper = new DBOpenHelper(getApplicationContext(), "Memo", null, 2);
                dbHelper.deleteMyCollection(str,username);
                Toast.makeText(getApplicationContext(), "删除成功,请刷新界面!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        button_up.setOnClickListener(new View.OnClickListener() {//delete
            @Override
            public void onClick(View v) {
                String wordNew="";
                wordNew = text.getText().toString();
                DBOpenHelper dbHelper = new DBOpenHelper(ListViewActivity.this, "Memo", null, 2);
                if(dbHelper.updateMemo(wordNew, str, username)){
                    Toast.makeText(getApplicationContext(), "更新成功,请刷新界面!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        button_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Toast.makeText(getApplicationContext(), "返回主页", Toast.LENGTH_SHORT).show();
            }
        });
    }


}