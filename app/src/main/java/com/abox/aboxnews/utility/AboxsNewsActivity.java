package com.abox.aboxnews.utility;

import android.support.v7.app.AppCompatActivity;

import com.abox.aboxnews.api.AboxsNewsApi;

public class AboxsNewsActivity extends AppCompatActivity {
    public AboxsNewsApi getAPI() {
        return AboxsNews.getAPI();
    }
}
