package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.Fragments.FavouriteFragment;
import com.example.myapplication.Fragments.SearchFragment;
import com.example.myapplication.Fragments.ShowEvents;

public class MyViewPagerAdapter extends FragmentStateAdapter  {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
      switch(position){
          case 0: return new SearchFragment();
          case 1: return new FavouriteFragment();
          case 2: return new ShowEvents();
          default: return new SearchFragment();
      }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
