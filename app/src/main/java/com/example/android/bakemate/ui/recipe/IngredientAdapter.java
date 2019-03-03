package com.example.android.bakemate.ui.recipe;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakemate.R;
import com.example.android.bakemate.model.Ingredient;
import com.example.android.bakemate.util.StringModifier;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredientList;

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_ingredient, viewGroup, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder ingredientViewHolder, int position) {
        final Ingredient currentIngredient = ingredientList.get(position);
        float currentIngredientQuantity = currentIngredient.getQuantity();
        String currentIngredientMeasure = currentIngredient.getMeasure();
        String currentIngredientName = currentIngredient.getIngredient();

        ingredientViewHolder.ingredientItem.setText(StringModifier.ingredientStringBuilder(
                currentIngredientQuantity, currentIngredientMeasure, currentIngredientName
        ));

        if (position %2 == 1) {
            ingredientViewHolder.ingredientItem.setBackgroundColor(Color.parseColor("#eeeeee"));
        }
    }

    @Override
    public int getItemCount() {
        if (ingredientList == null) return 0;
        return ingredientList.size();
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
        notifyDataSetChanged();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ingredient_item)
        TextView ingredientItem;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
