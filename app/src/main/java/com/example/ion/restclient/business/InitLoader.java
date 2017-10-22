package com.example.ion.restclient.business;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.Loader;

public class InitLoader  extends Loader<Boolean> {
    boolean updated=false;

    public InitLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(!updated){
            forceLoad();
        }else{
            deliverResult(updated);
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        Intent intent=new Intent(getContext(),UpdateArticlesService.class);
        getContext().startService(intent);
        updated=true;
    }
}
