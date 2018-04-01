package com.udacity.backingapp.retrofit;

import com.udacity.backingapp.model.googleimagesearchmodels.GoogleImageRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by feder on 31/03/2018.
 */

public interface GoogleImagesApiInterface {
    @GET
    Call<GoogleImageRoot> googleImages(@Url String url, @Query("q") String query, @Query("key") String apiKey);
}
