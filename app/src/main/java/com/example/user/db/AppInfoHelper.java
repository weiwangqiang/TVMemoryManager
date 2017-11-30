package com.example.user.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.user.bean.AppInfoBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by user on 2017/11/23.
 */

public class AppInfoHelper extends SQLiteOpenHelper {
    private String TAG = "AppInfoHelper " ;
    private static final int version = 1;//数据库版本
    private static final String name = "TvManagerAlive";//数据库名

    public AppInfoHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + AppInfoTable.TABLENAME + " ( "
                + AppInfoTable.PACKAGENAME + " VARCHAR unique, "
                + AppInfoTable.PROCESSNAME + " VARCHAR null );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + AppInfoTable.TABLENAME);
    }

    public void insert(AppInfoBean appInfoBean) {
        if (null == appInfoBean) return;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppInfoTable.PACKAGENAME, appInfoBean.getPackageName());
        values.put(AppInfoTable.PROCESSNAME, appInfoBean.getProcessName());
        db.insert(AppInfoTable.TABLENAME, null, values);
    }

    public List<AppInfoBean> selectAll() {
        List<AppInfoBean> res = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                AppInfoTable.PACKAGENAME,
                AppInfoTable.PROCESSNAME
        };
        Cursor c = db.query(
                AppInfoTable.TABLENAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                ""                                 // The sort order
        );
        while (c.moveToNext()) {
            AppInfoBean bean = new AppInfoBean();
            bean.setPackageName(c.getString(0));
            bean.setProcessName(c.getString(1));
            res.add(bean);
            Log.i(TAG, "selectAll: name "+bean.getProcessName());
        }
        c.close();
        return res;
    }
    public void delete(AppInfoBean appInfoBean){
        SQLiteDatabase db = getReadableDatabase();
        String selection = AppInfoTable.PACKAGENAME + " = ?";
        String[] selectionArgs = { appInfoBean.getPackageName() };
        db.delete(AppInfoTable.TABLENAME, selection, selectionArgs);
    }
}
