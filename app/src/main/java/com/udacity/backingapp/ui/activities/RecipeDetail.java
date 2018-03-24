package com.udacity.backingapp.ui.activities;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.backingapp.R;
import com.udacity.backingapp.application.BackingAppApplication;
import com.udacity.backingapp.model.Recipe;
import com.udacity.backingapp.model.Step;
import com.udacity.backingapp.ui.adapters.RecipesStepsAdapter;
import com.udacity.backingapp.ui.fragments.RecipeStepFragment;
import com.udacity.backingapp.ui.fragments.RecipeStepsFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecipeDetail extends AppCompatActivity implements RecipesStepsAdapter.RecipeStepClickListener {

    private Recipe recipe;

    private boolean twoPaneMode;

    @Inject
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        BackingAppApplication.appComponent().inject(this);

        if(findViewById(R.id.step_frame_container) != null) twoPaneMode = true;

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey("recipe")) {

            recipe = bundle.getParcelable("recipe");

            List<String> recipeStepsDescription = new ArrayList<>();

            for(Step step : recipe.getSteps()){
                recipeStepsDescription.add(step.getShortDescription());
            }

            RecipeStepsFragment recipeSteps = new RecipeStepsFragment();
            recipeSteps.setSteps(recipeStepsDescription);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_steps_list_container, recipeSteps)
                    .commit();
        }
    }

    @Override
    public void onRecipeStepClick(int position) {
        if (!twoPaneMode) {
            Intent intent = new Intent(this, RecipeStepDetail.class);
            Bundle bundle = new Bundle();

            if (position == 0) {
                intent.putParcelableArrayListExtra("step", new ArrayList(recipe.getIngredients()));
            } else {
                intent.putExtra("step", recipe.getSteps().get(position - 1));
            }

            startActivity(intent);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();

            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

            if (position == 0){
                recipeStepFragment.setIngredients(recipe.getIngredients());
            } else {
                recipeStepFragment.setStep(recipe.getSteps().get(position - 1));
            }

            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_detail_container, recipeStepFragment)
                    .commit();
        }
    }
}
