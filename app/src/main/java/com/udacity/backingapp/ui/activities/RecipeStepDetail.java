package com.udacity.backingapp.ui.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.backingapp.R;
import com.udacity.backingapp.model.Ingredient;
import com.udacity.backingapp.model.Step;
import com.udacity.backingapp.ui.fragments.RecipeStepFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

public class RecipeStepDetail extends AppCompatActivity {
    private List<Ingredient> ingredients;
    private Step step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey("step")){

            step = bundle.getParcelable("step");

            if (step== null)
                ingredients = bundle.getParcelableArrayList("step");


            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();


            if(ingredients != null){
                recipeStepFragment.setIngredients(ingredients);
            } else if(step != null){
                recipeStepFragment.setStep(step);
            }

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_step_detail_container, recipeStepFragment)
                    .commit();

        }
        else{
            Timber.e("No step provided");
            finish();
        }
    }
}
