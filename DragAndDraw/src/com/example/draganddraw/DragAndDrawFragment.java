package com.example.draganddraw;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DragAndDrawFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup parent, Bundle savedInstanceState) {
		View v = (View) inflater.inflate(R.layout.fragment_drag_and_draw,
				parent, false);
		return v;
	}

}