package com.udacity.backingapp.ui.adapters;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.backingapp.BuildConfig;
import com.udacity.backingapp.R;
import com.udacity.backingapp.dagger.ApplicationModule;
import com.udacity.backingapp.dagger.DaggerNetworkComponent;
import com.udacity.backingapp.dagger.NetworkComponent;
import com.udacity.backingapp.dagger.UserInterfaceComponent;
import com.udacity.backingapp.model.Recipe;
import com.udacity.backingapp.model.googleimagesearchmodels.GoogleImage;
import com.udacity.backingapp.model.googleimagesearchmodels.GoogleImageRoot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by federico.creti on 23/03/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {
    private static final String IMAGE_DIRECTORY = "RecipeImages";


    private List<Recipe> recipes;
    private List<String> imagesUri;
    private Context context;

    private RecipeClickListener recipeClickListener;

    private NetworkComponent networkComponent;

    private UserInterfaceComponent userInterfaceComponent;

    public RecipesAdapter(Context context, RecipeClickListener recipeClickListener){
        this.context = context;
        this.recipeClickListener = recipeClickListener;

        networkComponent = DaggerNetworkComponent.builder().applicationModule(new ApplicationModule(context)).build();
    }

    public void swapRecipesList(List<Recipe> recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public void setImagesUri(List<String> imagesUri){
        this.imagesUri = imagesUri;
    }

    public interface RecipeClickListener {
        void onRecipeClick(int position);
    }

    @Override
    public RecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipe_tile, parent, false);
        RecipesViewHolder recipesViewHolder = new RecipesViewHolder(view);
        return recipesViewHolder;
    }

    @Override
    public void onBindViewHolder(RecipesViewHolder holder, int position) {
        holder.bindRecipes(position);
    }

    @Override
    public int getItemCount() {
        if (recipes == null) return 0;
        return recipes.size();
    }

    class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_name) TextView recipeTitleTV;
        @BindView(R.id.recipe_image) ImageView recipeImage;


        public RecipesViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindRecipes(int position){
            final String recipeName = recipes.get(position).getName();
            recipeTitleTV.setText(recipeName);

            bindImage(recipes.get(position));
        }

        public void onClick(View view) {
            recipeClickListener.onRecipeClick(getAdapterPosition());
        }

        private void bindImage(final Recipe recipe){
            if(recipe.getImage() != null && !recipe.getImage().isEmpty()){
                networkComponent.getPicasso().load(recipe.getImage()).fit().into(recipeImage);
            } else {

                final String recipeName = recipe.getName();
                String apyKey = BuildConfig.GOOGLE_SEARCH_API_KEY;

                ContextWrapper cw = new ContextWrapper(context);
                final File directory = cw.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE);
                final File myImageFile = new File(directory, recipeName);

                if (myImageFile.exists()) {
                    networkComponent.getPicasso().load(myImageFile).fit().into(recipeImage);
                } else if (!apyKey.isEmpty()) {
                    networkComponent.getGoogleImagesApiInterface().googleImages("https://www.googleapis.com/customsearch/v1?&cx=001116670235643120820:acj_gjncnsa&searchType=image&safe=high&imgSize=large&exactTerms=dessert&fileType=jpg",
                            recipeName,
                            apyKey)
                            .enqueue(new Callback<GoogleImageRoot>() {
                                @Override
                                public void onResponse(Call<GoogleImageRoot> call, Response<GoogleImageRoot> response) {
                                    GoogleImageRoot googleImageRoot = response.body();
                                    if (googleImageRoot != null && googleImageRoot.items != null) {
                                        String imagePath = googleImageRoot.items.get(0).getLink();
                                        picassoLoader(googleImageRoot.items, imagePath, directory, recipeName);
                                    } else {
                                        Timber.d("Google API calls expired.");
                                    }

                                }

                                @Override
                                public void onFailure(Call<GoogleImageRoot> call, Throwable t) {

                                }
                            });
                }
            }
        }


        //Method used to load a image into the view using Picasso
        private void picassoLoader(final List<GoogleImage> imagesList, final String imageUrl, final File directory, final String recipeName){
            networkComponent.getPicasso().load(imageUrl).fit()
                    .into(recipeImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            //After the image is loaded into the imageView, I save it into a file in order to avoid to reload it from internet every time
                            new AsyncTask<Void, Void, Void>(){

                                @Override
                                protected Void doInBackground(Void... voids) {
                                    final File myImageFile = new File(directory, recipeName); // Create image file
                                    Bitmap bitmap = ((BitmapDrawable)recipeImage.getDrawable()).getBitmap();
                                    FileOutputStream fos = null;
                                    try {
                                        myImageFile.getParentFile().mkdirs();
                                        fos = new FileOutputStream(myImageFile);
                                        fos.flush();
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } finally {
                                        try {
                                            if (fos != null)
                                                fos.close();

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                }
                            }.execute();

                        }

                        @Override
                        public void onError() {
                            //If the first image found by google has some problems and cannot be loaded I try with the second result of google if also this result has problems, I keep
                            //the default image
                            if (imageUrl != imagesList.get(1).getLink())
                                picassoLoader(imagesList, imagesList.get(1).getLink(), directory, recipeName);
                        }
                    });
        }
    }
}
