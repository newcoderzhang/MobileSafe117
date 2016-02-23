package com.huilong.zhang.mobilesafe117.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.huilong.zhang.mobilesafe117.bean.BlackNumberInfo;
import com.huilong.zhang.mobilesafe117.db.BlackNumberOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mario on 2/23/16.
 */
public class BlackNumberDao {
    public BlackNumberOpenHelper blackNumberOpenHelper;

    public BlackNumberDao(Context context) {
        this.blackNumberOpenHelper = new BlackNumberOpenHelper(context);
    }

    public boolean addBlackNumber(String number,String mode) {
       SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
       ContentValues contentValues = new ContentValues();
       contentValues.put("number",number);
       contentValues.put("mode",mode);
       long rowid =db.insert("blacknumber",null,contentValues);
       if(rowid == -1){
           return false;
       }
       return true;
   }

    public boolean deleatBlackNumber(String number) {
        SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();
        int rowNumber = writableDatabase.delete("blacknumber", "number=?", new String[]{number});
        if(rowNumber == 0) {
            return false;
        }
        return true;
    }

    public boolean changeNumerMode(String number) {
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int rownumber = db.update("blacknumber", contentValues, "number=?", new String[]{number});
        if(rownumber == 0) {
            return false;
        }
        return true;
    }

    public String findNumberMode(String number) {
        String mode = "";
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        Cursor blacknumber = db.query("blacknumber", new String[]{mode}, "number=?", new String[]{number}, null, null, null);
        if(blacknumber.moveToNext()){
            mode = blacknumber.getString(0);
        }
        blacknumber.close();
        db.close();
        return mode;
    }

    public List<BlackNumberInfo> findALl() {
        List<BlackNumberInfo> blackNumberInfolists = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"number", "mode"}, null, null, null, null, null);
        while(cursor.moveToNext()) {
            BlackNumberInfo blacknumberinfo = new BlackNumberInfo();
            blacknumberinfo.setPhonenumber(cursor.getString(0));
            blacknumberinfo.setMode(cursor.getString(1));
            blackNumberInfolists.add(blacknumberinfo);
        }
        cursor.close();
        db.close();
        return blackNumberInfolists;
    }




}
