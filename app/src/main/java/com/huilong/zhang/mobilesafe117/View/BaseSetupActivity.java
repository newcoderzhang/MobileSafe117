package com.huilong.zhang.mobilesafe117.View;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.huilong.zhang.mobilesafe117.R;

public abstract class BaseSetupActivity extends Activity {
    private GestureDetector gestureDetector;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_base_setup);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);

        gestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                /**
                 * e1表示滑动起点  e2表示滑动终点
                 */
                if(e2.getRawX()-e1.getRawX() > 200) {
                    nexttouch();
                    return true;
                }
                if(e1.getRawX()-e2.getRawX() > 200) {
                    nexttouch();
                    return true;
                }
                if(Math.abs(e2.getRawY() - e1.getRawY()) >100 ) {
                    Toast.makeText(BaseSetupActivity.this,"请再次滑动屏幕",Toast.LENGTH_SHORT).show();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base_setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);  //委托手势处理器处理
        return super.onTouchEvent(event);
    }
    public abstract void nexttouch();
    public abstract void priverstouch();


}
