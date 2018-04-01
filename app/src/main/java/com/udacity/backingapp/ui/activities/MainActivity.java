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
import com.udacity.backingapp.dagger.NetworkComponent;
import com.udacity.backingapp.dagger.UserInterfaceComponent;
import com.udacity.backingapp.dagger.UserInterfaceModule;
import com.udacity.backingapp.model.Recipe;
import com.udacity.backingapp.model.googleimagesearchmodels.GoogleImageRoot;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BackingAppApplication.appComponent().inject(this);

        ButterKnife.bind(this);

        applicationModule = new ApplicationModule(context);

        networkComponent = DaggerNetworkComponent.builder().applicationModule(applicationModule).build();

        initUi();

        networkComponent.getRecepiesApiInterface().recipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipes = response.body();

                /*String apyKey = "AIzaSyDZ2lYm5KXgL1XuxsG2EfNYfEiOqZzkZW4";

                final List<String> imagesUrl = new ArrayList<>();
                for(Recipe recipe : recipes){
                    networkComponent.getGoogleImagesApiInterface().googleImages("https://www.googleapis.com/customsearch/v1?&cx=001116670235643120820:acj_gjncnsa&searchType=image&exactTerms=dessert&fileType=jpg", recipe.getName(), apyKey).enqueue(new Callback<GoogleImageRoot>() {
                        @Override
                        public void onResponse(Call<GoogleImageRoot> call, Response<GoogleImageRoot> response) {
                            GoogleImageRoot googleImageRoot = response.body();
                            imagesUrl.add(googleImageRoot.items.get(0).getLink());
                        }

                        @Override
                        public void onFailure(Call<GoogleImageRoot> call, Throwable t) {

                        }
                    });
                }*/

                //recipesAdapter.setImagesUri(imagesUrl);
                recipesAdapter.swapRecipesList(recipes);
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
}
