package com.example.application;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
    Context context;//上下文变量，在构造函数中传入
    SQLiteDatabase db;//sqlite数据库对象，完成各类数据库操作
//    public static String name = "Memo";
    String db_name = "Memo";//数据库文件名，在构造函数中传入
    String table_name = "user";//用于存放用户信息的表名
    final String Tag = "sqlop";//写入日志时用的标签

    public DBOpenHelper( Context context, String name,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
        this.context = context;
        this.db_name = name;
        //根据传入的数据库名打开或者创建一个数据库，返回sqlite对象
        //数据库文件默认存放在应用根目录的databases文件夹下，根据db_name命名，注意如果db_name没有后缀名，不会自动加.db
        db = context.openOrCreateDatabase(name, Context.MODE_PRIVATE, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    protected void createtable() {
        // TODO Auto-generated method stub
        try
        {
            //创建用户表的SQL语句
            String sql = "Create table if not exists " + table_name +
                    "(username Varchar Primary Key not null, pwd Varchar not null, phone Varchar);";
            db.execSQL(sql);//执行SQL语句
            Log.v(Tag, "Create Table...");
        }
        catch (Exception e)
        {
            Log.v(Tag, e.toString());
        }
    }
    //插入新的用户，注册用户时使用
    protected void insert(String username, String pwd, String phone)
    {
        try
        {
            db = context.openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);//首先根据数据库名打开数据库，返回sqlite对象
            String sql = "Insert into " + table_name +
                    " values('" + username +"','" + pwd +"','" + phone +"');";//构建插入用户的SQL
            db.execSQL(sql);//执行SQL
            Log.v(Tag, "Insert User...");
        }
        catch (Exception e)
        {
            Log.v(Tag, e.toString());
        }
    }
    //更新记录，修改密码及用户资料时使用
    protected void update(String username, String pwd, String phone)
    {
        try
        {
            db = context.openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
            String sql = "update " + table_name +
                    " set pwd='" + pwd +"',phone='" + phone +"' where username='" + username +"';";
            db.execSQL(sql);
            Log.v(Tag, "Update User...");
        }
        catch (Exception e)
        {
            Log.v(Tag, e.toString());
        }
    }

    //查询某一条记录，登录显示用户信息时使用
    protected Cursor select(String username)
    {
        try
        {
            db = context.openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
            String sql = "username='" + username +"'";
            Cursor cr = db.query(table_name, null, sql, null, null, null, null);//通过query方法进行查询，返回一个游标
            Log.v(Tag, "Select User...");
            return cr;
        }
        catch (Exception e)
        {
            Log.v(Tag, e.toString());
            return null;
        }
    }

    protected void createtword(String username) {
        // TODO Auto-generated method stub
        try
        {
            //创建用户表的SQL语句
            String sql = "Create table if not exists "+ username + "(_id integer primary key, word text);";
            db.execSQL(sql);//执行SQL语句
            Log.v(Tag, "Create Table...");
        }
        catch (Exception e)
        {
            Log.v(Tag, e.toString());
        }
    }

    protected List<String> readAllCommodities(String username) {
        List<String> allCommodities = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select * from " + username + " order by _id";
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()) {
            do {
                int index = cursor.getColumnIndex("word");
                String title = cursor.getString(index);
                allCommodities.add(title);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return allCommodities;
    }
    protected boolean addMyCollection(String str,String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word",str);
        db.insert(username,null,values);
        values.clear();
        return true;
    }
    protected void deleteMyCollection(String word,String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(db.isOpen()) {
            db.delete(username,"word=?",new String[]{word+""});
            db.close();
        }
    }

    protected boolean updateMemo(String word,String wordP,String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "update  "+ username +" set word=? where word=?";
        String[] obj = new String[]{word,wordP};
        db.execSQL(sql,obj);
        return true;
    }

    protected int getCount(String username){
        String sql = "select * from " + username;
        Cursor cursor = db.rawQuery(sql,null);
        int count = cursor.getCount();
        return count;
    }


}
