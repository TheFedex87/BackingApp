package com.udacity.backingapp.ui.activities;

import android.content.Context;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.support.v4.app.FragmentManager;
import android.widget.LinearLayout;

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

    @Inject
    Context context;

    //This is the fragment which contains the video player and step description
    private RecipeStepDescriptionFragment recipeStepDescriptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        Bundle bundle = getIntent().getExtras();

        recipeStepDescriptionFragment = DaggerUserInterfaceComponent.builder().applicationModule(new ApplicationModule(context)).build().getRecipeStepFragment();

        final RecipeStepDetail recipeStepDetail = this;
        BackingAppApplication.appComponent().inject(recipeStepDetail);

        if (bundle.containsKey("steps") && bundle.containsKey("ingredients")){
            ButterKnife.bind(this);

            previusStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //In order to get a new reference of RecipeStepDescriptionFragment (which I need to replace the fragment) I have to reinject this class
                    //BackingAppApplication.appComponent().inject(recipeStepDetail);
                    recipeStepDescriptionFragment = DaggerUserInterfaceComponent.builder().applicationModule(new ApplicationModule(context)).build().getRecipeStepFragment();
                    stepIndex--;
                    nextStep.setEnabled(true);
                    if (stepIndex == 0){
                        previusStep.setEnabled(false);
                        recipeStepDescriptionFragment.setIngredients(ingredients);
                    } else {
                        recipeStepDescriptionFragment.setStep(stepsList.get(stepIndex - 1));
                    }
                    updateFragment();
                }
            });

            nextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //In order to get a new reference of RecipeStepDescriptionFragment (which I need to replace the fragment) I have to reinject this class
                    //BackingAppApplication.appComponent().inject(recipeStepDetail);
                    recipeStepDescriptionFragment = DaggerUserInterfaceComponent.builder().applicationModule(new ApplicationModule(context)).build().getRecipeStepFragment();
                    stepIndex++;
                    previusStep.setEnabled(true);
                    if (stepIndex == stepsList.size()) {
                        nextStep.setEnabled(false);
                    }
                    recipeStepDescriptionFragment.setStep(stepsList.get(stepIndex - 1));
                    updateFragment();
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

            int orientation = getResources().getConfiguration().orientation;
            if(orientation == 2){
                ActionBar actionBar = getSupportActionBar();
                actionBar.hide();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }

            //if (savedInstanceState == null)
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
                .replace(R.id.recipe_step_detail_container, recipeStepDescriptionFragment)
                .commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("passedStepIndex", stepIndex);
    }
}
