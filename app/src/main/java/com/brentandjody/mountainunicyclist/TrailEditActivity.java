package com.brentandjody.mountainunicyclist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.brentandjody.mountainunicyclist.data.DBContract;
import com.brentandjody.mountainunicyclist.data.Photo;
import com.brentandjody.mountainunicyclist.data.Trail;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;


public class TrailEditActivity extends ActionBarActivity {

    private Trail mTrail = null;
    private EditText name;
    private RadioGroup difficulty;
    private RatingBar rating;
    private EditText description;
    private Spinner trailsystem;
    private LinearLayout photo_picker;
    private ImageButton locationButton;
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
        locationButton = (ImageButton) findViewById(R.id.location_picker_button);
        okButton = (Button) findViewById(R.id.ok_button);
        Intent intent = getIntent();
        if (intent.hasExtra("trailId")) {
            ParseQuery<Trail> query = Trail.getQuery();
            query.fromLocalDatastore();
            query.whereEqualTo(DBContract.Trail._ID, intent.getStringExtra("trailId"));
            query.getFirstInBackground(new GetCallback<Trail>() {
                @Override
                public void done(Trail trail, ParseException e) {
                    if (e == null) {
                        mTrail = trail;
                        setupViews();
                    } else {
                        Log.w("Load Trail", e.getMessage());
                    }
                }
            });
        } else {
            Log.i("TrailEdit", "Creating a new trail");
            mTrail = new Trail();
            mTrail.setID();
            mTrail.setRating(4);
            setupViews();
        }

    }

    @Override
    public void onBackPressed() {
        cancelTrail();
        super.onBackPressed();
    }

    private void saveTrail() {
        if (mTrail !=null) {
            mTrail.setName(name.getText().toString());
            switch (difficulty.getCheckedRadioButtonId()) {
                case R.id.easy:
                    mTrail.setDifficulty(Trail.Difficulty.EASY); break;
                case R.id.medium:
                    mTrail.setDifficulty(Trail.Difficulty.MEDIUM); break;
                case R.id.difficult:
                    mTrail.setDifficulty(Trail.Difficulty.DIFFICULT); break;
                case R.id.expert:
                    mTrail.setDifficulty(Trail.Difficulty.EXPERT); break;
                default:
                    mTrail.setDifficulty(Trail.Difficulty.MEDIUM);
            }
            mTrail.setRating(Math.round(rating.getRating()));
            mTrail.setDescription(description.getText().toString());
            //TODO:set trailsystem
            //TODO:set selected feature photoid
            // update missing trailid on photo (not always necessary)
            ParseQuery<Photo> query =  Photo.getQuery();
            query.fromLocalDatastore();
            query.whereEqualTo(DBContract.Photos._ID, mTrail.PhotoId());
            query.getFirstInBackground(new GetCallback<Photo>() {
                @Override
                public void done(Photo photo, ParseException e) {
                    photo.setOwnerId(mTrail.ID());
                }
            });
        }
        mTrail.pinInBackground();
        mTrail.saveEventually();
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void cancelTrail() {
        if (mTrail!=null)
            Photo.Delete(mTrail.PhotoId());
        finish();
    }

    private void setupViews() {
        Intent intent = getIntent();
        if (intent.hasExtra("location")) {
            mTrail.setLocation((LatLng) intent.getParcelableExtra("location"));
        }
        if (intent.hasExtra("photoid")) {
            ParseQuery<Photo> query = Photo.getQuery();
            query.fromLocalDatastore();
            query.whereEqualTo(DBContract.Photos._ID, intent.getStringExtra("photoid"));
            query.getFirstInBackground(new GetCallback<Photo>() {
                public void done(Photo photo, ParseException e) {
                if (e==null)
                    mTrail.setPhotoId(photo.ID());
                else
                    Log.w("TrailEdit", "SetPhotoId failed: " + e.getMessage());
                }
           });
        }
        name.setText(mTrail.Name());
        if (mTrail.Difficulty()==null) mTrail.setDifficulty(Trail.Difficulty.MEDIUM);
        difficulty.check(mTrail.getDifficultyResource());
        rating.setRating(mTrail.Rating());
        description.setText(mTrail.Description());
        setupButtonListeners();
        setupTrailsystem();
        setupPhotoPicker();
    }

    private void setupButtonListeners() {
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrailEditActivity.this, LocationPicker.class);
                intent.putExtra("location", mTrail.Location());
                startActivityForResult(intent, Application.EDIT_TRAIL);
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
        ParseQuery<Photo> query = Photo.getQuery();
        query.whereEqualTo(DBContract.Photos.COLUMN_OWNER_ID, mTrail.ID());
        query.findInBackground(new FindCallback<Photo>() {
            @Override
            public void done(List<Photo> photos, ParseException e) {
                if (e != null)
                    Log.w("SetupPhotoPicker()", e.getMessage());
                for (Photo photo : photos) {
                    ImageView iv = new ImageView(getApplication());
                    iv.setTag(0, photo.ID());
                    if (mTrail.PhotoId() == photo.ID()) {
                        markSelected(iv);
                    } else {
                        iv.setMaxHeight(96);
                    }
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            markSelected((ImageView) v);
                            mTrail.setPhotoId((String)v.getTag(0));
                        }
                    });
                    photo.LoadData(iv);
                    photo_picker.addView(iv);
                }

            }
        });
    }

    private void markSelected(ImageView iv) {
        for (int i = 0; i<photo_picker.getChildCount(); i++) {
            ((ImageView)photo_picker.getChildAt(i)).setMaxHeight(96);
            (photo_picker.getChildAt(i)).setBackgroundColor(Color.TRANSPARENT);
        }
        iv.setMaxHeight(128);
        iv.setBackgroundColor(Color.YELLOW);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Application.EDIT_TRAIL: {
                if (resultCode == RESULT_OK) {
                    mTrail.setLocation((LatLng) data.getParcelableExtra("location"));
                    Log.d("LATITUDE", mTrail.Location().latitude+"");
                    Log.d("LONGITUDE", mTrail.Location().longitude+"");
                }
            }
        }
    }

}
