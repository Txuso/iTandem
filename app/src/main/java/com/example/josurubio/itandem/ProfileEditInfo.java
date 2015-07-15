package com.example.josurubio.itandem;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Classes.CustomAdapter;
import Classes.Model;
import Classes.TandemUser;
import Classes.imageManager;


public class ProfileEditInfo extends Activity{

    ListView lv;
    Model[] modelItems;
    TextView inf;
    String lang = "";
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit_info);
        final EditText extract = (EditText)findViewById(R.id.extractEditText);
        extract.setVisibility(View.INVISIBLE);

        lv = (ListView) findViewById(R.id.findLanguagesList);
        lv.setVisibility(View.INVISIBLE);

        inf = (TextView)findViewById(R.id.textView7);
        inf.setVisibility(View.INVISIBLE);
        final Bundle extras = getIntent().getExtras();

        final Firebase tandemRef = new Firebase("https://blazing-fire-2203.firebaseio.com");
        final String newID = extras.getString("testID");
        final int menu = extras.getInt("kind");

        if (menu == 0 || menu == 1){

            inf.setVisibility(View.VISIBLE);
            if (menu == 0)
                inf.setText(R.string.languages_I_speak);
            if (menu == 1)
                inf.setText(R.string.languages_I_want_to_learn);
            lv.setVisibility(View.VISIBLE);
            modelItems = new Model[6];

            tandemRef.child("Language").addChildEventListener(new ChildEventListener() {

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

            CustomAdapter adapter = new CustomAdapter(this, modelItems);
            lv.setAdapter(adapter);

        }
        else
            if (menu == 2){
                extract.setVisibility(View.VISIBLE);
                extract.setText(extras.getString("text"));
            }


        Button accept = (Button)findViewById(R.id.buttonAccept);
        accept.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {


                switch (menu){
                    case 0:{

                        tandemRef.child("Tandem").child(extras.getString("id")).child("langKnown").removeValue();
                        checkLanguages(tandemRef,extras.getString("id"), 0);
                        tandemRef.child("Tandem").child(newID).child("langKnown").removeValue();
                        checkLanguages2(tandemRef,newID, 0 );

                    }break;
                    case 1:{

                        tandemRef.child("Tandem").child(extras.getString("id")).child("langLearn").removeValue();
                        checkLanguages(tandemRef,extras.getString("id"), 1);
                        tandemRef.child("Tandem").child(newID).child("langLearn").removeValue();
                        checkLanguages2(tandemRef,newID, 1);

                    }break;
                    case 2:
                        tandemRef.child("Tandem").child(extras.getString("id")).child("extract").setValue(extract.getText().toString());
                    break;

                }
                finish();

            }
        });

    }

    public void checkLanguages(Firebase ref, String id, int action){

        Map<String, Boolean> languages = new HashMap<String, Boolean>();

        for (int j = 0; j < modelItems.length; j++){
            if (modelItems[j].getValue() == 1) {
                languages.put(modelItems[j].getName(), true);
                if (action == 0) {
                   ref.child("Tandem").child(id).child("langKnown").setValue(languages);
                }
                else {
                    ref.child("Tandem").child(id).child("langLearn").setValue(languages);
                }
            }
        }
    }

    public void checkLanguages2(Firebase ref, String id, int action){

        Map<String, Boolean> languages = new HashMap<String, Boolean>();

        for (int j = 0; j < modelItems.length; j++){
            if (modelItems[j].getValue() == 0) {
                languages.put(modelItems[j].getName(),true);
                if (action == 0) {

                    ref.child("Tandem").child(id).child("langKnown").setValue(languages);

                }
                else {
                    ref.child("Tandem").child(id).child("langLearn").setValue(languages);

                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_edit_info, menu);
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
