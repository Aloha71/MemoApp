package com.example.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegUserActivity extends Activity{
    TextView tvuser,tvpwd,tvpwd2,tvphone,tvinfo;
    EditText etuser,etpwd,etpwd2,etphone,etinfo;
    Button btnRe;
    String username,pwd,pwd2,phone,userinfoStr;
    DBOpenHelper dbhelper;//用户数据库
    private File userFilePath = null;//保存文件路径
    private File userFile = null;//新建文件名称
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_user);
        this.setTitle("用户注册");
        GetView();//获取所有的UI控件
        SetView();//初始化所有UI控件

        btnRe = this.findViewById(R.id.btnRe);
        OnClickListener ls = new OnClickListener() {//定义注册按钮的点击事件，完成用户注册操作

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                username = etuser.getText().toString();//获取用户注册时各项输入内容
                pwd = etpwd.getText().toString();
                pwd2 = etpwd2.getText().toString();
                phone = etphone.getText().toString();
                userinfoStr = etinfo.getText().toString();
                dbhelper = new DBOpenHelper(RegUserActivity.this,"Memo",null,2);//实例化一个dbhelper,传入context，数据库名，版本号
                dbhelper.createtable();//调用createtable方法创建user表
                if (username.length()*pwd.length()*pwd2.length()!=0)//判断必填项是否都填写了
                {
                    if (pwd.equals(pwd2))//判断两次填写的密码是否一致
                    {
                        Cursor cursor = dbhelper.select(username);//首先根据用户名进行查询，判断是否已经存在同名用户
                        if(cursor.getCount()==0)//如果没有找到记录则可以注册
                        {
                            dbhelper.insert(username, pwd, phone);//通过insert方法在user表中插入一条记录，写入用户名、密码、电话到对应字段
                            if (userinfoStr.length()>0)//判断个人简介是否填写了，如果填写了，则将内容保存成一个txt文件
                            {
                                userFilePath = new File("/data/data/com.example.application/");
                                if(!userFilePath.exists()){
                                    userFilePath.mkdirs();
                                }
                                try {
                                    //以username为文件名在默认路径下创建一个txt文件
                                    FileOutputStream fos = openFileOutput(username+".txt",Context.MODE_PRIVATE);
                                    fos.write(userinfoStr.getBytes());
                                    fos.flush();
                                    fos.close();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                            Intent intent = new Intent(RegUserActivity.this,LoginActivity.class);//注册完成后进入注册成功界面
                            Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            RegUserActivity.this.finish();
                        }
                        else//如果游标中有记录，则表示该用户以及被注册过了
                        {
                            Toast.makeText(getBaseContext(), "该用户名已被注册", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else//如果两次密码不相同
                    {
                        Toast.makeText(getBaseContext(), "两次密码设置内容不一致", Toast.LENGTH_SHORT).show();
                    }
                }
                else//如果必填项不完整
                {
                    Toast.makeText(getBaseContext(), "请填写必要的信息（用户名，密码，确认密码）", Toast.LENGTH_SHORT).show();
                }
            }};
        btnRe.setOnClickListener(ls);
    }
    private void SetView() {
        // TODO Auto-generated method stub
        String str="用户名(<font color='#FF0000'>*</font>)";//设置一个红色的*号在后面，表示必填项
        tvuser.setText(Html.fromHtml(str));
        str="密码(<font color='#FF0000'>*</font>)";
        tvpwd.setText(Html.fromHtml(str));
        str="确认密码(<font color='#FF0000'>*</font>)";
        tvpwd2.setText(Html.fromHtml(str));
        tvphone.setText("输入手机");
        tvinfo.setText("个性签名");
        btnRe.setText("确认注册");
    }
    private void GetView() {
        // TODO Auto-generated method stub
        tvuser = this.findViewById(R.id.txtUser);
        tvpwd = this.findViewById(R.id.txtPwd);
        tvpwd2 = this.findViewById(R.id.txtPwd2);
        tvphone = this.findViewById(R.id.txtPhone);
        etuser = this.findViewById(R.id.user);//用户名输入框
        etpwd = this.findViewById(R.id.pwd1);
        etpwd2 = this.findViewById(R.id.pwd2);
        etphone = this.findViewById(R.id.phone);
        btnRe = this.findViewById(R.id.btnRe);
        tvinfo = this.findViewById(R.id.txtInfo);
        etinfo = this.findViewById(R.id.info);
    }
}