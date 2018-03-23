package com.udacity.backingapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.backingapp.R;
import com.udacity.backingapp.model.Recepie;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by federico.creti on 23/03/2018.
 */

public class RecepiesAdapter extends RecyclerView.Adapter<RecepiesAdapter.RecepiesViewHolder> {

    private List<Recepie> recepies;
    private Context context;

    public RecepiesAdapter(Context context){
        this.context = context;
    }

    public void swapRecepiesList(List<Recepie> recepies){
        this.recepies = recepies;
        notifyDataSetChanged();
    }

    @Override
    public RecepiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recepie_card, parent, false);
        RecepiesViewHolder recepiesViewHolder = new RecepiesViewHolder(view);
        return recepiesViewHolder;
    }

    @Override
    public void onBindViewHolder(RecepiesViewHolder holder, int position) {
        holder.bindRecepie(position);
    }

    @Override
    public int getItemCount() {
        if (recepies == null) return 0;
        return recepies.size();
    }

    class RecepiesViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.recepie_name) TextView recepieTitleTV;

        public RecepiesViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bindRecepie(int position){
            recepieTitleTV.setText(recepies.get(position).getName());
        }
    }
}
