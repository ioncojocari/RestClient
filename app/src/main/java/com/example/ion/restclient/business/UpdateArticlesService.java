package com.example.ion.restclient.business;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import com.example.ion.restclient.APP;
import com.example.ion.restclient.business.ws.IArticleService;
import javax.inject.Inject;
import io.realm.Realm;

/**
 * This service update articles,and download last ones
 */

public class UpdateArticlesService extends IntentService {
    @Inject
    protected IArticleService service;
    private static final String name=UpdateArticlesService.class.getCanonicalName();

    public UpdateArticlesService(){
        super(name);
    }

    public UpdateArticlesService(String name) {
        super(UpdateArticlesService.name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        APP.getComponent().inject(this);
    }

    private Realm initRealm(){
        Realm realm=Realm.getDefaultInstance();
        if(realm==null){
            Realm.init(this);
            realm=Realm.getDefaultInstance();
        };
        return realm;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Realm realm=initRealm();
        realm.beginTransaction();
        ArticleManager manager=ArticleManager.getInstance(realm);
        Long lastTimeChanged=manager.getLastChange();
        Long[]ids={manager.getMinId(),manager.getMaxId()};
        realm.commitTransaction();
        realm.close();
        service.getUpdate(lastTimeChanged,ids[0],ids[1]).enqueue(new SaveArticlesCallback(getBaseContext()));
        Log.d("UpdateArticleService","Called");
    }
}
