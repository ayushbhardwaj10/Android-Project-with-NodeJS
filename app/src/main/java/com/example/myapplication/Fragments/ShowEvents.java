package com.example.myapplication.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.EventModel;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.eventsRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowEvents extends Fragment {


    ArrayList<EventModel> eventList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get Shared Preferences instance
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_show_events, null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_show_events, container, false);

        ProgressBar loadingIndicator = view.findViewById(R.id.progressBarEventDetails);

        SharedPreferences sharedPrefs = getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);


        // Retrieve values from Shared Preferences
        String keyword = sharedPrefs.getString("keyword", "");
        String locationHash = sharedPrefs.getString("locationHash", "");
        String distance = sharedPrefs.getString("distance", "");
        String category = sharedPrefs.getString("category", "");

        System.out.println("Inside Show Events ::::::");
        System.out.println(keyword);
        System.out.println(locationHash);
        System.out.println(distance);
        System.out.println(category);

        // Calling API to fetch events list
        String FetchEventsurl = "http://nodejsapp-csci571.us-west-2.elasticbeanstalk.com/eventsTicketMaster";

        // Build the URL with the query parameters
        Uri.Builder builder = Uri.parse(FetchEventsurl).buildUpon()
                .appendQueryParameter("keyword", keyword)
                .appendQueryParameter("geopoint", locationHash)
                .appendQueryParameter("radius", distance)
                .appendQueryParameter("segmentID", category);
        String finalUrl = builder.build().toString();

        // Create a request queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        loadingIndicator.setVisibility(View.VISIBLE);

        // Create a string request to get the JSON response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the JSON response
                        loadingIndicator.setVisibility(View.INVISIBLE);

                        String jsonString = response;
                        try {
                            // Convert the JSON string to a JSONObject
                            JSONObject jsonObject = new JSONObject(jsonString);
                            // Get the value of the _embedded key
                            JSONArray eventsArray = jsonObject.getJSONObject("_embedded").getJSONArray("events");



                            for (int i = 0; i < eventsArray.length(); i++) {
                                JSONObject eventObject = eventsArray.getJSONObject(i);
                                String eventName = eventObject.getString("name");
                                String id =eventObject.getString("id");
                                String venueName = eventObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                                String categoryName = category;
                                String dateArray[] = eventObject.getJSONObject("dates").getJSONObject("start").getString("localDate").split("-");
                                String date = dateArray[1]+"/"+dateArray[2]+"/"+dateArray[0];
                                String timeTempFormat = eventObject.getJSONObject("dates").getJSONObject("start").getString("localTime");
                                String time = convertToAmPm(timeTempFormat);
                                String imgURL = eventObject.getJSONArray("images").getJSONObject(0).getString("url");
                                System.out.println("event b :"+i+" name :" + eventName);
                                System.out.println("event b:"+i+" venueName :" + venueName);
                                System.out.println("event b:"+i+" categoryName :" + categoryName);
                                System.out.println("event b:"+i+" date :" + date);
                                System.out.println("event b:"+i+" time :" + time);
                                System.out.println("event b:"+i+" imgURL :" + imgURL);
                                System.out.println("event b:"+i+" id :" + id);
                                eventList.add(new EventModel(eventName,venueName,categoryName,date,time,imgURL,id));

                                // Find the RecyclerView in the layout using its ID

                            }
                            System.out.println("Before recycle View...");
                            if(eventList.size()>0){
                                System.out.println("more events events");
                                TextView noEventFav = view.findViewById(R.id.backBtnNoEvent);
                                noEventFav.setVisibility(View.INVISIBLE);
                            }
                            else {
                                System.out.println("No events");
                                TextView noEventFav = view.findViewById(R.id.backBtnNoEvent);
                                noEventFav.setVisibility(View.VISIBLE);
                            }

                            RecyclerView recyclerView = view.findViewById(R.id.activityRecyclerView);
                            eventsRecyclerViewAdapter adapter  = new eventsRecyclerViewAdapter(getContext(),eventList );
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


                        } catch (JSONException e) {
                            System.out.println("no events");
                            TextView noEventFav = view.findViewById(R.id.backBtnNoEvent);
                            noEventFav.setVisibility(View.VISIBLE);

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
            }
        });

        // Add the request to the queue
        queue.add(stringRequest);

        Button backButton = view.findViewById(R.id.BackBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public String convertToAmPm(String timeString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");
        Date date = null;
        try {
            date = inputFormat.parse(timeString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (date != null) {
            return outputFormat.format(date);
        }
        return null;
    }


}