package com.huilong.zhang.mobilesafe117.View;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.huilong.zhang.mobilesafe117.R;

public class Setup4Activity extends BaseSetupActivity {
    SharedPreferences sharedPreferences;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        checkBox = (CheckBox) findViewById(R.id.checkbo_setup4);
        Boolean protect = sharedPreferences.getBoolean("protect",false);
        if(protect) {
            checkBox.setText("防盗保护已经开启");
            checkBox.setChecked(true);
        } else {
            checkBox.setText("防盗保护已经关闭");
            checkBox.setChecked(false);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checkBox.setText("防盗保护已经开启");
                    sharedPreferences.edit().putBoolean("protect",true).commit();
                }else {
                    checkBox.setText("防盗保护已经关闭");
                    sharedPreferences.edit().putBoolean("protect",false).commit();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setup4, menu);
        return true;
    }

    public  void next(View viw) {
        startActivity(new Intent(Setup4Activity.this, LostFindActivity.class));
        sharedPreferences.edit().putBoolean("configed",true).commit();
        finish();
    }
    public void previous(View viw) {
        startActivity(new Intent(Setup4Activity.this,Setup3Activity.class));
        finish();
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
    public void nexttouch() {
        startActivity(new Intent(Setup4Activity.this, LostFindActivity.class));
        sharedPreferences.edit().putBoolean("configed",true).commit();
        finish();

    }

    @Override
    public void priverstouch() {
        startActivity(new Intent(Setup4Activity.this,Setup3Activity.class));
        finish();

    }
}
