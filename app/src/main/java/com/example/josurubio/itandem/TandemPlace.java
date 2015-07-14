package com.example.josurubio.itandem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class TandemPlace extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tandem_place);

        Button findP = (Button) findViewById(R.id.findTandemPlace);
        final Button createP = (Button) findViewById(R.id.createTandemPlace);
        final Intent createAct = new Intent(TandemPlace.this, CreateTandemPlace.class);
        final Intent findAct = new Intent(TandemPlace.this, FindTandemPlace.class);

        createAct.putExtra("id", getIntent().getExtras().getString("id"));

        findP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAct.putExtra("id", getIntent().getExtras().getString("id"));
                findAct.putExtra("name", getIntent().getExtras().getString("name"));
               startActivity(findAct);
            }
        });

        createP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(createAct);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tandem_place, menu);
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
