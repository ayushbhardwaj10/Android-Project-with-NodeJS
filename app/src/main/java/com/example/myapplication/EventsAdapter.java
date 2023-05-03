package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.Fragments.FavouriteFragment;
import com.example.myapplication.Fragments.ShowEvents;

public class EventsAdapter extends FragmentStateAdapter {
    public EventsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new ShowEvents();
            case 1: return new FavouriteFragment();
            default: return new ShowEvents();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
