package com.androidkudus.lamianota.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.androidkudus.lamianota.database.note.Note;
import com.androidkudus.lamianota.database.note.NoteDao;
import com.androidkudus.lamianota.database.note.NoteRoomDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {
    private NoteDao noteDao;
    private ExecutorService executorService;

    public NoteRepository(Application application){
        executorService = Executors.newSingleThreadExecutor();
        NoteRoomDatabase noteRoomDatabase = NoteRoomDatabase.getNoteDatabse(application);
        noteDao = noteRoomDatabase.dao();
    }

    public LiveData<List<Note>> getAllNotes() {
        return noteDao.getAllNote();
    }

    public void insert (final Note note){
        executorService.execute(() -> noteDao.insert(note));
    }

    public void delete (final Note note){
        executorService.execute(() -> noteDao.delete(note));
    }

    public void update (final Note note){
        executorService.execute(() -> noteDao.update(note));
    }
}
