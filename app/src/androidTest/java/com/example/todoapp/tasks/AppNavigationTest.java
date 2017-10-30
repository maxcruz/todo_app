package com.example.todoapp.tasks;

import android.support.test.espresso.NoActivityResumedException;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import com.example.todoapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.todoapp.util.TestUtils.getToolbarNavigationContentDescription;
import static com.example.todoapp.action.NavigationViewActions.navigateTo;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AppNavigationTest {

    @Rule
    public ActivityTestRule<TasksActivity> mActivityTestRule =
            new ActivityTestRule<>(TasksActivity.class);

    @Test
    public void clickOnStatisticsNavigationItemAndShowsStatisticsScreen() {
        openStatisticsScreen();
        onView(withId(R.id.statistics)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnListNavigationItemAndShowsListScreen() {
        openStatisticsScreen();
        openTasksScreen();
        onView(withId(R.id.tasksContainer)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnAndroidHomeIconAndOpensNavigation() {
        onView(withId(R.id.drawerLayout))
                .check(matches(isClosed(Gravity.LEFT)));
        onView(withContentDescription(getToolbarNavigationContentDescription(
                mActivityTestRule.getActivity(), R.id.toolbar))).perform(click());
        onView(withId(R.id.drawerLayout))
                .check(matches(isOpen(Gravity.LEFT)));
    }

    @Test
    public void StatisticsAndBackNavigatesToTasks() {
        openStatisticsScreen();
        pressBack();
        onView(withId(R.id.tasksContainer)).check(matches(isDisplayed()));
    }

    @Test
    public void backFromTasksScreenAndExitsApp() {
        assertPressingBackExitsApp();
    }

    @Test
    public void backFromTasksScreenAfterStatsAndExitsApp() {
        openStatisticsScreen();
        openTasksScreen();
        assertPressingBackExitsApp();
    }

    private void assertPressingBackExitsApp() {
        try {
            pressBack();
            fail("Should kill the app and throw an exception");
        } catch (NoActivityResumedException e) {
            // Test OK
        }
    }

    private void openTasksScreen() {
        onView(withId(R.id.drawerLayout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(open());
        onView(withId(R.id.navigationView))
                .perform(navigateTo(R.id.list_navigation_menu_item));
    }

    private void openStatisticsScreen() {
        onView(withId(R.id.drawerLayout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(open());
        onView(withId(R.id.navigationView))
                .perform(navigateTo(R.id.statistics_navigation_menu_item));
    }

}