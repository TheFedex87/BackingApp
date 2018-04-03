package com.udacity.backingapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.backingapp.R;
import com.udacity.backingapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Federico on 24/03/2018.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {
    private List<Ingredient> ingredients;

    public void swapIngredients(List<Ingredient> ingredients){
        this.ingredients = ingredients;
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.ingredient, parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        holder.bindIngredient(position);
    }

    @Override
    public int getItemCount() {
        if (ingredients == null) return 0;
        return ingredients.size();
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ingredient_name)
        TextView ingredientName;

        @BindView(R.id.measure_tv)
        TextView measure;

        @BindView(R.id.quantity_tv)
        TextView quantity;

        public IngredientsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }

        public void bindIngredient(int position){
            this.ingredientName.setText(ingredients.get(position).getIngredient());
            this.measure.setText(ingredients.get(position).getMeasure());
            this.quantity.setText(String.valueOf(ingredients.get(position).getQuantity()));
        }
    }
}
