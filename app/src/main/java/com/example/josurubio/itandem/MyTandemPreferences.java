package com.example.josurubio.itandem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import Classes.CustomAdapter;
import Classes.GPSTracker;
import Classes.Model;


public class MyTandemPreferences extends Activity {

    int progressValue = 50;
    Double longitude;
    Double latitude;
    Geocoder gc;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_preferences);
        this.setTitle(getString(R.string.title_activity_my_preferences));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0xff2082b1));

        final Firebase tandemRef = new Firebase("https://blazing-fire-2203.firebaseio.com/Tandem");
        Button goB = (Button) findViewById(R.id.goButton3);
        Button myLocButton = (Button) findViewById(R.id.myLocButton);
        final EditText search = (EditText)findViewById(R.id.editText2);
        gc = new Geocoder(this.getApplicationContext());

        goB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String place = search.getText().toString();
                try {
                    List<Address> loc = gc.getFromLocationName(place, 1);
                    Address location = loc.get(0);
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    tandemRef.child(getIntent().getExtras().getString("id")).child("latitude").setValue(latitude);
                    tandemRef.child(getIntent().getExtras().getString("id")).child("longitude").setValue(longitude);

                   Toast.makeText(getApplicationContext(), getString(R.string.you_will_find_encounters) + " " + location.getLocality(), Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        myLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gps = new GPSTracker(MyTandemPreferences.this);
                if(gps.canGetLocation()) {

                    tandemRef.child(getIntent().getExtras().getString("id")).child("latitude").setValue(gps.getLatitude());
                    tandemRef.child(getIntent().getExtras().getString("id")).child("longitude").setValue(gps.getLongitude());

                    Toast.makeText(getApplicationContext(), getString(R.string.you_will_find_encounters) + " " + getString(R.string.you_will_find_encounters_in_your_current_location), Toast.LENGTH_SHORT).show();


                } else {
                    gps.showSettingsAlert();
                }
            }
        });

        SeekBar bar = (SeekBar)findViewById(R.id.seekBar);
        bar.setMax(50);

        final TextView dist = (TextView)findViewById(R.id.tandemDistanceText);


        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
                dist.setText(getString(R.string.tandem_distance) + "      " + progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button save = (Button)findViewById(R.id.saveChangesButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tandemRef.child(getIntent().getStringExtra("id")).child("distance").setValue(progressValue);
                Toast.makeText(getApplicationContext(), getString(R.string.you_will_find_encounters) + " " + progressValue + getString(R.string.km), Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                intent.putExtra("distance", progressValue+"");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_preferences, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                //NavUtils.navigateUpFromSameTask(this);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);


        }
    }
}
