package com.huilong.zhang.mobilesafe117.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.huilong.zhang.mobilesafe117.R;
import com.huilong.zhang.mobilesafe117.dao.AddressDao;

public class AddressService extends Service {
    private static final String TAG = "AddressService";
    private WindowManager mWM;
    private View view;
    private OutCallReceiver receiverone;

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
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(receiverone,intentFilter);
    }
    class Mylisten extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    String address = AddressDao.getAddress(incomingNumber);
                    Log.v(TAG,"incomingNumber is " + incomingNumber + " address is " +address);
                    showToast(address);

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.v(TAG,"zhaiji");
                    break;
                case TelephonyManager.CALL_STATE_IDLE:   //电话闲置状态
                     if(mWM!=null && view !=null) {
                         mWM.removeView(view);
                     }
                     view = null;
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
        unregisterReceiver(receiverone);
        super.onDestroy();
    }

    /**
     * 自定义归属地浮窗
     */
    private void showToast(String text) {
        mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");

//        view = new TextView(this);
//        view.setText(text);
//        view.setTextColor(Color.RED);
//        mWM.addView(view,params);
        view = View.inflate(this, R.layout.toast_address, null);

//        int[] bgs = new int[] { R.drawable.call_locate_white,
//                R.drawable.call_locate_orange, R.drawable.call_locate_blue,
//                R.drawable.call_locate_gray, R.drawable.call_locate_green };
//        int style = mPref.getInt("address_style", 0);
//
//        view.setBackgroundResource(bgs[style]);// 根据存储的样式更新背景
//
        TextView tvText = (TextView) view.findViewById(R.id.tv_number);
        tvText.setText(text);
        mWM.addView(view, params);// 将view添加在屏幕上(Window)
    }

    /**
     * ？存在问题，此广播手机无法接受
     */
    class OutCallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String number = getResultData();
            String address = AddressDao.getAddress(number);
            Log.v(TAG,"number is " + number + " address is " +address);
            showToast(address);
        }
    }
}
