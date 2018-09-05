package com.twistedeqations.dagger2tutorial;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.fatboyindustrial.gsonjodatime.Converters;
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

import static com.fatboyindustrial.gsonjodatime.Converters.DATE_TIME_TYPE;

public class GithubApplication extends Application {

    public static GithubApplication get(Activity activity) {
        return (GithubApplication) activity.getApplication();
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
//        Context context = this;

        // 2. Network Group (okhttp, logger, timber, cache, file)
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//            @Override
//            public void log(String message) {
//                Timber.i(message);
//            }
//        });

//        File cacheFile = new File(getCacheDir(), "okhttp_cache");
//        cacheFile.mkdirs();

//        Cache cache = new Cache(cacheFile, 10 * 1000 * 1000); // 10MB Cache

//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .cache(cache)
//            .build();

        // 3. Picasso Group (picasso, OkHttp3Downloader)
//        picasso = new Picasso.Builder(context)
//            .downloader(new OkHttp3Downloader(okHttpClient))
//            .build();

        // 4. Client Group (GithubService, retrofit, gson)
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeConverter());
//        Gson gson = gsonBuilder.create();

//        Retrofit gitHubRetrofit = new Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .client(okHttpClient)
//            .baseUrl("https://api.github.com/")
//            .build();

//        githubService = gitHubRetrofit.create(GithubService.class);

        component = DaggerGithubApplicationComponent.builder()
                .contextModule(new ContextModule(this))         // 1. Context Group - Mandantory
//                .networkModule(new NetworkModule())             // 2. Network Group
//                .picassoModule(new PicassoModule())             // 3. Picasso Group
//                .githubServiceModule(new GithubServiceModule()) // 4. Client Group
                .build();

        Log.i("Dagger 2", "component=" + component);

        githubService = component.getGithubService();
        picasso = component.getPicasso();

        GithubService githubService2 = component.getGithubService();
        Picasso picasso2 = component.getPicasso();

        GithubService githubService3 = component.getGithubService();
        Picasso picasso3 = component.getPicasso();

        Log.i("Dagger 2", "gitHubService=" + githubService);
        Log.i("Dagger 2", "picasso=" + picasso);

        Log.i("Dagger 2", "gitHubService2=" + githubService2);
        Log.i("Dagger 2", "picasso2=" + picasso2);

        Log.i("Dagger 2", "gitHubService3=" + githubService3);
        Log.i("Dagger 2", "picasso3=" + picasso3);


        // test for @Singleton
        GithubApplicationComponent component2 = DaggerGithubApplicationComponent.builder()
                .contextModule(new ContextModule(this))
                .build();

        Log.i("Dagger 2", "component2=" + component2);
        Log.i("Dagger 2", "gitHubService=" + component2.getGithubService());
        Log.i("Dagger 2", "picasso=" + component2.getPicasso());

        Log.i("Dagger 2", "gitHubService2=" + component2.getGithubService());
        Log.i("Dagger 2", "picasso2=" + component2.getPicasso());

        Log.i("Dagger 2", "gitHubService3=" + component2.getGithubService());
        Log.i("Dagger 2", "picasso3=" + component2.getPicasso());

        // * getPicasso, getGithubService generate new Instance every time calling.
        //   => all instance share dependency with same instance............ oh my god.. it can occur concurrency issues.
        //   => it can be a big big big big problem !
        //   => we want to have only one instance !
//        01-24 21:59:32.551 30683-30683/? I/Dagger 2: gitHubService=retrofit2.Retrofit$1@cc380f4
//        01-24 21:59:32.551 30683-30683/? I/Dagger 2: picasso=com.squareup.picasso.Picasso@140701d
//        01-24 21:59:32.551 30683-30683/? I/Dagger 2: gitHubService2=retrofit2.Retrofit$1@e06c292
//        01-24 21:59:32.551 30683-30683/? I/Dagger 2: picasso2=com.squareup.picasso.Picasso@7158463
//        01-24 21:59:32.551 30683-30683/? I/Dagger 2: gitHubService3=retrofit2.Retrofit$1@a11b860
//        01-24 21:59:32.551 30683-30683/? I/Dagger 2: picasso3=com.squareup.picasso.Picasso@9f8f119

        // * after add Scope => yeah single instance :-)
//        01-24 22:10:39.814 10593-10593/? I/Dagger 2: gitHubService=retrofit2.Retrofit$1@ee7ec06
//        01-24 22:10:39.814 10593-10593/? I/Dagger 2: picasso=com.squareup.picasso.Picasso@54d82c7
//        01-24 22:10:39.814 10593-10593/? I/Dagger 2: gitHubService2=retrofit2.Retrofit$1@ee7ec06
//        01-24 22:10:39.814 10593-10593/? I/Dagger 2: picasso2=com.squareup.picasso.Picasso@54d82c7
//        01-24 22:10:39.814 10593-10593/? I/Dagger 2: gitHubService3=retrofit2.Retrofit$1@ee7ec06
//        01-24 22:10:39.814 10593-10593/? I/Dagger 2: picasso3=com.squareup.picasso.Picasso@54d82c7
    }

    public GithubApplicationComponent component() {
        return component;
    }
}