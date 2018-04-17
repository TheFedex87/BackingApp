package com.udacity.backingapp.ui.activities;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.udacity.backingapp.R;
import com.udacity.backingapp.application.BackingAppApplication;
import com.udacity.backingapp.dagger.ApplicationModule;
import com.udacity.backingapp.dagger.DaggerUserInterfaceComponent;
import com.udacity.backingapp.dagger.UserInterfaceComponent;
import com.udacity.backingapp.model.Recipe;
import com.udacity.backingapp.model.Step;
import com.udacity.backingapp.ui.adapters.RecipesStepsAdapter;
import com.udacity.backingapp.ui.fragments.RecipeStepDescriptionFragment;
import com.udacity.backingapp.ui.fragments.RecipeStepsFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetail extends AppCompatActivity implements RecipesStepsAdapter.RecipeStepClickListener {

    private Recipe recipe;

    private boolean twoPaneMode;

    private UserInterfaceComponent userInterfaceComponent;

    @Nullable
    @BindView(R.id.recipe_step_detail_container)
    FrameLayout recipeStepDetailContainer;

    @Inject
    Context context;

    private RecipeStepsFragment recipeStepsFragment;

    private int selectedStep = -1;

    private Boolean loadNewFragmentOnTwoPanelMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        BackingAppApplication.appComponent().inject(this);

        userInterfaceComponent = DaggerUserInterfaceComponent.builder().applicationModule(new ApplicationModule(context)).build();

        ButterKnife.bind(this);

        //Retrieve from savedInstanceState the last selected step
        if (savedInstanceState != null && savedInstanceState.containsKey("LAST_SELECTED_STEP")){
            selectedStep = savedInstanceState.getInt("LAST_SELECTED_STEP");
        }

        //If this view is present inside this activity this mean we are in a tablet device and the screen is splitted in two panels
        if (recipeStepDetailContainer != null) twoPaneMode = true;

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey("recipe")) {

            recipe = bundle.getParcelable("recipe");

            List<String> recipeStepsDescription = new ArrayList<>();

            for (Step step : recipe.getSteps()) {
                recipeStepsDescription.add(step.getShortDescription());
            }

            //If no saved instance is present (eg: no rotation) I fill the fragment with all recipes data
            if (savedInstanceState == null) {
                recipeStepsFragment = (RecipeStepsFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_steps_list_container);//userInterfaceComponent.getRecipeStepsFragment();
                adaptStepsList(recipeStepsDescription);
                recipeStepsFragment.setSteps(recipeStepsDescription);
                recipeStepsFragment.setTwoPaneMode(twoPaneMode);

                /*FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_steps_list_container, recipeStepsFragment, "STEPS_LIST_FRAGMENT")
                        .commit();*/
            } else {
                //I retrieved the fragment from his ID
                recipeStepsFragment = (RecipeStepsFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_steps_list_container);
                recipeStepsFragment.setSteps(recipeStepsDescription);
                recipeStepsFragment.setTwoPaneMode(twoPaneMode);
                recipeStepsFragment.setSelectedStep(selectedStep);
            }

            loadNewFragmentOnTwoPanelMode = savedInstanceState == null;
            //If we are on screen rotation, I force the click in the previous clicked step, but advice this method to not load a new fragment,
            //but to reload it from the fragmentManager with findFragmentByTag
            if (twoPaneMode && selectedStep != -1){
                onRecipeStepClick(selectedStep);
            }
            loadNewFragmentOnTwoPanelMode = true;
        }
    }

    @Override
    public void onRecipeStepClick(int position) {
        //If we are not in a two panel layout a new activity with recipe step detail si opened
        if (!twoPaneMode) {
            Intent intent = new Intent(this, RecipeStepDetail.class);

            intent.putParcelableArrayListExtra("ingredients", new ArrayList(recipe.getIngredients()));
            intent.putParcelableArrayListExtra("steps", new ArrayList(recipe.getSteps()));
            intent.putExtra("stepIndex", position);

            startActivity(intent);
        } else {
            //If we are in a two panel layout the right side Fragment with recipe step detail is loaded
            //recipeStepsFragment.setSelectedStep(position);
            selectedStep = position;

            FragmentManager fragmentManager = getSupportFragmentManager();
            //BackingAppApplication.appComponent().inject(this);
            RecipeStepDescriptionFragment recipeStepDescriptionFragment;

            if(loadNewFragmentOnTwoPanelMode)
                recipeStepDescriptionFragment = userInterfaceComponent.getRecipeStepFragment();
            else
                recipeStepDescriptionFragment = (RecipeStepDescriptionFragment) getSupportFragmentManager().findFragmentByTag("STEPS_FRAGMENT");

            //Since we are on tablet even if we rotate the screen no full screen video mode is enabled
            recipeStepDescriptionFragment.setEnableFullScreenOnLandscape(false);

            if (position == 0){
                recipeStepDescriptionFragment.setIngredients(recipe.getIngredients());
            } else {
                recipeStepDescriptionFragment.setStep(recipe.getSteps().get(position - 1));
            }

            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_detail_container, recipeStepDescriptionFragment, "STEPS_FRAGMENT")
                    .commit();
        }
    }

    private void adaptStepsList(List<String> listToAdapt){
        listToAdapt.add(0, context.getString(R.string.recepy_ingredients));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("LAST_SELECTED_STEP", selectedStep);
    }
}
