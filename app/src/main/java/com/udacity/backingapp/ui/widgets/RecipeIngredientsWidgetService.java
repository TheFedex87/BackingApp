package com.udacity.backingapp.ui.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.udacity.backingapp.R;
import com.udacity.backingapp.model.Recipe;

import java.util.List;

/**
 * Created by federico.creti on 10/04/2018.
 */

public class RecipeIngredientsWidgetService extends IntentService {
    private static final String TAG = RecipeIngredientsWidgetService.class.getSimpleName();
    public static final String ACTION_MOVE_NEXT_RECIPE = "com.udacity.backingapp.ui.widgets.action.move_next_recipe";
    public static final String ACTION_MOVE_PREV_RECIPE = "com.udacity.backingapp.ui.widgets.action.move_prev_recipe";

    public RecipeIngredientsWidgetService(){
        super(RecipeIngredientsWidgetService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent.getAction().equals(ACTION_MOVE_NEXT_RECIPE) || intent.getAction().equals(ACTION_MOVE_PREV_RECIPE)){
            Bundle b = intent.getBundleExtra("BUNDLE");
            List<Recipe> recipes = b.getParcelableArrayList("RECIPES_LIST");
            int currentRecipeId = b.getInt("CURRENT_RECIPE_ID", 0);
            int appWidgetId = b.getInt("APP_WIDGET_ID", 0);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
            RemoteViews rv = new RemoteViews(getApplicationContext().getPackageName(), R.layout.recipe_ingredients_widget);
            RecipeIngredientsWidget.recipeChange(this,
                    appWidgetManager,
                    appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeIngredientsWidget.class)),
                    appWidgetId,
                    recipes,
                    currentRecipeId);
        }
    }
}
