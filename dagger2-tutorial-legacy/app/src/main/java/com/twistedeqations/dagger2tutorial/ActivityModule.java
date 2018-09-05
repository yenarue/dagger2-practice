package com.twistedeqations.dagger2tutorial;

import android.app.Activity;
import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yena on 17. 1. 24.
 */

@Module
public class ActivityModule {

    private final Activity context;

    // if module has constructor with parameters, the module is Mandantory module.
    public ActivityModule(Activity context) {
        this.context = context;
    }

    @Provides
    @GithubApplicationScope
    @Named("activity_context")
    public Context context() {
        return context;
    }
}
