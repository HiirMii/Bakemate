package com.example.android.bakemate.ui.list;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakemate.R;
import com.example.android.bakemate.di.Injectable;
import com.example.android.bakemate.model.Recipe;
import com.example.android.bakemate.repository.Resource;
import com.example.android.bakemate.ui.recipe.RecipeActivity;
import com.example.android.bakemate.viewmodel.RecipeCollectionViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment implements Injectable, RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String RECIPE_ID = "recipeId";
    private static final String RECIPE_NAME = "recipeName";

    @BindView(R.id.tb_list)
    Toolbar toolbar;
    @BindView(R.id.rv_recipes_list)
    RecyclerView recyclerView;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public ListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.app_name);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecipeCollectionViewModel recipeCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RecipeCollectionViewModel.class);
        recipeCollectionViewModel.getRecipesList().observe(this,
                recipes -> {
                    if (recipes != null) {
                        setRecipeListData(recipes);
                    }
                });
    }

    @Override
    public void onClick(int recipeId, String recipeName) {
        startRecipeActivity(recipeId, recipeName);
    }

    public void startRecipeActivity(Integer recipeId, String recipeName) {
        Activity container = getActivity();
        Intent i = new Intent(container, RecipeActivity.class);
        i.putExtra(RECIPE_ID, recipeId);
        i.putExtra(RECIPE_NAME, recipeName);
        startActivity(i);
    }

    public void setRecipeListData(Resource<List<Recipe>> recipeListData) {
        String checkDeviceWidth = getString(R.string.screen_type);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && checkDeviceWidth.equals("phone")) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
        } else if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && checkDeviceWidth.equals("phone")) ||
                (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && checkDeviceWidth.equals("tablet")) ||
                (checkDeviceWidth.equals("middle"))){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && checkDeviceWidth.equals("tablet")) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
            recyclerView.setLayoutManager(gridLayoutManager);
        }

        recyclerView.setHasFixedSize(true);
        RecipeAdapter recipeAdapter = new RecipeAdapter(this);
        recyclerView.setAdapter(recipeAdapter);
        recipeAdapter.setRecipeList(recipeListData.data);
    }
}
