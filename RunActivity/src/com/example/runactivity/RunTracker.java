package com.example.runactivity;

import android.support.v4.app.Fragment;

public class RunTracker extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new RunFragment();
	}


}
