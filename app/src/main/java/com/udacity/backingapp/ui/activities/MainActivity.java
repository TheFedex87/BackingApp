package com.udacity.backingapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.udacity.backingapp.R;
import com.udacity.backingapp.application.BackingAppApplication;
import com.udacity.backingapp.dagger.ApplicationModule;
import com.udacity.backingapp.dagger.DaggerNetworkComponent;
import com.udacity.backingapp.dagger.DaggerUserInterfaceComponent;
import com.udacity.backingapp.dagger.UserInterfaceComponent;
import com.udacity.backingapp.dagger.UserInterfaceModule;
import com.udacity.backingapp.model.Recipe;
import com.udacity.backingapp.ui.adapters.RecipesAdapter;

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
    private List<Recipe> recipe = null;

    //UI Elements
    @BindView(R.id.recipes_list_container)
    RecyclerView recipesContainerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BackingAppApplication.appComponent().inject(this);

        ButterKnife.bind(this);

        initUi();

        DaggerNetworkComponent.builder().build().getRecepiesApiInterface().recipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipe = response.body();
                recipesAdapter.swapRecipesList(recipe);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });
    }

    private void initUi(){
        UserInterfaceComponent userInterfaceComponent = DaggerUserInterfaceComponent.builder()
                .applicationModule(new ApplicationModule(context))
                .userInterfaceModule(new UserInterfaceModule(this))
                .build();
        recipesAdapter = userInterfaceComponent.getRecipesAdapter();
        recipesContainerList.setAdapter(userInterfaceComponent.getRecipesAdapter());
        recipesContainerList.setLayoutManager(userInterfaceComponent.getGridLayoutManager());
    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(this, RecipeDetail.class);
        intent.putExtra("recipe", recipe.get(position));

        startActivity(intent);
    }
}
