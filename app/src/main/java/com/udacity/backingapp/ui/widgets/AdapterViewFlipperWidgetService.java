package com.udacity.backingapp.ui.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

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
 * Created by feder on 09/04/2018.
 */

public class AdapterViewFlipperWidgetService extends RemoteViewsService {
    private final static String TAG = AdapterViewFlipperWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        List<String> recipes = null;
        if (intent.hasExtra("BUNDLE_RECIPES")) {
            Bundle b = intent.getBundleExtra("BUNDLE_RECIPES");
            recipes = b.getStringArrayList("RECIPES_LIST");
        }
        Log.d(TAG, "Creating adapter service");
        return new AdapterViewFlipperWidgetFactory(getApplicationContext(), recipes);
    }
}

class AdapterViewFlipperWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private final static String TAG = AdapterViewFlipperWidgetFactory.class.getSimpleName();
    private final Context context;
    private List<String> recipes;

    public AdapterViewFlipperWidgetFactory(Context context, List<String> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public void onCreate() {

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
        rv.setTextViewText(R.id.widget_recipe_title, recipes.get(i));

//        Intent intent = new Intent(context, ListViewWidgetService.class);
//        Bundle b = new Bundle();
//        b.putParcelableArrayList("INGREDIENTS_LIST", new ArrayList(recipes.get(i).getIngredients()));
//        intent.putExtra("BUNDLE_INGREDIENTS", b);
//        rv.setRemoteAdapter(R.id.widget_ingredients_list, intent);
//        Log.d(TAG, "Setting remote ListViewWidgetService");
//        rv.setEmptyView(R.id.widget_ingredients_list, R.id.widget_empty_ingredients);

//        Intent intent = new Intent(context, RecipeIngredientsWidgetService.class);
//        intent.setAction(RecipeIngredientsWidgetService.ACTION_CHANGE_RECIPE);
//        Bundle b = new Bundle();
//        b.putParcelableArrayList("INGREDIENTS_LIST", new ArrayList(recipes.get(i).getIngredients()));
//        intent.putExtra("BUNDLE_INGREDIENTS", b);
//        context.startService(intent);
//
//        Log.d(TAG, "Number of ingredients: " + recipes.get(i).getIngredients().size());

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
