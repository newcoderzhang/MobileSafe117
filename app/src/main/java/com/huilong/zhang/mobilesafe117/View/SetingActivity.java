package com.huilong.zhang.mobilesafe117.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.huilong.zhang.mobilesafe117.R;
import com.huilong.zhang.mobilesafe117.Service.AddressService;
import com.huilong.zhang.mobilesafe117.Service.CallSafeService;
import com.huilong.zhang.mobilesafe117.utils.ServicesStatusUtils;

public class SetingActivity extends Activity {

    private static final String TAG = "SetingActivity";
    private SettingItmeView sivupdate;
    private SettingItmeView setingaddress;
    private SettingClickView settingClickView;  //修改风格设置view
    private SettingClickView settingdrag;  //拖动位置
    private SettingItmeView  setSafeDao;   //黑名单设置
    final String[] items = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
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
        initSettingClick();
        initDragg();
        initSafeDao();
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
        boolean serviceRunning = ServicesStatusUtils.isServiceRunning(this, "com.huilong.zhang.mobilesafe117.Service.AddressService");
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
    private void initSettingClick() {
        settingClickView = (SettingClickView) findViewById(R.id.scv_address_style);
        int style = sharedPreferences.getInt("address_style", 0);// 读取保存的style
        settingClickView.setDesc(items[style]);

        settingClickView.setTitle("归属地提示风格");
        settingClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChooseDIalog();
            }
        });
    }

    private void showSingleChooseDIalog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        int style = sharedPreferences.getInt("address_style", 0);
        builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferences.edit().putInt("address_style", which).commit();// 保存选择的风格
                dialog.dismiss();
                settingClickView.setDesc(items[which]);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void initDragg() {
        settingdrag = (SettingClickView) findViewById(R.id.sc_tuodong);
        settingdrag.setTitle("拖动移动位置");
        settingdrag.setDesc("点击移动位置");
        settingdrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetingActivity.this, DragViewActivity.class));
                finish();
            }
        });

    }
    private void initSafeDao() {
        setSafeDao = (SettingItmeView) findViewById(R.id.safe_viww);
        boolean serviceRunning = ServicesStatusUtils.isServiceRunning(this, "com.huilong.zhang.mobilesafe117.Service.CallSafeService");
        if(serviceRunning) {
            setSafeDao.setcheckedone(true);
        }else {
            setSafeDao.setcheckedone(false);
        }
        setSafeDao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setSafeDao.ischecked()) {
                    Log.v(TAG, "setingaddress ischecked is fasle");
                    setSafeDao.setcheckedone(false);
                    stopService(new Intent(SetingActivity.this,AddressService.class));
                    //sharedPreferences.edit().putBoolean("auto_update",false).commit();
                }else{
                    Log.v(TAG, "ischecked is " + setingaddress.ischecked());
                    setSafeDao.setcheckedone(true);
                    startService(new Intent(SetingActivity.this, CallSafeService.class));
                    //sharedPreferences.edit().putBoolean("auto_update",true).commit();
                }

            }
        });
    }

}
