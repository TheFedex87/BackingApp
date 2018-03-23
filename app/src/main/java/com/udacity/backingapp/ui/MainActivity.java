package com.udacity.backingapp.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.udacity.backingapp.R;
import com.udacity.backingapp.application.BackingAppApplication;
import com.udacity.backingapp.dagger.ApplicationModule;
import com.udacity.backingapp.dagger.DaggerApplicationComponent;
import com.udacity.backingapp.dagger.DaggerNetworkComponent;
import com.udacity.backingapp.dagger.DaggerUserInterfaceComponent;
import com.udacity.backingapp.dagger.UserInterfaceComponent;
import com.udacity.backingapp.model.Recepie;
import com.udacity.backingapp.retrofit.RecepiesApiInterface;
import com.udacity.backingapp.ui.adapter.RecepiesAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    @Inject
    Context context;

    RecepiesAdapter recepiesAdapter;

    //UI Elements
    @BindView(R.id.recepies_list_container)
    RecyclerView recepiesContainerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BackingAppApplication.appComponent().inject(this);

        ButterKnife.bind(this);

        initUi();

        DaggerNetworkComponent.builder().build().getRecepiesApiInterface().recepies().enqueue(new Callback<List<Recepie>>() {
            @Override
            public void onResponse(Call<List<Recepie>> call, Response<List<Recepie>> response) {
                List<Recepie> recepies = response.body();
                recepiesAdapter.swapRecepiesList(recepies);
                //DaggerUserInterfaceComponent.builder().applicationModule(new ApplicationModule(context)).build().getRecepiesAdapter().swapRecepiesList(recepies);
            }

            @Override
            public void onFailure(Call<List<Recepie>> call, Throwable t) {

            }
        });
    }

    private void initUi(){
        UserInterfaceComponent userInterfaceComponent = DaggerUserInterfaceComponent.builder().applicationModule(new ApplicationModule(context)).build();
        recepiesAdapter = userInterfaceComponent.getRecepiesAdapter();
        recepiesContainerList.setAdapter(userInterfaceComponent.getRecepiesAdapter());
        recepiesContainerList.setLayoutManager(userInterfaceComponent.getGridLayoutManager());
    }
}
