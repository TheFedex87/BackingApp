package com.udacity.backingapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.udacity.backingapp.ui.activities.MainActivity;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.util.Checks.checkNotNull;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

/**
 * Created by federico.creti on 18/04/2018.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {
    private final String RECIPE_NAME = "Yellow Cake";
    private final int RECYCLER_POSITION = 2;

    private IdlingResource idlingResource;

    @Rule public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void registerHidlingResource() {
        idlingResource = activityActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void clickGridViewItemShouldOpensCorrespondentRecipe(){

        onView(withId(R.id.recipes_list_container)).perform(RecyclerViewActions.scrollToPosition(RECYCLER_POSITION))
                .check(matches(atPosition(RECYCLER_POSITION, hasDescendant(withText(RECIPE_NAME)))));

        onView(withId(R.id.recipes_list_container)).perform(RecyclerViewActions.actionOnItemAtPosition(RECYCLER_POSITION, click()));

        onView(allOf(isDescendantOfA(withResourceName("action_bar_container")), withText(RECIPE_NAME))).check(matches(isDisplayed()));
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
                    // has no item on such position
                    return false;
                }

                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
}

