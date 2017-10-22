package com.example.ion.restclient.business.ws;

import com.example.ion.restclient.models.Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IArticleService {
    String fullPath="api";
    final String ARTICLES_TABLE_NAME="articles";

    @GET(fullPath+"/" +ARTICLES_TABLE_NAME)
    public Call<Response> getAll(@Query("limit") Integer limit);
    
    @GET(fullPath+"/"+ARTICLES_TABLE_NAME)
    public Call<Response> getAfterId(@Query("limit") Integer limit,@Query("afterId") Long after);

    @GET(fullPath+"/"+ARTICLES_TABLE_NAME)
    public Call<Response> getBeforeId(@Query("limit") Integer limit,@Query("beforeId") Long before);

    @GET(fullPath+"/"+ARTICLES_TABLE_NAME)
    public Call<Response> getBeforeId(@Query("beforeId") Long before);

    @GET(fullPath+"/"+ARTICLES_TABLE_NAME)
    public Call<Response> getLast(@Query("lastId") Long afterId);

    @GET(fullPath+"/"+ARTICLES_TABLE_NAME)
    public Call<Response> getUpdate(@Query("lastChange")Long lastTimeChanged,@Query("min") Long min,@Query("max") Long max);

}
