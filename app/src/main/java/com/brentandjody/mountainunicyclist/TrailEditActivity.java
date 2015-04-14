package com.brentandjody.mountainunicyclist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.brentandjody.mountainunicyclist.data.LocationsDB;
import com.brentandjody.mountainunicyclist.data.Photo;
import com.brentandjody.mountainunicyclist.data.PhotoDB;
import com.brentandjody.mountainunicyclist.data.Trail;
import com.google.android.gms.maps.model.LatLng;


public class TrailEditActivity extends ActionBarActivity {
    private PhotoDB mPhotos;
    private LocationsDB mLocations;
    private Trail trail = null;
    private EditText name;
    private RadioGroup difficulty;
    private RatingBar rating;
    private EditText description;
    private Spinner trailsystem;
    private LinearLayout photo_picker;
    private Button cancelButton;
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trail_edit);

        name = (EditText) findViewById(R.id.title);
        difficulty = (RadioGroup) findViewById(R.id.difficulty);
        rating = (RatingBar) findViewById(R.id.rating);
        description = (EditText) findViewById(R.id.description);
        trailsystem = (Spinner) findViewById(R.id.trailsystem);
        photo_picker = (LinearLayout) findViewById(R.id.photos);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        okButton = (Button) findViewById(R.id.ok_button);
        setupViewFromIntent();


    }

    @Override
    protected void onResume() {
        super.onResume();
        ((Application) getApplication()).openDB();
        mPhotos = ((Application) getApplication()).getPhotos();
        mLocations = ((Application) getApplication()).getLocations();
        setupButtonListeners();
        setupTrailsystem();
        setupPhotoPicker();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((Application)getApplication()).closeDB();
        //TODO:clear trailsystem dropdown
        cancelButton.setOnClickListener(null);
        okButton.setOnClickListener(null);
        photo_picker.removeAllViews();
        mPhotos=null;
        mLocations=null;
    }

    private void saveTrail() {
        if (trail!=null) {
            trail.setName(name.getText().toString());
            switch (difficulty.getCheckedRadioButtonId()) {
                case R.id.easy:trail.setDifficulty(Trail.Difficulty.EASY); break;
                case R.id.medium:trail.setDifficulty(Trail.Difficulty.MEDIUM); break;
                case R.id.difficult:trail.setDifficulty(Trail.Difficulty.DIFFICULT); break;
                case R.id.expert:trail.setDifficulty(Trail.Difficulty.EXPERT); break;
                default:trail.setDifficulty(Trail.Difficulty.MEDIUM);
            }
            trail.setRating(Math.round(rating.getRating()));
            trail.setDescription(description.getText().toString());
            //TODO:set trailsystem
            //TODO:set selected feature photoid
            //save trail
            int trail_id=mLocations.InsertOrUpdate(trail);
            // update missing trailid on photo (not always necessary)
            if (trail.PhotoId()>=0) {
                Photo photo = mPhotos.Get(trail.PhotoId());
                if (photo != null) {
                    photo.setOwnerId(trail_id);
                    //save photo
                    mPhotos.InsertOrUpdate(photo);
                }
            }
        }
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void cancelTrail() {
        Photo photo = mPhotos.Get(trail.PhotoId());
        if (photo!=null && photo.Owner()==-1) {
            mPhotos.Delete(photo.ID());
        }
        finish();
    }

    private void setupViewFromIntent() {
        Intent intent = getIntent();
        //build tril object
        if (intent.hasExtra("trail"))
            trail = intent.getExtras().getParcelable("trail");
        else
            trail = new Trail();
        if (intent.hasExtra("location"))
            trail.setLocation((LatLng) intent.getExtras().getParcelable("location"));
        if (intent.hasExtra("photoid"))
            trail.setPhotoid(intent.getIntExtra("photoid", -1));

        name.setText(trail.Name());
        switch (trail.Difficulty()) {
            case EASY:
                difficulty.check(R.id.easy);
                break;
            case MEDIUM:
                difficulty.check(R.id.medium);
                break;
            case DIFFICULT:
                difficulty.check(R.id.difficult);
                break;
            case EXPERT:
                difficulty.check(R.id.expert);
                break;
            default:
                difficulty.check(R.id.medium);
                break;
        }
        rating.setRating(trail.Rating());
        description.setText(trail.Description());
    }

    private void setupButtonListeners() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTrail();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTrail();
            }
        });
    }

    private void setupTrailsystem() {
        //TODO: fill trailsystem dropdown
    }

    private void setupPhotoPicker() {

        if (trail.ID()<0) { //new trail
            if (trail.PhotoId()>=0) {
                Photo photo = mPhotos.Get(trail.PhotoId());
                ImageView iv = new ImageView(this);
                iv.setMaxHeight(96);
                iv.setImageBitmap(BitmapFactory.decodeByteArray(photo.Data(), 0, photo.Data().length));
                photo_picker.addView(iv);
            }
        } else { //existing trail
            for (Photo photo : mPhotos.GetPhotosForTrail(trail.ID())) {
                ImageView iv = new ImageView(this);
                iv.setMaxHeight(96);
                iv.setImageBitmap(BitmapFactory.decodeByteArray(photo.Data(), 0, photo.Data().length));
                photo_picker.addView(iv);
            }
        }
        //TODO:highlight current featured image
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
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
