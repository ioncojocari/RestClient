package com.example.ion.restclient.dagger.modules;

import android.content.Context;
import com.example.ion.restclient.utils.CustomImageCache;
import com.example.ion.restclient.dagger.scopes.ArticleServiceScope;
import com.example.ion.restclient.business.RestConfiguration;
import com.squareup.picasso.Cache;
import com.squareup.picasso.Picasso;
import java.io.File;
import dagger.Module;
import dagger.Provides;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @ArticleServiceScope
    public GsonConverterFactory getGsonFactory(){
        return GsonConverterFactory.create();
    }

    @Provides
    @ArticleServiceScope
    public RestConfiguration getConfiguration(){
        return RestConfiguration.getConfiguration();
    }

    @Provides
    @ArticleServiceScope
    public Cache getCache(File file){
        return new CustomImageCache(file);
    }

    @Provides
    @ArticleServiceScope
    public Picasso getPicasso(Cache cache,Context context){
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.memoryCache(cache);
        return builder.build();
    }

    @Provides
    @ArticleServiceScope
    public File getFile(Context context){
        return context.getCacheDir();
    }

}
