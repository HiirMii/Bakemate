package com.example.android.bakemate.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakemate.R;
import com.example.android.bakemate.db.IngredientConverter;
import com.example.android.bakemate.model.Ingredient;
import com.example.android.bakemate.util.StringModifier;

import java.util.ArrayList;
import java.util.List;

public class IngredientsWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;

    private List<Ingredient> ingredients = new ArrayList<>();

    public IngredientsWidgetFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        ingredients.clear();

        SharedPreferences recipeData = context.getSharedPreferences("recipeData", Context.MODE_PRIVATE);
        String gsonIngredientsList = recipeData.getString("ingredientsList", "");

        ingredients = IngredientConverter.toIngredientsList(gsonIngredientsList);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (ingredients == null) return 0;
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews ingredient = new RemoteViews(context.getPackageName(),
                R.layout.widget_list_item);

        final Ingredient currentIngredient = ingredients.get(position);

        float currentIngredientQuantity = currentIngredient.getQuantity();
        String currentIngredientMeasure = currentIngredient.getMeasure();
        String currentIngredientName = currentIngredient.getIngredient();

        ingredient.setTextViewText(R.id.widget_list_item_text, StringModifier.ingredientStringBuilder(
                currentIngredientQuantity, currentIngredientMeasure, currentIngredientName
        ));

        return ingredient;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
