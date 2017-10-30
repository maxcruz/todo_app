package com.example.todoapp.tasks;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.example.todoapp.Injection;
import com.example.todoapp.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TasksScreenTest {

    private final static String TITLE1 = "TITLE1";
    private final static String DESCRIPTION = "DESCR";
    private final static String TITLE2 = "TITLE2";

    @Rule
    public ActivityTestRule<TasksActivity> mTasksActivityTestRule =
            new ActivityTestRule<TasksActivity>(TasksActivity.class) {

                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();
                    Injection.INSTANCE.provideTasksRepository(InstrumentationRegistry.getTargetContext())
                            .deleteAllTasks();
                }

            };

    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(ListView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA LV with text " + itemText);
            }

        };
    }

    @Test
    public void editTask() throws Exception {
        createTask(TITLE1, DESCRIPTION);
        onView(withText(TITLE1)).perform(click());
        onView(withId(R.id.fabEditTask)).perform(click());
        String editTaskTitle = TITLE2;
        String editTaskDescription = "New Description";
        onView(withId(R.id.addTaskTitle)).perform(replaceText(editTaskTitle), closeSoftKeyboard());
        onView(withId(R.id.addTaskDescription)).perform(replaceText(editTaskDescription),
                closeSoftKeyboard());
        onView(withId(R.id.fabAddTask)).perform(click());
        onView(withItemText(editTaskTitle)).check(matches(isDisplayed()));
        onView(withItemText(TITLE1)).check(doesNotExist());
    }

    @Test
    public void addTaskToTasksList() throws Exception {
        createTask(TITLE1, DESCRIPTION);
        onView(withItemText(TITLE1)).check(matches(isDisplayed()));
    }

    @Test
    public void markTaskAsComplete() {
        viewAllTasks();
        createTask(TITLE1, DESCRIPTION);
        clickCheckBoxForTask(TITLE1);
        viewAllTasks();
        onView(withItemText(TITLE1)).check(matches(isDisplayed()));
        viewActiveTasks();
        onView(withItemText(TITLE1)).check(matches(not(isDisplayed())));
        viewCompletedTasks();
        onView(withItemText(TITLE1)).check(matches(isDisplayed()));
    }

    @Test
    public void markTaskAsActive() {
        viewAllTasks();
        createTask(TITLE1, DESCRIPTION);
        clickCheckBoxForTask(TITLE1);
        clickCheckBoxForTask(TITLE1);
        viewAllTasks();
        onView(withItemText(TITLE1)).check(matches(isDisplayed()));
        viewActiveTasks();
        onView(withItemText(TITLE1)).check(matches(isDisplayed()));
        viewCompletedTasks();
        onView(withItemText(TITLE1)).check(matches(not(isDisplayed())));
    }

    @Test
    public void showAllTasks() {
        createTask(TITLE1, DESCRIPTION);
        createTask(TITLE2, DESCRIPTION);
        viewAllTasks();
        onView(withItemText(TITLE1)).check(matches(isDisplayed()));
        onView(withItemText(TITLE2)).check(matches(isDisplayed()));
    }

    @Test
    public void showActiveTasks() {
        createTask(TITLE1, DESCRIPTION);
        createTask(TITLE2, DESCRIPTION);
        viewActiveTasks();
        onView(withItemText(TITLE1)).check(matches(isDisplayed()));
        onView(withItemText(TITLE2)).check(matches(isDisplayed()));
    }

    @Test
    public void showCompletedTasks() {
        createTask(TITLE1, DESCRIPTION);
        clickCheckBoxForTask(TITLE1);
        createTask(TITLE2, DESCRIPTION);
        clickCheckBoxForTask(TITLE2);
        viewCompletedTasks();
        onView(withItemText(TITLE1)).check(matches(isDisplayed()));
        onView(withItemText(TITLE2)).check(matches(isDisplayed()));
    }

    @Test
    public void clearCompletedTasks() {
        viewAllTasks();
        createTask(TITLE1, DESCRIPTION);
        clickCheckBoxForTask(TITLE1);
        createTask(TITLE2, DESCRIPTION);
        clickCheckBoxForTask(TITLE2);
        openActionBarOverflowOrOptionsMenu(getTargetContext());
        onView(withText(R.string.menu_clear)).perform(click());
        onView(withItemText(TITLE1)).check(matches(not(isDisplayed())));
        onView(withItemText(TITLE2)).check(matches(not(isDisplayed())));
    }

    @Test
    public void createOneTask_deleteTask() {
        viewAllTasks();
        createTask(TITLE1, DESCRIPTION);
        onView(withText(TITLE1)).perform(click());
        onView(withId(R.id.menu_delete)).perform(click());
        viewAllTasks();
        onView(withText(TITLE1)).check(matches(not(isDisplayed())));
    }

    private void viewAllTasks() {
        onView(withId(R.id.menu_filter)).perform(click());
        onView(withText(R.string.nav_all)).perform(click());
    }

    private void viewActiveTasks() {
        onView(withId(R.id.menu_filter)).perform(click());
        onView(withText(R.string.nav_active)).perform(click());
    }

    private void viewCompletedTasks() {
        onView(withId(R.id.menu_filter)).perform(click());
        onView(withText(R.string.nav_completed)).perform(click());
    }

    private void createTask(String title, String description) {
        onView(withId(R.id.fabAddTask)).perform(click());
        onView(withId(R.id.addTaskTitle)).perform(typeText(title), closeSoftKeyboard());
        onView(withId(R.id.addTaskDescription)).perform(typeText(description), closeSoftKeyboard());
        onView(withId(R.id.fabAddTask)).perform(click());
    }

    private void clickCheckBoxForTask(String title) {
        onView(allOf(withId(R.id.itemComplete), hasSibling(withText(title)))).perform(click());
    }

}
