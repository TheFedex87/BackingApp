package com.udacity.backingapp.ui.widgets;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.backingapp.R;
import com.udacity.backingapp.dagger.ApplicationModule;
import com.udacity.backingapp.dagger.DaggerNetworkComponent;
import com.udacity.backingapp.dagger.NetworkComponent;
import com.udacity.backingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by feder on 09/04/2018.
 */

public class RecipeIngredientsWidgetService extends RemoteViewsService {
    private final static String TAG = RecipeIngredientsWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        //List<Recipe> recipes = intent.getParcelableArrayListExtra("RECIPES_LIST");
        Log.d(TAG, "Creating factory");
        return new RecipeIngredientsWidgetFactory(getApplicationContext());
    }
}

class RecipeIngredientsWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<Recipe> recipes;

    public RecipeIngredientsWidgetFactory(Context context) {
        this.context = context;
        Recipe r = new Recipe();
        r.setName("Test");

        recipes = new ArrayList<>();
        recipes.add(r);
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
        rv.setTextViewText(R.id.widget_recipe_title, recipes.get(i).getName());

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
