package com.templo.androidcoursefinalproject.room_database.util;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.templo.androidcoursefinalproject.room_database.data.ProductDAO;
import com.templo.androidcoursefinalproject.room_database.data.UserDao;
import com.templo.androidcoursefinalproject.room_database.model.Product;
import com.templo.androidcoursefinalproject.room_database.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//First arg: array of Entities. Second arg: Version to give to the database. Third arg: Whether or not to export schema file.
@Database(entities = {User.class, Product.class}, version = 3, exportSchema = false)
public abstract class TheDatabase extends RoomDatabase {

    //The singleton instance.
    private static volatile TheDatabase INSTANCE;

    //RoomDatabase These abstract methods returning DAO to get to Entities.
    public abstract UserDao userDao();

    public abstract ProductDAO productDAO();

    public static final int NUMBER_OF_THREADS = 4;

    //Executor service helps write things into database. Its execution run things in the back threads (Not the main thread).
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TheDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TheDatabase.class) { //class is passed to synchronized bc method is static.
                if (INSTANCE == null) {
                    //"name" parameter is the name the database name.
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TheDatabase.class, "contact_database")
                            .addCallback(sRoomDatabaseCallback) //Will call this RoomDatabase.Callback object's onCreate method as callback when build is finished.
                            //If you don’t want to provide migrations and you specifically want your database to be cleared when you upgrade the version.
                            //(If this is not added, error will happen if update database version but not set migration path.)
                            .fallbackToDestructiveMigration()
                            //.allowMainThreadQueries() //Allow executing queries on main thread. (Not recommended.)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Callback sRoomDatabaseCallback =
        new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                /*
                .execute(): Executes the given command at some time in the future. The command may execute in a new thread, in a pooled thread,
                    or in the calling thread, at the discretion of the Executor implementation.
                 */
                Log.d("ROOM", "Test ROOM database build callback.");
                databaseWriteExecutor.execute(() -> {
                    //ROOM communication with database must be used in the background thread,
                    //  that's why it's in .execute callback.
                });
            }
        };
}
