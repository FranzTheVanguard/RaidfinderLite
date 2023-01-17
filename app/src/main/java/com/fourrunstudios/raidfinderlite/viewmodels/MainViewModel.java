package com.fourrunstudios.raidfinderlite.viewmodels;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;
import android.view.MenuItem;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fourrunstudios.raidfinderlite.MainActivity;
import com.fourrunstudios.raidfinderlite.R;
import com.fourrunstudios.raidfinderlite.RaidRepo;
import com.fourrunstudios.raidfinderlite.Tweet;
import com.fourrunstudios.raidfinderlite.retrofit.APIManager;
import com.fourrunstudios.raidfinderlite.retrofit.TwitterAPI;

import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.annotations.NonNull;

public class MainViewModel extends ViewModel {
    private String RAID_NAME_JP = "";
    private String RAID_NAME_EN = "";
    private final TwitterAPI twitterAPI = APIManager.getTwitterAPI();
    private RaidRepo raidRepo;
    public MutableLiveData<List<Tweet>> tweets;
    private Resources jpResource, enResource;
    private Locale defaultLocale;

    public String getLocaleRaid() {
        if(defaultLocale.getDisplayLanguage().equals("日本語")) return RAID_NAME_JP;
        return RAID_NAME_EN;
    }

    public MutableLiveData<List<Tweet>> getLiveRaid() {
        instRaidRepo();
        tweets = raidRepo.getLiveRaid(RAID_NAME_JP, RAID_NAME_EN, twitterAPI);
        return tweets;
    }
    public void updateRaid(){
        raidRepo.updateRaid(RAID_NAME_JP, twitterAPI);
        raidRepo.updateRaid(RAID_NAME_EN, twitterAPI);
    }
    private void instRaidRepo(){
        if(raidRepo==null) raidRepo = RaidRepo.getInstance();
    }
    public void idToRaidName(MenuItem item, MainActivity activity){
        boolean update = false;
        if(defaultLocale.getDisplayLanguage().equals("日本語")){
            if(!RAID_NAME_JP.equals(item.getTitle().toString())) update = true;
            RAID_NAME_EN = item.getTooltipText().toString();
            RAID_NAME_JP = item.getTitle().toString();
        }
        else{
            if(!RAID_NAME_JP.equals(item.getTitle().toString())) update = true;
            RAID_NAME_EN = item.getTitle().toString();
            RAID_NAME_JP = item.getTooltipText().toString();
        }
        if(update){
            initNewRaid(activity);
        }
    }
    private void initNewRaid(MainActivity activity){
        if(tweets!=null){
            activity.clearDisposables();
            clearRaid(activity);
        }
        activity.updateTitle(getLocaleRaid());
        activity.getRaid();
    }
    private void clearRaid(MainActivity activity){
        if(tweets.hasObservers()) tweets.removeObservers(activity);
    }
    @NonNull
    protected String getRaidString(int id, String locale, MainActivity activity) {
        if(defaultLocale.getDisplayLanguage().equals("日本語")){
            if(locale.equals("ja")) return jpResource.getString(id);
            if(locale.equals("en")) return enResource.getString(id);
        }
        else{
            if(locale.equals("ja")) return jpResource.getString(id);
            if(locale.equals("en")) return enResource.getString(id);
        }
        return "";
    }
    @NonNull
    private Resources getLocalizedResources(Context context, Locale desiredLocale) {
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(desiredLocale);
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources();
    }
    public void init(MainActivity activity){
        defaultLocale = Locale.getDefault();
        jpResource = getLocalizedResources(activity, new Locale("ja"));
        enResource = getLocalizedResources(activity, new Locale("en"));
    }
}
