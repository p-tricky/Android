package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CriminalFragment extends Fragment {
	private static final String TAG = "CrimeFragment";
	public static final String EXTRA_CRIME_ID = 
			"com.bignerdranch.android.criminalintent.crime_id";
	public static final String DIALOG_DATE = "date";
	public static final String DIALOG_IMAGE = "image";
	private static int REQUEST_CODE_DATE = 0;
	private static int REQUEST_CODE_PHOTO = 1;
	private static int REQUEST_CODE_CONTACT = 2;

	private Crime mCrime;
	
	private ImageButton mPhotoButton;
	private ImageView mPhotoView;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	private Button mSuspectButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UUID crimeId = (UUID)getArguments()
				.getSerializable(EXTRA_CRIME_ID);
		CrimeLab cl = CrimeLab.get(getActivity());
		mCrime = cl.getCrime(crimeId);
		setHasOptionsMenu(true);
	}
	
	public static CriminalFragment newInstance(UUID crimeId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CRIME_ID, crimeId);
		
		CriminalFragment fragment = new CriminalFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_criminal, parent, false);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		mPhotoButton = (ImageButton)v.findViewById(R.id.crime_imageButton);
		mPhotoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent gatherEvidence = new Intent(getActivity(), CrimeCameraActivity.class);
				startActivityForResult(gatherEvidence, REQUEST_CODE_PHOTO);
			}
		});
		

		// If camera is not available, disable camera functionality
		PackageManager pm = getActivity().getPackageManager();
		boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
					pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
					(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD &&
					Camera.getNumberOfCameras() > 0);
		if (!hasCamera) {
			mPhotoButton.setEnabled(false);
		}

		mPhotoView = (ImageView)v.findViewById(R.id.crime_imageView);
		mPhotoView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Photo p = mCrime.getPhoto();
				if (p == null) {
					return;
				}
				FragmentManager fm = getActivity()
						.getSupportFragmentManager();
				String path = getActivity()
						.getFileStreamPath(p.getFilename()).getAbsolutePath();
				ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
			}
		});
		
		mTitleField = (EditText)v.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, 
					int before, int count) {
				mCrime.setTitle(c.toString());
			}
			
			public void beforeTextChanged(CharSequence c, int start,
					int count, int after) {
				
			}
			
			public void afterTextChanged(Editable c) {
				
			}
		});
		
		mDateButton = (Button)v.findViewById(R.id.crime_date);
		mDateButton.setText(mCrime.getDate().toString());
		mDateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
				dialog.setTargetFragment(CriminalFragment.this, REQUEST_CODE_DATE);
				dialog.show(fm, DIALOG_DATE);
			}
		});
		
		mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mCrime.setSolved(isChecked);
			}
		});
		
		Button reportButton = (Button)v.findViewById(R.id.crime_reportButton);
		reportButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent sendReport = new Intent(Intent.ACTION_SEND);
				sendReport.setType("text/plain");
				sendReport.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
				sendReport.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
				sendReport = Intent.createChooser(sendReport, getString(R.string.send_report));
				startActivity(sendReport);
			}
		});

		mSuspectButton = (Button)v.findViewById(R.id.crime_suspectButton);
		mSuspectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent identifySuspect = new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(identifySuspect, REQUEST_CODE_CONTACT);
			}
		});
		
		if (mCrime.getSuspect() != null) {
			mSuspectButton.setText(mCrime.getSuspect());
		}

		return v;
	}
	
	private void showPhoto() {
		Photo p = mCrime.getPhoto();
		BitmapDrawable b = null;
		if (p != null) {
			String path = getActivity()
					.getFileStreamPath(p.getFilename()).getAbsolutePath();
			b = PictureUtils.getScaledDrawable(getActivity(), path);
		}
		mPhotoView.setImageDrawable(b);
	}
	
	private String getCrimeReport() {
		String solvedString = null;
		if (mCrime.isSolved()) {
			solvedString = getString(R.string.crime_report_solved);
		} else {
			solvedString = getString(R.string.crime_report_unsolved);
		}
		
		String dateFormat = "EEE, MMM dd";
		String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
		
		String suspect = mCrime.getSuspect();
		if (suspect == null) {
			suspect = getString(R.string.crime_report_no_suspect);
		} else {
			suspect = getString(R.string.crime_report_suspect, suspect);
		}
		
		String report = getString(R.string.crime_report, mCrime.getTitle(), 
				dateString, solvedString, suspect);
		
		return report;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		if (requestCode == REQUEST_CODE_DATE) {
			Date d = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(d);
			mDateButton.setText(mCrime.getDate().toString());
		} else if (requestCode == REQUEST_CODE_PHOTO) {
			// Create a new Photo object and attach it to the crime
			String filename = data
					.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
			if (filename != null) {
				Photo p = new Photo(filename);
				mCrime.setPhoto(p);
				showPhoto();
				Log.i(TAG, "Crime: " + mCrime.getTitle() + " has a photo");
			}
		} else if (requestCode == REQUEST_CODE_CONTACT) {
			Uri contactUri = data.getData();
			
			//Specify which fields you your query to return values for
			String[] queryFields = new String[] {
					ContactsContract.Contacts.DISPLAY_NAME
			};
			// Perform your query, the contactUri is like a "where" clause here
			Cursor c = getActivity().getContentResolver().query(contactUri,
					queryFields, null, null, null);

			// Double check that you got results
			if (c.getCount() == 0) {
				c.close();
				return;
			}
			
			// Pull out the first column of the first row of data--the suspect's name
			c.moveToFirst();  //moves cursor to the first row
			String suspect = c.getString(0); // extracts string from first column
			mCrime.setSuspect(suspect);
			mSuspectButton.setText(suspect);
			c.close();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (NavUtils.getParentActivityName(getActivity()) != null) {
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		CrimeLab.get(getActivity()).saveCrimes();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		showPhoto();
	}
}
