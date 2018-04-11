package com.udacity.backingapp.ui.widgets;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import java.util.Random;

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
        List<String> ingredients = null;
        int appWidgetId = -1;
        if (intent.hasExtra("BUNDLE")) {
            Bundle b = intent.getBundleExtra("BUNDLE");
            ingredients = b.getStringArrayList("INGREDIENTS_LIST");
            appWidgetId = b.getInt("APP_WIDGET_ID");
        }
        Log.d(TAG, "Creating adapter service");
        return new AdapterViewFlipperWidgetFactory(getApplicationContext(), ingredients, appWidgetId);
    }
}

class AdapterViewFlipperWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private final static String TAG = AdapterViewFlipperWidgetFactory.class.getSimpleName();
    private final Context context;
    private List<String> ingredients;
    int appWidgetId = -1;

    public AdapterViewFlipperWidgetFactory(Context context, List<String> ingredients, int appWidgetId) {
        this.context = context;
        this.ingredients = ingredients;
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
        if (ingredients == null) return 0;
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.single_recipe_widget);
        rv.setTextViewText(R.id.widget_recipe_title, ingredients.get(i));

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
