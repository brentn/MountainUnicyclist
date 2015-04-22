package com.brentandjody.mountainunicyclist;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;

public class PlacesFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TrailAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public PlacesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trails, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TrailAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent locationPickerIntent = new Intent(getActivity(), LocationPicker.class);
                startActivityForResult(locationPickerIntent, Application.NEW_LOCATION);
            }
        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Application.NEW_LOCATION:
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = new Intent (getActivity(), TrailEditActivity.class);
                    if (data.hasExtra("map_screenshot")) {
                        intent.putExtra("photo", data.getByteArrayExtra("map_screenshot"));
                        intent.putExtra("isTemporaryPhoto", true);
                    }
                    if (data.hasExtra("location"))
                        intent.putExtra("location", data.getParcelableExtra("location"));
                    startActivity(intent);
                }
                break;
            case Application.EDIT_TRAIL:
                if (resultCode == Activity.RESULT_OK) {
                    break;
                }
        }
    }

}


