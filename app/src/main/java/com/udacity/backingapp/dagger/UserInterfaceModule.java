package com.udacity.backingapp.dagger;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.udacity.backingapp.R;
import com.udacity.backingapp.ui.adapters.IngredientsAdapter;
import com.udacity.backingapp.ui.adapters.RecipesAdapter;
import com.udacity.backingapp.ui.adapters.RecipesStepsAdapter;
import com.udacity.backingapp.ui.fragments.RecipeStepDescriptionFragment;
import com.udacity.backingapp.ui.fragments.RecipeStepsFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by federico.creti on 23/03/2018.
 */
@Module
public class UserInterfaceModule {
    private RecipesAdapter.RecipeClickListener recipeClickListener;
    private RecipesStepsAdapter.RecipeStepClickListener recipeStepClickListener;

    public UserInterfaceModule(){ }

    public UserInterfaceModule(RecipesAdapter.RecipeClickListener recipeClickListener){
        this.recipeClickListener = recipeClickListener;
    }

    public UserInterfaceModule(RecipesStepsAdapter.RecipeStepClickListener recipeStepClickListener) {
        this.recipeStepClickListener = recipeStepClickListener;
    }

    @Singleton
    @Provides
    public RecipesAdapter provideRecipesAdapter(Context context){
        return new RecipesAdapter(context, recipeClickListener);
    }

    @Singleton
    @Provides
    public RecipesStepsAdapter provideRecipeStepsAdapter(Context context){
        return new RecipesStepsAdapter(context, recipeStepClickListener);
    }

    @Singleton
    @Provides
    public IngredientsAdapter ingredientsAdapter(){
        return new IngredientsAdapter();
    }

    @Provides
    public GridLayoutManager provideGridLayoutManager(Context context){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, context.getResources().getInteger(R.integer.grid_number_of_columns));
        return gridLayoutManager;
    }

    @Provides
    public LinearLayoutManager provideLinearLayoutManager(Context context){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        return linearLayoutManager;
    }

    @Provides
    public RecipeStepDescriptionFragment provideRecipeStepDescriptionFragment(){
        return new RecipeStepDescriptionFragment();
    }

    @Provides
    public RecipeStepsFragment provideRecipeStepsFragment(){
        return new RecipeStepsFragment();
    }
}
