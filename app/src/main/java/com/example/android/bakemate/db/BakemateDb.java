package com.example.android.bakemate.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.android.bakemate.model.Recipe;

/**
 * Main database description.
 */

@Database(entities = {Recipe.class}, version = 1)
public abstract class BakemateDb extends RoomDatabase {

    // --- DAO ---
    public abstract RecipeDao recipeDao();
}
