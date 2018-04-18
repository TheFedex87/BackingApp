package com.udacity.backingapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.udacity.backingapp.R;
import com.udacity.backingapp.application.BackingAppApplication;
import com.udacity.backingapp.dagger.ApplicationModule;
import com.udacity.backingapp.dagger.DaggerNetworkComponent;
import com.udacity.backingapp.dagger.DaggerUserInterfaceComponent;
import com.udacity.backingapp.dagger.NetworkComponent;
import com.udacity.backingapp.dagger.UserInterfaceComponent;
import com.udacity.backingapp.dagger.UserInterfaceModule;
import com.udacity.backingapp.model.Recipe;
import com.udacity.backingapp.model.googleimagesearchmodels.GoogleImageRoot;
import com.udacity.backingapp.ui.activities.IdlingResource.SimpleIdlingResource;
import com.udacity.backingapp.ui.adapters.RecipesAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipeClickListener {
    @Inject
    Context context;

    private RecipesAdapter recipesAdapter;
    private List<Recipe> recipes = null;

    //UI Elements
    @BindView(R.id.recipes_list_container)
    RecyclerView recipesContainerList;

    private ApplicationModule applicationModule;
    private NetworkComponent networkComponent;

    private Parcelable layoutManagerState;

    @Nullable
    private SimpleIdlingResource simpleIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BackingAppApplication.appComponent().inject(this);

        ButterKnife.bind(this);

        applicationModule = new ApplicationModule(context);

        networkComponent = DaggerNetworkComponent.builder().applicationModule(applicationModule).build();

        if (savedInstanceState != null){
            if (savedInstanceState.containsKey("scroll_position")){
                layoutManagerState = savedInstanceState.getParcelable("scroll_position");
            }
        }

        initUi();

        //getIdlingResource();
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource(){
        if (simpleIdlingResource == null){
            simpleIdlingResource = new SimpleIdlingResource();
        }

        return simpleIdlingResource;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (simpleIdlingResource != null) {
            simpleIdlingResource.setIdleState(false);
        }


        //Retrieve the list of recipes from web using Retrofit
        networkComponent.getRecepiesApiInterface().recipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipes = response.body();
                recipesAdapter.swapRecipesList(recipes);
                if (layoutManagerState != null)
                    recipesContainerList.getLayoutManager().onRestoreInstanceState(layoutManagerState);
                layoutManagerState = null;

                if (simpleIdlingResource != null){
                    simpleIdlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });
    }

    private void initUi(){
        ApplicationModule applicationModule = new ApplicationModule(context);

        UserInterfaceComponent userInterfaceComponent = DaggerUserInterfaceComponent.builder()
                .applicationModule(applicationModule)
                .userInterfaceModule(new UserInterfaceModule(this))
                .build();
        recipesAdapter = userInterfaceComponent.getRecipesAdapter();
        recipesContainerList.setAdapter(userInterfaceComponent.getRecipesAdapter());
        recipesContainerList.setLayoutManager(userInterfaceComponent.getGridLayoutManager());
    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(this, RecipeDetail.class);
        intent.putExtra("recipe", recipes.get(position));

        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("scroll_position", ((GridLayoutManager)recipesContainerList.getLayoutManager()).onSaveInstanceState());
    }
}
