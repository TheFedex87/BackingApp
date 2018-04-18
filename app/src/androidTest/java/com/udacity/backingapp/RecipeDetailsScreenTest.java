package com.udacity.backingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.backingapp.model.Ingredient;
import com.udacity.backingapp.model.Recipe;
import com.udacity.backingapp.model.Step;
import com.udacity.backingapp.ui.activities.MainActivity;
import com.udacity.backingapp.ui.activities.RecipeDetail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by feder on 18/04/2018.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeDetailsScreenTest {
    private final int RECYCLER_POSITION = 0;

    @Rule public ActivityTestRule<RecipeDetail> activityTestRule = new ActivityTestRule<RecipeDetail>(RecipeDetail.class) {
        @Override
        protected Intent getActivityIntent(){
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();

            Recipe recipe = new Recipe();
            recipe.setName("Test Recipe");
            List<Ingredient> ingredients = new ArrayList<Ingredient>() {
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

    @Before
    public void init(){
        /*activityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();*/
    }

    @Test
    public void clickGridViewItemShouldOpensCorrespondentStep(){
        onView(withId(R.id.recipe_steps_list_container)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));//.check(matches(isDisplayed()));
        onView(withId(R.id.step_description)).check(matches(withText("Step 1 long description")));
    }
}
