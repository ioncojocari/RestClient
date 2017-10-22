package com.example.ion.restclient.dagger.modules;

import android.content.Context;
import com.google.android.gms.gcm.GcmNetworkManager;
import dagger.Module;
import dagger.Provides;

@Module
public class ScheduleModule {
    @Provides
    public GcmNetworkManager getNetwork(Context context){
        return  GcmNetworkManager.getInstance(context);
    }
}
