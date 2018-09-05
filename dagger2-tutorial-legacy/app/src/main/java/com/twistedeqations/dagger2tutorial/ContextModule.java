package com.twistedeqations.dagger2tutorial;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yena on 17. 1. 24.
 */

@Module
public class ContextModule {

    private final Context context;

    // if module has constructor with parameters, the module is Mandantory module.
    public ContextModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @GithubApplicationScope
    @ApplicationContext // @Named("application_context")
    public Context context() {
        return context;
    }
}
