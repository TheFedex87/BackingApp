package com.udacity.backingapp.dagger;

import com.udacity.backingapp.retrofit.RecepiesApiInterface;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by federico.creti on 23/03/2018.
 */
@Singleton
@Component(modules = NetworkModule.class)
public interface NetworkComponent {
    RecepiesApiInterface getRecepiesApiInterface();
}
