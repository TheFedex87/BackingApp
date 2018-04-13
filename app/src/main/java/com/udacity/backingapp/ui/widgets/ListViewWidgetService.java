package com.udacity.backingapp.ui.widgets;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.backingapp.R;
import com.udacity.backingapp.model.Recipe;
import com.udacity.backingapp.ui.activities.RecipeDetail;

import java.util.List;

/**
 * Created by feder on 09/04/2018.
 */

public class ListViewWidgetService extends RemoteViewsService {
    private final static String TAG = ListViewWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Recipe recipe = null;
        int appWidgetId = -1;
        if (intent.hasExtra("BUNDLE")) {
            Bundle b = intent.getBundleExtra("BUNDLE");
            recipe = b.getParcelable("RECIPE");
            appWidgetId = b.getInt("APP_WIDGET_ID");
        }
        Log.d(TAG, "Creating adapter service");
        return new ListViewWidgetFactory(getApplicationContext(), recipe, appWidgetId);
    }
}

class ListViewWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private final static String TAG = ListViewWidgetFactory.class.getSimpleName();
    private final Context context;
    private Recipe recipe;
    int appWidgetId = -1;

    public ListViewWidgetFactory(Context context, Recipe recipe, int appWidgetId) {
        this.context = context;
        this.recipe = recipe;
        this.appWidgetId = appWidgetId;
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
        if (recipe == null) return 0;
        if (recipe.getIngredients() == null) return 0;
        return recipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = null;

        rv = new RemoteViews(context.getPackageName(), R.layout.single_line_ingredient_widget);
        rv.setTextViewText(R.id.widget_recipe_ingredient, recipe.getIngredients().get(i).getIngredient());
        rv.setTextViewText(R.id.widget_recipe_quantity, String.valueOf(recipe.getIngredients().get(i).getQuantity()));
        rv.setTextViewText(R.id.widget_recipe_measure, recipe.getIngredients().get(i).getMeasure());

        Intent openRecipeDetails = new Intent(context, RecipeDetail.class);
        openRecipeDetails.putExtra("recipe", recipe);
        rv.setOnClickFillInIntent(R.id.widget_recipe_ingredient, openRecipeDetails);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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
