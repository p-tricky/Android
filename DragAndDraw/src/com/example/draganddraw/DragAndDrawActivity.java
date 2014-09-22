package com.example.draganddraw;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class DragAndDrawActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new DragAndDrawFragment();
	}

}
