package com.example.josurubio.itandem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import AndroidChat.TandemEncounter;
import Classes.imageManager;


public class ShowProfile extends Activity {
    Button im;
    Boolean found;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        final Bundle extras = getIntent().getExtras();
        final String name = extras.get("nameAge").toString().substring(0, extras.get("nameAge").toString().length() - 3);

        final String fromId = extras.getString("idLogedUser");
        final String toId = extras.getString("id");
        final String logedUser = extras.get("name").toString();
        final String IKnowLang = extras.get("ISpeak").toString();
        final String ILearnLang = extras.get("ILearn").toString();
        final TextView nameAge = (TextView)findViewById(R.id.t1nameAge);
        final Intent chat = new Intent(this, ChatActivity.class);
        im = (Button)findViewById(R.id.foundUserPic);
        found = false;
        final Firebase mFirebaseRef = new Firebase("https://blazing-fire-2203.firebaseio.com/");
        final Firebase mFirebaseRef2 = new Firebase("https://blazing-fire-2203.firebaseio.com/TandemEncounter");

        nameAge.setText(extras.get("nameAge").toString());

        Button tandemB = (Button)findViewById(R.id.tandemButton);
        tandemB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mFirebaseRef2.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Map<String, Object> tp = (Map<String, Object>) dataSnapshot.getValue();
                        if ((tp.get("author").toString().equals(fromId) && tp.get("receptor").toString().equals(toId)) || (tp.get("author").toString().equals(toId) && tp.get("receptor").toString().equals(fromId))) {
                            found = true;
                        }

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Map<String, Object> tp = (Map<String, Object>) dataSnapshot.getValue();
                        if ((tp.get("author").toString().equals(fromId) && tp.get("receptor").toString().equals(toId)) || (tp.get("author").toString().equals(toId) && tp.get("receptor").toString().equals(fromId))) {
                            found = true;
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

                // if the user does not exist...
                if (!found) {
                    //we put some extra data on the next activity
                    chat.putExtra("from", logedUser);
                    chat.putExtra("to", name);
                    chat.putExtra("fromID", fromId);
                    chat.putExtra("toID", toId);
                    //we get the current time
                    String timeStamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
                    //we create the TandemEncounter instance
                    TandemEncounter enc = new TandemEncounter(fromId, toId, name, timeStamp, false);
                    //we create a new instance of TandemEncounter to get later the ID
                    Firebase newTandRef = mFirebaseRef2.push();
                    //we store the new TandemEncounter on the Firebase root
                    newTandRef.setValue(enc);
                    //we get the created TandemEncounter's ID
                    String encounterID = newTandRef.getKey();
                    //we put the extra data on the chat activity that is about to be launched
                    chat.putExtra("encounterID", encounterID);
                    //we start the new activity
                    startActivity(chat);
                }
                else
                    Toast.makeText(getApplicationContext(), getString(R.string.repeated_encounter) + " " + name, Toast.LENGTH_SHORT).show();



            }
        });

        final TextView lk = (TextView) findViewById(R.id.t4knownLang);
        final TextView ll = (TextView) findViewById(R.id.t5);
        final TextView extract = (TextView) findViewById(R.id.t7extractcontent);

        mFirebaseRef.child("Tandem").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                if (dataSnapshot.getKey().equals(extras.getString("id"))) {
                    Bitmap pic = imageManager.decodeBase64(user.get("image").toString());
                    Drawable d = new BitmapDrawable(getResources(), pic);
                    im.setBackground(d);

                    extract.setText("- " + user.get("extract").toString());
                    lk.setText("- " + IKnowLang);
                    ll.setText("- " + ILearnLang);

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                if (dataSnapshot.getKey().equals(extras.getString("id"))) {

                    extract.setText("- " + user.get("extract").toString());
                    lk.setText("- " + IKnowLang);
                    ll.setText("- " + ILearnLang);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_profile, menu);
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
