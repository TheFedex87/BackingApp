package com.udacity.backingapp.ui.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.udacity.backingapp.R;
import com.udacity.backingapp.dagger.ApplicationModule;
import com.udacity.backingapp.dagger.DaggerNetworkComponent;
import com.udacity.backingapp.dagger.NetworkComponent;
import com.udacity.backingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsWidget extends AppWidgetProvider {
    private List<Recipe> recipes;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, List<Recipe> recipes) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);
        Intent intent = new Intent(context, RecipeIngredientsWidgetService.class);
        //intent.putParcelableArrayListExtra("RECIPES_LIST", new ArrayList(recipes));
        views.setRemoteAdapter(R.id.widget_flipper, intent);

        views.setDisplayedChild(R.id.widget_flipper, 0);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        /*NetworkComponent networkComponent = DaggerNetworkComponent.builder().applicationModule(new ApplicationModule(context)).build();

        networkComponent.getRecepiesApiInterface().recipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipes = response.body();
                // There may be multiple widgets active, so update all of them

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });*/
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipes);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

