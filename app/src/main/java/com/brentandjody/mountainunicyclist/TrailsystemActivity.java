package com.brentandjody.mountainunicyclist;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.brentandjody.mountainunicyclist.data.Trailsystem;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.SaveCallback;


public class TrailsystemActivity extends ActionBarActivity {

    private LatLng mLocation = null;
    private TextView title;
    private TextView description;
    private TextView directions;
    private ImageView btnLocation;
    private TextView btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailsystem);
        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);
        directions = (TextView) findViewById(R.id.directions);
        btnLocation = (ImageView) findViewById(R.id.location_picker_button);
        btnSave = (TextView) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Trailsystem trailsystem = new Trailsystem();
                trailsystem.setTitle(title.getText().toString());
                trailsystem.setDescription(description.getText().toString());
                trailsystem.setDirections(directions.getText().toString());
                if (mLocation!=null)
                    trailsystem.setLocation(mLocation);
                trailsystem.pinInBackground();
                trailsystem.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null) {
                            Intent intent = TrailsystemActivity.this.getIntent();
                            String id = trailsystem.ID();
                            intent.putExtra(Trailsystem.ID_EXTRA, id);
                            TrailsystemActivity.this.setResult(RESULT_OK, intent);
                            TrailsystemActivity.this.finish();
                        } else Log.e("Trailsystem Save", e.getMessage());
                    }
                });
            }
        });
        getSupportActionBar().hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trailsystem, menu);
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
