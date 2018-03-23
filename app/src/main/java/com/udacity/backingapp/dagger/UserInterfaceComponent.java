package com.udacity.backingapp.dagger;

import android.support.v7.widget.GridLayoutManager;

import com.udacity.backingapp.ui.adapter.RecepiesAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by federico.creti on 23/03/2018.
 */
@Singleton
@Component(modules = { ApplicationModule.class, UserInterfaceModule.class })
public interface UserInterfaceComponent {
    RecepiesAdapter getRecepiesAdapter();

    GridLayoutManager getGridLayoutManager();
}
