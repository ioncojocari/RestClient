package com.example.ion.restclient.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.example.ion.restclient.APP;
import com.example.ion.restclient.R;
import com.example.ion.restclient.business.UpdateArticlesService;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import javax.inject.Inject;

/**
 * it runs when user isn't using this app
 */

public class GCMService extends GcmTaskService {
    private static final String TAG = "GCMService";
    public static final String TAG_TASK_ONEOFF_LOG = "TAG_TASK_ONEOFF_LOG";
    public static final String TAG_TASK_PERIODIC_LOG = "TAG_TASK_PERIODIC_LOG";
    @Inject
    protected MyUtils utils;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCraete ");
        APP.getUtilsComponent().inject(this);
        checkScheduled();
    }

    private void checkScheduled(){
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        //This is for version 5+ to not populate sql with task that already is set;
        if(!sharedPref.contains("updated")){
            utils.scheduleUpdate();
            sharedPref.edit().putBoolean("updated",true).apply();
        }
    }
    //task will run only when app is killed or in background;
    @Override
    public int onRunTask(TaskParams taskParams) {
        boolean isRunning = utils.isAppOnForeground();
        if (!isRunning) {
            switch (taskParams.getTag()) {
                case TAG_TASK_ONEOFF_LOG:
                    //bundle with other tasks and then execute
                    return GcmNetworkManager.RESULT_SUCCESS;
                case TAG_TASK_PERIODIC_LOG:
                    Intent serviceIntent = new Intent(this, UpdateArticlesService.class);
                    this.startService(serviceIntent);
                    return GcmNetworkManager.RESULT_SUCCESS;
                default:
                    return GcmNetworkManager.RESULT_FAILURE;
            }
        }
        //RESULT_RESCHEDULE means it will restart when app is killed
        return GcmNetworkManager.RESULT_SUCCESS;
    };

    //Reschedule task when user is updating app or google Play
    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
        utils.scheduleUpdate();
    }
}
