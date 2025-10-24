package com.metawebthree.app.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ErpDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "erp.db";

    public ErpDBHelper(@Nullable Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table erp(_id integer primary key autoincrement,name text,age integer );";
        sqLiteDatabase.execSQL(sql);
        String sql2 = "insert into erp(name,age) values('测试一',21),('测试二',21),('测试三',26)";
        sqLiteDatabase.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
