package com.example.myapplication.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.ArtistModal;
import com.example.myapplication.R;
import com.example.myapplication.VenueModal;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsFragment extends Fragment {

    public String EventType ="";   // to decide if it's 'music' event to show artistDetails or now

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_details, container, false);

        ProgressBar loadingIndicator = view.findViewById(R.id.progressBarEventDetails);
        ConstraintLayout detailsLayout = view.findViewById(R.id.detailsConstraintLayout);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String eventId = sharedPreferences.getString("eventID", "");

        ArrayList<ArtistModal> artistDetailsList = new ArrayList<>();

        // ####################### Event Details API ################################

        // Calling API to fetch events details
        String FetchEventsurl = "http://nodejsapp-csci571.us-west-2.elasticbeanstalk.com/eventDetailsTicketMaster";

        // Build the URL with the query parameters
        Uri.Builder builder = Uri.parse(FetchEventsurl).buildUpon()
                .appendQueryParameter("id", eventId);
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

                        String jsonString = response;
                        try {
                            // Convert the JSON string to a JSONObject
                            JSONObject jsonObject = new JSONObject(jsonString);
                            // Get the value of the _embedded key
                            ArrayList<String> artistNameList = new ArrayList<String>();
                            String artistName="",venue="",date="",time="", genre="",priceRange="",ticketStatus="",buyTicketURL="",seatMapURL="";
                            try {
                                JSONArray jsonArray = jsonObject.getJSONObject("_embedded").getJSONArray("attractions");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject nameObject = jsonArray.getJSONObject(i);
                                    artistName = artistName + nameObject.getString("name")+" | ";
                                    artistNameList.add(nameObject.getString("name"));
                                }
                                artistName= artistName.substring(0,artistName.length()-3);
                            } catch (Exception e){
                                artistName="";
                                System.out.println("artistName not found");
                            }

                            try {
                                venue = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                            } catch (Exception e){
                                venue="";
                                System.out.println("venue not found");
                            }
                            String venueDetail[] = {venue};
                            try {
                                date = jsonObject.getJSONObject("dates").getJSONObject("start").getString("localDate");
                                date = convertDateTime(date);
                            } catch (Exception e){
                                date="";
                                System.out.println("date not found");
                            }
                            try {
                                time = jsonObject.getJSONObject("dates").getJSONObject("start").getString("localTime");
                                time = convertTimeFormat(time);
                            } catch (Exception e){
                                time="";
                                System.out.println("time not found");
                            }

                            String genre1="", genre2="",genre3="";
                            try {
                                genre1 = jsonObject.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name");
                            } catch (Exception e){
                                genre1="";
                                System.out.println("genre1 not found");
                            }
                            try {
                                genre2 = jsonObject.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
                            } catch (Exception e){
                                genre2="";
                                System.out.println("genre2 not found");
                            }
                            try {
                                genre3 = jsonObject.getJSONArray("classifications").getJSONObject(0).getJSONObject("subGenre").getString("name");
                            } catch (Exception e){
                                genre3="";
                                System.out.println("genre3 not found");
                            }
                            genre = genre1;
                            if(genre2.length()>0)
                               genre = genre + " | " + genre2;
                            if(genre3.length()>0)
                                genre = genre + " | " + genre3;

                            String price1="", price2="";

                            try {
                                price1 = jsonObject.getJSONArray("priceRanges").getJSONObject(0).getString("min");
                            } catch (Exception e){
                                price1="";
                                System.out.println("price1 not found");
                            }
                            try {
                                price2 = jsonObject.getJSONArray("priceRanges").getJSONObject(0).getString("max");
                            } catch (Exception e){
                                price2="";
                                System.out.println("price2 not found");
                            }
                            priceRange = price1;
                            if(price2.length()>0)
                                priceRange = priceRange+" - " + price2+" (USD)";

                            try {
                                ticketStatus = jsonObject.getJSONObject("dates").getJSONObject("status").getString("code");
                            } catch (Exception e){
                                ticketStatus="";
                                System.out.println("ticketStatus not found");
                            }
                            //let eventType = eventDetails['_embedded']['attractions'][0]['classifications'][0]['segment']['name'];
                            try {
                                EventType = jsonObject.getJSONObject("_embedded").getJSONArray("attractions").getJSONObject(0).getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
                            } catch (Exception e){
                                EventType="";
                                System.out.println("eventType not found");
                            }


                            if(ticketStatus.equals("onsale"))
                                ticketStatus="On Sale";

                            if(ticketStatus.equals("rescheduled"))
                                ticketStatus="rescheduled";

                            if(ticketStatus.equals("offsale"))
                                ticketStatus="Off Sale";

                            if(ticketStatus.equals("Canceled"))
                                ticketStatus="Canceled";

                            if(ticketStatus.equals("Postponed"))
                                ticketStatus="Postponed";


                            try {
                                buyTicketURL = jsonObject.getString("url");
                            } catch (Exception e){
                                buyTicketURL="";
                                System.out.println("buyTicketURL not found");
                            }
                            try {
                                seatMapURL = jsonObject.getJSONObject("seatmap").getString("staticUrl");
                            } catch (Exception e){
                                seatMapURL="";
                                System.out.println("Seatmap not found");
                            }

                            System.out.println(artistName);
                            TextView detailTicketStatusRes = view.findViewById(R.id.detail_artistNameRes);
                            detailTicketStatusRes.post(new Runnable() {
                                public void run() {
                                    detailTicketStatusRes.setSelected(true);
                                }
                            });
                            detailTicketStatusRes.setText(artistName);

                            System.out.println(venue);
                            TextView venueRes = view.findViewById(R.id.detail_venueRes);
                            venueRes.post(new Runnable() {
                                public void run() {
                                    venueRes.setSelected(true);
                                }
                            });
                            venueRes.setText(venue);

                            System.out.println(date);
                            TextView dateRes = view.findViewById(R.id.detail_dateRes);
                            dateRes.post(new Runnable() {
                                public void run() {
                                    dateRes.setSelected(true);
                                }
                            });
                            dateRes.setText(date);

                            System.out.println(time);
                            TextView timeRes = view.findViewById(R.id.detail_timeRes);
                            timeRes.post(new Runnable() {
                                public void run() {
                                    timeRes.setSelected(true);
                                }
                            });
                            timeRes.setText(time);

                            System.out.println(genre);
                            TextView genreRes = view.findViewById(R.id.detail_genreRes);
                            genreRes.post(new Runnable() {
                                public void run() {
                                    genreRes.setSelected(true);
                                }
                            });
                            genreRes.setText(genre);

                            System.out.println(priceRange);
                            TextView priceRangeRes = view.findViewById(R.id.detail_priceRangeRes);
                            priceRangeRes.post(new Runnable() {
                                public void run() {
                                    priceRangeRes.setSelected(true);
                                }
                            });
                            priceRangeRes.setText(priceRange);

                            System.out.println(ticketStatus);

                            TextView ticketStatusRes = view.findViewById(R.id.detail_ticketStatusRes);
                            ticketStatusRes.post(new Runnable() {
                                public void run() {
                                    ticketStatusRes.setSelected(true);
                                }
                            });
                            TextView textView = view.findViewById(R.id.detail_ticketStatusRes);

                            if(ticketStatus.equals("On Sale"))
                              textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ticketstatusgreen));
                            else if(ticketStatus.equals("rescheduled") || ticketStatus.equals("Postponed") )
                                textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ticketstatusorange));
                            else if(ticketStatus.equals("Off Sale"))
                                textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ticketstatusred));
                            else textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ticketstatusblack));

                            ticketStatusRes.setText(ticketStatus);

                            System.out.println(buyTicketURL);
                            TextView buyAtRes = view.findViewById(R.id.detail_buyAtRes);
                            buyAtRes.post(new Runnable() {
                                public void run() {
                                    buyAtRes.setSelected(true);
                                }
                            });
                            SpannableString underlinedText = new SpannableString(buyTicketURL);
                            underlinedText.setSpan(new UnderlineSpan(), 0, buyTicketURL.length(), 0);
                            underlinedText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.customGreen)), 0, buyTicketURL.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            buyAtRes.setText(underlinedText);

                            buyAtRes.setAutoLinkMask(Linkify.WEB_URLS);

                            buyAtRes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String url = buyAtRes.getText().toString();
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(url));
                                    startActivity(intent);
                                }
                            });


                            //buyAtRes.setText(buyTicketURL);
                            ImageView imageViewRes = view.findViewById(R.id.seatMapRes);
                            if(seatMapURL.length()!=0){
                                Picasso.get()
                                        .load(seatMapURL)
                                        .into(imageViewRes);
                            }
                            System.out.println(seatMapURL);
                            System.out.println("EventType :" +EventType);



                            // ####################### Artist Event API ################################

                            System.out.println("before if :"+ artistNameList.size());
                            System.out.println("EventType 2nd" + EventType);
                            if(EventType.equals("Music")){
                                final int[] count = {0};
                                for(int i=0; i<artistNameList.size();i++){
                                    System.out.println("inside loop");
                                    String FetchEventsurl = "http://nodejsapp-csci571.us-west-2.elasticbeanstalk.com/SpotifyArtistDetails";

                                    // Build the URL with the query parameters
                                    Uri.Builder builder = Uri.parse(FetchEventsurl).buildUpon()
                                            .appendQueryParameter("artistName", artistNameList.get(i));
                                    String finalUrl = builder.build().toString();


                                    // Create a request queue
                                    RequestQueue queue = Volley.newRequestQueue(getContext());
                                    // Create a string request to get the JSON response
                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, finalUrl,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    // Handle the JSON response
                                                    String jsonString = response;
                                                    ArrayList<String> imgList = new ArrayList<>();
                                                    String artistID="";
                                                    String artistImgURL="";
                                                    String artistName="";
                                                    int followers=0;
                                                    int popularity=0;
                                                    String spotifyLink="";
                                                    try {
                                                        // Convert the JSON string to a JSONObject
                                                        JSONObject jsonObject = new JSONObject(jsonString);
                                                        // Get the value of the _embedded key
                                                        JSONArray jsonArray =new JSONArray(jsonObject.getString("albumsImgs"));
                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            String url = jsonArray.getString(i);
                                                            imgList.add(url);
                                                        }
                                                        artistID=jsonObject.getString("artistID");
                                                        artistImgURL = jsonObject.getString("artistImg");
                                                        artistName = jsonObject.getString("artistName");
                                                        followers = jsonObject.getInt("followers");
                                                        popularity = jsonObject.getInt("popularity");
                                                        spotifyLink = jsonObject.getString("spotifyLink");

                                                        System.out.println("Artist Details of 0 :");
                                                        System.out.println(imgList);
                                                        System.out.println(artistID);
                                                        System.out.println(artistImgURL);
                                                        System.out.println(artistName);
                                                        System.out.println(followers);
                                                        System.out.println(popularity);
                                                        System.out.println(spotifyLink);
                                                        artistDetailsList.add(new ArtistModal(imgList,artistID,artistImgURL,artistName,followers,popularity,spotifyLink));

//                                                        // ############# After All Artist Details Fetched ##################
                                                        System.out.println(String.valueOf(count[0]) +":*:"+ String.valueOf(artistNameList.size()-1));
                                                        if(count[0] == artistNameList.size()-1){
                                                            System.out.println("Final index :" + artistDetailsList.size());
                                                            Gson gson = new Gson();
                                                            String json = gson.toJson(artistDetailsList);
                                                            SharedPreferences.Editor editor = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                                                            editor.putString("artistListDetails", json);
                                                            editor.apply();



                                                            // ####################### Venue Details API ################################

                                                            // Calling API to fetch events list
                                                            String FetchEventsurl = "http://nodejsapp-csci571.us-west-2.elasticbeanstalk.com/getVenueDetails";

                                                            // Build the URL with the query parameters
                                                            Uri.Builder builder = Uri.parse(FetchEventsurl).buildUpon()
                                                                    .appendQueryParameter("venue", venueDetail[0]);
                                                            String finalUrl = builder.build().toString();

                                                            // Create a request queue
                                                            RequestQueue queue = Volley.newRequestQueue(getContext());

                                                            // Create a string request to get the JSON response
                                                            StringRequest stringRequest = new StringRequest(Request.Method.GET, finalUrl,
                                                                    new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            // Handle the JSON response
                                                                            System.out.println("inside venue response");
                                                                            String jsonString = response;
                                                                            try {
                                                                                // Convert the JSON string to a JSONObject
                                                                                JSONObject jsonObject = new JSONObject(jsonString);
                                                                                // Get the value of the _embedded key
                                                                                String venueDetails_venueName="",venueDetails_address="",venueDetails_cityState="",venueDetails_contactInfo="",venueDetails_openHours="",venueDetails_generalRule="",venueDetails_childRule="";
                                                                                try{
                                                                                    venueDetails_venueName = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                                                                                } catch(Exception e){
                                                                                    venueDetails_venueName="";
                                                                                }
                                                                                try{
                                                                                    venueDetails_address = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("address").getString("line1");

                                                                                }catch (Exception e){
                                                                                    venueDetails_address="";
                                                                                }
                                                                                try{
                                                                                    venueDetails_cityState = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("city").getString("name")+", "+ jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("state").getString("name");

                                                                                }catch (Exception e){
                                                                                    venueDetails_cityState="";
                                                                                }
                                                                                try{
                                                                                    venueDetails_contactInfo = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("boxOfficeInfo").getString("phoneNumberDetail");

                                                                                }catch (Exception e){
                                                                                    venueDetails_contactInfo="";
                                                                                }
                                                                                try{
                                                                                    venueDetails_openHours = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("boxOfficeInfo").getString("openHoursDetail");

                                                                                }catch (Exception e){
                                                                                    venueDetails_openHours="";
                                                                                }
                                                                                try{
                                                                                    venueDetails_generalRule = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("generalInfo").getString("generalRule");

                                                                                }catch (Exception e){
                                                                                    venueDetails_generalRule="";
                                                                                }
                                                                                try{
                                                                                    venueDetails_childRule = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("generalInfo").getString("childRule");

                                                                                }catch (Exception e){
                                                                                    venueDetails_childRule="";
                                                                                }

                                                                                double lat = 0.0,lang=0.0;

                                                                                try{
                                                                                    lat = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("location").getDouble("latitude");

                                                                                }catch (Exception e){
                                                                                    lat = 0.0;
                                                                                }
                                                                                try{
                                                                                    lang = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("location").getDouble("longitude");

                                                                                }catch (Exception e){
                                                                                    lang=0.0;
                                                                                }

                                                                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                                editor.putFloat("lat", (float) lat);
                                                                                editor.putFloat("lang", (float) lang);
                                                                                editor.apply();


                                                                                System.out.println("Venue Details *******");
                                                                                System.out.println(venueDetails_venueName);
                                                                                System.out.println(venueDetails_address);
                                                                                System.out.println(venueDetails_cityState);
                                                                                System.out.println(venueDetails_contactInfo);
                                                                                System.out.println(venueDetails_openHours);
                                                                                System.out.println(venueDetails_generalRule);
                                                                                System.out.println(venueDetails_childRule);

                                                                                // put to sharedPreferences
                                                                                sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                                                                editor = sharedPreferences.edit();
                                                                                editor.putString("venueDetails_venueName", venueDetails_venueName);
                                                                                editor.putString("venueDetails_address", venueDetails_address);
                                                                                editor.putString("venueDetails_cityState", venueDetails_cityState);
                                                                                editor.putString("venueDetails_contactInfo", venueDetails_contactInfo);
                                                                                editor.putString("venueDetails_openHours", venueDetails_openHours);
                                                                                editor.putString("venueDetails_generalRule", venueDetails_generalRule);
                                                                                editor.putString("venueDetails_childRule", venueDetails_childRule);
                                                                                editor.apply();






                                                                                loadingIndicator.setVisibility(View.INVISIBLE);
                                                                                detailsLayout.setVisibility(View.VISIBLE);




                                                                            } catch (JSONException e) {

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

                                                        }
                                                        count[0] = count[0] +1;

                                                    }  catch (JSONException e) {

                                                        e.printStackTrace();
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // Handle the error
                                        }
                                    });

                                    System.out.println("Index is :"+ i);

                                    // Add the request to the queue
                                    queue.add(stringRequest);
                                }
                            }
                            else {
                                Gson gson = new Gson();
                                artistDetailsList.clear();
                                String json = gson.toJson(artistDetailsList);
                                SharedPreferences.Editor editor = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                                editor.putString("artistListDetails", json);
                                editor.apply();
                                loadingIndicator.setVisibility(View.INVISIBLE);
                                detailsLayout.setVisibility(View.VISIBLE);



                                // ####################### Venue Details API ################################

                                System.out.println("No artist Data :" + venueDetail[0]);
                                // Calling API to fetch events list
                                String FetchEventsurl = "http://nodejsapp-csci571.us-west-2.elasticbeanstalk.com/getVenueDetails";

                                // Build the URL with the query parameters
                                Uri.Builder builder = Uri.parse(FetchEventsurl).buildUpon()
                                        .appendQueryParameter("venue", venueDetail[0]);
                                String finalUrl = builder.build().toString();

                                // Create a request queue
                                RequestQueue queue = Volley.newRequestQueue(getContext());

                                // Create a string request to get the JSON response
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, finalUrl,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                // Handle the JSON response
                                                System.out.println("inside venue response");
                                                String jsonString = response;
                                                try {
                                                    // Convert the JSON string to a JSONObject
                                                    JSONObject jsonObject = new JSONObject(jsonString);
                                                    // Get the value of the _embedded key
                                                    String venueDetails_venueName="",venueDetails_address="",venueDetails_cityState="",venueDetails_contactInfo="",venueDetails_openHours="",venueDetails_generalRule="",venueDetails_childRule="";
                                                    try{
                                                        venueDetails_venueName = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                                                    } catch(Exception e){
                                                        venueDetails_venueName="";
                                                    }
                                                    try{
                                                        venueDetails_address = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("address").getString("line1");

                                                    }catch (Exception e){
                                                        venueDetails_address="";
                                                    }
                                                    try{
                                                        venueDetails_cityState = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("city").getString("name")+", "+ jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("state").getString("name");

                                                    }catch (Exception e){
                                                        venueDetails_cityState="";
                                                    }
                                                    try{
                                                        venueDetails_contactInfo = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("boxOfficeInfo").getString("phoneNumberDetail");

                                                    }catch (Exception e){
                                                        venueDetails_contactInfo="";
                                                    }
                                                    try{
                                                        venueDetails_openHours = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("boxOfficeInfo").getString("openHoursDetail");

                                                    }catch (Exception e){
                                                        venueDetails_openHours="";
                                                    }
                                                    try{
                                                        JSONObject jsonObj = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("generalInfo");
                                                        venueDetails_generalRule = jsonObj.getString("generalRule");

                                                    }catch (Exception e){
                                                        venueDetails_generalRule="";
                                                    }
                                                    try{
                                                        venueDetails_childRule = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("generalInfo").getString("childRule");

                                                    }catch (Exception e){
                                                        venueDetails_childRule="";
                                                    }


                                                    System.out.println("Venue Details *******");
                                                    System.out.println(venueDetails_venueName);
                                                    System.out.println(venueDetails_address);
                                                    System.out.println(venueDetails_cityState);
                                                    System.out.println(venueDetails_contactInfo);
                                                    System.out.println(venueDetails_openHours);
                                                    System.out.println(venueDetails_generalRule);
                                                    System.out.println(venueDetails_childRule);

                                                    // put to sharedPreferences

                                                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("venueDetails_venueName", venueDetails_venueName);
                                                    editor.putString("venueDetails_address", venueDetails_address);
                                                    editor.putString("venueDetails_cityState", venueDetails_cityState);
                                                    editor.putString("venueDetails_contactInfo", venueDetails_contactInfo);
                                                    editor.putString("venueDetails_openHours", venueDetails_openHours);
                                                    editor.putString("venueDetails_generalRule", venueDetails_generalRule);
                                                    editor.putString("venueDetails_childRule", venueDetails_childRule);
                                                    editor.apply();



                                                    loadingIndicator.setVisibility(View.INVISIBLE);
                                                    detailsLayout.setVisibility(View.VISIBLE);



                                                } catch (JSONException e) {

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


                            }




//



                        } catch (JSONException e) {

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

        return view;
    }
    public static String convertDateTime(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        return localDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
    }
    public static String convertTimeFormat(String time) {
        try {
            // parse input time string to 24-hour format
            SimpleDateFormat sdfInput = new SimpleDateFormat("HH:mm:ss", Locale.US);
            Date date = sdfInput.parse(time);

            // convert to 12-hour format with AM/PM indicator
            SimpleDateFormat sdfOutput = new SimpleDateFormat("h:mm a", Locale.US);
            return sdfOutput.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return time; // return input string as is in case of error
        }
    }


}