package com.example.android.bakemate.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.bakemate.model.Recipe;
import com.example.android.bakemate.repository.RecipeRepository;
import com.example.android.bakemate.repository.Resource;

import java.util.List;

import javax.inject.Inject;

public class RecipeCollectionViewModel extends ViewModel {

    private RecipeRepository recipeRepository;

    @SuppressWarnings("unchecked")
    @Inject
    public RecipeCollectionViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public LiveData<Resource<List<Recipe>>> getRecipesList() {
        return recipeRepository.loadRecipes();
    }
}
