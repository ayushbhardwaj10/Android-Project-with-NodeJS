package com.example.myapplication.Fragments;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myapplication.MainActivity;
import com.example.myapplication.SearchMainActivity;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Arrays;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SearchFragment extends Fragment {

    String[] items = {"Default", "Music", "Sports", "Arts & Theatre", "Film", "Miscellaneous"};
    LocationManager locationManager;
    private double latitude;
    private double longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Spinner spinner = view.findViewById(R.id.spinner);

        ProgressBar loadingIndicator = view.findViewById(R.id.loading_indicator);

        // Add any initialization code here
        final String[] fruits = {"Apple", "Apple2", "Apple3", "Apple4", "Apple5", "Banana", "Cherry", "Date", "Elderberry", "Fig", "Grape"};

        //Creating the instance of ArrayAdapter containing list of fruit names
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, fruits);

        //Getting the instance of AutoCompleteTextView
        final AutoCompleteTextView actv = (AutoCompleteTextView) view.findViewById(R.id.keyword);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        // Add a TextWatcher to the AutoCompleteTextView
        actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Make an HTTP request to the API to get the suggestions
                loadingIndicator.setVisibility(View.VISIBLE);
                String query = s.toString();
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = "http://nodejsapp-csci571.us-west-2.elasticbeanstalk.com/suggestTicketMaster?value=" + query;
                System.out.println("New keyword :" + s);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                loadingIndicator.setVisibility(View.INVISIBLE);
                                System.out.println("Response from API :");
                                System.out.println(response);
                                Gson gson = new Gson();
                                Type type = new TypeToken<String[]>() {
                                }.getType();
                                String[] suggestions = gson.fromJson(response, type);
                                System.out.println("Suggestions :");
                                for (String suggestion : suggestions) {
                                    System.out.println(suggestion);
                                }

                                // Update the adapter's data with the new suggestions
                                ArrayAdapter<String> adapter = (ArrayAdapter<String>) actv.getAdapter();
                                adapter.clear();
                                adapter.addAll(suggestions);
                                adapter.notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error in Response");
                        System.out.println(error);
                    }
                });
                queue.add(stringRequest);
            }


            @Override
            public void afterTextChanged(Editable editable) {
            }

        });

        actv.setTextColor(Color.WHITE);

        // Search Click Listener
        Button searchButton = view.findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Do something when the button is clicked
                System.out.println("New click....!!!!!! *******");
                String keyword = ((EditText) (view.findViewById(R.id.keyword))).getText().toString();
                String distance = ((EditText) (view.findViewById(R.id.distance))).getText().toString();
                Spinner spinner = view.findViewById(R.id.spinner);
                Switch mySwitch = view.findViewById(R.id.autoDetectSwitch);
                String category = spinner.getSelectedItem().toString();

                if(keyword.length()==0 || distance.length()==0)
                {
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                String location = "";
                String locationHash="";

//                MainActivity activity = (MainActivity) getActivity();
//                activity.changeViewPager(2);



                if(mySwitch.isChecked()) {
                      locationHash ="9q5ckq49c";


                } else {
                    //*********Assuming location typed is also "San Francisco")
                     location = ((EditText) (view.findViewById(R.id.location))).getText().toString();
                     if(location.length()==0) {
                         Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                         return;
                     }
                    locationHash="9q8yyk8yu";

                }

                // Get Shared Preferences instance
                SharedPreferences sharedPrefs = getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();

                // Add values to Shared Preferences
                editor.putString("keyword",keyword );
                editor.putString("locationHash", locationHash);
                editor.putString("distance", distance);
                editor.putString("category", category);

                // Save changes
                editor.apply();


                // change the intent to events list
                Intent intent = new Intent(getActivity(), SearchMainActivity.class);
                startActivity(intent);


            }

        });

        Switch mySwitch = view.findViewById(R.id.autoDetectSwitch);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    TextView myTextView = view.findViewById(R.id.location);
                    myTextView.setVisibility(View.GONE);
                } else {
                    // The switch is off

                    TextView myTextView = view.findViewById(R.id.location);
                    myTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        Button clearButton = view.findViewById(R.id.clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("inside clear");
                EditText keywordEditText = view.findViewById(R.id.keyword);
                TextView distanceTextView = view.findViewById(R.id.distance);
                Spinner spinner = view.findViewById(R.id.spinner);
                TextView locationTextView = view.findViewById(R.id.location);
                Switch autoDetectSwitch = view.findViewById(R.id.autoDetectSwitch);

                // Clear the EditText
                keywordEditText.setText("");

//                // Reset the distance TextView
//                distanceTextView.setText("10");
//
//                // Reset the spinner to its default value
//                spinner.setSelection(0);
//
//                // Clear the location TextView
//                locationTextView.setText("");
//
//                // Turn off the auto-detect switch
//                autoDetectSwitch.setChecked(false);
            }
        });




//        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, items);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                requireContext(), android.R.layout.simple_spinner_dropdown_item, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView) view).setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                return view;
            }
        };

        adapter2.setDropDownViewResource(R.layout.spinner_item);

        spinner.setAdapter(adapter2);


        return view;
    }

 }