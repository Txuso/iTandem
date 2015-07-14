package com.example.josurubio.itandem;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by josu on 11/06/15.
 */
public class MessagePullService extends IntentService {
    private Firebase mFirebaseRef;
    private static final String FIREBASE_URL = "https://blazing-fire-2203.firebaseio.com/Tandem";
    Bundle extras;
    ArrayList<String> conv;
    String id;



    public MessagePullService() {
        super("test-service");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        extras = intent.getExtras();
        id = extras.getString("id");
        mFirebaseRef = new Firebase(FIREBASE_URL);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mFirebaseRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Map<String, Object> tandem = (Map<String, Object>) dataSnapshot.getValue();
                        /*
                        if (dataSnapshot.getKey().equals(id)) {

                            if (tandem.get("newMessage").toString().equals("true")) {

                                NotificationManager notiMan = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                                String name = tandem.get("newMessageValue").toString().substring(0, tandem.get("newMessageValue").toString().indexOf(":-:"));
                                String value = tandem.get("newMessageValue").toString();
                                String[] parts = value.split("\\^");
                                String name = parts[0];
                                String fromID = parts[1];
                                String encounterID = parts[2];


                                Notification note = new Notification(R.drawable.profileimage, "You have a new Tandem Message from " + "Josu", System.currentTimeMillis());

                                Intent launchAct = new Intent(MessagePullService.this, ChatActivity.class);
                                launchAct.putExtra("to", tandem.get("name").toString());
                                launchAct.putExtra("toID", id);
                                launchAct.putExtra("from", name);
                                launchAct.putExtra("fromID", fromID);
                                launchAct.putExtra("encounterID", encounterID);
                                mFirebaseRef.child(id).child("newMessage").setValue(false);
                                mFirebaseRef.child(id).child("newMessageValue").setValue("");
                                PendingIntent i = PendingIntent.getActivity(MessagePullService.this, 0, launchAct, 0);
                                //  String message = tandem.get("newMessageValue").toString().substring(tandem.get("newMessageValue").toString().indexOf(":-:"), tandem.get("newMessageValue").toString().length());
                                note.setLatestEventInfo(MessagePullService.this, "You have a new Tandem Message from ", "Aqui y alliiii", i);

                                notiMan.notify(9999, note);
                            }
                        }
                        */
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        final Map<String, Object> tandem = (Map<String, Object>) dataSnapshot.getValue();
                        if (dataSnapshot.getKey().equals(id))
                            if (!tandem.get("newMessageValue").toString().equals(" ^ ^ ")) {

                                NotificationManager notiMan = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                String value = tandem.get("newMessageValue").toString();
                                String[] parts = value.split("\\^");
                                String name = parts[0];

                                String fromID = parts[1];
                                String encounterID = parts[2];


                                Notification note = new Notification(R.drawable.logo, "You have a new Tandem Message from " + "", System.currentTimeMillis());

                                Intent launchAct = new Intent(MessagePullService.this, ChatActivity.class);
                                launchAct.putExtra("to", name);
                                launchAct.putExtra("toID", fromID);
                                launchAct.putExtra("from", tandem.get("name").toString());
                                launchAct.putExtra("fromID", id);
                                launchAct.putExtra("encounterID", encounterID);
                                launchAct.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                mFirebaseRef.child(id).child("newMessage").setValue(false);
                                PendingIntent i = PendingIntent.getActivity(MessagePullService.this, 0, launchAct, PendingIntent.FLAG_UPDATE_CURRENT);
                                note.flags |= Notification.FLAG_AUTO_CANCEL;
                                note.setLatestEventInfo(MessagePullService.this, "You have a new Tandem Message from " + name, "Click here to see the message", i);
                                notiMan.notify(9999, note);


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
        }, 0, 10000);





    }

}
