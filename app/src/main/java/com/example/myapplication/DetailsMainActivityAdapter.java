package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.Fragments.ArtistFragment;
import com.example.myapplication.Fragments.DetailsFragment;
import com.example.myapplication.Fragments.FavouriteFragment;
import com.example.myapplication.Fragments.ShowEvents;
import com.example.myapplication.Fragments.VenueFragment;

public class DetailsMainActivityAdapter extends FragmentStateAdapter {
    public DetailsMainActivityAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new DetailsFragment();
            case 1: return new ArtistFragment();
            case 2: return new VenueFragment();
            default: return new DetailsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
