package com.brentandjody.mountainunicyclist;

import java.util.Locale;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.brentandjody.mountainunicyclist.data.Photo;
import com.brentandjody.mountainunicyclist.data.Trail;
import com.melnykov.fab.FloatingActionButton;


public class MainActivity extends ActionBarActivity {
    static final int NEW_TRAIL = 1;
    static final int EDIT_TRAIL = 2;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return new PlacesFragment().newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    public static class PlacesFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecyclerView mRecyclerView;
        private TrailAdapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlacesFragment newInstance(int sectionNumber) {
            PlacesFragment fragment = new PlacesFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

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
            Trail.LoadTrailAdapter(mAdapter);
            mRecyclerView.setAdapter(mAdapter);
            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            fab.attachToRecyclerView(mRecyclerView);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent locationPickerIntent = new Intent(getActivity(), LocationPickerActivity.class);
                    startActivityForResult(locationPickerIntent, NEW_TRAIL);
                }
            });

            return rootView;
        }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case NEW_TRAIL: {
                    if (resultCode == RESULT_OK) {
                        Intent intent = new Intent (getActivity(), TrailEditActivity.class);
                        if (data.hasExtra("map_screenshot")) {
                            byte[] image = data.getByteArrayExtra("map_screenshot");
                            Photo photo = new Photo(image);
                            intent.putExtra("photoid", photo.ID());
                        }
                        mAdapter.notifyDataSetChanged();
                        intent.putExtra("location", data.getExtras().getParcelable("location"));
                        startActivity(intent);
                    }
                }
                case EDIT_TRAIL: {
                    if (resultCode == RESULT_OK)
                        mAdapter.notifyDataSetChanged();
                }
            }
        }

    }

}
