package com.udacity.backingapp.application;

import android.app.Application;

import com.udacity.backingapp.dagger.ApplicationComponent;
import com.udacity.backingapp.dagger.ApplicationModule;
import com.udacity.backingapp.dagger.DaggerApplicationComponent;
import com.udacity.backingapp.dagger.DaggerUserInterfaceComponent;
import com.udacity.backingapp.dagger.UserInterfaceComponent;

/**
 * Created by federico.creti on 23/03/2018.
 */

public class BackingAppApplication extends Application {
    private static ApplicationComponent appComponent;
    //private static UserInterfaceComponent userInterfaceComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this.getApplicationContext())).build();
        //userInterfaceComponent = DaggerUserInterfaceComponent.builder().build();
    }

    public static ApplicationComponent appComponent(){
        return appComponent;
    }
    //public static UserInterfaceComponent userInterfaceComponent() { return userInterfaceComponent; }
}
