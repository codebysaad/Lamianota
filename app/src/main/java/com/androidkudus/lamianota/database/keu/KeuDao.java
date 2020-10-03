package com.androidkudus.lamianota.database.keu;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface KeuDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertKeu(Keu keu);

    @Update()
    void updateKeu(Keu keu);

    @Delete()
    void deleteKeu(Keu keu);

    @Query("SELECT * FROM keu ORDER BY id ASC")
    LiveData<List<Keu>> getAllKeuNote();
}
