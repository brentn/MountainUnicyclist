package com.brentandjody.mountainunicyclist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.brentandjody.mountainunicyclist.data.Difficulty;
import com.brentandjody.mountainunicyclist.data.Flags;
import com.brentandjody.mountainunicyclist.data.ListItem;
import com.brentandjody.mountainunicyclist.data.Photo;
import com.brentandjody.mountainunicyclist.data.PhotoPicker;
import com.brentandjody.mountainunicyclist.data.Trail;
import com.brentandjody.mountainunicyclist.data.Trailsystem;
import com.brentandjody.mountainunicyclist.helpers.LocationHelper;
import com.google.android.gms.maps.model.LatLng;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class TrailEditActivity extends ActionBarActivity {

    private Trail mTrail = null;
    private boolean mIsNewImage =false;
    private EditText name;
    private RadioGroup difficulty;
    private RatingBar rating;
    private EditText description;
    private EditText directions;
    private CheckBox isMuni;
    private Spinner trailsystem;
    private ArrayAdapter<ListItem> trailsystemAdapter;
    private ImageButton locationButton;
    private Button okButton;
    private ImageView addPhotoButton;
    private Uri mOutputFileUri;
    private ImageView mSelectedPhoto;
    private PhotoPicker mPhotoPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trail_edit);

        name = (EditText) findViewById(R.id.title);
        difficulty = (RadioGroup) findViewById(R.id.difficulty);
        directions = (EditText) findViewById(R.id.directions);
        isMuni = (CheckBox) findViewById(R.id.isMuni);
        rating = (RatingBar) findViewById(R.id.rating);
        description = (EditText) findViewById(R.id.description);
        trailsystem = (Spinner) findViewById(R.id.trailsystem);
        locationButton = (ImageButton) findViewById(R.id.location_picker_button);
        okButton = (Button) findViewById(R.id.ok_button);
        addPhotoButton = (ImageView) findViewById(R.id.add_photo_button);
        mPhotoPicker = new PhotoPicker(this, (LinearLayout)findViewById(R.id.photos));
        trailsystemAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item);
        trailsystem.setAdapter(trailsystemAdapter);
        trailsystem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = ((ListItem)trailsystem.getItemAtPosition(position)).Value();
                if (value==Trailsystem.NEW) {
                    Intent intent = new Intent(TrailEditActivity.this, TrailsystemActivity.class);
                    startActivityForResult(intent, Application.NEW_TRAILSYSTEM);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Intent intent = getIntent();
        if (intent.hasExtra("trailId")) {
            Trail.Load(intent.getStringExtra("trailId"), LocationHelper.getGPS(this), new GetCallback<Trail>() {
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
            mIsNewImage =true;
            //need to save to get ID
            mTrail.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    mTrail.getObjectId();
                    setupViews();
                }
            });
        }
        getSupportActionBar().hide();
    }

    @Override
    public void onBackPressed() {
        cancelTrail();
        super.onBackPressed();
    }

    private void saveTrail() {
        if (mTrail !=null) {
            mTrail.setName(name.getText().toString());
            Difficulty d = new Difficulty();
            d.setFromRadioButton(difficulty.getCheckedRadioButtonId());
            mTrail.setDifficulty(d);
            mTrail.setRating(Math.round(rating.getRating()));
            mTrail.setDescription(description.getText().toString());
            mTrail.setIsMuni(isMuni.isChecked());
            mTrail.setDirections(directions.getText().toString());
            mTrail.setTrailsystem(((ListItem) trailsystem.getSelectedItem()).Value());
            //TODO:set selected feature photoid
            mTrail.saveInBackground();
            mTrail.notifyObservers();
        }
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void cancelTrail() {
        if (mTrail!=null && mIsNewImage)
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
            if (intent.hasExtra("isTemporaryPhoto")) Flags.setTemporary(photo.ID());
            photo.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        mTrail.setPhotoId(photo.ID());
                        mPhotoPicker.addImage(image, photo.ID());
                        Log.d("SetupViews", "Photo saved");
                    } else Log.w("SetupViews", e.getMessage());
                }
            });
        }
        name.setText(mTrail.Name());
        difficulty.check(mTrail.Difficulty().RadioButton());
        isMuni.setChecked(mTrail.IsMuni());
        rating.setRating(mTrail.Rating());
        description.setText(mTrail.Description());
        directions.setText(mTrail.Directions());
        Trailsystem.LoadAll(trailsystem, mTrail.TrailsystemId());
        mPhotoPicker.setup(mTrail.ID());
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
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageIntent();
            }
        });
    }

    private void setupTrailsystem() {
        //TODO: fill trailsystem dropdown
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v instanceof ImageView) {
            mSelectedPhoto = (ImageView) v;
            getMenuInflater().inflate(R.menu.menu_photo, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                new AlertDialog.Builder(TrailEditActivity.this)
                        .setTitle(getString(R.string.delete_photo))
                        .setMessage(getString(R.string.confirm_delete_photo))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Photo.Delete((String) mSelectedPhoto.getTag());
                                mPhotoPicker.removeView(mSelectedPhoto);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Application.NEW_TRAILSYSTEM:
                if (resultCode== RESULT_OK) {
                    if (data.hasExtra(Trailsystem.ID_EXTRA)) {
                        String value = data.getStringExtra(Trailsystem.ID_EXTRA);
                        Trailsystem.LoadAll(trailsystem, value);
                        mTrail.setTrailsystem(value);
                        trailsystem.setSelection(0);
                        for (int i=0; i<trailsystemAdapter.getCount(); i++) {
                            if ((trailsystemAdapter.getItem(i).Value().equals(value))) {
                                trailsystem.setSelection(i);
                            }
                        }
                    }
                }
                break;
            case Application.EDIT_TRAIL: {
                if (resultCode == RESULT_OK) {
                    mTrail.setLocation((LatLng) data.getParcelableExtra("location"));
                    Log.d("LATITUDE", mTrail.Location().latitude+"");
                    Log.d("LONGITUDE", mTrail.Location().longitude+"");
                }
            }
            case Application.GET_PHOTO: {
                if (resultCode == RESULT_OK) {
                    final boolean isCamera;
                    if (data == null) {
                        isCamera = true;
                    } else {
                        final String action = data.getAction();
                        if (action == null) {
                            isCamera = false;
                        } else {
                            isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        }
                    }

                    byte[] image=null;
                    if (isCamera) {
                        image=uriToByte(mOutputFileUri);
                    } else {
                        if (data!=null)
                            image=uriToByte(data.getData());
                    }
                    if (image!=null) {  //to get ID
                        final Photo photo = new Photo();
                        final byte[] img=image;
                        photo.setOwnerId(mTrail.ID());
                        photo.setData(image);
                        photo.pinInBackground();
                        photo.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    mPhotoPicker.addImage(img, photo.ID());
                                    Log.d("SaveNewPhoto", "Saved");
                                } else Log.e("SaveNewPhoto", e.getMessage());
                            }
                        });
                    }

                }
            }
        }
    }

    private void openImageIntent() {

        // Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final File sdImageMainDirectory = new File(root, "img_"+System.currentTimeMillis()+".jpg");
        mOutputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, ((Application)getApplication()).GET_PHOTO);
    }

    private byte[] uriToByte(Uri uri){
        byte[] data = null;
        try {
            ContentResolver cr = getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }


}
