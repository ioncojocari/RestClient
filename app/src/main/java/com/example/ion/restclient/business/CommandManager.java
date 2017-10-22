package com.example.ion.restclient.business;

import android.content.Context;
import android.content.Intent;

public class CommandManager {
    private final Intent intent;
    private final Context context;
    public CommandManager(Context context){
        this.intent=new Intent(context,InternetService.class);
        this.context=context;
    }

    public void loadLast(){
        intent.putExtra(InternetService.COMAND_NAME, Commands.LOAD_LAST);
        startService();
    }

    public void loadPrev(){
        intent.putExtra(InternetService.COMAND_NAME, Commands.LOAD_PREV);
        startService();
    }

    private void startService(){
        context.startService(intent);
    }
}
