package com.udacity.backingapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.backingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Federico on 24/03/2018.
 */

public class RecipesStepsAdapter extends RecyclerView.Adapter<RecipesStepsAdapter.RecipeStepsViewHolder> {
    private Context context;
    private List<String> recepySteps;

    private RecipeStepClickListener recipeStepClickListener;

    private int selectedPosition = RecyclerView.NO_POSITION;

    private boolean highLightSelected = false;

    public RecipesStepsAdapter(Context context, RecipeStepClickListener recipeStepClickListener){
        this.context = context;
        this.recipeStepClickListener = recipeStepClickListener;
    }

    public void swapRecipeSteps(List<String> recepySteps){
        this.recepySteps = recepySteps;
        notifyDataSetChanged();
    }

    public interface RecipeStepClickListener{
        void onRecipeStepClick(int position);
    }

    public void setHighLightSelected(boolean highLightSelected) {
        this.highLightSelected = highLightSelected;
    }

    public void setSelectedPosition(int selectedPosition){
        this.selectedPosition = selectedPosition;
    }

    @Override
    public RecipeStepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.recipe_step_button, parent, false);

        return new RecipeStepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeStepsViewHolder holder, int position) {
        holder.bindStep(position);

        if(highLightSelected)
            holder.recipeStepDescription.setBackgroundColor(selectedPosition == position ? context.getResources().getColor(R.color.colorAccent) : context.getResources().getColor(R.color.colorPrimaryLight));
    }

    @Override
    public int getItemCount() {
        if (recepySteps == null) return 0;
        return recepySteps.size();
    }

    class RecipeStepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.recipe_step_description) TextView recipeStepDescription;

        public RecipeStepsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindStep(int position){
            recipeStepDescription.setText(recepySteps.get(position));
            if(position == selectedPosition) {
                if(highLightSelected) {
                    recipeStepDescription.setBackgroundColor(selectedPosition == position ? context.getResources().getColor(R.color.colorAccent) : context.getResources().getColor(R.color.colorPrimaryLight));
                    recipeStepClickListener.onRecipeStepClick(getAdapterPosition());
                }
            }
        }

        @Override
        public void onClick(View view) {
            if (highLightSelected) {
                //Remove background selected color from previous button
                notifyItemChanged(selectedPosition);
                //Save selected position
                selectedPosition = getAdapterPosition();
                //Apply background selected color to new selected position
                notifyItemChanged(selectedPosition);
            }

            recipeStepClickListener.onRecipeStepClick(getAdapterPosition());
        }
    }
}
