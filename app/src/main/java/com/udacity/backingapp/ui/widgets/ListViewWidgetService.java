package com.udacity.backingapp.ui.widgets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.backingapp.R;

import java.util.List;

/**
 * Created by feder on 09/04/2018.
 */

public class ListViewWidgetService extends RemoteViewsService {
    private final static String TAG = ListViewWidgetService.class.getSimpleName();



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
        return new ListViewWidgetFactory(getApplicationContext(), ingredients, appWidgetId);
    }
}

class ListViewWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private final static String TAG = ListViewWidgetFactory.class.getSimpleName();
    private final Context context;
    private List<String> ingredients;
    int appWidgetId = -1;

    public ListViewWidgetFactory(Context context, List<String> ingredients, int appWidgetId) {
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
        RemoteViews rv = null;
        if (i == 0){
            rv = new RemoteViews(context.getPackageName(), R.layout.single_line_recipe_name_widget);
            rv.setTextViewText(R.id.widget_recipe_title, ingredients.get(i));
        } else {
            rv = new RemoteViews(context.getPackageName(), R.layout.single_line_ingredient_widget);
            rv.setTextViewText(R.id.widget_recipe_ingredient, ingredients.get(i));
        }

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
