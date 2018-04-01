package com.udacity.backingapp.dagger;

import com.squareup.picasso.Picasso;
import com.udacity.backingapp.retrofit.GoogleImagesApiInterface;
import com.udacity.backingapp.retrofit.RecipesApiInterface;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by federico.creti on 23/03/2018.
 */
@Singleton
@Component(modules = { NetworkModule.class, ApplicationModule.class })
public interface NetworkComponent {
    RecipesApiInterface getRecepiesApiInterface();
    GoogleImagesApiInterface getGoogleImagesApiInterface();
    Picasso getPicasso();
}
