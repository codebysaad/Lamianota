package com.androidkudus.lamianota.utils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.androidkudus.lamianota.database.note.Note;

import java.util.List;

public class NoteDiffCallback extends DiffUtil.Callback {
    private final List<Note> mOldNoteList;
    private final List<Note> mNewNoteList;

    public NoteDiffCallback(List<Note> mOldNoteList, List<Note> mNewNoteList){
        this.mOldNoteList = mOldNoteList;
        this.mNewNoteList = mNewNoteList;
    }
    @Override
    public int getOldListSize() {
        return mOldNoteList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewNoteList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldNoteList.get(oldItemPosition).getId() == mNewNoteList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Note oldNote = mOldNoteList.get(oldItemPosition);
        final Note newNote = mNewNoteList.get(newItemPosition);
        return oldNote.getTitle().equals(newNote.getTitle()) && oldNote.getDescription().equals(newNote.getDescription());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
