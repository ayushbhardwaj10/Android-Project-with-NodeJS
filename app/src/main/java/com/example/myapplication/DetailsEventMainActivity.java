package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.myapplication.Fragments.ArtistFragment;
import com.google.android.material.tabs.TabLayout;

public class DetailsEventMainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    DetailsMainActivityAdapter detailsMainActivityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_event_main);

        tabLayout = findViewById((R.id.tableLayoutEventDetails));

        viewPager2 = findViewById(R.id.viewPagerEventDetails);

        detailsMainActivityAdapter = new DetailsMainActivityAdapter(this);
        viewPager2.setAdapter( detailsMainActivityAdapter);

//        this.changeViewPager(2);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}