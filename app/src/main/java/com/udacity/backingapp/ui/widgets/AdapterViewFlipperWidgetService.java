package com.udacity.backingapp.ui.widgets;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.backingapp.R;
import com.udacity.backingapp.dagger.ApplicationModule;
import com.udacity.backingapp.dagger.DaggerNetworkComponent;
import com.udacity.backingapp.dagger.NetworkComponent;
import com.udacity.backingapp.model.Ingredient;
import com.udacity.backingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by feder on 09/04/2018.
 */

public class AdapterViewFlipperWidgetService extends RemoteViewsService {
    private final static String TAG = AdapterViewFlipperWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        //List<Recipe> recipes = intent.getParcelableArrayListExtra("RECIPES_LIST");
        Log.d(TAG, "Creating adapter service");

        return new AdapterViewFlipperWidgetFactory(getApplicationContext());
    }
}

class AdapterViewFlipperWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private final static String TAG = AdapterViewFlipperWidgetFactory.class.getSimpleName();
    private final Context context;
    private List<Recipe> recipes;

    public AdapterViewFlipperWidgetFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Creating adapter factory");
        NetworkComponent networkComponent = DaggerNetworkComponent.builder().applicationModule(new ApplicationModule(context)).build();
        networkComponent.getRecepiesApiInterface().recipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipes = response.body();
                // There may be multiple widgets active, so update all of them
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeIngredientsWidget.class)), R.id.widget_flipper);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (recipes == null) return 0;
        return recipes.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.single_recipe_widget);
        rv.setTextViewText(R.id.widget_recipe_title, recipes.get(i).getName());

        String ingredients = "";
        for (Ingredient ingredient : recipes.get(i).getIngredients()) {
            ingredients += ingredient.getIngredient() + "\n";
        }

        rv.setTextViewText(R.id.widget_ingredients_list, ingredients);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
