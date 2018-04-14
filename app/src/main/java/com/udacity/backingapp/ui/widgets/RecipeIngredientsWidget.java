package com.udacity.backingapp.ui.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.udacity.backingapp.R;
import com.udacity.backingapp.dagger.ApplicationModule;
import com.udacity.backingapp.dagger.DaggerNetworkComponent;
import com.udacity.backingapp.dagger.NetworkComponent;
import com.udacity.backingapp.model.Recipe;
import com.udacity.backingapp.ui.activities.MainActivity;
import com.udacity.backingapp.ui.activities.RecipeDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsWidget extends AppWidgetProvider {
    private static final String TAG = RecipeIngredientsWidget.class.getSimpleName();
    //Memorize the current recipe for every widget loaded on screen
    private static Map<Integer, Integer> currentRecipes = new HashMap<>();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int[] appWidgetIds, List<Recipe> recipes, int currentRecipeId) {

        Recipe recipe = recipes.get(currentRecipeId);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);

        Intent intent = new Intent(context, ListViewWidgetService.class);
        Bundle b = new Bundle();
        b.putParcelable("RECIPE", recipe);
        b.putInt("APP_WIDGET_ID", appWidgetId);
        intent.putExtra("BUNDLE", b);
        Random rnd = new Random();
        intent.setData(Uri.fromParts("content", String.valueOf(rnd.nextInt()), null));
        views.setRemoteAdapter(R.id.widget_list_view, intent);
        Log.d(TAG, "Setting remote adapter");

        Intent navigateRecipeIntent = new Intent(context, RecipeIngredientsWidgetService.class);
        navigateRecipeIntent.setAction(RecipeIngredientsWidgetService.ACTION_MOVE_NEXT_RECIPE);
        Bundle nextBundle = new Bundle();
        nextBundle.putParcelableArrayList("RECIPES_LIST", new ArrayList(recipes));
        nextBundle.putInt("APP_WIDGET_ID", appWidgetId);
        int nextRecipeId = currentRecipeId + 1;
        if (nextRecipeId >= recipes.size())
            nextRecipeId = 0;
        nextBundle.putInt("CURRENT_RECIPE_ID", nextRecipeId);
        navigateRecipeIntent.putExtra("BUNDLE", nextBundle);
        PendingIntent pendingIntent = PendingIntent.getService(context, appWidgetId, navigateRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_next_view, pendingIntent);

        navigateRecipeIntent = new Intent(context, RecipeIngredientsWidgetService.class);
        navigateRecipeIntent.setAction(RecipeIngredientsWidgetService.ACTION_MOVE_PREV_RECIPE);
        Bundle prevBundle = new Bundle();
        prevBundle.putParcelableArrayList("RECIPES_LIST", new ArrayList(recipes));
        prevBundle.putInt("APP_WIDGET_ID", appWidgetId);
        int prevRecipeId = currentRecipeId - 1;
        if (prevRecipeId == -1)
            prevRecipeId = recipes.size() - 1;
        prevBundle.putInt("CURRENT_RECIPE_ID", prevRecipeId);
        navigateRecipeIntent.putExtra("BUNDLE", prevBundle);
        pendingIntent = PendingIntent.getService(context, appWidgetId, navigateRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_prev_view, pendingIntent);

        Intent openRecipeDetails = new Intent(context, RecipeDetail.class);
        PendingIntent recipeDetailPendingIntent = PendingIntent.getActivity(context, 0, openRecipeDetails, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, recipeDetailPendingIntent);

        views.setTextViewText(R.id.widget_recipe_name, recipe.getName());
        Intent recipeListIntent = new Intent(context, MainActivity.class);
        PendingIntent recipeListPendingIntent = PendingIntent.getActivity(context, 0, recipeListIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_recipe_name, recipeListPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void recipeChange(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, int appWidgetId, List<Recipe> recipes, int currentRecipeId){
        for (int appWidgetIdLocal : appWidgetIds){
            if (appWidgetIdLocal == appWidgetId){
                currentRecipes.put(appWidgetId, currentRecipeId);
                updateAppWidget(context, appWidgetManager, appWidgetId, appWidgetIds, recipes, currentRecipeId);
            }
        }
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        NetworkComponent networkComponent = DaggerNetworkComponent.builder().applicationModule(new ApplicationModule(context)).build();

        networkComponent.getRecepiesApiInterface().recipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();
                // There may be multiple widgets active, so update all of them
                for (int appWidgetId : appWidgetIds) {
                    int currentRecipeId = 0;
                    if (currentRecipes.containsKey(appWidgetId)) {
                        currentRecipeId = currentRecipes.get(appWidgetId);
                    }
                    updateAppWidget(context, appWidgetManager, appWidgetId, appWidgetIds, recipes, currentRecipeId);
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

