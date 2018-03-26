package com.udacity.backingapp.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.backingapp.R;
import com.udacity.backingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by federico.creti on 23/03/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {

    private List<Recipe> recipes;
    private Context context;

    private RecipeClickListener recipeClickListener;



    public RecipesAdapter(Context context, RecipeClickListener recipeClickListener){
        this.context = context;
        this.recipeClickListener = recipeClickListener;
    }

    public void swapRecipesList(List<Recipe> recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public interface RecipeClickListener {
        void onRecipeClick(int position);
    }

    @Override
    public RecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipe_card, parent, false);
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

        @BindView(R.id.recepie_name) TextView recepieTitleTV;


        public RecipesViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindRecipes(int position){
            recepieTitleTV.setText(recipes.get(position).getName());
        }

        @Override
        public void onClick(View view) {
            recipeClickListener.onRecipeClick(getAdapterPosition());
        }
    }
}
