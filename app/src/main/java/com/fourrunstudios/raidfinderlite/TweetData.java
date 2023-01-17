package com.fourrunstudios.raidfinderlite;

import java.util.List;

public class TweetData {
    private List<Tweet> data;

    public TweetData(List<Tweet> data) {
        this.data = data;
    }

    public List<Tweet> getData() {
        return data;
    }

}
