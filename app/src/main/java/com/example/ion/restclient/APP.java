package com.example.ion.restclient;

import android.app.Application;
import android.os.Build;

import com.example.ion.restclient.dagger.AppComponent;
import com.example.ion.restclient.dagger.DaggerAppComponent;
import com.example.ion.restclient.dagger.DaggerUtilsComponent;
import com.example.ion.restclient.dagger.UtilsComponent;
import com.example.ion.restclient.dagger.modules.ContextModule;
import com.example.ion.restclient.utils.MyUtils;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;
import io.realm.Realm;

public class APP extends Application {

    private static AppComponent appComponent;
    private static UtilsComponent utilsComponent;
    @Inject
    protected Picasso picasso;
    @Inject
    protected MyUtils utils;
    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
        buildAppComponent();
        buildUtilsComponent();
        appComponent.inject(this);
        picassoEnableCache();
        //networkManager before 5.0(JobScheduler) kills all tasks;
        if(Build.VERSION_CODES.LOLLIPOP>Build.VERSION.SDK_INT){
            utils.scheduleUpdate();
        }

    }

    private void buildUtilsComponent() {
        utilsComponent= DaggerUtilsComponent.builder()
                                            .contextModule(new ContextModule(this))
                                            .build();
    }

    private void buildAppComponent() {
        appComponent= DaggerAppComponent.builder()
                                        .contextModule(new ContextModule(this))
                                        .build();
    }

    public static AppComponent getComponent(){
        return appComponent;
    }

    private void picassoEnableCache(){
        Picasso.setSingletonInstance(picasso);
    }

    private void initRealm(){
        Realm.init(this);
    }

    public static UtilsComponent getUtilsComponent(){
        return utilsComponent;
    }

}
