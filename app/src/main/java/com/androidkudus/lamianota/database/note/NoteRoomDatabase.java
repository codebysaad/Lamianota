package com.androidkudus.lamianota.database.note;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteRoomDatabase extends RoomDatabase {
    public abstract NoteDao dao();
    private static volatile NoteRoomDatabase INSTANCE;

    public static NoteRoomDatabase getNoteDatabse(final Context context){
        if (INSTANCE == null){
            synchronized (NoteRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NoteRoomDatabase.class, "noteDatabase").build();
                }
            }
        }
        return INSTANCE;
    }
}
