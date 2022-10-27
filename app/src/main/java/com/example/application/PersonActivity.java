package com.example.application;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PersonActivity extends Activity {
    ImageButton buttonRefresh,buttonAdd,buttonHome,buttonPerson;
    Button button,btexit;
    TextView text,tv;
    DBOpenHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        dbHelper = new  DBOpenHelper(PersonActivity.this,"Memo", null, 2);
        Intent intent = new Intent(PersonActivity.this.getIntent());

        String username = intent.getStringExtra("username").toString();//获取用户名

        int index = dbHelper.getCount(username);//获取记录行数
        String score = String.valueOf(index);

        //初始化组件
        button = this.findViewById(R.id.person_button);
        buttonAdd = this.findViewById(R.id.person_add_product);
        buttonHome = this.findViewById(R.id.person_home_page);
        buttonRefresh = this.findViewById(R.id.person_refresh);
        buttonPerson = this.findViewById(R.id.person_personal_center);
        btexit = this.findViewById(R.id.exit_button);
        text = this.findViewById(R.id.person_text);
        tv = this.findViewById(R.id.tvperson);

        tv.setText(username+" 记录的备忘录总条数为");
        text.setText(score);//显示记录条数

        button.setOnClickListener(new View.OnClickListener() {//软件介绍界面跳转
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, AppActivity.class);
                startActivity(intent);
            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {//home界面跳转
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, MainActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
                PersonActivity.this.finish();
                Toast.makeText(getApplicationContext(), "返回主页", Toast.LENGTH_SHORT).show();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {//Add界面跳转
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, AddActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        buttonRefresh.setOnClickListener(new View.OnClickListener() {//刷新界面
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "已经刷新,请回主页查看", Toast.LENGTH_SHORT).show();
            }
        });

        buttonPerson.setOnClickListener(new View.OnClickListener() {///person界面跳转
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "已经在个人中心", Toast.LENGTH_SHORT).show();
            }
        });


        btexit.setOnClickListener(new View.OnClickListener() {//退出登录
            @Override
            public void onClick(View view) {
                showDialog(1);
            }
        });
    }


    public Dialog onCreateDialog(int id){//对话框函数
        // TODO Auto-generated method stub
        if (id==1)//当点击注销超链接时打开的对话框
        {
            AlertDialog.Builder dialog1 = new AlertDialog.Builder(PersonActivity.this);
            dialog1.setTitle("退出登录");
            dialog1.setMessage("确定要退出当前用户登录吗？");
            dialog1.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(PersonActivity.this, LoginActivity.class);
                    //清除已保留的信息,并退出登录
                    SharedPreferences user = PersonActivity.this.getSharedPreferences("user",MODE_PRIVATE);
                    SharedPreferences.Editor editor = user.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(intent);
                    PersonActivity.this.finish();
                }
            });
            dialog1.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // setTitle("点击了对话框的取消按钮");
                }
            });
            return dialog1.create();
        }
        else
        {
            AlertDialog.Builder dialog1 = new AlertDialog.Builder(PersonActivity.this);
            return dialog1.create();
            }

    }


}