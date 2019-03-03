package com.example.android.bakemate.api;

import android.arch.lifecycle.LiveData;

import com.example.android.bakemate.model.Recipe;

import java.util.List;

import retrofit2.http.GET;

/**
 * REST API access point
 */

public interface RecipeService {
    @GET("baking.json")
    LiveData<ApiResponse<List<Recipe>>>getRecipes();
}
