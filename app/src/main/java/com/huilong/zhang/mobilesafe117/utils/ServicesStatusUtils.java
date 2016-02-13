package com.huilong.zhang.mobilesafe117.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Mario on 2/13/16.
 */
public class ServicesStatusUtils {
    public static boolean isServiceRunning(Context context,String servicename) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo runningServiceInfo:runningServices) {
            String className = runningServiceInfo.service.getClassName();
            if(className.equals(servicename)) {
                return true;                    //遍历到一个就说明存在
            }
        }
        return false;
    }
}
