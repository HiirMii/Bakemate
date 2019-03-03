package com.example.android.bakemate.ui.list;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakemate.R;
import com.example.android.bakemate.db.IngredientConverter;
import com.example.android.bakemate.model.Recipe;
import com.example.android.bakemate.util.Images;
import com.example.android.bakemate.widget.IngredientsWidgetProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final RecipeAdapterOnClickHandler recipeAdapterOnClickHandler;
    private List<Recipe> recipeList;

    public interface RecipeAdapterOnClickHandler {
        void onClick(int recipeId, String recipeName);
    }

    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler) {
        recipeAdapterOnClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_recipe, viewGroup, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int position) {
        final Recipe currentRecipe = recipeList.get(position);

        Images.loadImage(currentRecipe, recipeViewHolder.recipeImage);

        recipeViewHolder.recipeName.setText(currentRecipe.getName());
    }

    @Override
    public int getItemCount() {
        if (recipeList == null) return 0;
        return recipeList.size();
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.iv_recipe_item_image)
        ImageView recipeImage;
        @BindView(R.id.tv_recipe_item_name)
        TextView recipeName;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Recipe currentRecipe = recipeList.get(getAdapterPosition());
            recipeAdapterOnClickHandler.onClick(currentRecipe.getId(), currentRecipe.getName());
            SharedPreferences recipeData = v.getContext()
                    .getSharedPreferences("recipeData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = recipeData.edit();

            String ingredientsListToGson = IngredientConverter.fromIngredientsList(currentRecipe.getIngredients());

            editor.putString("ingredientsList", ingredientsListToGson);
            editor.putString("recipeName", currentRecipe.getName());
            editor.apply();
            Timber.d("Data saved in shared preferences: %s", recipeData.getString("recipeName", ""));

            Intent intent = new Intent(v.getContext(), IngredientsWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            v.getContext().sendBroadcast(intent);
        }
    }
}
