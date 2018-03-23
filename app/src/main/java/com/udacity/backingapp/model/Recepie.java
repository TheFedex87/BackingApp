package com.udacity.backingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by federico.creti on 23/03/2018.
 */

public class Recepie {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("ingredients")
    private List<Ingredient> ingredients;

    @SerializedName("steps")
    private List<Step> steps;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        steps = steps;
    }
}
