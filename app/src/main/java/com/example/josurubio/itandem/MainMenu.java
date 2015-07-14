package com.example.josurubio.itandem;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.view.View;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.android.Facebook;
import com.facebook.android.Util;
import com.facebook.internal.Utility;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import java.util.ArrayList;
import java.util.Map;
import Classes.ItemObject;
import Classes.NavigationAdapter;
import Classes.imageManager;

public class MainMenu extends TabActivity implements AdapterView.OnItemClickListener {
    private DrawerLayout drawerLayout;
    //The listview that will contain the different options of the Navigation Drawer.
    private ListView listView;
    //the tabhost of the main menu
    TabHost myTBH;
    //options of the Navigation Drawer
    //private String [] options = {"My Profile", "Discovery Preferences", "Lo Otro"};
    private String[] options;
    // this is the bar that is on the top of screen, it has the button to open the navigation drawer and the setting button
    private ActionBarDrawerToggle toggle;
    private ArrayList<ItemObject> listItems;
    private TypedArray listIcons;
    NavigationAdapter navAdapter;
    Bundle extras;


    Intent findTIntent;
    protected String latitude = "", longitude = "";

    Firebase myFirebaseRef;
    TabHost.TabSpec findTSpec;
    int distanceTandem = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main_menu);
        myFirebaseRef = new Firebase("https://blazing-fire-2203.firebaseio.com/Tandem");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listView = (ListView) findViewById(R.id.listView);
        myTBH = getTabHost();





        // we set the behaviour of the action bar when it's opened and closed
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(getResources().getString(R.string.app_name));
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(getString(R.string.options));
                invalidateOptionsMenu();
            }

        };
        extras = getIntent().getExtras();
        Intent service = new Intent(this, MessagePullService.class);
        service.putExtra("id", extras.getString("id"));
        this.startService(service);

        //we declare the header that the list will use
        final View header = getLayoutInflater().inflate(R.layout.header, null);
        /*
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
               Map<String, Object> user = (Map<String, Object>) snapshot.child(extras.getString("id")).getValue();
                //Bitmap pic = imageManager.getResizedBitmap(imageManager.decodeBase64(user.get("image").toString()),150,150);
                String aaa = (String) user.get("name");
                //BitmapDrawable ob = new BitmapDrawable(getResources(), pic);
                //header.setBackground(ob);
                Toast.makeText(getApplicationContext(), user.get("name").toString(), toast.LENGTH_SHORT).show();



            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        */
