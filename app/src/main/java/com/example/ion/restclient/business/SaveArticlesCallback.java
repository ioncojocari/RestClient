package com.example.ion.restclient.business;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.example.ion.restclient.APP;
import com.example.ion.restclient.utils.CustomImageCache;
import com.example.ion.restclient.utils.MyUtils;
import com.example.ion.restclient.activities.MainActivity;
import com.example.ion.restclient.models.Article;
import com.example.ion.restclient.IDisplayResponse;
import com.example.ion.restclient.models.Response;
import com.squareup.picasso.Cache;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.File;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * This class saves,updates,deletes articles, ->syncronize data
 */

public class SaveArticlesCallback implements Callback<Response> {
    private static final String TAG="SaveArticlesCallback";
    private Context context;
    @Inject
    protected MyUtils utils;
    public SaveArticlesCallback(Context context){
        this.context=context;
        APP.getUtilsComponent().inject(this);
    }

    @Override
    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
        Response rs = response.body();
        if (validResponse(response, rs)) {
            List<Article> articles = rs.getResult();
            Long[] needToDelete = rs.getNeedToDeletePrim();
            Integer limit=rs.getLimit();
            Realm realm = Realm.getDefaultInstance();
            try {
                if (needToDelete != null && needToDelete.length > 0) {
                    deleteArticlesWithIds(realm, needToDelete);
                }
                if (articles != null && !articles.isEmpty()) {
                    updateArticles(limit,realm,articles);
                }
            }catch(Exception e){
                    Log.d(TAG, "onResponse Exception: "+e.getMessage()+",class:"+e.getClass());
                    if(realm.isInTransaction())
                    realm.cancelTransaction();
            } finally{
                    realm.close();
            }
            if (displayResponse != null) {
                displayResponse.success(null);
            }
            return;
            }
            if (displayResponse != null)
                displayResponse.error(null);
    }

    private void updateArticles(Integer limit,Realm realm,List<Article> articles){
        final Cache cache =new CustomImageCache(context.getCacheDir());
        if(limit!=null&& limit!=0){
            //if limit !=0 && limit!=rs.getResult().size() then delete all data;
            if(limit!=articles.size()){
                deleteAllArticles(realm,cache);
            }
        }
        saveImages(articles,realm,cache);
        saveArticlesInRealm(realm,articles);
    }

    private void saveArticlesInRealm(Realm realm,List<Article> articles){
        realm.beginTransaction();
        realm.insertOrUpdate(articles);
        realm.commitTransaction();
    }

    private void deleteAllArticles(Realm realm,Cache cache){
        //deleting all images from cache
        realm.beginTransaction();
        List<Article> articles=realm.where(Article.class).findAll();
        realm.commitTransaction();
        if(articles.size()>0) {
            deleteOldImages(realm, cache, articles);
            //delete all articles from realm;
            realm.beginTransaction();
            realm.delete(Article.class);
            realm.commitTransaction();
        }
    }

    private void saveImages(List<Article> articles,Realm realm,Cache cache) {
        deleteOldImages(realm,cache,articles);
        Log.d(TAG, "saveImages: begin");
        showDir(context.getCacheDir());
        Log.d(TAG, "saveImages: end");
        downloadNewImages(articles,cache);
    }

    private void downloadNewImages(List<Article>articles,Cache cache){
        for (Article article : articles) {
            Log.d(TAG,"need to download : "+article.getUrl());
            final String url= utils.getUrl(article.getUrl());
            Picasso.with(context).load(url).into(new MyTarget(url,cache));
        }
    }

    private void deleteOldImages(Realm realm,Cache cache,List<Article>articles){
        //extract article ids;
        Long[] ids=new Long[articles.size()];
        int i=0;
        for(Article a:articles){
            ids[i++]=a.getId();
        }
        //get old url
        realm.beginTransaction();
        List<Article> urls=realm.where(Article.class).in(Article.ID_FIELD,ids).findAll();
        //connection was closed becouse file operation may run too much time;
        realm.commitTransaction();
        if(!urls.isEmpty()) {
            for (Article a : urls) {
                String oldFullUrl = utils.getUrl(a.getUrl());
                //deletes from cache
                cache.clearKeyUri(oldFullUrl);
            }
        }

    }

    private void deleteArticlesWithIds(Realm realm,Long[] needToDelete) {
        realm.beginTransaction();
        RealmResults<Article >rs=realm.where(Article.class).in(Article.ID_FIELD,needToDelete).findAll();
        rs.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public void showDir(File fs){
        if(fs.isDirectory()){
            for(File f:fs.listFiles()) {
                Log.d(TAG, "file name :" + f.getAbsolutePath() + ",isDir:" + f.isDirectory());
                if(f.isDirectory()){
                    showDir(f);
                }else{
                    Log.d(TAG,"filesize : "+f.length()/1024+" KB");
                }
            }
        }
    };

    @Override
    public void onFailure(Call<Response> call, Throwable t) {
        Log.d(TAG," Load callback onfailure");
        if(displayResponse!=null)
            displayResponse.error(t.getMessage());
        Log.d(TAG,"message : "+t.getMessage());
    }

    private boolean validResponse(retrofit2.Response<Response> response,Response rs){
        if(response.isSuccessful()) {
            if (rs != null && rs.getSuccessful()) {
                List<Article> articles = rs.getResult();
                Map<String,Object> additional=rs.getAdditionalProperties();
                List<Long > needToDelete=rs.getNeedToDelete();
                if (!articles.isEmpty()
                        ||(!additional.isEmpty())
                        || (needToDelete!=null&&!needToDelete.isEmpty())) {
                    return true;
                }
            }
        }
        return false;
    }

    private IDisplayResponse displayResponse=new IDisplayResponse() {

        @Override
        public void error(@Nullable String message) {
            pushNotificationForBroadcast(message,false);
        }

        @Override
        public void success(@Nullable String message) {
            pushNotificationForBroadcast(message,true);
        }

        public void pushNotificationForBroadcast(String message,boolean succesful){
            Intent intent = new Intent(MainActivity.INTENT_FILTER_NAME);
            // You can also include some extra data.
            intent.putExtra(Response.MESSAGE_FIELD,message);
            intent.putExtra(Response.SUCCESSFUL_FIELD,succesful);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    };

    private class MyTarget implements Target {
        private final Cache cache;
        private final String newUrl;
        MyTarget( String newUrl,Cache cache){
            this.cache=cache;
            this.newUrl=newUrl;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            cache.set(newUrl,bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }
}
