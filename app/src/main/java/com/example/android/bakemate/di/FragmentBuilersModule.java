package com.example.android.bakemate.di;

import com.example.android.bakemate.ui.list.ListFragment;
import com.example.android.bakemate.ui.recipe.RecipeFragment;
import com.example.android.bakemate.ui.step.StepFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuilersModule {
    @ContributesAndroidInjector
    abstract ListFragment contributesListFragment();

    @ContributesAndroidInjector
    abstract RecipeFragment contributesRecipeFragment();

    @ContributesAndroidInjector
    abstract StepFragment contributesStepFragment();
}
