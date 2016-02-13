package com.huilong.zhang.mobilesafe117.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.huilong.zhang.mobilesafe117.R;
import com.huilong.zhang.mobilesafe117.Service.LocationService;


public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG ="SmsReceiver" ;

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        Object[] objectses = (Object[]) intent.getExtras().get("pdus");
//        for (Object object:objectses
//             ) {  //短信最多140个字节
//            SmsMessage fromPdu = SmsMessage.createFromPdu((byte[])object);
//            String originationAddress = fromPdu.getOriginatingAddress();
//            String messageBody = fromPdu.getMessageBody();
            //Log.v(TAG,"originationAddress is " + originationAddress + " messageBody is " + messageBody);
            //if ("#*alarm*#".equals(messageBody)) {
                MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                player.setVolume(1f, 1f);
                player.setLooping(true);
                player.start();
            //}
        //}
        context.startService(new Intent(context,LocationService.class));
    }
}
