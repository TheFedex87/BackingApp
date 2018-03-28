package com.udacity.backingapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.backingapp.R;
import com.udacity.backingapp.application.BackingAppApplication;
import com.udacity.backingapp.dagger.ApplicationModule;
import com.udacity.backingapp.dagger.DaggerUserInterfaceComponent;
import com.udacity.backingapp.model.Ingredient;
import com.udacity.backingapp.model.Step;
import com.udacity.backingapp.ui.adapters.IngredientsAdapter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

/**
 * Created by Federico on 24/03/2018.
 */

public class RecipeStepDescriptionFragment extends Fragment {
    private List<Ingredient> ingredients;
    private Step step;


    @Nullable @BindView(R.id.ingredients_container)
    RecyclerView ingredientsContainer;

    @Nullable @BindView(R.id.step_description)
    TextView recipeStepDescription;

    @Inject
    Context context;

    @Inject
    public RecipeStepDescriptionFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //BackingAppApplication.appComponent().inject(this);

        View rootView = null;

        if(ingredients != null){
            rootView = inflater.inflate(R.layout.recipe_ingredients, container, false);

            ButterKnife.bind(this, rootView);

            IngredientsAdapter ingredientsAdapter = DaggerUserInterfaceComponent.builder().applicationModule(new ApplicationModule(context)).build().getIngredientsAdapter();
            ingredientsContainer.setAdapter(ingredientsAdapter);
            ingredientsContainer.setLayoutManager(DaggerUserInterfaceComponent.builder().applicationModule(new ApplicationModule(context)).build().getLinearLayoutManager());
            ingredientsAdapter.swapIngredients(ingredients);


        } else if(step != null){
            rootView = inflater.inflate(R.layout.recipe_step, container, false);

            ButterKnife.bind(this, rootView);

            recipeStepDescription.setText(step.getDescription());
        }


        return rootView;

    }

    public void setStep(@NonNull Step step){
        //If we are set a step this meas we are not passing ingredients, so we put them to null
        ingredients = null;
        this.step = step;
    }

    public void setIngredients(@NonNull List<Ingredient> ingredients){
        //If we are set the ingredients this meas we are not passing a step, so we put it to null
        step = null;
        this.ingredients = ingredients;
    }
}
