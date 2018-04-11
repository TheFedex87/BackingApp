package com.udacity.backingapp.ui.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.udacity.backingapp.R;
import com.udacity.backingapp.dagger.ApplicationModule;
import com.udacity.backingapp.dagger.DaggerNetworkComponent;
import com.udacity.backingapp.dagger.NetworkComponent;
import com.udacity.backingapp.model.Ingredient;
import com.udacity.backingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsWidget extends AppWidgetProvider {
    private static final String TAG = RecipeIngredientsWidget.class.getSimpleName();
    //private List<Recipe> recipes;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int[] appWidgetIds, List<Recipe> recipes) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);
        Intent intent = new Intent(context, AdapterViewFlipperWidgetService.class);
        Bundle b = new Bundle();
        b.putParcelableArrayList("RECIPES_LIST", new ArrayList(recipes));
        b.putInt("APP_WIDGET_ID", appWidgetId);
        intent.putExtra("BUNDLE_RECIPES", b);
        views.setRemoteAdapter(R.id.widget_flipper, intent);
        Log.d(TAG, "Setting remote adapter");

        Intent navigateRecipeIntent = new Intent(context, RecipeIngredientsWidgetService.class);
        navigateRecipeIntent.setAction(RecipeIngredientsWidgetService.ACTION_MOVE_NEXT_RECIPE);
        navigateRecipeIntent.putExtra("APP_WIDGET_ID", appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, navigateRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_next_view, pendingIntent);

        navigateRecipeIntent = new Intent(context, RecipeIngredientsWidgetService.class);
        navigateRecipeIntent.setAction(RecipeIngredientsWidgetService.ACTION_MOVE_PREV_RECIPE);
        navigateRecipeIntent.putExtra("APP_WIDGET_ID", appWidgetId);
        pendingIntent = PendingIntent.getService(context, 0, navigateRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_prev_view, pendingIntent);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_flipper);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }



//    public static void updateIngredientsList(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, int appWidgetId, List<Ingredient> ingredients){
//        //List<Recipe> recipes = new ArrayList<>();
//        updateAppWidget(context, appWidgetManager, appWidgetId, appWidgetIds, null, ingredients);
//
//    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        NetworkComponent networkComponent = DaggerNetworkComponent.builder().applicationModule(new ApplicationModule(context)).build();

        networkComponent.getRecepiesApiInterface().recipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();
                // There may be multiple widgets active, so update all of them
                for (int appWidgetId : appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId, appWidgetIds, recipes);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });
//        Log.d(TAG, "Updating widget");
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId, appWidgetIds);
//        }

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

