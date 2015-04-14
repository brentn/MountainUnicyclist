package com.brentandjody.mountainunicyclist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.brentandjody.mountainunicyclist.data.Photo;
import com.brentandjody.mountainunicyclist.data.PhotoDB;
import com.brentandjody.mountainunicyclist.data.Trail;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.concurrent.Callable;

import bolts.Task;

/**
 * Created by brent on 07/04/15.
 */
public class TrailAdapter extends RecyclerView.Adapter<TrailAdapter.ViewHolder> {


    private List<Trail> mDataset;
    private PhotoDB mPhotos;
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

    public TrailAdapter(Context context, List<Trail> dataset) {
        mContext = context;
        mPhotos = ((Application)context.getApplicationContext()).getPhotos();
        mDataset = dataset;
        mMyLocation = LocationHelper.getGPS(context);

    }

    @Override
    public TrailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trail_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ViewHolder finalHolder = holder;
        final Trail trail = mDataset.get(position);
        holder.mTitle.setText(trail.Name());
        holder.mDescription.setText(trail.Description());
        holder.mPhoto.setImageBitmap(null);
        //indirect values
        Task.callInBackground(new Callable<Void>() {
            public Void call() {
                Photo photo = mPhotos.Get(trail.PhotoId());
                finalHolder.mPhoto.setImageBitmap(BitmapFactory.decodeByteArray(photo.Data(), 0, photo.Data().length));
                return null;
            }
        });
        int resID = trail.getDifficultyIcon();
        if (resID >=0) holder.mDifficulty.setImageResource(resID);
        else holder.mDifficulty.setImageResource(0);
        String trailsystem = "No trailsystem"; //TODO:lookup trailsystem
        holder.mTrailsystem.setText(trailsystem);

        //calculated or processed values
        int rating = trail.Rating(); //TODO: this should take into consideration ride ratings
        holder.mRating.setText(trail.getStars());
        float[] result = new float[3];
        Location.distanceBetween(mMyLocation.latitude,
                mMyLocation.longitude,
                trail.Location().latitude,
                trail.Location().longitude,
                result);
        float distance = result[0]/10;
        holder.mDistance.setText("approx. "+String.format("%.1f", distance)+ " km. away");
        holder.mRideStats.setText("Rides: 0/0");
        holder.mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TrailEditActivity.class);
                intent.putExtra("trail", trail);
                mContext.startActivity(intent);
            }
        });
        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TrailActivity.class );
                intent.putExtra("trail", trail);
                ((Activity)mContext).startActivityForResult(intent, MainActivity.EDIT_TRAIL);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
