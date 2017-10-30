package com.example.todoapp.util;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

public class TestUtils {

    public static String getToolbarNavigationContentDescription(@NonNull Activity activity,
                                                                @IdRes int toolbar1) {
        Toolbar toolbar = activity.findViewById(toolbar1);
        if (toolbar != null) {
            return (String) toolbar.getNavigationContentDescription();
        } else {
            throw new RuntimeException("No toolbar found.");
        }
    }

}
