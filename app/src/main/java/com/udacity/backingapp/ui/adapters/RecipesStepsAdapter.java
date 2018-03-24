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

public class RecipesStepsAdapter extends RecyclerView.Adapter<RecipesStepsAdapter.RecepyStepsViewHolder> {
    private Context context;
    private List<String> recepySteps;

    private RecipeStepClickListener recipeStepClickListener;

    public RecipesStepsAdapter(Context context, RecipeStepClickListener recipeStepClickListener){
        this.context = context;
        this.recipeStepClickListener = recipeStepClickListener;
    }

    public void swapRecepySteps(List<String> recepySteps){
        this.recepySteps = recepySteps;
        this.recepySteps.add(0, context.getString(R.string.recepy_ingredients));
        notifyDataSetChanged();
    }

    public interface RecipeStepClickListener{
        void onRecipeStepClick(int position);
    }

    @Override
    public RecepyStepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.recipe_step_button, parent, false);

        return new RecepyStepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecepyStepsViewHolder holder, int position) {
        holder.bindStep(position);
    }

    @Override
    public int getItemCount() {
        if (recepySteps == null) return 0;
        return recepySteps.size();
    }

    class RecepyStepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.recipe_step_description) TextView recepyStepDescription;

        public RecepyStepsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindStep(int position){
            recepyStepDescription.setText(recepySteps.get(position));
        }

        @Override
        public void onClick(View view) {
            recipeStepClickListener.onRecipeStepClick(getAdapterPosition());
        }
    }
}
