package com.udacity.backingapp.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.udacity.backingapp.R;
import com.udacity.backingapp.application.BackingAppApplication;
import com.udacity.backingapp.dagger.ApplicationModule;
import com.udacity.backingapp.dagger.DaggerNetworkComponent;
import com.udacity.backingapp.dagger.DaggerUserInterfaceComponent;
import com.udacity.backingapp.exoplayer.ExoPlayerManager;
import com.udacity.backingapp.model.Ingredient;
import com.udacity.backingapp.model.Step;
import com.udacity.backingapp.ui.adapters.IngredientsAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Federico on 24/03/2018.
 */

public class RecipeStepDescriptionFragment extends Fragment {
    private List<Ingredient> ingredients;
    private Step step;
    private boolean enableFullScreenOnLandscape = false;

    private long playerPosition;
    private boolean playerPlayWhenReady;

    @Nullable @BindView(R.id.step_container)
    ScrollView stepContainer;

    @Nullable @BindView(R.id.ingredients_container)
    RecyclerView ingredientsContainer;

    @Nullable @BindView(R.id.step_description)
    TextView recipeStepDescription;

    @Nullable @BindView(R.id.playerView)
    SimpleExoPlayerView simpleExoPlayerView;

    @Nullable @BindView(R.id.recipe_step_image_container)
    ImageView recipeStepImageContainer;

    @Inject
    Context context;

    @Inject
    ExoPlayerManager exoPlayerManager;

    public RecipeStepDescriptionFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        BackingAppApplication.appComponent().inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = null;

        int width = 0;
        int height = 0;
        int orientation = 0;
        //If we are on a phone (or a small screen device) I save the information of screen orientation, width and height in order to resize the ExoPlayerView at full screen in landscape mode
        if (enableFullScreenOnLandscape) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            width = metrics.widthPixels;
            height = metrics.heightPixels;
            orientation = getResources().getConfiguration().orientation;
        }

        if(ingredients != null){
            rootView = inflater.inflate(R.layout.recipe_ingredients, container, false);

            ButterKnife.bind(this, rootView);

            if(orientation == 2 && enableFullScreenOnLandscape) {
                ingredientsContainer.setMinimumHeight(height - 30 - (int) (context.getResources().getDimension(R.dimen.steps_navigation_button_height)));
            }

            IngredientsAdapter ingredientsAdapter = DaggerUserInterfaceComponent.builder().applicationModule(new ApplicationModule(context)).build().getIngredientsAdapter();
            ingredientsContainer.setAdapter(ingredientsAdapter);
            ingredientsContainer.setLayoutManager(DaggerUserInterfaceComponent.builder().applicationModule(new ApplicationModule(context)).build().getLinearLayoutManager());
            ingredientsAdapter.swapIngredients(ingredients);
        } else if(step != null){
            rootView = inflater.inflate(R.layout.recipe_step, container, false);

            ButterKnife.bind(this, rootView);

            if(orientation == 2 && enableFullScreenOnLandscape) {
                if ((step.getVideoURL() != null && !step.getVideoURL().isEmpty()) ||
                        (step.getThumbnailURL() != null && !step.getThumbnailURL().isEmpty() && step.getThumbnailURL().endsWith("mp4"))) {
                    simpleExoPlayerView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                } else if (step.getThumbnailURL() != null && !step.getThumbnailURL().isEmpty()) {
                    recipeStepImageContainer.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                } else {
                    recipeStepDescription.setMinimumHeight(height - 30 - (int) (context.getResources().getDimension(R.dimen.steps_navigation_button_height)));
                }
            }

            recipeStepDescription.setText(step.getDescription());

            if ((step.getVideoURL() != null && !step.getVideoURL().isEmpty())) {
                exoPlayerManager.initializePlayer(simpleExoPlayerView);

                boolean playWhenReady = true;
                if (savedInstanceState != null) {
                    if (savedInstanceState.containsKey("PLAYER_POSITION")) {
                        exoPlayerManager.setCurrentPosition(savedInstanceState.getLong("PLAYER_POSITION"));
                    }
                    if (savedInstanceState.containsKey("PLAYER_PLAY_WHEN_READY")){
                        playWhenReady = savedInstanceState.getBoolean("PLAYER_PLAY_WHEN_READY");
                    }
                }

                Uri mediaUri = Uri.parse(step.getVideoURL());

                try {
                    exoPlayerManager.setMediaAndState(mediaUri, getContext(), playWhenReady);
                } catch (Exception ex) {
                    Timber.e("Error loading media: " + ex.getMessage());
                    Toast.makeText(context, "Error playing media", Toast.LENGTH_LONG).show();
                }
            } else{
                simpleExoPlayerView.setVisibility(View.GONE);
            }

            if(step.getThumbnailURL() != null && !step.getThumbnailURL().isEmpty() && !step.getThumbnailURL().endsWith("mp4")){
                DaggerNetworkComponent.builder().applicationModule(new ApplicationModule(context)).build()
                        .getPicasso().load(Uri.parse(step.getThumbnailURL())).fit().into(recipeStepImageContainer);
            } else{
                recipeStepImageContainer.setVisibility(View.GONE);
            }
        }


        return rootView;

    }

    public void setStep(@NonNull Step step){
        //If we are set a step this meas we are not passing ingredients, so we put them to null
        ingredients = null;
        this.step = step;
    }

    public void setIngredients(@NonNull List<Ingredient> ingredients){
        //If we are set the ingredients this meas we are not passing a step, so we put it to null
        step = null;
        this.ingredients = ingredients;
    }

    public void setEnableFullScreenOnLandscape(boolean enableFullScreenOnLandscape){
        this.enableFullScreenOnLandscape = enableFullScreenOnLandscape;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (exoPlayerManager != null) {
            outState.putLong("PLAYER_POSITION", playerPosition);
            outState.putBoolean("PLAYER_PLAY_WHEN_READY", playerPlayWhenReady);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayerManager != null) {
            playerPlayWhenReady = exoPlayerManager.getPlayWhenReady();
            playerPosition = exoPlayerManager.getCurrentPosition();
            exoPlayerManager.ReleasePlayer();
        }
    }
}
