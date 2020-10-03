package com.androidkudus.lamianota.ui.note.insert;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.androidkudus.lamianota.database.note.Note;
import com.androidkudus.lamianota.repository.NoteRepository;

public class InsertUpdateNoteViewModel extends ViewModel {
    private NoteRepository mNoteRepository;

    public InsertUpdateNoteViewModel(Application application){
        mNoteRepository = new NoteRepository(application);
    }

    public void insert(Note note) {
        mNoteRepository.insert(note);
    }

    public void update(Note note) {
        mNoteRepository.update(note);
    }

    public void delete(Note note) {
        mNoteRepository.delete(note);
    }
}
