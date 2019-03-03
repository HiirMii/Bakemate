package com.example.android.bakemate.ui.recipe;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakemate.R;
import com.example.android.bakemate.di.Injectable;
import com.example.android.bakemate.model.Recipe;
import com.example.android.bakemate.model.Step;
import com.example.android.bakemate.util.Images;
import com.example.android.bakemate.viewmodel.RecipeViewModel;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeFragment extends Fragment implements Injectable, StepAdapter.StepAdapterOnClickHandler {

    private static final String RECIPE_ID = "recipeId";

    Integer recipeId;

    boolean isTwoPane;

    @BindView(R.id.appbar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.tb_recipe)
    Toolbar toolbar;
    @BindView(R.id.iv_backDrop)
    ImageView backdropImage;
    @BindView(R.id.sv_image)
    ImageView scrollViewImage;
    @BindView(R.id.tv_ingredients_label)
    TextView ingredientsLabel;
    @BindView(R.id.rv_ingredients_list)
    RecyclerView ingredientsRecyclerView;
    @BindView(R.id.tv_steps_label)
    TextView stepsLabel;
    @BindView(R.id.rv_steps_list)
    RecyclerView stepsRecyclerView;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public interface OnRecipeClickListener {
        void onRecipeSelected(int StepId, Step step, int stepListLength);
    }

    OnRecipeClickListener callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (OnRecipeClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement OnRecipeClickListener");
        }
    }

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(getActivity()).getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isTwoPane = getResources().getBoolean(R.bool.isTwoPane);

        if (isTwoPane) {
            appBarLayout.setVisibility(View.GONE);
        }

        recipeId = Objects.requireNonNull(Objects.
                requireNonNull(getActivity()).getIntent().getExtras()).getInt(RECIPE_ID);

        RecipeViewModel recipeViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RecipeViewModel.class);
        recipeViewModel.getSingleRecipe(recipeId).observe(this,
                recipe -> {
                    if (recipe != null) {
                        setIngredientsListFromRecipe(recipe);
                        setStepsListFromRecipe(recipe);
                        if (!isTwoPane) {
                            scrollViewImage.setVisibility(View.GONE);
                            setToolbarTitle(recipe);
                            Images.loadImage(recipe, backdropImage);
                        } else {
                            scrollViewImage.setVisibility(View.VISIBLE);
                            Images.loadImage(recipe, scrollViewImage);
                        }
                    }
                });
    }

    @Override
    public void onClick(int stepId, Step step, int stepListLength) {
        callback.onRecipeSelected(stepId, step, stepListLength);
    }

    public void setIngredientsListFromRecipe(Recipe recipe) {
        LinearLayoutManager ingredientsLayoutManager = new LinearLayoutManager(getActivity());
        ingredientsRecyclerView.setLayoutManager(ingredientsLayoutManager);
        IngredientAdapter ingredientAdapter = new IngredientAdapter();
        ingredientsRecyclerView.setAdapter(ingredientAdapter);
        ingredientsRecyclerView.setNestedScrollingEnabled(false);
        ingredientAdapter.setIngredientList(recipe.getIngredients());
    }

    public void setStepsListFromRecipe(Recipe recipe) {
        LinearLayoutManager stepsLayoutManager = new LinearLayoutManager(getActivity());
        stepsRecyclerView.setLayoutManager(stepsLayoutManager);
        StepAdapter stepAdapter = new StepAdapter(this);
        stepsRecyclerView.setAdapter(stepAdapter);
        stepsRecyclerView.setNestedScrollingEnabled(false);
        stepAdapter.setStepList(recipe.getSteps());
    }

    public void setToolbarTitle(Recipe recipe) {
        toolbar.setTitle(recipe.getName());
    }
}
