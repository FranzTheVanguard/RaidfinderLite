package com.fourrunstudios.raidfinderlite.retrofit;

import com.fourrunstudios.raidfinderlite.TweetData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TwitterAPI {
    @GET("2/tweets/search/recent?tweet.fields=created_at")
    Call<TweetData> findRaid(@Query("query") String raidName);

    @GET("2/tweets/search/recent?tweet.fields=created_at")
    Call<TweetData> findRaidAfter(@Query("query") String raidName, @Query(("since_id")) Long sinceId);
}
