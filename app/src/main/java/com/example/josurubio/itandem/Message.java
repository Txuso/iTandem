package com.example.josurubio.itandem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import Classes.CustomTandemListViewAdapter;
import Classes.RoundedImageView;
import Classes.TandemListRowItem;
import Classes.imageManager;


public class Message extends Activity implements AdapterView.OnItemClickListener {

    ListView listView;
    List<TandemListRowItem> rowItems;
    String from = "";
    String to = "";
    String id = "";
    Firebase myFirebaseRef;
    int position = 0;
    CustomTandemListViewAdapter adapter;
    TandemListRowItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Bundle extras = getIntent().getExtras();

        id = extras.getString("id");
        myFirebaseRef = new Firebase("https://blazing-fire-2203.firebaseio.com/");
        rowItems = new ArrayList<TandemListRowItem>();

        myFirebaseRef.child("TandemEncounter").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Map<String, Object> tandemEncounter = (Map<String, Object>) dataSnapshot.getValue();

                if (tandemEncounter.get("author").toString().equals(id)) {
                    from = tandemEncounter.get("author").toString();
                    to = tandemEncounter.get("receptor").toString();

                    myFirebaseRef.child("Tandem").child(tandemEncounter.get("receptor").toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Map<String, Object> tandem = (Map<String, Object>) snapshot.getValue();

                            Bitmap pic = imageManager.getResizedBitmap(imageManager.decodeBase64(tandem.get("image").toString()), 80, 80);
                            Bitmap picRounded = RoundedImageView.getCroppedBitmap(pic, 300);
                            BitmapDrawable ima = new BitmapDrawable(picRounded);
                            item = new TandemListRowItem(ima, "", tandemEncounter.get("date").toString(), "", "");



                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    Toast.makeText(getApplicationContext(), item.getnameAge() + "", Toast.LENGTH_SHORT).show();


                    rowItems.add(item);
                    listView.invalidateViews();

                }
                else if (tandemEncounter.get("receptor").toString().equals(id)){
                    from = tandemEncounter.get("receptor").toString();
                    to = tandemEncounter.get("author").toString();
                    item = new TandemListRowItem(new BitmapDrawable(), "", tandemEncounter.get("date").toString(), "", dataSnapshot.getKey());

                    myFirebaseRef.child("Tandem").child(tandemEncounter.get("receptor").toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Map<String, Object> tandem = (Map<String, Object>) snapshot.getValue();
                            Bitmap pic = imageManager.getResizedBitmap(imageManager.decodeBase64(tandem.get("image").toString()), 80, 80);
                            Bitmap picRounded = RoundedImageView.getCroppedBitmap(pic, 300);
                            BitmapDrawable ima = new BitmapDrawable(picRounded);
                            item.setnameAge(tandem.get("name").toString());
                            item.setImageId(ima);


                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                    rowItems.add(item);
                    listView.invalidateViews();
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

        listView = (ListView) findViewById(R.id.TandemEncounters);
        adapter = new CustomTandemListViewAdapter(this,
                R.layout.tandem_list_item, rowItems);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
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

    public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    public boolean onContextItemSelected (MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        position = info.position;
        switch (item.getItemId()){
            case R.id.delete_conver:
                myFirebaseRef.child("TandemEncounter").child(rowItems.get(position).getId()).removeValue();
                myFirebaseRef.child("IndividualChat").child(rowItems.get(position).getId()).removeValue();
                adapter.remove(adapter.getItem(position));
                adapter.notifyDataSetChanged();
                return true;
            default: return super.onContextItemSelected(item);

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Toast.makeText(getApplicationContext(), rowItems.get(position).getId(), Toast.LENGTH_SHORT).show();

        Intent chatIntent = new Intent(this, ChatActivity.class);
        chatIntent.putExtra("fromID", from);
        chatIntent.putExtra("toID",  to);
        chatIntent.putExtra("from", rowItems.get(position).getnameAge());
        chatIntent.putExtra("encounterID", rowItems.get(position).getId());
        startActivity(chatIntent);

    }
}
