package com.udacity.backingapp.retrofit;

import com.udacity.backingapp.model.Recepie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by federico.creti on 23/03/2018.
 */

public interface RecepiesApiInterface {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recepie>> recepies();
}
