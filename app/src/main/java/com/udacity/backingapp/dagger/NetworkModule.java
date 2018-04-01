package com.udacity.backingapp.dagger;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.udacity.backingapp.retrofit.GoogleImagesApiInterface;
import com.udacity.backingapp.retrofit.RecipesApiInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by federico.creti on 23/03/2018.
 */

@Module
public class NetworkModule {
    private final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";

    @Singleton
    @Provides
    public RecipesApiInterface provideRecepiesApiInterface(Retrofit retrofit){
        return retrofit.create(RecipesApiInterface.class);
    }

    @Singleton
    @Provides
    public GoogleImagesApiInterface provideGoogleImagesApiInterface(Retrofit retrofit){
        return retrofit.create(GoogleImagesApiInterface.class);
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(Gson gson){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Singleton
    @Provides
    public Gson provideGson(){
        return new GsonBuilder().create();
    }


    @Singleton
    @Provides
    public Picasso providePicasso(Context context){
        return Picasso.with(context);
    }
}
