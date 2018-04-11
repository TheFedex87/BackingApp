package com.udacity.backingapp.ui.widgets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.backingapp.R;
import com.udacity.backingapp.model.Ingredient;
import com.udacity.backingapp.model.Recipe;

import java.util.List;

/**
 * Created by federico.creti on 11/04/2018.
 */

public class ListViewWidgetService extends RemoteViewsService {
    private static final String TAG = ListViewWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "Loading ListViewWidgetService");
        List<Ingredient> ingredients = null;
        if (intent.hasExtra("BUNDLE_INGREDIENTS")) {
            Bundle b = intent.getBundleExtra("BUNDLE_INGREDIENTS");
            ingredients = b.getParcelableArrayList("INGREDIENTS_LIST");
        }
        return new ListViewWidgetFactory(getApplicationContext(), ingredients);
    }
}

class ListViewWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<Ingredient> ingredients;

    public ListViewWidgetFactory(Context context, List<Ingredient> ingredients){
        this.context = context;
        this.ingredients = ingredients;
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
        return 1;
        //if (ingredients == null) return 0;
        //return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);
        rv.setTextViewText(R.id.ingredient_name_widget, "PDPDPD");

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
