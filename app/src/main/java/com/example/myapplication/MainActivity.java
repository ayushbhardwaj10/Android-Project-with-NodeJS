 package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.*;

import com.google.android.material.tabs.TabLayout;

 public class  MainActivity extends AppCompatActivity {

     private TabLayout tabLayout;
     private ViewPager2 viewPager2;
     MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById((R.id.tableLayout));
        viewPager2 = findViewById(R.id.viewPager);

        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager2.setAdapter( myViewPagerAdapter);

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
    public void changeViewPager(int number){
        this.viewPager2.setCurrentItem(number);
    }





//     public void searchEvents(View view){
//
//         System.out.println("search event clicked..!");
////        String keyword  = ((EditText) findViewById(R.id.keyword)).getText().toString();
////        System.out.println("Keyword :" + keyword);
//
//
//     }

}