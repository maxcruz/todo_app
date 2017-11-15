package com.example.todoapp.addedittask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class AddEditTaskActivityTest {

    private AddEditTaskActivity addEditTaskActivity;

    @Before
    public void setUp() throws Exception {
        addEditTaskActivity = Robolectric.setupActivity(AddEditTaskActivity.class);

    }

    @Test
    public void onCreate() throws Exception {
    }

    @Test
    public void onSaveInstanceState() throws Exception {
    }

    @Test
    public void onSupportNavigateUp() throws Exception {
    }

}