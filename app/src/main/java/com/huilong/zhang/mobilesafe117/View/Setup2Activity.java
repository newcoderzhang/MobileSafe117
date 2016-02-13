package com.huilong.zhang.mobilesafe117.View;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.huilong.zhang.mobilesafe117.R;

public class Setup2Activity extends BaseSetupActivity {
    private  SettingItmeView settingItmeView2;
    private static final String TAG = "Setup2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        settingItmeView2 = (SettingItmeView) findViewById(R.id.sim);

        String sim = sharedPreferences.getString("sim",null);
        Log.v(TAG,"sim" + sim);
        if(!TextUtils.isEmpty(sim)) {
            settingItmeView2.setcheckedone(true);
        } else {
            settingItmeView2.setcheckedone(false);
        }
        settingItmeView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(settingItmeView2.ischecked()) {
                    settingItmeView2.setcheckedone(false);
                    sharedPreferences.edit().remove("sim").commit();

                }else {
                    settingItmeView2.setcheckedone(true);
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String simSeriaNub = telephonyManager.getSimSerialNumber();
                    Log.v(TAG, "simSeriaNub is " + simSeriaNub);
                    sharedPreferences.edit().putString("sim",simSeriaNub).commit();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setup2, menu);
        return true;
    }
    public  void next(View viw) {

    }
    public void previous(View viw) {
        startActivity(new Intent(Setup2Activity.this,Setup1Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
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
        String simnum = sharedPreferences.getString("sim",null);
        if(TextUtils.isEmpty(simnum)) {
            Toast.makeText(this,"SIM卡必须绑定",Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(Setup2Activity.this,Setup3Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);

    }

    @Override
    public void priverstouch() {
        startActivity(new Intent(Setup2Activity.this,Setup1Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);

    }
}
