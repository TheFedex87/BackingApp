package com.udacity.backingapp.retrofit;

import com.udacity.backingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by federico.creti on 23/03/2018.
 */

public interface RecipesApiInterface {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> recipes();
}
