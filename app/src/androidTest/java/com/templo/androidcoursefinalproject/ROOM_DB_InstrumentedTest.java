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

    @Test
    public void testRelationalDatabase() {
        Application application = (Application) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();

        UserViewModel userViewModel = new ViewModelProvider.AndroidViewModelFactory(application)
                .create(UserViewModel.class);

        UserViewModel.insert(application, new User("TestUser", "test_user1234@youmail.com", "user_test_1234", null));

        CategoryViewModel categoryViewModel = new ViewModelProvider.AndroidViewModelFactory(application)
                .create(CategoryViewModel.class);
        CategoryViewModel.insert(application, new Category("TestCategory"));

        ProductViewModel productViewModel = new ViewModelProvider.AndroidViewModelFactory(application)
                .create(ProductViewModel.class);
        ProductViewModel.insert(application, new Product("TestProduct", 1, null, null, null, null, null, 1, "TestLocation", "TestDescription"));

        RelationalViewModel relationalViewModel =  new ViewModelProvider.AndroidViewModelFactory(application)
                .create(RelationalViewModel.class);

        //Current thread seems to be background thread instead of main thread. Therefore, .observe is not required.
        assertNotEquals(relationalViewModel.getAllUsersWithProducts(application), null);
        assertNotEquals(relationalViewModel.getAllCategoriesWithProducts(application), null);

        //If need to run things on the main thread (Might not work for assert().):
//        Handler handler = new Handler(Looper.getMainLooper()); //This is the main thread
//        handler.post(new Runnable() { //task to run on main thread
//                         @Override
//                         public void run() {
//
//                         }
//                     }
//        );
    }
}