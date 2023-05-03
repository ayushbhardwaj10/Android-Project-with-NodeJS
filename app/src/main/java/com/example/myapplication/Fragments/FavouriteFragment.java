package com.example.myapplication.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.EventModel;
import com.example.myapplication.R;
import com.example.myapplication.eventsRecyclerViewAdapter;
import com.example.myapplication.favRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavouriteFragment extends Fragment {

    public static FragmentManager fragmentManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_favourite, container, false);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentManager = getParentFragmentManager();
        System.out.println("On Favourite triggered..!!");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Gson gson = new Gson();

        // if already present in shared preferences object already present
        if (sharedPreferences.contains("favEvents")) {
            System.out.println("shared pref data found ********");
            String json = sharedPreferences.getString("favEvents", "");
            Type type = new TypeToken<ArrayList<EventModel>>() {
            }.getType();
            ArrayList<EventModel> currentFavList = gson.fromJson(json, type);
            System.out.println("shared pref data size:: ********" + currentFavList.size());
            if(currentFavList.size()>0){
                TextView noEventFav = getView().findViewById(R.id.noEventFav);
                noEventFav.setVisibility(View.INVISIBLE);

                RecyclerView recyclerView = getView().findViewById(R.id.favouriteRecyclerView);
                favRecyclerViewAdapter adapter  = new favRecyclerViewAdapter(getContext(),currentFavList );
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            else {
                // show no fav events message
                System.out.println("Empty Favourite case#1");


                //updating recycler view. (Bug fix : It was not deleting the last event card left)
                RecyclerView recyclerView = getView().findViewById(R.id.favouriteRecyclerView);
                favRecyclerViewAdapter adapter  = new favRecyclerViewAdapter(getContext(),currentFavList );
                recyclerView.setAdapter(adapter);
                adapter.updateData(currentFavList);
                adapter.notifyDataSetChanged();

                TextView noEventFav = getView().findViewById(R.id.noEventFav);
                noEventFav.setVisibility(View.VISIBLE);

            }

        }
        else {
            // show no fav events message
            System.out.println("Empty Favourite case#2");
        }
    }
    public void refreshFavoriteFragment() {
        // Reload data and update views here
        onResume();
    }

}