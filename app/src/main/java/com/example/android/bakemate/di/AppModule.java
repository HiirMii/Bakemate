package com.example.android.bakemate.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.android.bakemate.api.RecipeService;
import com.example.android.bakemate.db.BakemateDb;
import com.example.android.bakemate.db.RecipeDao;
import com.example.android.bakemate.util.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {
    @Singleton @Provides
    RecipeService provideRecipeService() {
        return new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(RecipeService.class);
    }

    @Singleton @Provides
    BakemateDb provideDb(Application app) {
        return Room.databaseBuilder(app, BakemateDb.class, "bakemate.db").build();
    }

    @Singleton @Provides
    RecipeDao provideRecipeDao(BakemateDb db) {
        return db.recipeDao();
    }
}
