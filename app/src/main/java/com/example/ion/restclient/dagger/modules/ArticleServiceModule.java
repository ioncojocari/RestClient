package com.example.ion.restclient.dagger.modules;

import com.example.ion.restclient.dagger.scopes.ArticleServiceScope;
import com.example.ion.restclient.business.ws.IArticleService;
import com.example.ion.restclient.business.RestConfiguration;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@ArticleServiceScope
@Module
public class ArticleServiceModule {
    @Provides
    public IArticleService getArticleService(Retrofit retrofit){
        return  retrofit.create(IArticleService.class);
    }

    @Provides
    public Retrofit getRetrofit(RestConfiguration configuration, GsonConverterFactory factory){
        return new Retrofit.Builder()
                .baseUrl(configuration.getHostName())
                .addConverterFactory(factory)
                .build();
    }
}
