package com.silion.floatwindow;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.silion.floatwindow.service.FloatWindowService;
import com.silion.floatwindow.util.AppUtils;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testIsHome () {
        boolean isHome = AppUtils.isHome(getContext());
        android.util.Log.v("silion", "android demo test test");
    }
}