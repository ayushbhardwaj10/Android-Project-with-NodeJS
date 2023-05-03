package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Fragments.FavouriteFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

public class favRecyclerViewAdapter extends RecyclerView.Adapter<favRecyclerViewAdapter.MyViewHolder >{
    Context context;
    ArrayList<EventModel> eventList;

    public favRecyclerViewAdapter(Context context, ArrayList<EventModel> eventList){
        this.context = context;
        this.eventList = eventList;
    }
    public void updateData(ArrayList<EventModel> data) {
        eventList = data;
    }
    public void updateDataRecycler(ArrayList<EventModel> data) {
        eventList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public favRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater .inflate(R.layout.recyclerview_event_row,parent, false);
        return new favRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull favRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.eventName.setText(eventList.get(position).getEventName());
        holder.venueName.setText(eventList.get(position).getVenueName());
        holder.categoryName.setText(eventList.get(position).getCategoryName());
        holder.date.setText(eventList.get(position).getDate());
        holder.time.setText(eventList.get(position).getTime());
        // Load the image using Glide
        Glide.with(context)
                .load(eventList.get(position).getImgUrl())
                .into(holder.eventImage);

        holder.eventName.post(new Runnable() {
            @Override
            public void run() {
                holder.eventName.setSelected(true);
            }
        });
        holder.venueName.post(new Runnable() {
            @Override
            public void run() {
                holder.venueName.setSelected(true);
            }
        });
        // Marking the events favourite if found in shared favoru
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favEvents", "");
        Type type = new TypeToken<ArrayList<EventModel>>(){}.getType();
        ArrayList<EventModel> currentFavList = gson.fromJson(json, type);

        if (currentFavList != null) {
            for (EventModel event : currentFavList) {
                if (event.getId().equals(eventList.get(holder.getAdapterPosition()).getId())) {
                    holder.favIconImage.setImageResource(R.drawable.baseline_favorite_24);
                }
            }
        }
//        ######## Clear shared preferences ###### to use this comment line 66-77
//        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();
//        ########################################
        holder.favIconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//             System.out.println(eventList.get(position).getEventName());
                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
                Gson gson = new Gson();

                // if already present in shared preferences object already present
                if (sharedPreferences.contains("favEvents")) {
                    String json = sharedPreferences.getString("favEvents", "");
                    Type type = new TypeToken<ArrayList<EventModel>>(){}.getType();
                    ArrayList<EventModel> currentFavList = gson.fromJson(json, type);
                    // adding current event to favourite list

                    // checking if event ID already exists in sharedPreferences, if Yes do not add, else add it
                    boolean eventAlreadyPresent = false;
                    for (EventModel event : currentFavList) {
                        if (event.getId().equals(eventList.get(holder.getAdapterPosition()).getId())) {
                            eventAlreadyPresent = true;
                        }
                    }

                    if(eventAlreadyPresent==false){
                        currentFavList.add(eventList.get(holder.getAdapterPosition()));
                        json = gson.toJson(currentFavList);
                        SharedPreferences.Editor editor = context.getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                        editor.putString("favEvents", json);
                        editor.apply();
                        System.out.println("Current events count :" + currentFavList.size());
                        holder.favIconImage.setImageResource(R.drawable.baseline_favorite_24);
                        String eventName = eventList.get(holder.getAdapterPosition()).getEventName();
                        Toast.makeText(context, eventName +" added to favourites", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // if already exists, remove from the favourites.
                        System.out.println("Already Exist");

                        Iterator<EventModel> iterator = currentFavList.iterator();
                        while (iterator.hasNext()) {
                            EventModel event = iterator.next();
                            if (event.getId().equals(eventList.get(holder.getAdapterPosition()).getId())) {
                                // Remove the item from the list used to populate the RecyclerView
                                iterator.remove();
                            }
                        }



                        json = gson.toJson(currentFavList);
                        SharedPreferences.Editor editor = context.getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                        editor.putString("favEvents", json);
                        editor.apply();
                        holder.favIconImage.setImageResource(R.drawable.favempty);
//                        notifyItemRemoved(holder.getAdapterPosition());
//                        notifyItemRangeChanged(holder.getAdapterPosition(), eventList.size());

                        String eventName = eventList.get(holder.getAdapterPosition()).getEventName();
                        Toast.makeText(context, eventName +" removed from favourites", Toast.LENGTH_SHORT).show();

                        // updating the recycler view
//                        FavouriteFragment ff = new FavouriteFragment();
//                        FavouriteFragment.fragmentManager.beginTransaction().replace(R.id.favouriteFragmentConstraint,ff).commit();


                    }

                }else {
                    // creating a new favourite list
                    ArrayList<EventModel> newFavList = new ArrayList<>();
                    newFavList.add(eventList.get(holder.getAdapterPosition()));
                    String json = gson.toJson(newFavList);
                    SharedPreferences.Editor editor = context.getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                    editor.putString("favEvents", json);
                    editor.apply();
                    System.out.println("Current events count :" + newFavList.size());
                    holder.favIconImage.setImageResource(R.drawable.baseline_favorite_24);
                    String eventName = eventList.get(holder.getAdapterPosition()).getEventName();
                    Toast.makeText(context, eventName +" added to favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView eventImage;
        TextView eventName;
        TextView venueName;
        TextView categoryName;
        TextView date;
        TextView time;

        ImageView favIconImage;

        public CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.eventImg);
            eventName = itemView.findViewById(R.id.eventName);
            venueName = itemView.findViewById(R.id.venue);
            categoryName = itemView.findViewById(R.id.category);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            favIconImage = itemView.findViewById(R.id.favIcon);
        }
    }
}
