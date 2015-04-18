package com.brentandjody.mountainunicyclist;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brentandjody.mountainunicyclist.data.DBContract;
import com.brentandjody.mountainunicyclist.data.Photo;
import com.brentandjody.mountainunicyclist.data.Trail;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;


public class TrailActivity extends ActionBarActivity {
    private Trail mTrail = null;
    private ImageView feature_photo;
    private TextView name;
    private ImageView difficulty;
    private TextView rating;
    private TextView description;
    private TextView trailsystem;
    private LinearLayout photo_picker;
    private TextView commentsButton;
    private TextView ridesButton;
    private TextView featuresButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trail);
        feature_photo = (ImageView) findViewById(R.id.feature_photo);
        name = (TextView) findViewById(R.id.title);
        difficulty = (ImageView) findViewById(R.id.difficulty_icon);
        rating = (TextView) findViewById(R.id.rating);
        description = (TextView) findViewById(R.id.description);
        trailsystem = (TextView) findViewById(R.id.trailsystem);
        photo_picker = (LinearLayout) findViewById(R.id.photos);
        commentsButton = (TextView) findViewById(R.id.add_comment_button);
        ridesButton = (TextView) findViewById(R.id.rides_button);
        featuresButton = (TextView) findViewById(R.id.features_button);
        mTrail = new Trail();
        Intent intent = getIntent();
        if (intent.hasExtra("trailId")) {
            mTrail.Load(intent.getStringExtra("trailId"), new GetCallback<Trail>() {
                @Override
                public void done(Trail trail, ParseException e) {
                    if (e==null) {
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
        name.setText(mTrail.Name());
        difficulty.setImageResource(mTrail.Difficulty().Icon());
        rating.setText(mTrail.Stars());
        description.setText(mTrail.Description());
        //TODO:trailsystem
        setupPhotoPicker();
    }

    private void setupPhotoPicker() {
        Photo.LoadImagesForOwner(mTrail.ID(), new FindCallback<Photo>() {
            @Override
            public void done(List<Photo> photos, ParseException e) {
                if (e==null) {
                    for (Photo photo : photos) {
                        ImageView iv = new ImageView(getApplication());
                        iv.setMaxHeight(96);
                        photo.LoadInto(iv);
                        photo_picker.addView(iv);
                    }
                } else Log.w("SetupPhotoPicker()", e.getMessage());
            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
