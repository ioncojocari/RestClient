package com.example.ion.restclient.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.example.ion.restclient.business.RestConfiguration;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

import java.util.List;

public class MyUtils {

    private GcmNetworkManager manager;
    private Context context;

    public MyUtils(GcmNetworkManager manager,Context context){
        this.manager=manager;
        this.context=context;
    }

    public void scheduleUpdate(){
        Task task = new PeriodicTask.Builder()
                .setService(GCMService.class)
                //trigger this task once 18-36 hours
                .setPeriod(36*60)
                .setFlex(18*60)
                .setTag(GCMService.TAG_TASK_PERIODIC_LOG)
                ///non celular data
                .setRequiredNetwork(Task.NETWORK_STATE_UNMETERED)
                .setRequiresCharging(true)
                .build();
        manager.schedule(task);
    }

    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                    appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public void cancelTasks(){
        manager.cancelAllTasks(GCMService.class);
    }

    public String getUrl(String end){
        String url= RestConfiguration.getConfiguration().getFullPath()+RestConfiguration.imagesSuffix+end;
        return url;
    }

}
