package com.udacity.backingapp.ui.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.udacity.backingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsWidget extends AppWidgetProvider {
    private static final String TAG = RecipeIngredientsWidget.class.getSimpleName();
    //private List<Recipe> recipes;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int[] appWidgetIds) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);
        Intent intent = new Intent(context, AdapterViewFlipperWidgetService.class);
        //Bundle b = new Bundle();
        //b.putParcelableArrayList("RECIPES_LIST", new ArrayList(recipes));
        //b.putInt("REC", 1);
        //intent.putExtras(b);

        //intent.putParcelableArrayListExtra("RECIPES_LIST", new ArrayList(recipes));
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

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        NetworkComponent networkComponent = DaggerNetworkComponent.builder().applicationModule(new ApplicationModule(context)).build();
//
//        networkComponent.getRecepiesApiInterface().recipes().enqueue(new Callback<List<Recipe>>() {
//            @Override
//            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
//                List<Recipe> recipes = response.body();
//                // There may be multiple widgets active, so update all of them
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Recipe>> call, Throwable t) {
//
//            }
//        });
        Log.d(TAG, "Updating widget");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, appWidgetIds);
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

