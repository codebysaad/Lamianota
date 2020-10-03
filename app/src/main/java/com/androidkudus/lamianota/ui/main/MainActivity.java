package com.androidkudus.lamianota.ui.main;

import android.content.Intent;
import android.os.Bundle;

import com.androidkudus.lamianota.R;
import com.androidkudus.lamianota.ui.profile.ProfileActivity;
import com.androidkudus.lamianota.utils.ZoomTransformer;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private AppCompatImageButton btnReminder, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.androidkudus.lamianota.databinding.ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        btnProfile = findViewById(R.id.btn_profile);
        btnReminder = findViewById(R.id.btn_reminder);
        MainViewModel viewModel = new MainViewModel(getSupportFragmentManager(),
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager),
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mainBinding.setViewModel(viewModel);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewPager.setPageTransformer(true, new ZoomTransformer());
        btnProfile.setOnClickListener(v -> {startActivity(new Intent(getApplicationContext(), ProfileActivity.class));});
    }
}