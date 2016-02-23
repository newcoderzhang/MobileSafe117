package com.huilong.zhang.mobilesafe117;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import com.huilong.zhang.mobilesafe117.dao.BlackNumberDao;

import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    public Context context;

    @Override
    protected void setUp() throws Exception {
        context = getContext();
        super.setUp();

    }

    public void testAdd(){
        BlackNumberDao blackNumberDao = new BlackNumberDao(context);
        Random random = new Random();
        for (int i = 0; i < 200 ; i++) {
            Long number = 1305113508L +i;
            blackNumberDao.addBlackNumber(number + "",String.valueOf(random.nextInt(3) + 1));
        }
    }
}