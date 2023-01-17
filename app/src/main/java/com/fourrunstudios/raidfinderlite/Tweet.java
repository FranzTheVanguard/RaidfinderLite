package com.fourrunstudios.raidfinderlite;


import static java.lang.Integer.parseInt;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tweet{
    private String created_at;
    private String text;
    private Long id;
    private int colonIndex;
    private int tIndex;
    private boolean containText;
    private String extraText = "";

    public Tweet(String created_at, String text, Long id) {
        this.created_at = created_at;
        this.text = text;
        this.id = id;
    }

    public void init() {
        colonIndex = text.indexOf(":");
        tIndex = created_at.indexOf("T");
        if(colonIndex>9){
            containText = true;
            extraText = text.substring(0, Math.abs(colonIndex-9));
        }
        else containText = false;
    }

    public String getCreated_at() {
        return created_at.substring(tIndex+1, tIndex+9);
    }
    public Date getDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            return format.parse(created_at);
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getRaidId() {
        String temp;
        if(text.length() > 8){
            temp = text.substring(Math.abs(colonIndex-9),Math.abs(colonIndex-1));
        }
        else temp = text;
        return temp;
    }
    public Long getId(){
        return id;
    }

    public String getExtraText() {
        return extraText;
    }
}