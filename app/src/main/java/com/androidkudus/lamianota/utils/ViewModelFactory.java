package com.androidkudus.lamianota.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.androidkudus.lamianota.ui.keu.insert.InsertUpdateKeuViewModel;
import com.androidkudus.lamianota.ui.keu.main.KeuanganViewModel;
import com.androidkudus.lamianota.ui.note.insert.InsertUpdateNoteViewModel;
import com.androidkudus.lamianota.ui.note.main.NoteViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static volatile ViewModelFactory INSTANCE;
    private final Application mApplication;

    private ViewModelFactory(Application application){
        mApplication = application;
    }

    public static ViewModelFactory getInstance(Application application){
        if (INSTANCE == null){
            synchronized (ViewModelFactory.class){
                if (INSTANCE == null){
                    INSTANCE = new ViewModelFactory(application);
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        if (modelClass.isAssignableFrom(NoteViewModel.class)){
            return (T) new NoteViewModel(mApplication);
        }
        else if (modelClass.isAssignableFrom(InsertUpdateNoteViewModel.class)){
            return (T) new InsertUpdateNoteViewModel(mApplication);
        } else if (modelClass.isAssignableFrom(KeuanganViewModel.class)){
            return (T) new KeuanganViewModel(mApplication);
        }
        else if (modelClass.isAssignableFrom(InsertUpdateKeuViewModel.class)){
            return (T) new InsertUpdateKeuViewModel(mApplication);
        }
        throw new IllegalArgumentException("Unknown ViewModel Class: "+ modelClass.getName());
    }
}
