package com.example.android.bakemate.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.bakemate.model.Recipe;
import com.example.android.bakemate.repository.RecipeRepository;

import javax.inject.Inject;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository recipeRepository;

    @Inject
    public RecipeViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public LiveData<Recipe> getSingleRecipe(Integer recipeId) {
        return recipeRepository.loadSingleRecipe(recipeId);
    }
}
