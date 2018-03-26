package com.udacity.backingapp.ui.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.backingapp.R;
import com.udacity.backingapp.application.BackingAppApplication;
import com.udacity.backingapp.dagger.ApplicationModule;
import com.udacity.backingapp.dagger.DaggerUserInterfaceComponent;
import com.udacity.backingapp.model.Ingredient;
import com.udacity.backingapp.model.Step;
import com.udacity.backingapp.ui.fragments.RecipeStepFragment;
import com.udacity.backingapp.ui.fragments.RecipeStepsFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class RecipeStepDetail extends AppCompatActivity {
    private List<Ingredient> ingredients;
    private Step step;

    @Inject
    Context context;

    @Inject
    RecipeStepsFragment recipeStepsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        Bundle bundle = getIntent().getExtras();

        BackingAppApplication.appComponent().inject(this);

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
