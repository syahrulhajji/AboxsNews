package com.abox.aboxnews.utility;

import android.support.multidex.MultiDexApplication;

import com.abox.aboxnews.api.AboxsNewsApi;
import com.abox.news.aboxnews.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AboxsNews extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();

    }
    public static AboxsNewsApi getAPI() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(AboxsNewsApi.SERVER).client(getClient())
                .addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(AboxsNewsApi.class);
    }
    public static OkHttpClient getClient() {

        return new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1,TimeUnit.MINUTES)
                .writeTimeout(1,TimeUnit.MINUTES)
                .addInterceptor(getLoggingInterceptor())
                .build();
    }

    public static HttpLoggingInterceptor.Level getInterceptorLevel() {
        if (BuildConfig.DEBUG) return HttpLoggingInterceptor.Level.BODY;
        else return HttpLoggingInterceptor.Level.NONE;
    }

    public static HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(getInterceptorLevel());
        return httpLoggingInterceptor;
    }
}
