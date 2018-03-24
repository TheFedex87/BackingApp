package com.udacity.backingapp.dagger;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.udacity.backingapp.ui.adapters.IngredientsAdapter;
import com.udacity.backingapp.ui.adapters.RecipesStepsAdapter;
import com.udacity.backingapp.ui.adapters.RecipesAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by federico.creti on 23/03/2018.
 */
@Singleton
@Component(modules = { ApplicationModule.class, UserInterfaceModule.class })
public interface UserInterfaceComponent {
    RecipesAdapter getRecipesAdapter();
    RecipesStepsAdapter getRecipeStepsAdapter();
    IngredientsAdapter getIngredientsAdapter();

    GridLayoutManager getGridLayoutManager();
    LinearLayoutManager getLinearLayoutManager();
}
