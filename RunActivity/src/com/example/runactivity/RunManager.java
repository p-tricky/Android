package com.example.runactivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.example.runactivity.RunDatabaseHelper.LocationCursor;
import com.example.runactivity.RunDatabaseHelper.RunCursor;

public class RunManager {
	private static final String TAG = "RunManager";
	
	private static final String PREFS_FILE = "runs";
	private static final String PREF_CURRENT_RUN_ID = "RunManager.currentRunId";
	
	public static final String ACTION_LOCATION =
			"com.example.runtracker.ACTION_LOCATION";
	
	private static final String TEST_PROVIDER = "TEST_PROVIDER";

	private static RunManager sRunManager;
	private Context mAppContext;
	private LocationManager mLocationManager;
	private RunDatabaseHelper mHelper;
	private SharedPreferences mPrefs;
	private long mCurrentRunId;
	
	private RunManager(Context appContext) {
		mAppContext = appContext;
		mLocationManager = (LocationManager) mAppContext
				.getSystemService(Context.LOCATION_SERVICE);
		mHelper = new RunDatabaseHelper(mAppContext);
		mPrefs = mAppContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
		mCurrentRunId = mPrefs.getLong(PREF_CURRENT_RUN_ID, -1);
	}
	
	public static RunManager get(Context c) {
		if (sRunManager == null) {
			sRunManager = new RunManager(c.getApplicationContext());
		}
		return sRunManager;
	}
	
	private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
		Intent broadcast = new Intent(ACTION_LOCATION);
		int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
		
		return PendingIntent.getBroadcast(mAppContext, 0, broadcast, flags);
	}
	
	public void startLocationUpdates() {
		String provider = LocationManager.GPS_PROVIDER;
		// If you have the test provider and it's enabled, use it
		if (mLocationManager.getProvider(TEST_PROVIDER) != null &&
				mLocationManager.isProviderEnabled(TEST_PROVIDER)) {
			provider = TEST_PROVIDER;
		}
		Log.d(TAG, "Using provider " + provider);
		
		// Get the last known location and broadcast it if you have one
		Location lastKnown = mLocationManager.getLastKnownLocation(provider);
		if (lastKnown != null) {
			// reset the time to now
			lastKnown.setTime(System.currentTimeMillis());
			broadcastLocation(lastKnown);
		}
		
		// Start updates from the location manager
		PendingIntent pi = getLocationPendingIntent(true);
		mLocationManager.requestLocationUpdates(provider, 0, 0, pi);
	}
	
	private void broadcastLocation(Location location) {
		Intent broadcast = new Intent(ACTION_LOCATION);
		broadcast.putExtra(LocationManager.KEY_LOCATION_CHANGED, location);
		mAppContext.sendBroadcast(broadcast);
	}
	
	public Run startNewRun() {
		// Insert a run into the db
		Run run = insertRun();
		// Start tracking the run
		startTrackingRun(run);
		return run;
	}
	
	public Run getRun(long id) {
		Run run = null;
		RunCursor cursor = mHelper.queryRun(id);
		cursor.moveToFirst();
		// If you got a row, get a run
		if (!cursor.isAfterLast()) run = cursor.getRun();
		cursor.close();
		return run;
	}
	
	public void startTrackingRun(Run run) {
		// Keep the ID
		mCurrentRunId = run.getId();
		// Store it in shared preferences
		mPrefs.edit().putLong(PREF_CURRENT_RUN_ID, mCurrentRunId).commit();
		// Start location updates
		startLocationUpdates();
	}
	
	public void stopRun() {
		stopLocationUpdates();
		mCurrentRunId = -1;
		mPrefs.edit().remove(PREF_CURRENT_RUN_ID).commit();
	}

	public Run insertRun() {
		Run run = new Run();
		run.setId(mHelper.insertRun(run));
		return run;
	}
	
	public RunCursor queryRuns() {
		return mHelper.queryRuns();
	}
	
	public Location getLastLocationForRun(long runId) {
		Location loc = null;
		LocationCursor cursor = mHelper.queryLastLocationForRun(runId);
		cursor.moveToFirst();
		// If you get a row, get a location
		if (!cursor.isAfterLast()) {
			loc = cursor.getLocation();
		}
		cursor.close();
		return loc;
	}
	
	public LocationCursor queryLocationsForRun(long runId) {
		return mHelper.queryLocationsForRun(runId);
	}
	
	public void insertLocation(Location loc) {
		if (mCurrentRunId != -1) {
			mHelper.insertLocation(mCurrentRunId, loc);
		} else {
			Log.e(TAG, "Location received with no tracking run.");
		}
	}
	
	public void stopLocationUpdates() {
		PendingIntent pi = getLocationPendingIntent(false);
		if (pi != null) {
			mLocationManager.removeUpdates(pi);
			pi.cancel();
		}
	}
	
	public boolean isTrackingRun() {
		return getLocationPendingIntent(false) != null;
	}
	
	public boolean isTrackingRun(Run run) {
		return run != null && run.getId() == mCurrentRunId;
	}
}
