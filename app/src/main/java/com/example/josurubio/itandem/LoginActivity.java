package com.example.josurubio.itandem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.firebase.client.Firebase;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import AndroidChat.Language;
import Classes.TandemUser;
import Classes.imageManager;


public class LoginActivity extends Activity{


    private boolean isResumed = false;
    //UiLifecycleHelper helps to create automatically open, save and restore the Active Session
    private UiLifecycleHelper uiHelper;
    //Firebase reference
    private Firebase mFirebaseRef;
    //It provides asynchronous notification of Session state changes
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            onFacebookSessionStateChange(session, state, exception);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //declaration of the uiHelper
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //We must do this once to set the context of the application
        Firebase.setAndroidContext(this);
        //The Facebook login button which a component within the GUI
        LoginButton mFacebookLoginButton  = (LoginButton) findViewById(R.id.login_with_facebook);
        //Data that will be gathered from the user's Facebook account is set here
        mFacebookLoginButton.setReadPermissions(Arrays.asList("public_profile","user_birthday"));

        Button log = (Button) findViewById(R.id.button);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> langKnown = new ArrayList<String>();
                ArrayList<String> langLearn = new ArrayList<String>();
                //We set the default image and we encode it to base64
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.profileimage);
                String im = imageManager.encodeTobase64(bm);
                //We create the tandem user with the needed data
                TandemUser tandemUser2 = new TandemUser("Andrea", 25 , im, langKnown, langLearn, "",
                        "0", "0", 50, " ^ ^ ");
                //Firebase instance creation in the Tandem branch
                mFirebaseRef = new Firebase("https://blazing-fire-2203.firebaseio.com/Tandem");
                Firebase newTandRef = mFirebaseRef.push();
                //We store the user in the Firebase root
                newTandRef.setValue(tandemUser2);
                //we get the created user's ID
                String logedUserID = newTandRef.getKey();

                Intent intent = new Intent(LoginActivity.this, MainMenu.class);

                //we put it as an extra data in the next activity
                intent.putExtra("id", logedUserID);
                intent.putExtra("name", "Andrea");

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }

    /* Handle any changes to the Facebook session */
    private void onFacebookSessionStateChange(final Session session, SessionState state, Exception exception) {

        if (isResumed) {
            if (session != null && session.isOpened())
                Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        //we get data from the Facebook account
                        String fbName = user.getName();
                        String fbAge = user.asMap().get("birthday").toString();
                        //we create the instance of the MainMenu
                        Intent intent = new Intent(LoginActivity.this, MainMenu.class);
                        //we put the user's name as an extra data to the next activity
                        intent.putExtra("name", fbName);

                        ArrayList<String> langKnown = new ArrayList<String>();
                        ArrayList<String> langLearn = new ArrayList<String>();
                        //We set the default image and we encode it to base64
                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.profileimage);
                        String im = imageManager.encodeTobase64(bm);
                        //We create the tandem user with the needed data
                        TandemUser tandemUser = new TandemUser(fbName, getAge(fbAge), im, langKnown, langLearn, "",
                                "0", "0", 50," ^ ^ ");
                        //Firebase instance creation in the Tandem branch
                        mFirebaseRef = new Firebase("https://blazing-fire-2203.firebaseio.com/Tandem");

                        mFirebaseRef.removeValue();
                        // we create a new instance cause it will be useful to get the ID of the new user
                        Firebase newTandRef = mFirebaseRef.push();
                        //We store the user in the Firebase root
                        newTandRef.setValue(tandemUser);
                        //we get the created user's ID
                        String logedUserID = newTandRef.getKey();
                        //we put it as an extra data in the next activity
                        intent.putExtra("id", logedUserID);

                        Language ing = new Language("English", "Ingelesa", "Inglés");
                        Language eusk = new Language("Basque", "Euskera", "Vasco");
                        Language esp = new Language("Spanish", "Gaztelania", "Español");
                        Language frn = new Language("French", "Frantsesa", "Francés");
                        Language it = new Language("Italian", "Italiera", "Italiano");
                        Language cat = new Language("Catalan", "Katalan", "Catalán");

                        Firebase f = new Firebase("https://blazing-fire-2203.firebaseio.com");

                        f.child("Language").removeValue();
                        f.child("Language").push().setValue(eusk);
                        f.child("Language").push().setValue(cat);
                        f.child("Language").push().setValue(ing);
                        f.child("Language").push().setValue(frn);
                        f.child("Language").push().setValue(it);
                        f.child("Language").push().setValue(esp);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
        }
        else
            Log.e("ERROR", "ERROR");

    }

    //we get the age from a date
    public int getAge(String age) {

        int year = Integer.parseInt(age.substring(6));
        int day = Integer.parseInt(age.substring(3, 5));
        int month = Integer.parseInt(age.substring(0, 2));

        Date now = new Date();
        int nowMonth = now.getMonth()+ 1;
        int nowYear = now.getYear()+1900;
        int result = nowYear - year;

        if (month > nowMonth) {
            result--;
        }
        else if (month == nowMonth) {
            int nowDay = now.getDate();

            if (day > nowDay) {
                result--;
            }
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isResumed = true;
        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed())) {
            onFacebookSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
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
