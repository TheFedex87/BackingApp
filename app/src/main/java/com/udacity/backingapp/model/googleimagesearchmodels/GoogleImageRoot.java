package com.udacity.backingapp.model.googleimagesearchmodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by feder on 31/03/2018.
 */

public class GoogleImageRoot {
    @SerializedName("items")
    public List<GoogleImage> items;
}
