package com.huilong.zhang.mobilesafe117.View;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.huilong.zhang.mobilesafe117.R;

public class Setup1Activity extends BaseSetupActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);

    }

    @Override
    public void nexttouch() {
        startActivity(new Intent(Setup1Activity.this, Setup2Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);

    }


    @Override
    public void priverstouch() {


    }


}
