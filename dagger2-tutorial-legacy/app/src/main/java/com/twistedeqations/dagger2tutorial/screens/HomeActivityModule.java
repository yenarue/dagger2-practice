package com.twistedeqations.dagger2tutorial.screens;

import com.squareup.picasso.Picasso;
import com.twistedeqations.dagger2tutorial.screens.home.AdapterRepos;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yena on 17. 1. 25.
 */

@Module
public class HomeActivityModule {

    private final HomeActivity homeActivity;

    public HomeActivityModule(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

//    @Provides
//    @HomeActivityScope
//    public AdapterRepos adapterRepos(Picasso picasso) {
//        return new AdapterRepos(homeActivity, picasso);
//    }

    @Provides
    @HomeActivityScope
    public HomeActivity homeActivity() {
        return homeActivity;
    }
}
