package com.udacity.backingapp.ui.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.udacity.backingapp.R;

/**
 * Created by federico.creti on 10/04/2018.
 */

public class RecipeIngredientsWidgetService extends IntentService {
    private static final String TAG = RecipeIngredientsWidgetService.class.getSimpleName();
    public static final String ACTION_MOVE_NEXT_RECIPE = "com.udacity.backingapp.ui.widgets.action.move_next_recipe";
    public static final String ACTION_MOVE_PREV_RECIPE = "com.udacity.backingapp.ui.widgets.action.move_prev_recipe";
    public static final String ACTION_CHANGE_RECIPE = "com.udacity.backingapp.ui.widgets.action.change_recipe";

    public RecipeIngredientsWidgetService(){
        super(RecipeIngredientsWidgetService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent.getAction().equals(ACTION_MOVE_NEXT_RECIPE)){
            int appWidgetId = intent.getIntExtra("APP_WIDGET_ID", 0);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
            RemoteViews rv = new RemoteViews(getApplicationContext().getPackageName(), R.layout.recipe_ingredients_widget);
            rv.showNext(R.id.widget_flipper);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        } else if(intent.getAction().equals(ACTION_MOVE_PREV_RECIPE)) {
            int appWidgetId = intent.getIntExtra("APP_WIDGET_ID", 0);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
            RemoteViews rv = new RemoteViews(getApplicationContext().getPackageName(), R.layout.recipe_ingredients_widget);
            rv.showPrevious(R.id.widget_flipper);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        } else if(intent.getAction().equals(ACTION_CHANGE_RECIPE)){
            Log.d(TAG, "Received action: " + ACTION_CHANGE_RECIPE);
        }
    }
}
