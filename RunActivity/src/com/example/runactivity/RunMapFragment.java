package com.example.runactivity;

import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.runactivity.RunDatabaseHelper.LocationCursor;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

public class RunMapFragment extends SupportMapFragment 
		implements LoaderCallbacks<Cursor> {
	private static final String ARG_RUN_ID = "RUN_ID";
	private static final int LOAD_LOCATIONS = 0;
	
	private GoogleMap mGoogleMap;
	private LocationCursor mLocationCursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Check for a Run Id as an argument and find the run
		Bundle args = getArguments();
		long runId = args.getLong(ARG_RUN_ID, -1);
		if (runId != -1) {
			LoaderManager lm = getLoaderManager();
			lm.initLoader(LOAD_LOCATIONS, args, this);
		}
	}
	
	public static RunMapFragment newInstance(long runId) {
		Bundle args = new Bundle();
		args.putLong(ARG_RUN_ID, runId);
		RunMapFragment rf = new RunMapFragment();
		rf.setArguments(args);
		return rf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup parent, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, parent, savedInstanceState);
		
		//Stash a reference to the GoogleMap
		mGoogleMap = getMap();
		// Show the user's location
		mGoogleMap.setMyLocationEnabled(true);
		
		return v;
	}

	private void updateUI() {
		if (mGoogleMap == null || mLocationCursor == null) return;
		
		// Set up an overlay on the map for this run's locations
		// Create a polyline with all of the points
		PolylineOptions line = new PolylineOptions();
		LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
		// Iterate over the locations
		mLocationCursor.moveToFirst();
		while (!mLocationCursor.isAfterLast()) {
			Location loc = mLocationCursor.getLocation();
			LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
			line.add(latLng);
			latLngBuilder.include(latLng);
			mLocationCursor.moveToNext();
		}
		// Add the polyline to the map
		mGoogleMap.addPolyline(line);
		// Make the map zoom to show the track with some padding
		// Use the size of the current display in pixels as a bounding box
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		// Construct a movement instruction for the map camera
		LatLngBounds latLngBounds = latLngBuilder.build();
		CameraUpdate movement = CameraUpdateFactory.newLatLngBounds(latLngBounds, 
				display.getWidth(), display.getHeight(), 15);
		mGoogleMap.moveCamera(movement);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		long runId = arg1.getLong(ARG_RUN_ID, -1);
		return new LocationListCursorLoader(getActivity(), runId);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		mLocationCursor = (LocationCursor)arg1;
		updateUI();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// Stop using the data
		mLocationCursor.close();
		mLocationCursor = null;
	}
}
