package com.example.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    ImageButton buttonRefresh,buttonAdd,buttonHome,buttonPerson;
    Button btmod;
    String username;
    TextView tvself;
    EditText etinfo;
    DBOpenHelper dbHelper;
    File userFilePath;
    List<String> listString=new ArrayList<String>();
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this.getIntent());

        username = intent.getStringExtra("username").toString();//接收用户名信息

        dbHelper = new  DBOpenHelper(MainActivity.this,"Memo", null, 2);
        dbHelper.createtword(username);//创建对应用户备忘录表
        int index = dbHelper.getCount(username);//获取记录行数

        buttonPerson = this.findViewById(R.id.ib_personal_center);///person界面跳转
        buttonRefresh = this.findViewById(R.id.refresh);//刷新界面
        buttonAdd = this.findViewById(R.id.ib_add_product);//Add界面跳转
        buttonHome = this.findViewById(R.id.ib_home_page);//home界面跳转
        listview = this.findViewById(R.id.main_list);//List界面跳转
        tvself = this.findViewById(R.id.tvself);
        btmod = this.findViewById(R.id.btchange);

        tvself.setText(readUserinfo(username));//读取个性签名填入textview中

        //显示备忘录功能
        listString = dbHelper.readAllCommodities(username);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,listString);
        listview.setAdapter(adapter);

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listString = dbHelper.readAllCommodities(username);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,listString);
                listview.setAdapter(adapter);
                Toast.makeText(getApplicationContext(), "已经刷新", Toast.LENGTH_SHORT).show();
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = (String) listview.getAdapter().getItem(position);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("position",position);
                bundle1.putString("title",s);
                Intent intent = new Intent(MainActivity.this, ListViewActivity.class);
                intent.putExtras(bundle1);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "已在主页", Toast.LENGTH_SHORT).show();
            }
        });

        buttonPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PersonActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
                MainActivity.this.finish();
                Toast.makeText(getApplicationContext(), "进入个人中心", Toast.LENGTH_SHORT).show();
            }
        });

        btmod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(2);
            }
        });
    }

    public Dialog onCreateDialog(int id){
        // TODO Auto-generated method stub
        if (id==1)//当点击注销超链接时打开的对话框
        {
            AlertDialog.Builder dialog1 = new AlertDialog.Builder(MainActivity.this);
            return dialog1.create();
        }
        else//当点击修改签名时打开的对话框
        {
            LayoutInflater li = LayoutInflater.from(this);//自定义对话框布局
            final View edit = li.inflate(R.layout.editinfo, null);//获取r.layout.editinfo中的布局文件
            AlertDialog.Builder dialog1 = new AlertDialog.Builder(MainActivity.this);
            etinfo = edit.findViewById(R.id.editUserInfo);//获取布局中的edittext控件
            etinfo.setText(readUserinfo(username));//获取原始的个性签名
            dialog1.setIcon(R.drawable.ic_launcher).setTitle("修改个性签名").setView(edit);
            dialog1.setPositiveButton("确定", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {//当点击确定时，修改个性签名
                    String n_userinfo = etinfo.getText().toString();//获取edittext控件中的内容
                    if (n_userinfo.length()==0)//如果为空则改为无
                        n_userinfo = "无个性签名";
                    userFilePath = new File("/data/data/com.example.logintest/");//创建files文件夹，不写也不影响
                    if(!userFilePath.exists()){
                        userFilePath.mkdirs();
                    }
                    try {
                        //以username为文件名在默认路径下打开或者创建一个txt文件
                        FileOutputStream fos = openFileOutput(username+".txt",Context.MODE_PRIVATE);
                        fos.write(n_userinfo.getBytes());//写入文件流
                        fos.flush();
                        fos.close();
                        tvself.setText(readUserinfo(username));//更新个性签名后同时更新文本框里显示的原始个性签名内容
                        Toast.makeText(getBaseContext(), "个性签名已修改", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            dialog1.setNegativeButton("取消", new DialogInterface.OnClickListener(){//当点击确定时，退出
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            return dialog1.create();
        }
    }

    private CharSequence readUserinfo(String username) {
        // TODO Auto-generated method stub
        String result="";
        try{
            File userFile = new File("/data/data/com.example.application/files/"+username+".txt");
            if (!userFile.exists())//判断username.txt是否存在，如果不存在，则表示用户再注册时没有填写个人简介
                return "未填写";
            FileInputStream fis = this.openFileInput(username+".txt");//如存在username.txt则打开
            int lenght = fis.available();
            byte[] buffer = new byte[lenght];
            fis.read(buffer);
            result = new String(buffer, "UTF-8");//转换成String
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;//返回结果
    }
}
