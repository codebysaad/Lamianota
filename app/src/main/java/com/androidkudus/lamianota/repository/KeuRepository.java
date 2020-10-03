package com.androidkudus.lamianota.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.androidkudus.lamianota.database.keu.Keu;
import com.androidkudus.lamianota.database.keu.KeuDao;
import com.androidkudus.lamianota.database.keu.KeuRoomDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KeuRepository {
    private KeuDao keuDao;
    private ExecutorService executorService;

    public KeuRepository(Application application){
        executorService = Executors.newSingleThreadExecutor();
        KeuRoomDatabase keuRoomDatabase = KeuRoomDatabase.getKeuDatabse(application);
        keuDao = keuRoomDatabase.dao();
    }

    public LiveData<List<Keu>> getAllKeuNote() {
        return keuDao.getAllKeuNote();
    }

    public void insertKeu (final Keu keu){
        executorService.execute(() -> keuDao.insertKeu(keu));
    }

    public void deleteKeu (final Keu keu){
        executorService.execute(() -> keuDao.deleteKeu(keu));
    }

    public void updateKeu (final Keu keu){
        executorService.execute(() -> keuDao.updateKeu(keu));
    }
}
