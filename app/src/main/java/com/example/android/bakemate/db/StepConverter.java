package com.example.android.bakemate.db;

import android.arch.persistence.room.TypeConverter;

import com.example.android.bakemate.model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class StepConverter {

    @TypeConverter
    public static String fromStepsList(List<Step> stepList) {
        if (stepList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Step>>() {}.getType();
        return gson.toJson(stepList, type);
    }

    @TypeConverter
    public static List<Step> toStepsList(String stepsString) {
        if (stepsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Step>>() {}.getType();
        return gson.fromJson(stepsString, type);
    }
}
