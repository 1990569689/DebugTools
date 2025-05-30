package com.ddonging.debugtools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    //Log
    public static final String CREATE_LOG = "create table MqttLog(" + "id integer primary key autoincrement," + "name text," + "username text," +  "password text," +  "address text," +  "sid text," +  "topic text," +  "port text," + "date text)";
    private final Context mContext;
    //构造方法：
    // 第一个参数Context上下文，
    // 第二个参数数据库名，
    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory , int version){
        super(context,name ,factory,version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //调用SQLiteDatabase中的execSQL（）执行建表语句。
        db.execSQL(CREATE_LOG);
        //创建成功
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //更新表
        db.execSQL("drop table if exists Log");
        onCreate(db);
    }
}