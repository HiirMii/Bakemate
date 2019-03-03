package com.example.android.bakemate.db;

import android.arch.persistence.room.TypeConverter;

import com.example.android.bakemate.model.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class IngredientConverter {

    @TypeConverter
    public static String fromIngredientsList(List<Ingredient> ingredientList) {
        if (ingredientList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        return gson.toJson(ingredientList, type);
    }

    @TypeConverter
    public static List<Ingredient> toIngredientsList(String ingredientsString) {
        if (ingredientsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        return gson.fromJson(ingredientsString, type);
    }
}
