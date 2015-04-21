package com.brentandjody.mountainunicyclist;

import android.app.ActionBar;
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
import com.brentandjody.mountainunicyclist.data.Difficulty;
import com.brentandjody.mountainunicyclist.data.Photo;
import com.brentandjody.mountainunicyclist.data.Trail;
import com.brentandjody.mountainunicyclist.helpers.LocationHelper;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;


public class TrailEditActivity extends ActionBarActivity {

    private static LinearLayout.LayoutParams unselected = new LinearLayout.LayoutParams(96, LinearLayout.LayoutParams.WRAP_CONTENT);
    private static LinearLayout.LayoutParams selected = new LinearLayout.LayoutParams(128, LinearLayout.LayoutParams.WRAP_CONTENT);

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
            Trail.Load(intent.getStringExtra("trailId"), new GetCallback<Trail>() {
                @Override
                public void done(Trail trail, ParseException e) {
                    if (e==null) {
                        mTrail = trail;
                        Log.d("EditTrail", "Trail loaded");
                        setupViews();
                    } else Log.w("EditTrail", e.getMessage());
                }
            });
        } else {
            Log.i("TrailEdit", "Creating a new trail");
            mTrail = new Trail();
            //need to save to get ID
            mTrail.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    mTrail.getObjectId();
                    setupViews();
                }
            });
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
                    mTrail.setDifficulty(new Difficulty(0)); break;
                case R.id.medium:
                    mTrail.setDifficulty(new Difficulty(1)); break;
                case R.id.difficult:
                    mTrail.setDifficulty(new Difficulty(2)); break;
                case R.id.expert:
                    mTrail.setDifficulty(new Difficulty(3)); break;
                default:
                    mTrail.setDifficulty(new Difficulty());
            }
            mTrail.setRating(Math.round(rating.getRating()));
            mTrail.setDescription(description.getText().toString());
            //TODO:set trailsystem
            //TODO:set selected feature photoid
        }
        mTrail.Save();
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
        if (intent.hasExtra("photo")) {
            final byte[] image = intent.getByteArrayExtra("photo");
            final Photo photo = new Photo();
            photo.setData(image);
            photo.setOwnerId(mTrail.ID());
            if (intent.hasExtra("isTemporaryPhoto")) photo.FLAGS().setTemporary();
            setupPhotoPicker();
            photo.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        mTrail.setPhotoId(photo.ID());
                        addImageToPhotoPicker(image, photo.ID());
                        Log.d("SetupViews", "Photo saved");
                    } else Log.w("SetupViews", e.getMessage());
                }
            });
        }
        name.setText(mTrail.Name());
        difficulty.check(mTrail.Difficulty().Resource());
        rating.setRating(mTrail.Rating());
        description.setText(mTrail.Description());
        setupButtonListeners();
        setupTrailsystem();
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
        Photo.LoadImagesForOwner(mTrail.ID(), new FindCallback<Photo>() {
            @Override
            public void done(List<Photo> photos, ParseException e) {
                if (e==null) {
                    for (Photo photo : photos) {
                        ImageView iv = new ImageView(getApplication());
                        iv.setTag(photo.ID());
                        if (mTrail.PhotoId() == photo.ID()) {
                            markSelected(iv);
                        } else {
                            iv.setLayoutParams(unselected);
                        }
                        iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                markSelected((ImageView) v);
                                mTrail.setPhotoId((String) v.getTag());
                            }
                        });
                        photo_picker.addView(iv);
                    }
                } else Log.w("SetupPhotoPicker()", e.getMessage());
            }
        });
    }

    private void addImageToPhotoPicker(byte[] image, String id) {
        ImageView iv = new ImageView(getApplication());
        iv.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        iv.setTag(id);
        if (mTrail.PhotoId() == id) {
            markSelected(iv);
        }
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markSelected((ImageView) v);
                mTrail.setPhotoId((String) v.getTag());
            }
        });
        photo_picker.addView(iv);
        photo_picker.getParent().requestLayout();
    }

    private void markSelected(ImageView iv) {
        for (int i = 0; i<photo_picker.getChildCount(); i++) {
            ((ImageView)photo_picker.getChildAt(i)).setLayoutParams(unselected);
        }
        iv.setLayoutParams(selected);
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
