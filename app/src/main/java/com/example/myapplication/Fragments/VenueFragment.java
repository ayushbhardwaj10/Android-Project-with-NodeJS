package com.example.myapplication.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.EventModel;
import com.example.myapplication.R;
import com.example.myapplication.VenueModal;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class VenueFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_venue, container, false);

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("venueDetails_venueName", venueDetails_venueName);
//        editor.putString("venueDetails_address", venueDetails_address);
//        editor.putString("venueDetails_cityState", venueDetails_cityState);
//        editor.putString("venueDetails_contactInfo", venueDetails_contactInfo);
//        editor.putString("venueDetails_openHours", venueDetails_openHours);
//        editor.putString("venueDetails_generalRule", venueDetails_generalRule);
//        editor.putString("venueDetails_childRule", venueDetails_childRule);
//        editor.apply();

          SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
          String venueDetails_venueName = sharedPreferences.getString("venueDetails_venueName", "");
          String venueDetails_address = sharedPreferences.getString("venueDetails_address", "");
          String venueDetails_cityState = sharedPreferences.getString("venueDetails_cityState", "");
          String venueDetails_contactInfo = sharedPreferences.getString("venueDetails_contactInfo", "");
          String venueDetails_openHours = sharedPreferences.getString("venueDetails_openHours", "");
          String venueDetails_generalRule = sharedPreferences.getString("venueDetails_generalRule", "");
          String venueDetails_childRule = sharedPreferences.getString("venueDetails_childRule", "");

          System.out.println("data in venue Tab :");
          System.out.println("venueDetails_venueName :"+ venueDetails_venueName);
          System.out.println("venueDetails_address :"+ venueDetails_address);
          System.out.println("venueDetails_cityState :"+ venueDetails_cityState);
          System.out.println("venueDetails_contactInfo :"+ venueDetails_contactInfo);
          System.out.println("venueDetails_openHours :"+ venueDetails_openHours);
          System.out.println("venueDetails_generalRule :"+ venueDetails_generalRule);
          System.out.println("venueDetails_childRule :"+ venueDetails_childRule);

         TextView venueNameTextView = view.findViewById(R.id.venueName);
         venueNameTextView.setText(venueDetails_venueName);
         venueNameTextView.post(new Runnable() {
             public void run() {
                 venueNameTextView.setSelected(true);
             }
           });

        TextView venueAddressTextView = view.findViewById(R.id.venueAddress);
        venueAddressTextView.setText(venueDetails_address);
        venueAddressTextView.post(new Runnable() {
            public void run() {
                venueAddressTextView.setSelected(true);
            }
        });

        TextView venuecityStateTextView = view.findViewById(R.id.venueCityState);
        venuecityStateTextView.setText(venueDetails_cityState);
        venuecityStateTextView.post(new Runnable() {
            public void run() {
                venuecityStateTextView.setSelected(true);
            }
        });

        TextView venueContactTextView = view.findViewById(R.id.venueContactInfo);
        venueContactTextView.setText(venueDetails_contactInfo);
        venueContactTextView.post(new Runnable() {
            public void run() {
                venueContactTextView.setSelected(true);
            }
        });

        TextView openHoursTextView = view.findViewById(R.id.venueDetails_openHours);
        openHoursTextView.setText(venueDetails_openHours);
        openHoursTextView.setOnClickListener(new View.OnClickListener() {
            boolean isExpanded = false;

            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    // If the TextView is already expanded, shrink it back
                    openHoursTextView.setMaxLines(3);
                    openHoursTextView.setEllipsize(TextUtils.TruncateAt.END);
                    isExpanded = false;
                } else {
                    // If the TextView is not expanded, expand it
                    openHoursTextView.setMaxLines(Integer.MAX_VALUE);
                    openHoursTextView.setEllipsize(null);
                    isExpanded = true;
                }
            }
        });

        TextView generalRulesTextView = view.findViewById(R.id.venueDetails_generalRules);
        generalRulesTextView.setText(venueDetails_generalRule);
        generalRulesTextView.setOnClickListener(new View.OnClickListener() {
            boolean isExpanded = false;

            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    // If the TextView is already expanded, shrink it back
                    generalRulesTextView.setMaxLines(3);
                    generalRulesTextView.setEllipsize(TextUtils.TruncateAt.END);
                    isExpanded = false;
                } else {
                    // If the TextView is not expanded, expand it
                    generalRulesTextView.setMaxLines(Integer.MAX_VALUE);
                    generalRulesTextView.setEllipsize(null);
                    isExpanded = true;
                }
            }
        });

        TextView childRulesTextView = view.findViewById(R.id.venueDetails_childRules);
        childRulesTextView.setText(venueDetails_childRule);
        childRulesTextView.setOnClickListener(new View.OnClickListener() {
            boolean isExpanded = false;

            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    // If the TextView is already expanded, shrink it back
                    childRulesTextView.setMaxLines(3);
                    childRulesTextView.setEllipsize(TextUtils.TruncateAt.END);
                    isExpanded = false;
                } else {
                    // If the TextView is not expanded, expand it
                    childRulesTextView.setMaxLines(Integer.MAX_VALUE);
                    childRulesTextView.setEllipsize(null);
                    isExpanded = true;
                }
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        double lat = (double) sharedPreferences.getFloat("lat", 0);
        double lang = (double) sharedPreferences.getFloat("lang", 0);

        System.out.println("Maps Coordinates :");
        System.out.println(lat);
        System.out.println(lang);



        // Add a marker for the given latitude and longitude values
        LatLng location = new LatLng(lat, lang);
        googleMap.addMarker(new MarkerOptions().position(location));

        // Move the camera to the marker location
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}