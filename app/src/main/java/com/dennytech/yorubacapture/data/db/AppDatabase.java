package com.dennytech.yorubacapture.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dennytech.yorubacapture.data.model.User;

@Database(entities = { User.class }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract UserDao userDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Creating database >>
                    INSTANCE = buildDatabase(context);
                }
            }
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class, "Yoruba.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

    }
}