/*
        mFirebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> message = (Map<String, Object>) dataSnapshot.getValue();
                if (message.get("receptorID").toString().equals(extras.getString("id").toString())){
                    Toast.makeText(MainMenu.this, "Dentro aquiiii", Toast.LENGTH_SHORT).show();

                    NotificationManager notiMan = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                    Notification note = new Notification(R.drawable.profileimage, "You have a new Tandem Message from " + message.get("author"), System.currentTimeMillis());

                    Intent launchAct = new Intent(MainMenu.this, ChatActivity.class);
                    launchAct.putExtra("from", message.get("author").toString());
                    launchAct.putExtra("to", message.get("receptor").toString());
                    launchAct.putExtra("fromID", message.get("authorID").toString());
                    launchAct.putExtra("toID", message.get("receptorID").toString());

                    PendingIntent i = PendingIntent.getActivity(MainMenu.this, 0, launchAct,0);

                    note.setLatestEventInfo(MainMenu.this, "You have a new Tandem Message from ", message.get("message").toString(), i);

                    notiMan.notify(9999, note);

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
        */


        myFirebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
               if (dataSnapshot.getKey().equals(extras.getString("id"))){

                   Bitmap pic = imageManager.getResizedBitmap(imageManager.decodeBase64(user.get("image").toString()),150,150);
                   BitmapDrawable ob = new BitmapDrawable(getResources(), pic);
                   header.setBackground(ob);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                if (dataSnapshot.getKey().equals(extras.getString("id"))){

                    Bitmap pic = imageManager.getResizedBitmap(imageManager.decodeBase64(user.get("image").toString()),150,150);
                    BitmapDrawable ob = new BitmapDrawable(getResources(), pic);
                    header.setBackground(ob);
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


        //we set the header in the list
        listView.addHeaderView(header);

        //we get the images from the drawable
        listIcons = getResources().obtainTypedArray(R.array.navigation_icons);
        //We get the titles form the string-array
        options = getResources().getStringArray(R.array.nav_options);
        //The lis of title in the navigation list
        listItems = new ArrayList<ItemObject>();
        //we add the ItemObject to the array
        //My Tandem Preferences
        listItems.add(new ItemObject(options[0], listIcons.getResourceId(0, -1)));
        //Share iTandem
        listItems.add(new ItemObject(options[1], listIcons.getResourceId(1, -1)));
        //Logout
        listItems.add(new ItemObject(options[2], listIcons.getResourceId(2, -1)));


        //we create the NavigationAdapter and we use the listItems to fill it
        navAdapter = new NavigationAdapter(this, listItems);
        listView.setAdapter(navAdapter);


        // we edit some functionality of the action bar
        listView.setItemsCanFocus(false);
        listView.setOnItemClickListener(this);
        drawerLayout.setDrawerListener(toggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0xff2082b1));



        // we fill the tabhost with the different options of the menu
        findTSpec = myTBH.newTabSpec("FIND TANDEM");
        findTIntent = new Intent(this, FindTandem.class);
        findTIntent.putExtra("name", extras.getString("name"));
        findTIntent.putExtra("id", extras.getString("id"));
        //findTIntent.putExtra("latitude", extras.getString("latitude"));
        //findTIntent.putExtra("longitude", extras.getString("longitude"));
        findTIntent.putExtra("latitude", latitude);
        findTIntent.putExtra("longitude", longitude);
        findTIntent.putExtra("distance", distanceTandem+"");
        findTSpec.setContent(findTIntent);
        findTSpec.setIndicator(getString(R.string.find_tandem));

        TabHost.TabSpec tandemPSpec = myTBH.newTabSpec("TANDEM PLACE");
        tandemPSpec.setIndicator(getString(R.string.title_activity_tandem_place));
        Intent tandemPIntent = new Intent(this, TandemPlace.class);
        tandemPIntent.putExtra("name", extras.getString("name"));
        tandemPIntent.putExtra("id", extras.getString("id"));
        tandemPSpec.setContent(tandemPIntent);


        TabHost.TabSpec messageSpec = myTBH.newTabSpec("MESSAGES");
        Intent messagesIntent = new Intent(this, Message.class);
        messagesIntent.putExtra("name", extras.getString("name"));
        messagesIntent.putExtra("id", extras.getString("id"));
        messageSpec.setContent(messagesIntent);
        messageSpec.setIndicator(getString(R.string.title_activity_message));



        myTBH.addTab(findTSpec);
        myTBH.addTab(tandemPSpec);
        myTBH.addTab(messageSpec);


    }

    protected void onPostCreated(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    /*
    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event = to accurately track the time people spend in iTandem
        AppEventsLogger.deactivateApp(this);
    }
    */
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
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        listView.setItemChecked(position, true);
        drawerLayout.closeDrawer(listView);

        switch (position) {
            case 1: {
                Bundle extras = getIntent().getExtras();
                Intent myPref = new Intent(this, MyTandemPreferences.class);
                myPref.putExtra("id", extras.getString("id"));
                startActivityForResult(myPref, 1);
                break;
            }
            case 2: {
                try{
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "iTandem");
                    String sAux = "\n Download this application and learn, discover new languages with people around you!!\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Choose an application to share iTandem"));
                }
                catch (Exception e){

                }
                break;
            }
            case 3: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setTitle("Logout from iTandem")
                        .setMessage("Are you sure you want to log out from iTandem")
                        .setIcon(R.drawable.logo)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Yes button clicked, do something
                                // find the active session which can only be facebook in my app
                                Session session = Session.getActiveSession();

                                // run the closeAndClearTokenInformation which does the following
                                // DOCS : Closes the local in-memory Session object and clears any persistent
                                // cache related to the Session.
                                session.closeAndClearTokenInformation();
                                // return the user to the login screen
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                // make sure the user can not access the page after he/she is logged out
                                // clear the activity stack
                                finish();
                                myFirebaseRef.child(extras.getString("id")).removeValue();
                            }
                        })
                        .setNegativeButton("No", null)						//Do nothing on no
                        .show();



            } break;
            default: {

                Bundle extras = getIntent().getExtras();
                String tName = extras.getString("name");
                Intent profile = new Intent(this, MyProfile.class);
                profile.putExtra("action", 0);
                profile.putExtra("name", tName);
                profile.putExtra("id", extras.getString("id"));
                startActivity(profile);
                break;

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1){

            distanceTandem = Integer.parseInt(data.getStringExtra("distance"));
            findTIntent.putExtra("distance", distanceTandem+"");

        }


    }




}
