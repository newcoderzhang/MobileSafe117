package com.huilong.zhang.mobilesafe117.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.huilong.zhang.mobilesafe117.dao.BlackNumberDao;

import java.lang.reflect.Method;

public class CallSafeService extends Service {
    private BlackNumberDao dao;
    TelephonyManager telephonyManager;
    public CallSafeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dao = new BlackNumberDao(this);
        InnerReceiver innerReceiver = new InnerReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(innerReceiver, intentFilter);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        MyPhoneListener listener = new MyPhoneListener();

        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class MyPhoneListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            //            * @see TelephonyManager#CALL_STATE_IDLE  电话闲置
//            * @see TelephonyManager#CALL_STATE_RINGING 电话铃响的状态
//            * @see TelephonyManager#CALL_STATE_OFFHOOK 电话接通
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    String mode = dao.findNumberMode(incomingNumber);
                    /**
                     * 黑名单拦截模式
                     * 1 全部拦截 电话拦截 + 短信拦截
                     * 2 电话拦截
                     * 3 短信拦截
                     */
                    if(mode.equals("1")|| mode.equals("2")){
                        System.out.println("挂断黑名单电话号码");

                        Uri uri = Uri.parse("content://call_log/calls");
                        getContentResolver().registerContentObserver(uri,true,new MyContentObserver(new Handler(),incomingNumber));
                        //挂断电话
                        endCall();
                    }
                    break;

            }

        }
    }
    private class MyContentObserver extends ContentObserver {
        String incomingNumber;

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContentObserver(Handler handler,String incomingNumber) {
            super(handler);
            this.incomingNumber = incomingNumber;
        }

        @Override
        public void onChange(boolean selfChange) {
            getContentResolver().unregisterContentObserver(this);
            deleteCallLog(incomingNumber);
            super.onChange(selfChange);
        }
    }

    //删掉电话号码
    private void deleteCallLog(String incomingNumber) {

        Uri uri = Uri.parse("content://call_log/calls");

        getContentResolver().delete(uri,"number=?",new String[]{incomingNumber});

    }
    private void endCall(){
        try {
            //通过类加载器加载ServiceManager
            Class<?> clazz = getClassLoader().loadClass("android.os.ServiceManager");
            //通过反射得到当前的方法
            Method method = clazz.getDeclaredMethod("getService", String.class);

            IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);

            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);

            iTelephony.endCall();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("短信来了");

            //还存在bug拦截不住短信
            Bundle bundle = intent.getExtras();

            Object[] objects = (Object[]) intent.getExtras().get("pdus");

            for (Object object : objects) {// 短信最多140字节,
                // 超出的话,会分为多条短信发送,所以是一个数组,因为我们的短信指令很短,所以for循环只执行一次
                SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
                String originatingAddress = message.getOriginatingAddress();// 短信来源号码
                String messageBody = message.getMessageBody();// 短信内容
                //通过短信的电话号码查询拦截的模式
                String mode = dao.findNumberMode(originatingAddress);
                /**
                 * 黑名单拦截模式
                 * 1 全部拦截 电话拦截 + 短信拦截
                 * 2 电话拦截
                 *
                 * 3 短信拦截
                 */
                if (mode.equals("1")) {
                    abortBroadcast();
                    System.out.println("短信拦截");
                } else if (mode.equals("3")) {
                    abortBroadcast();
                    System.out.println("拦截");
                }
                //智能拦截模式 发票  你的头发漂亮 分词
                if (messageBody.contains("fapiao")) {
                    abortBroadcast();
                }

            }
        }
}}
