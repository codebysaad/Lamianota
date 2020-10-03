package com.androidkudus.lamianota.ui.note.main;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.androidkudus.lamianota.database.note.Note;
import com.androidkudus.lamianota.repository.NoteRepository;

import java.util.List;

public class NoteViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private NoteRepository noteRepository;

    public NoteViewModel(Application application){
        noteRepository = new NoteRepository(application);
    }

    LiveData<List<Note>> getAllData(){
        return noteRepository.getAllNotes();
    }
}
