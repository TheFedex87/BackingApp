package com.udacity.backingapp.dagger;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import com.udacity.backingapp.R;
import com.udacity.backingapp.ui.adapter.RecepiesAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by federico.creti on 23/03/2018.
 */
@Module
public class UserInterfaceModule {

    @Singleton
    @Provides
    public RecepiesAdapter provideRecepiesAdapter(Context context){
        return new RecepiesAdapter(context);
    }

    @Provides
    public GridLayoutManager provideGridLayoutManager(Context context){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, context.getResources().getInteger(R.integer.grid_number_of_columns));
        return gridLayoutManager;
    }


}
