package com.brentandjody.mountainunicyclist;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.brentandjody.mountainunicyclist.data.Photo;
import com.brentandjody.mountainunicyclist.data.Trail;
import com.brentandjody.mountainunicyclist.helpers.LocationHelper;
import com.google.android.gms.maps.model.LatLng;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by brent on 07/04/15.
 */
public class TrailAdapter extends RecyclerView.Adapter<TrailAdapter.ViewHolder> implements Observer {


    private List<Trail> mDataset;
    private Context mContext;
    private LatLng mMyLocation;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout mTitlebar;
        public TextView mTitle;
        public View mCard;
        public TextView mDistance;
        public TextView mRating;
        public TextView mTrailsystem;
        public TextView mDescription;
        public TextView mRideStats;
        public ImageView mPhoto;
        public ImageView mDifficulty;
        public TextView mEditBtn;
        public ViewHolder(View v) {
            super(v);
            mCard = v;
            mTitle = (TextView) v.findViewById(R.id.title);
            mTitlebar = (FrameLayout) v.findViewById(R.id.titlebar);
            mDistance = (TextView) v.findViewById(R.id.distance);
            mPhoto = (ImageView) v.findViewById(R.id.feature_photo);
            mDifficulty = (ImageView) v.findViewById(R.id.difficulty_icon);
            mRating = (TextView) v.findViewById(R.id.rating);
            mTrailsystem = (TextView) v.findViewById(R.id.trailsystem);
            mDescription = (TextView) v.findViewById(R.id.description);
            mRideStats = (TextView) v.findViewById(R.id.rides);
            mEditBtn = (TextView) v.findViewById(R.id.edit_button);
        }
    }

    public TrailAdapter(Context context) {
        mContext = context;
        mDataset = null;
        Trail.registerForUpdates(this);
        LoadFromParse();
        LoadAllTrails();
        mMyLocation = LocationHelper.getGPS(context);
    }

    @Override
    public void update(Observable observable, Object data) {
        Trail updated_trail = (Trail)data;
        if (mDataset.contains(updated_trail)) {
            Log.d("TrailAdapter", "Updated Trail in dataset");
        } else {
            mDataset.add(updated_trail);
            Log.d("TrailAdapter", "Added Trail to dataset");
        }
        notifyDataSetChanged();
    }

    public void LoadAllTrails() {
        Trail.LoadAllTrails(new FindCallback<Trail>() {
            @Override
            public void done(List<Trail> trails, ParseException e) {
                if (e==null) {
                    mDataset = trails;
                    notifyDataSetChanged();
                    Log.d("LoadAllTrails", "succeeded loading "+trails.size()+" trails");
                } else Log.w("LoadAllTrails", e.getMessage());

            }
        });
    }

    private void LoadFromParse() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if ((ni != null) && (ni.isConnected())) {
            ParseQuery<Trail> trail_query = Trail.getQuery();
            trail_query.findInBackground(new FindCallback<Trail>() {
                @Override
                public void done(List<Trail> trails, ParseException e) {
                    if (e==null) {
                        for (Trail trail : trails) {
                            trail.pinInBackground();
                            trail.force_update();
                        }
                        Log.d("LoadFromParse", trails.size() + " trails downloaded from Parse.com");
                    } else Log.w("LoadFromParse", e.getMessage());
                }
            });
            ParseQuery<Photo> photo_query = Photo.getQuery();
            photo_query.findInBackground(new FindCallback<Photo>() {
                @Override
                public void done(List<Photo> photos, ParseException e) {
                    for (Photo photo : photos) {
                        photo.pinInBackground();
                    }
                }
            });
        } else {
            //TODO:
        }
    }

    @Override
    public TrailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trail_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Trail trail = mDataset.get(position);
        if (trail.Name()==null) {
            removeTrail(trail);
            return;
        }
        final ImageView featured_image = holder.mPhoto;
        if (trail.Name().isEmpty()) holder.mTitle.setText(mContext.getString(R.string.trail));
        else holder.mTitle.setText(trail.Name());
        holder.mDescription.setText(trail.Description());
        holder.mPhoto.setImageBitmap(null);
        //indirect values
        Photo.LoadImage(trail.PhotoId(), new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e == null) {
                    featured_image.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                    Log.d("TrailAdapter", "featured image loaded");
                } else Log.w("TrailAdapter", e.getMessage());
            }
        });
        holder.mDifficulty.setImageResource(trail.Difficulty().Resource());
        String trailsystem = ""; //TODO:lookup trailsystem
        holder.mTrailsystem.setText(trailsystem);

        //calculated or processed values
        int rating = trail.Rating(); //TODO: this should take into consideration ride ratings
        holder.mRating.setText(trail.Stars());
        float[] result = new float[3];
        mMyLocation = LocationHelper.getGPS(mContext);
        if (mMyLocation!=null && trail.Location()!=null) {
            float[] distances = new float[3];
            Location.distanceBetween(mMyLocation.latitude, mMyLocation.longitude,
                    trail.Location().latitude, trail.Location().longitude, distances);
            holder.mDistance.setText("approx. " + String.format("%.0f", distances[0]/1000) + " km. away");
        } else holder.mDistance.setVisibility(View.INVISIBLE);
        holder.mRideStats.setText("Rides: 0/0");
        holder.mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TrailEditActivity.class);
                intent.putExtra("trailId", trail.ID());
                mContext.startActivity(intent);
            }
        });
        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TrailActivity.class );
                intent.putExtra("trailId", trail.ID());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataset==null) return 0;
        return mDataset.size();
    }

    private void removeTrail(Trail trail) {
        trail.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                Trail.LoadAllTrails(new FindCallback<Trail>() {
                    @Override
                    public void done(List<Trail> trails, ParseException e) {
                        mDataset = trails;
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }


}
