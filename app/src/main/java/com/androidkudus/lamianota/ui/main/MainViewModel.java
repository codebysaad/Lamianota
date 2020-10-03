package com.androidkudus.lamianota.ui.main;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.androidkudus.lamianota.adapter.ViewPagerAdapter;
import com.androidkudus.lamianota.ui.keu.main.KeuanganFragment;
import com.androidkudus.lamianota.ui.note.main.NoteFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainViewModel {
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout.ViewPagerOnTabSelectedListener viewPagerOnTabSelectedListener;
    private TabLayout.TabLayoutOnPageChangeListener tabLayoutOnPageChangeListener;

    public MainViewModel(FragmentManager fm,
                         TabLayout.ViewPagerOnTabSelectedListener viewPagerOnTabSelectedListener,
                         TabLayout.TabLayoutOnPageChangeListener tabLayoutOnPageChangeListener) {
        this.viewPagerOnTabSelectedListener = viewPagerOnTabSelectedListener;
        this.tabLayoutOnPageChangeListener = tabLayoutOnPageChangeListener;

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new NoteFragment());
        fragments.add(new KeuanganFragment());
        viewPagerAdapter = new ViewPagerAdapter(fm, fragments);
    }

    public FragmentStatePagerAdapter getViewPagerAdapter() {
        return viewPagerAdapter;
    }

    public TabLayout.ViewPagerOnTabSelectedListener getViewPagerOnTabSelectedListener() {
        return viewPagerOnTabSelectedListener;
    }

    public TabLayout.TabLayoutOnPageChangeListener getTabLayoutOnPageChangeListener() {
        return tabLayoutOnPageChangeListener;
    }
}
