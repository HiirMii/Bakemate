package com.example.android.bakemate.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.bakemate.AppExecutors;
import com.example.android.bakemate.api.ApiResponse;
import com.example.android.bakemate.api.RecipeService;
import com.example.android.bakemate.db.RecipeDao;
import com.example.android.bakemate.model.Recipe;
import com.example.android.bakemate.util.RateLimiter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * RecipeRepository that handles Recipe instances.
 */

@Singleton
public class RecipeRepository {

    private final RecipeDao recipeDao;

    private final RecipeService recipeService;

    private final AppExecutors appExecutors;

    private RateLimiter<String> recipeListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    public RecipeRepository(AppExecutors appExecutors,RecipeDao recipeDao,
                            RecipeService recipeService) {
        this.recipeDao = recipeDao;
        this.recipeService = recipeService;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<List<Recipe>>> loadRecipes() {
        return new NetworkBoundResource<List<Recipe>,List<Recipe>>(appExecutors) {
            private String rateLimiterKey = "key";
            @Override
            protected void saveCallResult(@NonNull List<Recipe> item) {
                recipeDao.insertRecipes(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Recipe> data) {
                return data == null || data.isEmpty() || recipeListRateLimit.shouldFetch(rateLimiterKey);
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                return recipeDao.getAllRecipes();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Recipe>>> createCall() {
                return recipeService.getRecipes();
            }

            @Override
            protected void onFetchFailed() {
                recipeListRateLimit.reset(rateLimiterKey);
            }
        }.asLiveData();
    }

    public LiveData<Recipe> loadSingleRecipe(Integer recipeId) {
        return recipeDao.getSingleRecipe(recipeId);
    }
}
