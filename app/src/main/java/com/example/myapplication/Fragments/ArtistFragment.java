package com.example.myapplication.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.ArtistModal;
import com.example.myapplication.R;
import com.example.myapplication.artistDetailsRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArtistFragment extends Fragment {

    public ArtistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_artist, container, false);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("artistListDetails", "");
        Type type = new TypeToken<ArrayList<ArtistModal>>(){}.getType();
        ArrayList<ArtistModal> artistDetailsList = gson.fromJson(json, type);
        System.out.println("Artist Fragment size ::" + artistDetailsList.size());
        TextView noArtistDetailText = view.findViewById(R.id.noArtistDetailText);

        if(artistDetailsList.size()==0){
            noArtistDetailText.setVisibility(View.VISIBLE);
        }
        else  noArtistDetailText.setVisibility(View.INVISIBLE);

        for(int i=0; i<artistDetailsList.size(); i++){
            System.out.println(artistDetailsList.get(i).getArtistID());
            System.out.println(artistDetailsList.get(i).getArtistImg());
            System.out.println(artistDetailsList.get(i).getArtistName());
            System.out.println(artistDetailsList.get(i).getAlbumsImgs());
            System.out.println(artistDetailsList.get(i).getFollowers());
            System.out.println(artistDetailsList.get(i).getPopularity());
            System.out.println(artistDetailsList.get(i).getSpotifyLink());
        }

        RecyclerView recyclerView = view.findViewById(R.id.ArtistRecyclerView);
        artistDetailsRecyclerViewAdapter adapter  = new artistDetailsRecyclerViewAdapter(getContext(),artistDetailsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
}