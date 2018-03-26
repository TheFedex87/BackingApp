package com.udacity.backingapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.backingapp.R;
import com.udacity.backingapp.application.BackingAppApplication;
import com.udacity.backingapp.dagger.ApplicationModule;
import com.udacity.backingapp.dagger.DaggerUserInterfaceComponent;
import com.udacity.backingapp.dagger.UserInterfaceComponent;
import com.udacity.backingapp.dagger.UserInterfaceModule;
import com.udacity.backingapp.ui.adapters.RecipesStepsAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Federico on 24/03/2018.
 */

public class RecipeStepsFragment extends Fragment  {
    @BindView(R.id.recipes_steps_container)
    RecyclerView recipeStepsContainer;

    private List<String> stepsList;

    private RecipesStepsAdapter.RecipeStepClickListener recipeStepClickListener;

    private boolean singlePane;

    @Inject
    public RecipeStepsFragment() {}


    @Inject
    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            recipeStepClickListener = (RecipesStepsAdapter.RecipeStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement RecipeStepClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BackingAppApplication.appComponent().inject(this);

        View rootView = inflater.inflate(R.layout.recipe_steps_list, container, false);

        ButterKnife.bind(this, rootView);

        UserInterfaceComponent daggerUserInterfaceComponent = DaggerUserInterfaceComponent.builder()
                .applicationModule(new ApplicationModule(context))
                .userInterfaceModule(new UserInterfaceModule(recipeStepClickListener))
                .build();

        RecipesStepsAdapter recipeStepsAdapter = daggerUserInterfaceComponent.getRecipeStepsAdapter();
        recipeStepsAdapter.setHighLightSelected(!singlePane);
        recipeStepsContainer.setAdapter(recipeStepsAdapter);
        recipeStepsContainer.setLayoutManager(daggerUserInterfaceComponent.getLinearLayoutManager());

        if(stepsList != null){
            recipeStepsAdapter.swapRecipeSteps(stepsList);
        }

        return rootView;

    }

    public void setSteps(List<String> stepsList){
        this.stepsList = stepsList;
    }

    public void setSinglePane(boolean singlePane){ this.singlePane = singlePane; }
}
