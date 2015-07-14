package com.example.josurubio.itandem;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Classes.GPSTracker;

public class FindTandemPlace extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Geocoder gc;
    Double longitude = 0.0;
    Double latitude = 0.0;
    Firebase tandemPlaceRef;
    TextView title;
    TextView description;
    TextView when;
    TextView members;
    String encounterID = "";
    String from = "";
    String fromID= "";
    Button join;
    String tpID = "";
    Map<String, Boolean> newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tandem_place);
        setUpMapIfNeeded();
        Firebase.setAndroidContext(this);
        newUser = new HashMap<String, Boolean>();


        gc = new Geocoder(getApplicationContext());
        Button goB = (Button) findViewById(R.id.goButton2);
        tandemPlaceRef = new Firebase("https://blazing-fire-2203.firebaseio.com/TandemPlaces");
        final EditText search = (EditText)findViewById(R.id.searchPlaceEdit2);
        join = (Button)findViewById(R.id.joinTandemGroupButton);

        title = (TextView)findViewById(R.id.tandemPlaceTitle2);
        description = (TextView)findViewById(R.id.tandemPlaceDescription2);
        when = (TextView)findViewById(R.id.whenFrecuencytext2);
        members = (TextView) findViewById(R.id.numberMemberText);
        from = getIntent().getStringExtra("name");
        fromID = getIntent().getStringExtra("id");
        goB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String place = search.getText().toString();
                try {
                    List<Address> loc = gc.getFromLocationName(place, 1);
                    Address location = loc.get(0);

                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    tandemPlaceRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Map<String, Object> tp = (Map<String, Object>) dataSnapshot.getValue();
                            Double latitude2 = (Double) tp.get("latitude");
                            Double longitude2 = (Double) tp.get("longitude");
                            float[] dist = new float[1];

                            Location.distanceBetween(latitude, longitude, latitude2, longitude2, dist);
                            //Here we check if the distance between the loged user and the found user is less than the discovery preferences criteria
                            if (dist[0] / 1000 <= 30) {

                                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude2, longitude2)).title(tp.get("title").toString()));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));

                            }

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newUser.put(fromID, true);
                tandemPlaceRef.child(tpID).child("tandemUsers").setValue(newUser);

                Intent tpm = new Intent(FindTandemPlace.this, TandemPlaceMessages.class);
                tpm.putExtra("encounterID", encounterID);
                tpm.putExtra("from", from);
                tpm.putExtra("fromID", fromID);

                startActivity(tpm);

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
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker) {
                    tandemPlaceRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Map<String, Object> tp = (Map<String, Object>) dataSnapshot.getValue();

                            if (tp.get("longitude").toString().equals(marker.getPosition().longitude + "") && tp.get("latitude").toString().equals(marker.getPosition().latitude + "") && tp.get("title").equals(marker.getTitle())){
                                mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng((Double) tp.get("latitude"), (Double) tp.get("longitude"))));
                                title.setText(tp.get("title").toString());
                                description.setText(tp.get("description").toString());
                                when.setText(tp.get("when").toString());
                                members.setText(dataSnapshot.child("tandemUsers").getChildrenCount()+"");
                                encounterID = dataSnapshot.getKey();
                                tpID = dataSnapshot.getKey();

                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    return false;
                }
            });
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
}
