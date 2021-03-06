package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Crime {
	
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_DATE = "date";
	private static final String JSON_SOLVED = "solved";
	private static final String JSON_PHOTO = "photo";
	private static final String JSON_SUSPECT = "suspect";

	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mSolved;
	private Photo mPhoto;
	private String mSuspect;


	public Crime() {
		//Generate unique identifier
		mId = UUID.randomUUID();
		mDate = new Date();
	}
	
	public Crime(JSONObject jo) throws JSONException {
		mId = UUID.fromString(jo.getString(JSON_ID));
		mDate = new Date(jo.getLong(JSON_DATE));
		mSolved = jo.getBoolean(JSON_SOLVED);
		if (jo.has(JSON_TITLE)) {
			mTitle = jo.getString(JSON_TITLE);
		}
		if (jo.has(JSON_PHOTO)) {
			mPhoto = new Photo(jo.getJSONObject(JSON_PHOTO));
		}
		if (jo.has(JSON_SUSPECT)) {
			mSuspect = jo.getString(JSON_SUSPECT);
		}
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_SOLVED, mSolved);
		json.put(JSON_DATE, mDate.getTime());
		if (mPhoto != null) {
			json.put(JSON_PHOTO, mPhoto.toJSON());
		}
		json.put(JSON_SUSPECT, mSuspect);
		return json;
	}

	@Override
	public String toString() {
		return mTitle;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public UUID getId() {
		return mId;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public boolean isSolved() {
		return mSolved;
	}

	public void setSolved(boolean solved) {
		mSolved = solved;
	}
	
	public Photo getPhoto() {
		return mPhoto;
	}
	
	public void setPhoto(Photo p) {
		mPhoto = p;
	}

	public String getSuspect() {
		 return mSuspect;
	 }
	
	public void setSuspect(String suspect) {
		mSuspect = suspect;
	}
}
