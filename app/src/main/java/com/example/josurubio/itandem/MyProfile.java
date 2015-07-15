package com.example.josurubio.itandem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import Classes.GPSTracker;
import Classes.TandemUser;
import Classes.imageManager;


public class MyProfile extends Activity {

    ArrayList<String> langKnown = new ArrayList<String>();
    ArrayList<String> langLearn = new ArrayList<String>();
    ImageButton im;
    Firebase mFirebaseRef;
    Firebase languagesRef;
    protected String latitude = "", longitude = "";
    GPSTracker gps;



    private static int RESULT_LOAD_IMAGE = 2;

     Bundle extras;
    TandemUser tandemUser = new TandemUser("",0,null,langKnown,langLearn,"", "", "", 50 , "");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        this.setTitle("My Profile");
        extras = getIntent().getExtras();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0xff2082b1));
        final Button langKnow = (Button) findViewById(R.id.buttonSpeak);
        Button langLearn = (Button) findViewById(R.id.buttonLearn);
        Button extractButton = (Button) findViewById(R.id.buttonExtract);
        im = (ImageButton)findViewById(R.id.profilePic);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        tandemUser.setName(extras.get("name").toString());

        //Test users
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.profileimage);
        mFirebaseRef = new Firebase("https://blazing-fire-2203.firebaseio.com/Tandem");
        languagesRef = new Firebase("https://blazing-fire-2203.firebaseio.com/Language");

        Firebase newTandRef = mFirebaseRef.push();
        Bitmap bmm = BitmapFactory.decodeResource(getResources(), R.drawable.profileimage);
        String imm = imageManager.encodeTobase64(bmm);
        TandemUser tandemUser1 = new TandemUser("Ander", 25, imm,langKnown,langKnown, "kln", "-2.5037039", "43.1873519", 50, "");
        newTandRef.setValue(tandemUser1);
        final String newID = newTandRef.getKey();


        final TextView nameAge = (TextView)findViewById(R.id.nameAge);
        final TextView extract = (TextView) findViewById(R.id.extractContent);
        final TextView langKnownText = (TextView) findViewById(R.id.knownlang);
        final TextView speakLangText = (TextView) findViewById(R.id.langLearn);
        gps = new GPSTracker(MyProfile.this);
        if(gps.canGetLocation()) {

            latitude = gps.getLatitude() + "";
            mFirebaseRef.child(extras.getString("id")).child("latitude").setValue(latitude);
            longitude = gps.getLongitude() + "";
            mFirebaseRef.child(extras.getString("id")).child("longitude").setValue(longitude);

        } else {
            gps.showSettingsAlert();
        }

        mFirebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();

                if (dataSnapshot.getKey().equals(extras.getString("id"))) {
                    String age = user.get("age").toString();
                    String extractNew = user.get("extract").toString();

                    tandemUser.setAge(Integer.parseInt(age));
                    tandemUser.setExtract(extractNew);

                    Bitmap pic = imageManager.decodeBase64(user.get("image").toString());
                    BitmapDrawable ob = new BitmapDrawable(getResources(), pic);

                    im.setBackground(ob);

                    nameAge.setText(tandemUser.getName() + " - " + tandemUser.getAge());
                    extract.setText(extractNew);

                    langKnownText.setText("- ");

                    for (final DataSnapshot hizk : dataSnapshot.child("langKnown").getChildren()) {

                        langKnownText.setText(langKnownText.getText() + hizk.getKey() + ", ");
                    }

                    speakLangText.setText("- ");

                    for (final DataSnapshot hizk2 : dataSnapshot.child("langLearn").getChildren()) {

                        speakLangText.setText(speakLangText.getText() + hizk2.getKey() + ", ");

                    }


                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();

                if (dataSnapshot.getKey().equals(extras.getString("id"))) {
                    extract.setText("- " + user.get("extract").toString());

                    langKnownText.setText("- ");

                    for (final DataSnapshot hizk : dataSnapshot.child("langKnown").getChildren()) {

                        langKnownText.setText(langKnownText.getText() + hizk.getKey() + ", ");
                    }

                    speakLangText.setText("- ");

                    for (final DataSnapshot hizk2 : dataSnapshot.child("langLearn").getChildren()) {

                        speakLangText.setText(speakLangText.getText() + hizk2.getKey() + ", " );

                    }

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



        extract.setText(tandemUser.getExtract());



            final Intent edit = new Intent(this, ProfileEditInfo.class);

            langKnow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    edit.putExtra("kind", 0);
                    edit.putExtra("name", tandemUser.getName());
                    edit.putExtra("testID", newID);
                    edit.putExtra("id", extras.getString("id"));
                    startActivityForResult(edit, 1);
                }
            });

            langLearn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    edit.putExtra("kind", 1);
                    edit.putExtra("name", tandemUser.getName());
                    edit.putExtra("testID", newID);
                    edit.putExtra("id", extras.getString("id"));
                    startActivityForResult(edit, 1);
                }
            });

            extractButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    edit.putExtra("kind", 2);
                    edit.putExtra("text", tandemUser.getExtract());
                    edit.putExtra("name", tandemUser.getName());
                    edit.putExtra("id", extras.getString("id"));
                    startActivity(edit);
                }
            });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      switch (item.getItemId()) {
          case android.R.id.home: {

              //NavUtils.navigateUpFromSameTask(this);
              onBackPressed();
              return true;
          }
          default:
              return super.onOptionsItemSelected(item);

      }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data){
            Uri selectedImage  = data.getData();
            String[] filePathColum = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColum, null, null, null);

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColum[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap selectedIm = BitmapFactory.decodeFile(picturePath);

            try {

                Bitmap image = imageManager.decodeUri(this, selectedImage, 375);
                im.setImageBitmap(image);

                String imageFile = imageManager.encodeTobase64(selectedIm);
                mFirebaseRef.child(extras.getString("id")).child("image").setValue(imageFile);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
