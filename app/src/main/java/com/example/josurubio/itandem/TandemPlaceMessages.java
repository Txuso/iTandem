package com.example.josurubio.itandem;

import android.app.ListActivity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.firebase.client.Firebase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import AndroidChat.Chat;
import AndroidChat.ChatListAdapter;
import AndroidChat.GroupChatListAdapter;


public class TandemPlaceMessages extends ListActivity {
    private Firebase mFirebaseRef;
    String from = "";
    String fromID = "";
    private GroupChatListAdapter mChatListAdapter;



    private static final String FIREBASE_URL = "https://blazing-fire-2203.firebaseio.com/GroupChat";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tandem_place_messages);
        getWindow().setBackgroundDrawableResource(R.drawable.background);


        Bundle extras = getIntent().getExtras();
        String encounterID = extras.getString("encounterID");
        from = extras.getString("from");
        fromID = extras.getString("fromID");

        mFirebaseRef = new Firebase(FIREBASE_URL).child(encounterID);


        final ListView listView = getListView();
        EditText inputText = (EditText) findViewById(R.id.messageInput2);
        mChatListAdapter = new GroupChatListAdapter(mFirebaseRef.limit(100), R.layout.group_chat_message, this);
        listView.setAdapter(mChatListAdapter);

        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tandem_place_messages, menu);
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

    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput2);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Chat chat = new Chat(input, from, fromID, getCurrentTime());
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(chat);
            inputText.setText("");
        }
    }

    public String getCurrentTime(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("KK:mm");
        date.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
        String localTime = date.format(currentLocalTime);

        return localTime;
    }
}
