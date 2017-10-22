package com.example.ion.restclient.business;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

public class InternetService extends IntentService {
    public final static String SERVICE_NAME="InternetService";
    public final static String COMAND_NAME="cmdName";
    private CommandFactory factory;

    public InternetService(String name) {
        super(SERVICE_NAME);
    }

    public InternetService(){
        super(SERVICE_NAME);
    }

    private void init(){
        if(factory==null)
        factory=new CommandFactory(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG","InternetService onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        factory.onDestroy();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("TAG","InternetService starts");
        if(intent!=null) {
            Bundle bundle = intent.getExtras();
            if (bundle.containsKey(COMAND_NAME)) {
                if (bundle.get(COMAND_NAME) instanceof Commands) {
                    init();
                    Commands cmd = (Commands) bundle.get(COMAND_NAME);
                    factory.executeCommand(cmd);
                }
            }
        }
    }
}
