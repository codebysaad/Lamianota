package com.androidkudus.lamianota.ui.keu.main;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.androidkudus.lamianota.database.keu.Keu;
import com.androidkudus.lamianota.repository.KeuRepository;

import java.util.List;

public class KeuanganViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private KeuRepository keuRepository;

    public KeuanganViewModel(Application application){
        keuRepository = new KeuRepository(application);
    }

    LiveData<List<Keu>> getAllData(){
        return keuRepository.getAllKeuNote();
    }
}
