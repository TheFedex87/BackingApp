package com.udacity.backingapp.application;

import android.app.Application;

import com.udacity.backingapp.dagger.ApplicationComponent;
import com.udacity.backingapp.dagger.ApplicationModule;
import com.udacity.backingapp.dagger.DaggerApplicationComponent;

/**
 * Created by federico.creti on 23/03/2018.
 */

public class BackingAppApplication extends Application {
    private static ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this.getApplicationContext())).build();
    }

    public static ApplicationComponent appComponent(){
        return appComponent;
    }
}
