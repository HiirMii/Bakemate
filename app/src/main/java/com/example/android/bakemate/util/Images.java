package com.example.android.bakemate.util;

import android.widget.ImageView;

import com.example.android.bakemate.R;
import com.example.android.bakemate.model.Recipe;
import com.squareup.picasso.Picasso;

public class Images {

    public static int setImageDrawable(Recipe recipe) {

        int currentDrawable;

        switch (recipe.getName()) {
            case "Nutella Pie":
                currentDrawable = R.drawable.nutella_pie;
                break;
            case "Brownies":
                currentDrawable = R.drawable.brownie;
                break;
            case "Yellow Cake":
                currentDrawable = R.drawable.yellow_cake;
                break;
            case "Cheesecake":
                currentDrawable = R.drawable.cheesecake;
                break;
            default:
                currentDrawable = R.drawable.placeholder_icon;
        }

        return currentDrawable;
    }

    public static void loadImage(Recipe recipe, ImageView imageView) {
        String recipeImage = recipe.getImage();
        if (recipeImage.equals("")) {
            Picasso.get()
                    .load(Images.setImageDrawable(recipe))
                    .placeholder(R.drawable.placeholder_icon)
                    .into(imageView);
        } else {
            Picasso.get()
                    .load(recipeImage)
                    .placeholder(R.drawable.placeholder_icon)
                    .into(imageView);
        }
    }
}
