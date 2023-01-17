package com.fourrunstudios.raidfinderlite.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIManager {
    private static final String TOKEN = "AAAAAAAAAAAAAAAAAAAAANG3dQEAAAAA6I39d3FtYWp%2BTyhqWYma650eFYE%3DrxVugPCaPbD0zz43NZww86fShxIuvZst5OEuo6vqCUGAcls0bp";
    private static final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
        Request newRequest  = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + TOKEN)
                .build();
        return chain.proceed(newRequest);
    }).build();
    private static final Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.twitter.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()).build();
    private static final TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);

    public static TwitterAPI getTwitterAPI() {
        return twitterAPI;
    }
}
