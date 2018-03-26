package com.udacity.backingapp.ui.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.backingapp.R;
import com.udacity.backingapp.application.BackingAppApplication;
import com.udacity.backingapp.model.Ingredient;
import com.udacity.backingapp.model.Step;
import com.udacity.backingapp.ui.fragments.RecipeStepDescriptionFragment;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class RecipeStepDetail extends AppCompatActivity {
    private List<Ingredient> ingredients;
    private List<Step> stepsList;
    private int stepIndex;

    @Inject
    Context context;

    //This is the fragment which contains the video player and step description
    @Inject
    RecipeStepDescriptionFragment recipeStepFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        Bundle bundle = getIntent().getExtras();

        BackingAppApplication.appComponent().inject(this);

        //recipeStepsFragment = BackingAppApplication.userInterfaceComponent().getRecipeStepsFragment();

        if (bundle.containsKey("steps") && bundle.containsKey("ingredients")){

            stepsList = bundle.getParcelableArrayList("steps");
            ingredients = bundle.getParcelableArrayList("ingredients");
            stepIndex = bundle.getInt("stepIndex");

            if (stepIndex == 0)
                recipeStepFragment.setIngredients(ingredients);
            else
                recipeStepFragment.setStep(stepsList.get(stepIndex - 1));

            updateFragment();
        }
        else{
            Timber.e("No step provided");
            finish();
        }
    }

    private void updateFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_step_detail_container, recipeStepFragment)
                .commit();
    }
}
