package com.huilong.zhang.mobilesafe117.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

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
        Cursor blacknumber = db.query("blacknumber", new String[]{"mode"}, "number=?", new String[]{number}, null, null, null);
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
        //Thread.sleep(3000);
        SystemClock.sleep(3000);
        return blackNumberInfolists;
    }

    /**
     * 分页加载数据
     * @param pageNumber  表示当前页码
     * @param pagedatenumber    表示每页有多少数据
     * @return
     *
     * limit 表示限制当前有多少数据
     * offset表示跳过  从第几行开始
     */


    public List<BlackNumberInfo> findALlBypage(int pageNumber,int pagedatenumber) {
        List<BlackNumberInfo> blackNumberInfolists = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursorrawQu = db.rawQuery("select * from blacknumber limit ?offset ?",
                new String[]{String.valueOf(pagedatenumber), String.valueOf(pagedatenumber * pageNumber)});
        while(cursorrawQu.moveToNext()) {
            BlackNumberInfo blacknumberinfo = new BlackNumberInfo();
            blacknumberinfo.setPhonenumber(cursorrawQu.getString(1));
            blacknumberinfo.setMode(cursorrawQu.getString(0));
            blackNumberInfolists.add(blacknumberinfo);
        }
        cursorrawQu.close();
        db.close();
        //Thread.sleep(3000);
        SystemClock.sleep(1000);
        return blackNumberInfolists;
    }

    /**
     * 分批加载数据---比较常用的加载模式
     * @param startIndex 起始位置
     * @param maxCount   每个页面最大数目
     * @return
     */

    public List<BlackNumberInfo> findPar2(int startIndex, int maxCount) {
        List<BlackNumberInfo> blackNumberInfolists = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from blacknumber limit ? offset ?", new String[]{String.valueOf(maxCount),
                String.valueOf(startIndex)});while(cursor.moveToNext()) {
            BlackNumberInfo blacknumberinfo = new BlackNumberInfo();
            blacknumberinfo.setPhonenumber(cursor.getString(0));
            blacknumberinfo.setMode(cursor.getString(1));
            blackNumberInfolists.add(blacknumberinfo);
        }
        cursor.close();
        db.close();
        //Thread.sleep(3000);
        SystemClock.sleep(1000);
        return blackNumberInfolists;

    }

    public int getTotalNumber() {
            SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;

    }




}
