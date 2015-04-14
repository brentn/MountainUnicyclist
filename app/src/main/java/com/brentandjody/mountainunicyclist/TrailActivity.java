package com.brentandjody.mountainunicyclist;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.brentandjody.mountainunicyclist.data.Photo;
import com.brentandjody.mountainunicyclist.data.PhotoDB;
import com.brentandjody.mountainunicyclist.data.Trail;


public class TrailActivity extends ActionBarActivity {
    private PhotoDB mPhotos;
    private Trail trail = null;
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
        Intent intent = getIntent();
        if (intent.hasExtra("trail"))
            trail = intent.getExtras().getParcelable("trail");
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

    }

    @Override
    protected void onPause() {
        super.onPause();
        ((Application)getApplication()).closeDB();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((Application)getApplication()).openDB();
        mPhotos = ((Application)getApplication()).getPhotos();
        populateFields();
    }

    private void populateFields() {
        if (trail==null) return;
        Photo photo = mPhotos.Get(trail.PhotoId());
        if (photo!=null)
            feature_photo.setImageBitmap(BitmapFactory.decodeByteArray(photo.Data(), 0, photo.Data().length));
        name.setText(trail.Name());
        difficulty.setImageResource(trail.getDifficultyIcon());
        rating.setText(trail.getStars());
        description.setText(trail.Description());
        //TODO:trailsystem
        setupPhotoPicker();
    }

    private void setupPhotoPicker() {
        for (Photo photo : mPhotos.GetPhotosForTrail(trail.ID())) {
            ImageView iv = new ImageView(this);
            iv.setMaxHeight(96);
            iv.setImageBitmap(BitmapFactory.decodeByteArray(photo.Data(), 0, photo.Data().length));
            photo_picker.addView(iv);
        }
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
