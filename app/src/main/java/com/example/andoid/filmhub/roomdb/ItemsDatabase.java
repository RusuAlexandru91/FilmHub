package com.example.andoid.filmhub.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Items.class}, version = 1)
public abstract class ItemsDatabase extends RoomDatabase {

    public abstract ItemsDao userDao();

    // Creating the Singletone
    private static ItemsDatabase INSTANCE;

    public static ItemsDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ItemsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ItemsDatabase.class, "filmhub_db_table").build();
                }
            }
        }
        return INSTANCE;
    }
}
