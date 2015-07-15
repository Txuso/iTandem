package com.example.josurubio.itandem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import java.util.Map;
import Classes.CustomAdapter;
import Classes.Model;


public class FindTandem extends Activity {
    Bundle extra;
    Firebase tandemRef;
    Firebase tRef;
    int distanceTandem;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tandem);

        extra = getIntent().getExtras();
        tandemRef = new Firebase("https://blazing-fire-2203.firebaseio.com/Language");
        tRef = new Firebase("https://blazing-fire-2203.firebaseio.com/Tandem");
        final Model[] modelItems = new Model[6];
        populateList(modelItems);
        distanceTandem = 0;
        latitude = 0;
        longitude = 0;

        ListView itemss = (ListView) findViewById(R.id.kakakaka);

        modelItems[0] = new Model(getString(R.string.catalan), 0, "");
        modelItems[1] = new Model(getString(R.string.english), 0, "");
        modelItems[2] = new Model(getString(R.string.spanish), 0, "");
        modelItems[3] = new Model(getString(R.string.basque), 0, "");
        modelItems[4] = new Model(getString(R.string.french), 0, "");
        modelItems[5] = new Model(getString(R.string.italian), 0, "");

        tandemRef.addChildEventListener(new ChildEventListener() {
            int i = 0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> hizk2 = (Map<String, Object>) dataSnapshot.getValue();
                //We include the code of each language
                modelItems[i].setId(dataSnapshot.getKey());
                i++;

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

        tRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> tandem = (Map<String, Object>) dataSnapshot.getValue();
                if (dataSnapshot.getKey().equals(extra.getString("id"))){
                    distanceTandem = Integer.parseInt(tandem.get("distance").toString());
                    longitude = Double.parseDouble(tandem.get("longitude").toString());
                    latitude = Double.parseDouble(tandem.get("latitude").toString());

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Map<String, Object> tandem = (Map<String, Object>) dataSnapshot.getValue();
                if (dataSnapshot.getKey().equals(extra.getString("id"))){
                    distanceTandem = Integer.parseInt(tandem.get("distance").toString());
                    longitude = Double.parseDouble(tandem.get("longitude").toString());
                    latitude = Double.parseDouble(tandem.get("latitude").toString());

                }
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

        CustomAdapter adapter2 = new CustomAdapter(this, modelItems);
        itemss.setAdapter(adapter2);

        //The activity to be triggered
        final Intent result = new Intent(this, FindTandemResult.class);

        Button findTandem = (Button)findViewById(R.id.findTandemButton);
        findTandem.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //We fill the filters depending on the chosen languages
                final String[] filters = fillCriteria(modelItems);
                result.putExtra("find",filters );
                result.putExtra("name", extra.getString("name"));
                result.putExtra("id", extra.getString("id"));
                result.putExtra("distance", distanceTandem+"");
                result.putExtra("latitude", latitude+"");
                result.putExtra("longitude", longitude+"");

                startActivity(result);
            }
        });

    }

    // Method that returns a String[] with the selected languages
    private String[] fillCriteria(Model[] modelItems) {
        String[] filters = new String[modelItems.length];
        int i = 0;
        for (int j = 0; j < modelItems.length; j++){

            if (modelItems[j].getValue() == 1) {
                filters[i] = modelItems[j].getName();
                i++;
            }
        }
        return filters;
    }
    private void populateList(final Model[] modelItems) {
        tandemRef.addChildEventListener(new ChildEventListener() {
            int i = 0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> hizk2 = (Map<String, Object>) dataSnapshot.getValue();
                modelItems[i] = new Model(hizk2.get(getString(R.string.name)).toString(),0, dataSnapshot.getKey());
                i++;

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}