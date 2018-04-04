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
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
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
import javax.inject.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

/**
 * Created by Federico on 24/03/2018.
 */

public class RecipeStepDescriptionFragment extends Fragment {
    private List<Ingredient> ingredients;
    private Step step;
    private boolean enableFullScreenOnLandscape = false;

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
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        BackingAppApplication.appComponent().inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //BackingAppApplication.appComponent().inject(this);

        View rootView = null;

        int width = 0;
        int height = 0;
        int orientation = 0;
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
                ingredientsContainer.setMinimumHeight(height - 20 - (int) (context.getResources().getDimension(R.dimen.steps_navigation_button_height)));
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
                    recipeStepDescription.setMinimumHeight(height - 20 - (int) (context.getResources().getDimension(R.dimen.steps_navigation_button_height)));
                }
            }

            recipeStepDescription.setText(step.getDescription());

            if ((step.getVideoURL() != null && !step.getVideoURL().isEmpty()) ||
                    (step.getThumbnailURL() != null && !step.getThumbnailURL().isEmpty() && step.getThumbnailURL().endsWith("mp4"))) {
                exoPlayerManager.initializePlayer(simpleExoPlayerView);

                Uri mediaUri = null;
                if (step.getVideoURL() != null && !step.getVideoURL().isEmpty())
                    mediaUri = Uri.parse(step.getVideoURL());
                else
                    mediaUri = Uri.parse(step.getThumbnailURL());

                try {
                    exoPlayerManager.setMediaAndPlay(mediaUri, getContext());
                } catch (Exception ex) {
                    //TODO: manage error
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
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayerManager != null)
            exoPlayerManager.ReleasePlayer();
    }
}
