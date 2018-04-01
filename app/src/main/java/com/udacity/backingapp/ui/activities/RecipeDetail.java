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
import com.udacity.backingapp.ui.fragments.RecipeStepDescriptionFragment;
import com.udacity.backingapp.ui.fragments.RecipeStepsFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecipeDetail extends AppCompatActivity implements RecipesStepsAdapter.RecipeStepClickListener {

    private Recipe recipe;

    private boolean twoPaneMode;

    private int selectedStepsOnTwoPaneMode = -1;

    //This is the fragment which contains the list of all steps of a recipe
    @Inject
    RecipeStepsFragment recipeStepsFragment;

    //This is the fragment which contains the video player and step description
    @Inject
    RecipeStepDescriptionFragment recipeStepDescriptionFragment;

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

            if (savedInstanceState == null) {
                adaptStepsList(recipeStepsDescription);
                recipeStepsFragment.setSteps(recipeStepsDescription);
                recipeStepsFragment.setTwoPaneMode(twoPaneMode);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_steps_list_container, recipeStepsFragment, "STEPS_LIST_FRAGMENT")
                        .commit();
            } else if(twoPaneMode) {
                selectedStepsOnTwoPaneMode = savedInstanceState.getInt("selectedStep");
                recipeStepsFragment.setSelectedStep(selectedStepsOnTwoPaneMode);
            }

        }
    }

    @Override
    public void onRecipeStepClick(int position) {
        if (!twoPaneMode) {
            Intent intent = new Intent(this, RecipeStepDetail.class);

            intent.putParcelableArrayListExtra("ingredients", new ArrayList(recipe.getIngredients()));
            intent.putParcelableArrayListExtra("steps", new ArrayList(recipe.getSteps()));
            intent.putExtra("stepIndex", position);

            startActivity(intent);
        } else {
            selectedStepsOnTwoPaneMode = position;

            FragmentManager fragmentManager = getSupportFragmentManager();
            BackingAppApplication.appComponent().inject(this);
            //recipeStepDescriptionFragment = new RecipeStepDescriptionFragment();
            recipeStepDescriptionFragment.setEnableFullScreenOnLandscape(false);

            if (position == 0){
                recipeStepDescriptionFragment.setIngredients(recipe.getIngredients());
            } else {
                recipeStepDescriptionFragment.setStep(recipe.getSteps().get(position - 1));
            }

            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_detail_container, recipeStepDescriptionFragment)
                    .commit();
        }
    }

    private void adaptStepsList(List<String> listToAdapt){
        listToAdapt.add(0, context.getString(R.string.recepy_ingredients));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedStep", selectedStepsOnTwoPaneMode);
    }
}
