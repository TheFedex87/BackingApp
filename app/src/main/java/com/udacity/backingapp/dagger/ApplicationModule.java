package com.udacity.backingapp.dagger;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by federico.creti on 23/03/2018.
 */

@Module
public class ApplicationModule {
    private Context context;

    public ApplicationModule(Context context){
        this.context = context;
    }

    @Singleton
    @Provides
    public Context provideContext(){
        return context;
    }
}
