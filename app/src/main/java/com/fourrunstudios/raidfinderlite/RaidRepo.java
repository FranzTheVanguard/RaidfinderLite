package com.fourrunstudios.raidfinderlite;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.fourrunstudios.raidfinderlite.retrofit.TwitterAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class RaidRepo {
    private MutableLiveData<List<Tweet>> tweets;
    private Long newestId;
    private Long oldestId;
    private static RaidRepo instance;

    public MutableLiveData<List<Tweet>> getLiveRaid(String raidNameJP, String raidNameEN, TwitterAPI twitterAPI){
        if(tweets==null){
            tweets = new MutableLiveData<>();
        }
        twitterAPI.findRaid(raidNameJP + " 参戦ID").enqueue(new Callback<TweetData>() {
            @Override
            public void onResponse(Call<TweetData> call, Response<TweetData> response) {
                Log.d("", "RaidFinderApp - Request = "+call.request());
                if(response.isSuccessful()&&response.body().getData()!=null){
                    List<Tweet> temp = response.body().getData();
                    sortTrimRaids(temp);
                    tweets.setValue(temp);
                    Log.d("", "RaidFinderApp - Tweet Count: "+tweets.getValue().size());
                    newestId = tweets.getValue().get(0).getId();
                    oldestId = tweets.getValue().get(tweets.getValue().size()-1).getId();
                    Log.d("", "newest "+newestId);
                    Log.d("", "oldest "+oldestId);
                    loadENLiveRaid(raidNameEN, twitterAPI);
                }

                else {

                    Log.d("", "onResponse: No "+raidNameJP+ " Loaded");
                }
            }

            @Override
            public void onFailure(Call<TweetData> call, Throwable t) {
                Log.d("", "onFailure: ");
            }
        });
        return tweets;
    }
    public void loadENLiveRaid(String raidNameEN, TwitterAPI twitterAPI){
        twitterAPI.findRaidAfter(raidNameEN + " Battle ID", oldestId).enqueue(new Callback<TweetData>() {
            @Override
            public void onResponse(Call<TweetData> call, Response<TweetData> response) {
                Log.d("", "RaidFinderApp - Request = "+call.request());
                if(response.isSuccessful()&&response.body().getData()!=null){
                    List<Tweet> temp = response.body().getData();
                    tweets.getValue().addAll(temp);
                    sortTrimRaids(tweets.getValue());
                    tweets.setValue(tweets.getValue());
                    Log.d("", "RaidFinderApp - Tweet Count: "+tweets.getValue().size());
                    newestId = tweets.getValue().get(0).getId();
                    oldestId = tweets.getValue().get(tweets.getValue().size()-1).getId();
                    Log.d("", "newest "+newestId);
                    Log.d("", "oldest "+oldestId);
                    //updateLiveRaid(raidName, twitterAPI, mainActivity);
                }

                else {

                    Log.d("", "onResponse: No "+raidNameEN+ " Loaded");
                }
            }

            @Override
            public void onFailure(Call<TweetData> call, Throwable t) {
                Log.d("", "onFailure: ");
            }
        });
    }
    public void updateRaid(String raidName,TwitterAPI twitterAPI){
        twitterAPI.findRaidAfter(raidName, newestId).enqueue(new Callback<TweetData>() {
            @Override
            public void onResponse(Call<TweetData> call, Response<TweetData> response) {
                if(response.isSuccessful()&&response.body().getData()!=null){
                    List<Tweet> temp = response.body().getData();
                    tweets.getValue().addAll(temp);
                    sortTrimRaids(tweets.getValue());
                    tweets.setValue(tweets.getValue());
                    Log.d("", "Added "+temp.size()+" "+raidName);
                    newestId = tweets.getValue().get(0).getId();
                    oldestId = tweets.getValue().get(tweets.getValue().size()-1).getId();
                }
                else Log.d("", "No updates for "+raidName);
            }

            @Override
            public void onFailure(Call<TweetData> call, Throwable t) {

            }
        });
    }
    private void sortTrimRaids(List<Tweet> list) {
        list.sort((t1, t2) -> t2.getId().compareTo(t1.getId()));
        if(list.size()>20){
            list.subList(0,19);
        }
    }
    public static RaidRepo getInstance(){
        if(instance==null){
            instance = new RaidRepo();
        }
        return instance;
    }
}
