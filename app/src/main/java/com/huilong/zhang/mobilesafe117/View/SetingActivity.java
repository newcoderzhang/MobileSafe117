package com.huilong.zhang.mobilesafe117.View;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.huilong.zhang.mobilesafe117.R;
import com.huilong.zhang.mobilesafe117.Service.AddressService;
import com.huilong.zhang.mobilesafe117.utils.ServicesStatusUtils;

public class SetingActivity extends Activity {

    private static final String TAG = "SetingActivity";
    private SettingItmeView sivupdate;
    private SettingItmeView setingaddress;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        sivupdate = (SettingItmeView) findViewById(R.id.myview);
        boolean flag = sharedPreferences.getBoolean("auto_update",true);
        if(flag) {
            //sivupdate.setDesc("自动更新开启");
            sivupdate.setcheckedone(true);
        }else {
            //sivupdate.setDesc("自动更新关闭");
            sivupdate.setcheckedone(false);

        }
        sivupdate.setOnClickListener(new SettingItmeView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sivupdate.ischecked()) {
                    Log.v(TAG,"ischecked is false" );
                    sivupdate.setcheckedone(false);
                    sharedPreferences.edit().putBoolean("auto_update",false).commit();
                }else{
                    Log.v(TAG,"ischecked is true");
                    sivupdate.setcheckedone(true);
                    sharedPreferences.edit().putBoolean("auto_update",true).commit();
                }
            }
        });
        initAddressView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_seting, menu);
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


    /**
     * 初始化归属地开关
     */
    private void initAddressView() {
        setingaddress = (SettingItmeView) findViewById(R.id.address);
        boolean serviceRunning = ServicesStatusUtils.isServiceRunning(this,"com.huilong.zhang.mobilesafe117.Service.AddressService");
        if(serviceRunning) {
            setingaddress.setcheckedone(true);
        }else {
            setingaddress.setcheckedone(false);
        }
        setingaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setingaddress.ischecked()) {
                    Log.v(TAG, "setingaddress ischecked is fasle");
                    setingaddress.setcheckedone(false);
                    stopService(new Intent(SetingActivity.this,AddressService.class));
                    //sharedPreferences.edit().putBoolean("auto_update",false).commit();
                }else{
                    Log.v(TAG, "ischecked is " + setingaddress.ischecked());
                    setingaddress.setcheckedone(true);
                    startService(new Intent(SetingActivity.this, AddressService.class));
                    //sharedPreferences.edit().putBoolean("auto_update",true).commit();
                }

            }
        });
    }
}
