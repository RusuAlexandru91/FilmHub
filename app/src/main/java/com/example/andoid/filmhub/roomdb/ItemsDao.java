package com.example.andoid.filmhub.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Items items);

    @Query("SELECT * FROM filmhub_db_table")
    LiveData<List<Items>> getAllItems();

    @Query("SELECT * FROM filmhub_db_table WHERE item_id = :id")
    Items findItemId(String id);

    @Query("DELETE FROM filmhub_db_table WHERE item_id = :id")
    void deleteItemWhereId(String id);

}
