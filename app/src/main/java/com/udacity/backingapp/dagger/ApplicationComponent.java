package com.udacity.backingapp.dagger;

import com.udacity.backingapp.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by federico.creti on 23/03/2018.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
}
