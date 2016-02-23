package com.huilong.zhang.mobilesafe117.dao;

import android.content.Context;

import com.huilong.zhang.mobilesafe117.db.BlackNumberOpenHelper;

/**
 * Created by Mario on 2/23/16.
 */
public class BlackNumberDao {
    public BlackNumberOpenHelper blackNumberOpenHelper;

    public BlackNumberDao(Context context) {
        this.blackNumberOpenHelper = new BlackNumberOpenHelper(context);
    }


}
