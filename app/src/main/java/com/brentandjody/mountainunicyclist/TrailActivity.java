package com.brentandjody.mountainunicyclist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.brentandjody.mountainunicyclist.data.Photo;
import com.brentandjody.mountainunicyclist.data.PhotoPicker;
import com.brentandjody.mountainunicyclist.data.Trail;
import com.brentandjody.mountainunicyclist.helpers.LocationHelper;
import com.google.android.gms.maps.model.LatLng;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;


public class TrailActivity extends ActionBarActivity {
    private Trail mTrail = null;
    private ImageView feature_photo;
    private TextView name;
    private TextView distance;
    private ImageView difficulty;
    private TextView rating;
    private TextView description;
    private TextView trailsystem;
    private PhotoPicker photoPicker;
    private ScrollView scrollView;
    private TextView commentsButton;
    private TextView ridesButton;
    private TextView featuresButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trail);
        feature_photo = (ImageView) findViewById(R.id.feature_photo);
        name = (TextView) findViewById(R.id.title);
        distance = (TextView) findViewById(R.id.distance);
        difficulty = (ImageView) findViewById(R.id.difficulty_icon);
        rating = (TextView) findViewById(R.id.rating);
        description = (TextView) findViewById(R.id.description);
        trailsystem = (TextView) findViewById(R.id.trailsystem);
        photoPicker = new PhotoPicker(this, (LinearLayout) findViewById(R.id.photos));
        commentsButton = (TextView) findViewById(R.id.add_comment_button);
        ridesButton = (TextView) findViewById(R.id.rides_button);
        featuresButton = (TextView) findViewById(R.id.features_button);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scrollView.getScrollY()<50) getSupportActionBar().show();
                else getSupportActionBar().hide();
            }
        });
        mTrail = new Trail();
        setTitle("");

        Intent intent = getIntent();
        if (intent.hasExtra("trailId")) {
            Trail.Load(intent.getStringExtra("trailId"), LocationHelper.getGPS(this), new GetCallback<Trail>() {
                @Override
                public void done(Trail trail, ParseException e) {
                    if (e==null) {
                        mTrail = trail;
                        Log.d("TrailActivity", "trail loaded");
                        populateFields();
                    } else Log.w("TrailActivity", e.getMessage());
                }
            });
        }
    }

    private void populateFields() {
        Photo.LoadImage(mTrail.PhotoId(), new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e==null) {
                    feature_photo.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                    Log.d("PopulateTrail", "feature photo loaded");
                } else Log.w("PopulateTrail", e.getMessage());
            }
        });
        LatLng myLocation = LocationHelper.getGPS(getApplication());
        name.setText(mTrail.Name());
        float[] distances = new float[3];
        Location.distanceBetween(myLocation.latitude, myLocation.longitude,
                mTrail.Location().latitude, mTrail.Location().longitude, distances);
        distance.setText("approx. " + String.format("%.0f", distances[0]/1000) + " km. away");
        difficulty.setImageResource(mTrail.Difficulty().Icon());
        rating.setText(mTrail.Stars());
        description.setText(mTrail.Description());
        //TODO:trailsystem
        photoPicker.setup(mTrail.ID());
        photoPicker.select(mTrail.PhotoId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case (R.id.action_edit):
                Intent intent = new Intent(this, TrailEditActivity.class);
                intent.putExtra("trailId", mTrail.ID());
                startActivity(intent);
                return true;
            case (R.id.action_delete):
                new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete_trail))
                    .setMessage(getString(R.string.confirm_delete_trail))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mTrail.Delete();
                            Toast.makeText(TrailActivity.this, "trail deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
