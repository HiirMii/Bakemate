package com.example.android.bakemate.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.android.bakemate.viewmodel.BakemateViewModelFactory;
import com.example.android.bakemate.viewmodel.RecipeCollectionViewModel;
import com.example.android.bakemate.viewmodel.RecipeViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RecipeViewModel.class)
    abstract ViewModel bindsRecipeViewModel(RecipeViewModel recipeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeCollectionViewModel.class)
    abstract ViewModel bindsRecipeCollectionViewModel(RecipeCollectionViewModel recipeCollectionViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindsViewModelFactory(BakemateViewModelFactory factory);
}
