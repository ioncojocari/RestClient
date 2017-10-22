package com.example.ion.restclient.business;

import android.content.res.Resources;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class ActivityOptionsManager {
    private AppCompatActivity appCompatActivity;
    private Resources resources;
    public ActivityOptionsManager(AppCompatActivity activity){
        appCompatActivity=activity;
        resources=appCompatActivity.getResources();
    }

    public  ActivityOptionsCompat getOptions(View view, int stringId){
        String name=resources
                .getString(stringId);
        Log.d(":TAG","name:"+name);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(appCompatActivity, view,name);
        return options;
    }

    public ActivityOptionsCompat getOptions(Pair<View,Integer>... pairs){
        Pair<View,String>[] result=new Pair[pairs.length];
        int i=0;
        for(Pair<View,Integer> pair:pairs){
            String name=resources.getString(pair.second);
            result[i]=Pair.create(pair.first,name);
            i++;
        }
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(appCompatActivity, result);
        return options;
    }
}
