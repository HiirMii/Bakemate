package com.example.android.bakemate.di;

import com.example.android.bakemate.ui.list.ListActivity;
import com.example.android.bakemate.ui.recipe.RecipeActivity;
import com.example.android.bakemate.ui.step.StepActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {
    @ContributesAndroidInjector(modules = FragmentBuilersModule.class)
    abstract ListActivity contributeListActivity();

    @ContributesAndroidInjector(modules = FragmentBuilersModule.class)
    abstract RecipeActivity contributeRecipeActivity();

    @ContributesAndroidInjector(modules = FragmentBuilersModule.class)
    abstract StepActivity contributeStepActivity();
}
