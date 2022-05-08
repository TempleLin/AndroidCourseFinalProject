package com.templo.androidcoursefinalproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.ViewModelProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.templo.androidcoursefinalproject.room_database.model.Category;
import com.templo.androidcoursefinalproject.room_database.model.CategoryViewModel;
import com.templo.androidcoursefinalproject.room_database.model.Product;
import com.templo.androidcoursefinalproject.room_database.model.ProductViewModel;
import com.templo.androidcoursefinalproject.room_database.model.RelationalViewModel;
import com.templo.androidcoursefinalproject.room_database.model.User;
import com.templo.androidcoursefinalproject.room_database.model.UserViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ROOM_DB_InstrumentedTest {
    Application application;
    UserViewModel userViewModel;
    CategoryViewModel categoryViewModel;
    ProductViewModel productViewModel;
    RelationalViewModel relationalViewModel;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.templo.androidcoursefinalproject", appContext.getPackageName());
    }

    //Failed.
    //TODO: Implement ROOM database unit testing.
    @Test
    public void testRelationalDatabase() {
        application = (Application) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        userViewModel = new ViewModelProvider.AndroidViewModelFactory(application).create(UserViewModel.class);
        categoryViewModel = new ViewModelProvider.AndroidViewModelFactory(application).create(CategoryViewModel.class);
        productViewModel = new ViewModelProvider.AndroidViewModelFactory(application).create(ProductViewModel.class);
        relationalViewModel =  new ViewModelProvider.AndroidViewModelFactory(application).create(RelationalViewModel.class);

//        UserViewModel.deleteAll(application);
//        CategoryViewModel.deleteAll(application);
//        ProductViewModel.deleteAll(application);

        //Current thread seems to not be main thread instead of main thread. Therefore, .observe is not required.
        assertNull(relationalViewModel.getAllCategoriesWithProducts(application).getValue());
        assertNull(relationalViewModel.getAllUsersWithProducts(application).getValue());

        ProductViewModel.insert(application, new Product("TestProduct", 1, null, null, null, null, null, 1, "TestLocation", "TestDescription"));

        UserViewModel.insert(application, new User("TestUser", "test_user1234@youmail.com", "user_test_1234", null));
        CategoryViewModel.insert(application, new Category("TestCategory"));

        assertNotNull(relationalViewModel.getAllCategoriesWithProducts(application));
        assertNotNull(relationalViewModel.getAllUsersWithProducts(application).getValue());

        assertEquals(1, relationalViewModel.getAllCategoriesWithProducts(application).getValue().size());
        assertEquals(1, relationalViewModel.getAllUsersWithProducts(application).getValue().size());

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