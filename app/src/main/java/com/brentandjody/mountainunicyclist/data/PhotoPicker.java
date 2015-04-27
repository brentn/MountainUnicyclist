package com.brentandjody.mountainunicyclist.data;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.brentandjody.mountainunicyclist.data.Photo;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by brent on 25/04/15.
 */
public class PhotoPicker {
    private static LinearLayout.LayoutParams unselected = new LinearLayout.LayoutParams(196, LinearLayout.LayoutParams.WRAP_CONTENT);
    private static LinearLayout.LayoutParams selected = new LinearLayout.LayoutParams(228, LinearLayout.LayoutParams.WRAP_CONTENT);

    private Context mContext;
    private LinearLayout mLayout;

    public PhotoPicker(Context context, LinearLayout layout) {
        mContext = context;
        mLayout = layout;
    }

    public void setup(String owner_id) {
        Photo.LoadImagesForOwner(owner_id, new FindCallback<Photo>() {
            @Override
            public void done(List<Photo> photos, ParseException e) {
                if (e == null) {
                    for (Photo photo : photos) {
                        addPhoto(photo);
                    }
                } else Log.w("SetupPhotoPicker()", e.getMessage());
            }
        });
    }

    public void addPhoto(Photo photo) {
        ImageView iv = new ImageView(mContext);
        iv.setTag(photo.ID());
        photo.LoadInto(iv);
        addView(iv);
    }

    public void addImage(byte[] image, String id) {
        ImageView iv = new ImageView(mContext);
        iv.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        iv.setTag(id);
        iv.setLayoutParams(unselected);
        addView(iv);
    }

    public void removeView(ImageView view) {
        mLayout.removeView(view);
        mLayout.getParent().requestLayout();
    }

    public void select(String id) {
        for (int i = 0; i < mLayout.getChildCount(); i++) {
            View image = mLayout.getChildAt(i);
            if (((String) image.getTag()).equals(id)) {
                markSelected(image);
            }
        }
    }

    private void addView(View iv) {
        iv.setPadding(5,5,5,5);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markSelected(v);
            }
        });
        int pos = mLayout.getChildCount()-1;
        mLayout.addView(iv, pos);
        mLayout.getParent().requestLayout();
    }

    private void markSelected(View view) {
        for (int i = 0; i<mLayout.getChildCount(); i++) {
            mLayout.getChildAt(i).setLayoutParams(unselected);
        }
        view.setLayoutParams(selected);
        mLayout.getParent().requestLayout();
    }


}
