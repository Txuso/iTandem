package com.example.josurubio.itandem;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import Classes.TandemPlaceClass;

public class CreateTandemPlace extends FragmentActivity{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Geocoder gc;
    Double longitude;
    Double latitude;
    Firebase tandemPlaceRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tandem_place);
        setUpMapIfNeeded();

        // the Geocoder will help us to get locations information such us name...
        gc = new Geocoder(getApplicationContext());

        //button to search a place
        Button goB = (Button) findViewById(R.id.goButton);

        //Button to create a new Tandem Place
        Button createB = (Button) findViewById(R.id.createTPButton);

        //Edit text in which we write the place to create
        final EditText search = (EditText)findViewById(R.id.searchPlaceEdit);
        tandemPlaceRef = new Firebase("https://blazing-fire-2203.firebaseio.com/TandemPlaces");
        final String creatroID = getIntent().getExtras().getString("id");

        final EditText title = (EditText)findViewById(R.id.tandemPlaceTitle);
        final EditText description = (EditText)findViewById(R.id.tandemPlaceDescription);
        final EditText when = (EditText)findViewById(R.id.whenFrecuencytext);

        //Event that is triggered when we find a place to create the Tandem Place
        goB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String place = search.getText().toString();
                try {
                    //we get the location from the address.
                    List<Address> loc = gc.getFromLocationName(place, 1);
                    Address location = loc.get(0);

                    //we get the coordinates from the location
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    //we draw a mark and make zoom to the place we want to create
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(title.getText().toString()));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //Event triggered when we click on Create Button
        createB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //We include the current user into the participants
                HashMap<String,Boolean > participants = new HashMap<String, Boolean>();
                participants.put(creatroID, true);
                //We create the Tandem Place adding it to the Firebase Reference and we close the activity
                TandemPlaceClass tp = new TandemPlaceClass(creatroID, title.getText().toString(), description.getText().toString(),
                        when.getText().toString(), latitude, longitude, participants);
                tandemPlaceRef.push().setValue(tp);
                finish();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {


                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location location) {
                        //We draw a marker in our current position
                        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("It's Me!"));


                    }
                });

            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

    }
    /** A method to download json data from url */




}
