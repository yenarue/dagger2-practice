package com.twistedeqations.dagger2tutorial;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.twistedeqations.dagger2tutorial.network.DateTimeConverter;
import com.twistedeqations.dagger2tutorial.network.GithubService;

import org.joda.time.DateTime;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class LegacyGithubApplication extends Application {

    public static LegacyGithubApplication get(Activity activity) {
        return (LegacyGithubApplication) activity.getApplication();
    }

    private GithubApplicationComponent component;
    private GithubService githubService;
    private Picasso picasso;

    /** Activity has so many dependencies **/
    //       Activity
    // GithubService  picasso
    /////////use just Component!!!!!!!!!!!!!/////////
    // retrofit       OkHttp3Downloader
    // gson         okhttp
    //      logger        cache
    //      timber               file

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());


        // 1. Context
        Context context = this;

        // 2. Network Group (okhttp, logger, timber, cache, file)
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.i(message);
            }
        });

        File cacheFile = new File(context.getCacheDir(), "okhttp_cache");
        cacheFile.mkdirs();

        Cache cache = new Cache(cacheFile, 10 * 1000 * 1000); // 10MB Cache

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .cache(cache)
            .build();

        // 3. Picasso Group (picasso, OkHttp3Downloader)
        picasso = new Picasso.Builder(context)
            .downloader(new OkHttp3Downloader(okHttpClient))
            .build();

        // 4. Client Group (GithubService, retrofit, gson)
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeConverter());
        Gson gson = gsonBuilder.create();

        Retrofit gitHubRetrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .baseUrl("https://api.github.com/")
            .build();

        githubService = gitHubRetrofit.create(GithubService.class);
    }

    public GithubApplicationComponent component() {
        return component;
    }
}