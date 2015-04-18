package com.brentandjody.mountainunicyclist;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brentandjody.mountainunicyclist.data.DBContract;
import com.brentandjody.mountainunicyclist.data.Trail;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;


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
        Intent intent = getIntent();
        if (intent.hasExtra("trailId")) {
            ParseQuery<Trail> query = Trail.getQuery();
            query.fromLocalDatastore();
            query.whereEqualTo(DBContract.Trail._ID, intent.getStringExtra("trailId"));
            query.getFirstInBackground(new GetCallback<Trail>() {
                @Override
                public void done(Trail trail, ParseException e) {
                if (e==null) {
                    mTrail = trail;
                    populateFields();
                } else {
                    Log.w("Error loading trail", e.getMessage() );
                }
                }
            });
        }
    }

    private void populateFields() {
        if (mTrail!=null && mTrail.Photo()!=null)
            feature_photo.setImageBitmap(
                BitmapFactory.decodeByteArray(mTrail.Photo().Data(), 0, mTrail.Photo().Data().length));
        name.setText(mTrail.Name());
        difficulty.setImageResource(mTrail.getDifficultyIcon());
        rating.setText(mTrail.getStars());
        description.setText(mTrail.Description());
        //TODO:trailsystem
        setupPhotoPicker();
    }

    private void setupPhotoPicker() {
//        for (Photo photo : Photos.GetPhotosForTrail(mTrail.ID())) {
//            ImageView iv = new ImageView(this);
//            iv.setMaxHeight(96);
//            iv.setImageBitmap(BitmapFactory.decodeByteArray(photo.Data(), 0, photo.Data().length));
//            photo_picker.addView(iv);
//        }
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
