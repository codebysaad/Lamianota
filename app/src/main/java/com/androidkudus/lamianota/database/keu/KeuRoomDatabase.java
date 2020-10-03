package com.androidkudus.lamianota.database.keu;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Keu.class}, version = 1, exportSchema = false)
public abstract class KeuRoomDatabase extends RoomDatabase {
    public abstract KeuDao dao();
    private static volatile KeuRoomDatabase INSTANCE;

    public static KeuRoomDatabase getKeuDatabse(final Context context){
        if (INSTANCE == null){
            synchronized (KeuRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            KeuRoomDatabase.class, "keuDatabase").build();
                }
            }
        }
        return INSTANCE;
    }
}
