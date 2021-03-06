package com.bignerdranch.android.geoquiz;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	private static final String TAG = "QuizActivity";
	private static final String KEY_INDEX = "index";
	private static final String CHEATING_HISTORY = "cheats";
	protected static final String EXTRA_ANSWER_IS_TRUE = 
			"com.bignerdranch.android.geoquiz.answer_is_true";
	protected static final String EXTRA_ANSWER_SHOWN = 
			"com.bignerdranch.android.geoquiz.answer_shown";
	private TrueFalse[] mQuestionBank = new TrueFalse[] {
			new TrueFalse(R.string.question_constantinople, false),
			new TrueFalse(R.string.question_oceans, true),
			new TrueFalse(R.string.question_mideast, false),
			new TrueFalse(R.string.question_africa, false),
			new TrueFalse(R.string.question_americas, true),
			new TrueFalse(R.string.question_asia, true),
	};
	private int mCurrentIndex;
	private TextView mQuestionTextView;
	private Button mTrueButton;
	private Button mFalseButton;
	private Button mNextButton;
	private Button mCheatButton;
	private boolean[] mCheats;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);
        
        if (savedInstanceState != null) {
        	mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
        	mCheats = savedInstanceState.getBooleanArray(CHEATING_HISTORY);
        } else {
        	mCheats = new boolean[mQuestionBank.length];
        	for (int i=0; i<mQuestionBank.length; i++) {
        		mCheats[i] = false;
        	}
        }
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        
        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (mQuestionBank[mCurrentIndex].isTrueQuestion()) {
        			if (!mCheats[mCurrentIndex]){
        				Toast.makeText(MainActivity.this, 
        						R.string.correct_toast,
        						Toast.LENGTH_SHORT).show();
        			} else {
        				Toast.makeText(MainActivity.this,
        						R.string.judgement_toast, 
        						Toast.LENGTH_LONG).show();
        			}

        		} else {
        				Toast.makeText(MainActivity.this, 
        						R.string.incorrect_toast,
        						Toast.LENGTH_SHORT).show();
        		}
        	}
        });

        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (!mQuestionBank[mCurrentIndex].isTrueQuestion()) {
        			if (!mCheats[mCurrentIndex]){
        				Toast.makeText(MainActivity.this, 
        						R.string.correct_toast,
        						Toast.LENGTH_SHORT).show();
        			} else {
        				Toast.makeText(MainActivity.this, 
        						R.string.judgement_toast, 
        						Toast.LENGTH_LONG).show();
        			}
        		} else {
        				Toast.makeText(MainActivity.this, 
        						R.string.incorrect_toast,
        						Toast.LENGTH_SHORT).show();
        		}
        	}
        });

        mNextButton = (Button)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		mCurrentIndex = (mCurrentIndex+1) % mQuestionBank.length;
        		int question = mQuestionBank[mCurrentIndex].getQuestion();
        		mQuestionTextView.setText(question);
        	}
        });
        
        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//might need to change getBaseContext() to QuizActivity.this
				Intent badIntentions = new Intent(getBaseContext(), CheatActivity.class);
				badIntentions.putExtra(EXTRA_ANSWER_IS_TRUE, mQuestionBank[mCurrentIndex].isTrueQuestion());
				startActivityForResult(badIntentions, 0);
			}
        });

        updateQuestion();
    }
    
    public void updateQuestion() {
    	int question = mQuestionBank[mCurrentIndex].getQuestion();
        mQuestionTextView.setText(question);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (data == null) {
    		return;
    	}
    	mCheats[mCurrentIndex] = data.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
    	super.onSaveInstanceState(savedInstanceState);
    	Log.i(TAG, "onSavedInstanceState");
    	savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    	savedInstanceState.putBooleanArray(CHEATING_HISTORY, mCheats);
    }

    @Override
    public void onPause() {
    	super.onPause();
    	Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
    	super.onResume();
    	Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
    	super.onStop();
    	Log.d(TAG, "onStop() called");
    }

    @Override
    public void onStart() {
    	super.onStart();
    	Log.d(TAG, "onStart() called");
    }

    @Override
    public void onDestroy() {
    	super.onDestroy();
    	Log.d(TAG, "onDestroy() called");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
