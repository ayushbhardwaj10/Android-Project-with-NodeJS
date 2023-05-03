package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class artistDetailsRecyclerViewAdapter extends RecyclerView.Adapter<artistDetailsRecyclerViewAdapter.MyViewHolder >{
    Context context;
    ArrayList<ArtistModal> artistList;

    public artistDetailsRecyclerViewAdapter(Context context, ArrayList<ArtistModal> artistList){
        this.context = context;
        this.artistList = artistList;
    }
    public void updateData(ArrayList<ArtistModal> data) {
        artistList = data;
    }
    public void updateDataRecycler(ArrayList<ArtistModal> data) {
        artistList = data;
        notifyDataSetChanged();
    }
    public static String formatFollowers(int number) {
        if (number >= 1000000) {
            int millions = number / 1000000;
            return millions + "M";
        } else if (number >= 1000) {
            int thousands = number / 1000;
            return thousands + "K";
        } else {
            return String.valueOf(number);
        }
    }



    @NonNull
    @Override
    public artistDetailsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater .inflate(R.layout.recyclerview_artist_row,parent, false);
        return new artistDetailsRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull artistDetailsRecyclerViewAdapter.MyViewHolder holder, int position) {

        Glide.with(context)
                .load(artistList.get(position).getArtistImg())
                .into(holder.artistImage);

        Glide.with(context)
                .load(artistList.get(position).getAlbumsImgs().get(0))
                .into(holder.albumImage1);

        Glide.with(context)
                .load(artistList.get(position).getAlbumsImgs().get(1))
                .into(holder.albumImage2);

        Glide.with(context)
                .load(artistList.get(position).getAlbumsImgs().get(2))
                .into(holder.albumImage3);

        String artistFollowers = String.valueOf(formatFollowers(artistList.get(position).getFollowers())) + " Followers";
        holder.artistFollowers.setText(artistFollowers);
        holder.artistName.setText(artistList.get(position).getArtistName());
        holder.artistName.post(new Runnable() {
            @Override
            public void run() {
                holder.artistName.setSelected(true);
            }
        });

        SpannableString content = new SpannableString("Open Spotify Link");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        content.setSpan(new ForegroundColorSpan(context.getColor(R.color.customGreen)), 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.SpotifyLink.setText(content);

        final String url = artistList.get(position).getSpotifyLink();

// Set an OnClickListener for the text view to open the link in browser
        holder.SpotifyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });


        holder.popularityPercent.setText(String.valueOf(artistList.get(position).getPopularity()));
        holder.progressBar.setProgress((artistList.get(position).getPopularity()));


    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView artistImage;
        ImageView albumImage1;
        ImageView albumImage2;
        ImageView albumImage3;
        TextView artistFollowers;
        TextView SpotifyLink;
        TextView popularityPercent;
        TextView artistName;

        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.artistDetail_Name);
            artistImage = itemView.findViewById(R.id.artistDetail_artistImg);
            albumImage1 = itemView.findViewById(R.id.artistDetail_albumImg1);
            albumImage2 = itemView.findViewById(R.id.artistDetail_albumImg2);
            albumImage3 = itemView.findViewById(R.id.artistDetail_albumImg3);
            artistFollowers = itemView.findViewById(R.id.artistDetail_followers);
            SpotifyLink = itemView.findViewById(R.id.artistDetails_spotifyLink);
            popularityPercent = itemView.findViewById(R.id.artistDetail_popularityPercent);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
