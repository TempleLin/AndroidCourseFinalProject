package com.templo.androidcoursefinalproject;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.templo.androidcoursefinalproject.room_database.model.Category;
import com.templo.androidcoursefinalproject.room_database.model.CategoryViewModel;
import com.templo.androidcoursefinalproject.room_database.model.Product;
import com.templo.androidcoursefinalproject.room_database.model.ProductViewModel;
import com.templo.androidcoursefinalproject.room_database.model.RelationalViewModel;
import com.templo.androidcoursefinalproject.room_database.model.User;
import com.templo.androidcoursefinalproject.room_database.model.UserViewModel;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.templo.androidcoursefinalproject", appContext.getPackageName());
    }
}