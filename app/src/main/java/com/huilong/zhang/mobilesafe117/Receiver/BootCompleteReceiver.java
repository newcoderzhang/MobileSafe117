package com.huilong.zhang.mobilesafe117.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {
    private static final String TAG ="BootCompleteReceiver" ;

    public BootCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        SharedPreferences sharedPreferences = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        String sim = sharedPreferences.getString("sim",null);
        if(!TextUtils.isEmpty(sim)) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String currentSim = telephonyManager.getSimOperatorName();
            if(currentSim.equals(sim)) {
                Log.v(TAG,"SIM序列号相等 " + currentSim);
                Toast.makeText(context, "boot completed action has got", Toast.LENGTH_LONG).show();
            }else {
                Log.v(TAG,"序列号不一致");
            }
        }
    }
}
