package com.example.android.bakemate.ui.recipe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.example.android.bakemate.R;
import com.example.android.bakemate.model.Step;
import com.example.android.bakemate.ui.step.StepActivity;
import com.example.android.bakemate.ui.step.StepFragment;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class RecipeActivity extends AppCompatActivity implements HasSupportFragmentInjector, RecipeFragment.OnRecipeClickListener {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private boolean isTwoPane;

    private static final String RECIPE_NAME = "recipeName";
    private static final String STEP_ID = "stepId";
    private static final String STEP = "step";
    private static final String STEP_LIST_LENGTH = "stepListLength";

    @BindView(R.id.tb_recipe)
    Toolbar toolbar;
    @BindView(R.id.empty_step)
    FrameLayout emptyStateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        isTwoPane = getResources().getBoolean(R.bool.isTwoPane);

        if (isTwoPane) {
            ButterKnife.bind(this);
            toolbar.setTitle(Objects.requireNonNull(getIntent().getExtras()).getString(RECIPE_NAME));
            toolbar.setTitleTextColor(Color.WHITE);
        }

        if (savedInstanceState == null) {
            if (isTwoPane) {
                emptyStateLayout.setVisibility(View.VISIBLE);

                RecipeFragment recipeFragment = new RecipeFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipe_fragment_container, recipeFragment)
                        .commit();
            } else {
                RecipeFragment recipeFragment = new RecipeFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.activity_recipe, recipeFragment)
                        .commit();
            }
        }
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onRecipeSelected(int stepId, Step step, int stepListLength) {
        if (isTwoPane) {
            emptyStateLayout.setVisibility(View.GONE);

            Bundle arguments = new Bundle();
            arguments.putInt(STEP_ID, stepId);
            arguments.putParcelable(STEP, step);
            arguments.putInt(STEP_LIST_LENGTH, stepListLength);

            StepFragment stepFragment = new StepFragment();
            stepFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_fragment_container, stepFragment)
                    .commit();
        } else {
            Intent i = new Intent(this, StepActivity.class);
            i.putExtra(STEP_ID, stepId);
            i.putExtra(STEP, step);
            i.putExtra(STEP_LIST_LENGTH, stepListLength);
            startActivity(i);
        }
    }
}
