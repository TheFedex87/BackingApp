package com.udacity.backingapp.ui.activities;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.support.v4.app.FragmentManager;
import android.widget.ScrollView;

import com.udacity.backingapp.R;
import com.udacity.backingapp.application.BackingAppApplication;
import com.udacity.backingapp.dagger.ApplicationModule;
import com.udacity.backingapp.dagger.DaggerUserInterfaceComponent;
import com.udacity.backingapp.dagger.UserInterfaceComponent;
import com.udacity.backingapp.model.Ingredient;
import com.udacity.backingapp.model.Step;
import com.udacity.backingapp.ui.fragments.RecipeStepDescriptionFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeStepDetail extends AppCompatActivity {
    private List<Ingredient> ingredients;
    private List<Step> stepsList;
    private int stepIndex;

    @BindView(R.id.previous_step)
    Button previusStep;

    @BindView(R.id.next_step)
    Button nextStep;

    @Nullable @BindView(R.id.main_step_container)
    ScrollView mainStepContainer;

    @Inject
    Context context;

    //This is the fragment which contains the video player and step description
    private RecipeStepDescriptionFragment recipeStepDescriptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        Bundle bundle = getIntent().getExtras();

        if (savedInstanceState == null)
            recipeStepDescriptionFragment = DaggerUserInterfaceComponent.builder().applicationModule(new ApplicationModule(context)).build().getRecipeStepDescriptionFragment();
        else
            recipeStepDescriptionFragment = (RecipeStepDescriptionFragment) getSupportFragmentManager().findFragmentByTag("STEPS_FRAGMENT");

        final UserInterfaceComponent userInterfaceComponent = DaggerUserInterfaceComponent.builder().applicationModule(new ApplicationModule(context)).build();

        final RecipeStepDetail recipeStepDetail = this;
        BackingAppApplication.appComponent().inject(recipeStepDetail);

        if (bundle.containsKey("steps") && bundle.containsKey("ingredients")){
            ButterKnife.bind(this);

            if (bundle.containsKey("recipe_name")){
                getSupportActionBar().setTitle(bundle.getString("recipe_name"));
            }

            //This button navigate to previous step
            previusStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recipeStepDescriptionFragment = userInterfaceComponent.getRecipeStepDescriptionFragment();
                    stepIndex--;
                    nextStep.setEnabled(true);
                    if (stepIndex == 0){
                        previusStep.setEnabled(false);
                        recipeStepDescriptionFragment.setIngredients(ingredients);
                    } else {
                        recipeStepDescriptionFragment.setStep(stepsList.get(stepIndex - 1));
                    }
                    updateFragment();

                    if (mainStepContainer != null)
                        mainStepContainer.scrollTo(0, 0);
                }
            });

            //This button navigate to next step
            nextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recipeStepDescriptionFragment = userInterfaceComponent.getRecipeStepDescriptionFragment();
                    stepIndex++;
                    previusStep.setEnabled(true);
                    if (stepIndex == stepsList.size()) {
                        nextStep.setEnabled(false);
                    }
                    recipeStepDescriptionFragment.setStep(stepsList.get(stepIndex - 1));
                    updateFragment();

                    if (mainStepContainer != null)
                        mainStepContainer.scrollTo(0, 0);
                }
            });

            stepsList = bundle.getParcelableArrayList("steps");
            ingredients = bundle.getParcelableArrayList("ingredients");

            if (savedInstanceState == null || !savedInstanceState.containsKey("passedStepIndex"))
                stepIndex = bundle.getInt("stepIndex");
            else{
                stepIndex = savedInstanceState.getInt("passedStepIndex");
            }


            if (stepIndex == 0) {
                recipeStepDescriptionFragment.setIngredients(ingredients);
                previusStep.setEnabled(false);
            } else {
                recipeStepDescriptionFragment.setStep(stepsList.get(stepIndex - 1));
                if(stepIndex == stepsList.size()){
                    nextStep.setEnabled(false);
                }
            }

            //if we are in landscape mode, we set a full screen flag and hide the action bar
            int orientation = getResources().getConfiguration().orientation;
            if(orientation == 2){
                ActionBar actionBar = getSupportActionBar();
                actionBar.hide();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }

            updateFragment();
        }
        else{
            Timber.e("No step provided");
            finish();
        }
    }

    private void updateFragment(){
        recipeStepDescriptionFragment.setEnableFullScreenOnLandscape(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .replace(R.id.recipe_step_detail_container, recipeStepDescriptionFragment, "STEPS_FRAGMENT")
                .commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("passedStepIndex", stepIndex);
    }
}
