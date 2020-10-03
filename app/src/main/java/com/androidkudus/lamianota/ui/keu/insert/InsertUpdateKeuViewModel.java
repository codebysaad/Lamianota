package com.androidkudus.lamianota.ui.keu.insert;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.androidkudus.lamianota.database.keu.Keu;
import com.androidkudus.lamianota.repository.KeuRepository;

public class InsertUpdateKeuViewModel extends ViewModel {
    private KeuRepository mKeuRepository;

    public InsertUpdateKeuViewModel(Application application){
        mKeuRepository = new KeuRepository(application);
    }

    public void insert(Keu keu) {
        mKeuRepository.insertKeu(keu);
    }

    public void update(Keu keu) {
        mKeuRepository.updateKeu(keu);
    }

    public void delete(Keu keu) {
        mKeuRepository.deleteKeu(keu);
    }
}
