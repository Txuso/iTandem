package com.example.josurubio.itandem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import Classes.CustomTandemListViewAdapter;
import Classes.RoundedImageView;
import Classes.TandemListRowItem;
import Classes.imageManager;


public class FindTandemResult extends Activity implements AdapterView.OnItemClickListener{
    Firebase mFirebaseRef;
    Firebase languagesRef;

    ArrayList<String> langLearnList;
    String sl = "";
    String ll = "";

    Bundle extra;
    ListView listView;
    List<TandemListRowItem> rowItems;
    int distanceTandem;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tandem_result);

        distanceTandem = 0;
        latitude = 0;
        longitude = 0;
        sl = getString(R.string.I_speak);
        ll= getString(R.string.I_want_to_learn);
        rowItems = new ArrayList<TandemListRowItem>();
        mFirebaseRef = new Firebase("https://blazing-fire-2203.firebaseio.com/Tandem");
        languagesRef = new Firebase("https://blazing-fire-2203.firebaseio.com/Language");
        extra = getIntent().getExtras();
        final String[] filters = extra.getStringArray("find");

        distanceTandem = Integer.parseInt(extra.getString("distance"));

        longitude = Double.parseDouble(extra.getString("longitude"));
        latitude = Double.parseDouble(extra.getString("latitude"));
        mFirebaseRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(final DataSnapshot snapshot, String s) {

                    if (filters[0] != null){
                    for (final DataSnapshot child : snapshot.child("langKnown").getChildren()) {

                        if (filters[0].equals(child.getKey())) {
                            Map<String, Object> user = (Map<String, Object>) snapshot.getValue();

                            double latitude2 = Double.parseDouble(user.get("latitude").toString());
                            double longitude2 = Double.parseDouble(user.get("longitude").toString());
                            double distance = distance(latitude,longitude, latitude2, longitude2, 'K');

                            //Here we check if the distance between the loged user and the found user is less than the discovery preferences criteria
                                if (distance <= distanceTandem) {
                                    Bitmap pic = imageManager.getResizedBitmap(imageManager.decodeBase64(user.get("image").toString()), 80, 80);
                                    Bitmap picRounded = RoundedImageView.getCroppedBitmap(pic, 300);
                                    BitmapDrawable ima = new BitmapDrawable(getApplicationContext().getResources(), picRounded);

                                    final TandemListRowItem item = new TandemListRowItem(ima, user.get("name") + " " + user.get("age"), sl, ll, snapshot.getKey());
                                    item.setknownLang(item.getknownLang() + child.getKey() + " ");

                                    for (final DataSnapshot langLearn : snapshot.child("langLearn").getChildren()) {
                                        item.setSpeakLang(item.getSpeakLang() + langLearn.getKey() + " ");

                                    }
                                    rowItems.add(item);
                                    listView.invalidateViews();
                                }
                            }

                        }
                    }
                    sl = getString(R.string.I_speak);
                    ll = getString(R.string.I_want_to_learn);

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


            listView = (ListView) findViewById(R.id.tandemList);
            CustomTandemListViewAdapter adapter = new CustomTandemListViewAdapter(this,
                    R.layout.tandem_list_item, rowItems);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_tandem_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

         Intent profile = new Intent(this, ShowProfile.class);
        profile.putExtra("idLogedUser", extra.getString("id"));
        profile.putExtra("id", rowItems.get(position).getId());
        profile.putExtra("ISpeak", rowItems.get(position).getknownLang().replace(getString(R.string.I_speak), ""));
        profile.putExtra("ILearn", rowItems.get(position).getSpeakLang().replace(getString(R.string.I_want_to_learn), ""));
        profile.putExtra("name", extra.getString("name"));
        profile.putExtra("nameAge", rowItems.get(position).getnameAge());
       startActivity(profile);
    }

    //This method calculates the exact distance between two points and it returns the number depending on the unit
    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
