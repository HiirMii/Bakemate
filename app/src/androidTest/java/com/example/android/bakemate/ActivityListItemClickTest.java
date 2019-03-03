package com.example.android.bakemate;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakemate.ui.list.ListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class ActivityListItemClickTest {

    private static final String INGREDIENTS_LABEL = "Ingredients";
    private static final String STEPS_LABEL = "Method";

    @Rule
    public ActivityTestRule<ListActivity> activityTestRule = new ActivityTestRule<>(ListActivity.class);

    @Test
    public void clickOnRecyclerViewItemInListActivity_opensRecipeActivity() {
        onView(withId(R.id.rv_recipes_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.tv_ingredients_label))
                .check(matches(isDisplayed()));

    }

    @Test
    public void checkIfLabelsAreProperlyDisplayed() {
        onView(withId(R.id.rv_recipes_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.tv_ingredients_label))
                .check(matches(withText(INGREDIENTS_LABEL)));
        onView(withId(R.id.tv_steps_label))
                .check(matches(withText(STEPS_LABEL)));
    }
}
