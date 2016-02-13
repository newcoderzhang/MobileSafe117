package com.huilong.zhang.mobilesafe117.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class AddressService extends Service {
    private static final String TAG = "AddressService";

    public AddressService() {
    }
    private Mylisten mylisten;
    TelephonyManager tm;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return  null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mylisten = new Mylisten();
        tm.listen(mylisten,PhoneStateListener.LISTEN_CALL_STATE);
    }
    class Mylisten extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.v(TAG, "来电 " + incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.v(TAG,"zhaiji");
                    break;
                default:
                    Log.v(TAG,"no");
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    @Override
    public void onDestroy() {
        tm.listen(mylisten,PhoneStateListener.LISTEN_NONE);
        super.onDestroy();
    }
}
