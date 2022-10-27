package com.example.application;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
    EditText etname,etpwd;
    Button btlogin,btenroll;
    String username,pwd;
    DBOpenHelper dbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etname = this.findViewById(R.id.etname);
        etpwd = this.findViewById(R.id.etpwd);
        btlogin = this.findViewById(R.id.btlogin);
        btenroll = this.findViewById(R.id.btenroll);

        btlogin.setOnClickListener(new View.OnClickListener() {//登陆按钮
            @Override
            public void onClick(View view) {
                username = etname.getText().toString();//获取登录名和密码
                pwd = etpwd.getText().toString();
                //实例化一个dbhelper,传入context，数据库名，版本号
                dbhelper = new DBOpenHelper(LoginActivity.this,"Memo",null,2);
                dbhelper.createtable();//调用createtable方法创建user表
                if (username.length()*pwd.length()!=0)//判断登录名和密码是否都已填写
                {
                    Cursor cursor = dbhelper.select(username);//首先根据用户名进行查询，判断该用户是否存在
                    if(cursor.getCount()>0)//如果用户存在
                    {
                        cursor.moveToFirst();//定位游标到查询到的第一条记录
                        String userpwd = cursor.getString(1);//获取记录中第二个字段（密码）的值
                        if (userpwd.equals(pwd))//判断用户输入的密码是否和数据库中查询到的密码一致
                        {
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
                                SharedPreferences.Editor edit = sharedPreferences.edit();
                                //使用put方法将数据存入Shareprences中
                                edit.putString("username",username);
                                edit.putString("password",userpwd);
                                edit.commit();
                                intent.putExtra("username", username);//传递用户名信息
                                startActivity(intent);
                                LoginActivity.this.finish();
                            } else//密码不正确时
                                Toast.makeText(getBaseContext(), "密码不正确", Toast.LENGTH_SHORT).show();
                    }
                    else//找不到用户信息时
                    {
                        Toast.makeText(getBaseContext(), "用户名不存在", Toast.LENGTH_SHORT).show();
                        }
                }
                else//填入信息不全时
                {
                    Toast.makeText(getBaseContext(), "请填写必要的登录信息（用户名，密码）", Toast.LENGTH_SHORT).show();
                    }
        }});

        btenroll.setOnClickListener(new View.OnClickListener() {//注册按钮
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegUserActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}