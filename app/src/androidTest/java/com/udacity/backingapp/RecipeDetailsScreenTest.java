package com.udacity.backingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.udacity.backingapp.model.Ingredient;
import com.udacity.backingapp.model.Recipe;
import com.udacity.backingapp.model.Step;
import com.udacity.backingapp.ui.activities.MainActivity;
import com.udacity.backingapp.ui.activities.RecipeDetail;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.util.Checks.checkNotNull;

/**
 * Created by feder on 18/04/2018.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeDetailsScreenTest {
    private final int RECYCLER_POSITION = 1;

    private List<Ingredient> ingredients;

    @Rule public ActivityTestRule<RecipeDetail> activityTestRule = new ActivityTestRule<RecipeDetail>(RecipeDetail.class) {
        @Override
        protected Intent getActivityIntent(){
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();

            Recipe recipe = new Recipe();
            recipe.setName("Test Recipe");
            ingredients = new ArrayList<Ingredient>() {
                {
                    add(new Ingredient(1, "CUP", "Ingredient 1"));
                    add(new Ingredient(1, "CUP", "Ingredient 2"));
                    add(new Ingredient(1, "CUP", "Ingredient 3"));
                }
            };
            List<Step> steps = new ArrayList<Step>(){
                {
                    add(new Step("Step 1 long description", "Step 1", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4", ""));
                    add(new Step("Step 2 long description", "Step 2", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4", ""));
                    add(new Step("Step 3 long description", "Step 3", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9cb_4-press-crumbs-in-pie-plate-creampie/4-press-crumbs-in-pie-plate-creampie.mp4", ""));
                }
            };

            recipe.setIngredients(ingredients);
            recipe.setSteps(steps);


            Intent result = new Intent(targetContext, RecipeDetail.class);
            result.putExtra("recipe", recipe);
            return result;
        }
    };

    @Test
    public void clickGridViewItemStepShouldOpensCorrespondentStep(){
        onView(withId(R.id.recipe_steps_list_container)).perform(RecyclerViewActions.actionOnItemAtPosition(RECYCLER_POSITION, click()));//.check(matches(isDisplayed()));
        onView(withId(R.id.step_description)).check(matches(withText("Step 1 long description")));
    }

    @Test
    public void clickGridViewItemIngredientsShouldOpensIngredientsList(){
        onView(withId(R.id.recipe_steps_list_container)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));//.check(matches(isDisplayed()));
        for(int i = 0; i <= 2; i++){
            onView(withId(R.id.ingredients_container)).check(matches(atPosition(i, hasDescendant(withText(ingredients.get(i).getIngredient())))));
        }
    }

    //This is a custom matcher which look in a specific position of a recycler view: https://stackoverflow.com/a/34795431/1857023
    private Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    return false;
                }

                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
}
