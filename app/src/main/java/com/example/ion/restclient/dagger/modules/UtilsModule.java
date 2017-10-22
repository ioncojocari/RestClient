package com.example.ion.restclient.dagger.modules;

import android.content.Context;
import com.example.ion.restclient.utils.MyUtils;
import com.google.android.gms.gcm.GcmNetworkManager;
import dagger.Module;
import dagger.Provides;

@Module
public class UtilsModule {
    @Provides
    public MyUtils getUtils(GcmNetworkManager manager,Context context){
        return new MyUtils(manager,context);
    }
}
