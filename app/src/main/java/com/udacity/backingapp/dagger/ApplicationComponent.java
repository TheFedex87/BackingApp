package com.udacity.backingapp.dagger;

import com.udacity.backingapp.ui.activities.MainActivity;
import com.udacity.backingapp.ui.activities.RecipeDetail;
import com.udacity.backingapp.ui.activities.RecipeStepDetail;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by federico.creti on 23/03/2018.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
    void inject(RecipeDetail recipeDetail);
    void inject(RecipeStepDetail recipeStepDetail);
}
