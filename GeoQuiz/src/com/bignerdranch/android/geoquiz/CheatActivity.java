package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends Activity {

	private boolean mAnswer;
	private TextView mAnswerTextView;
	private Button mShowAnswerButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheat);
		
		mAnswer = getIntent().getBooleanExtra(MainActivity.EXTRA_ANSWER_IS_TRUE, false);
		mAnswerTextView = (TextView)findViewById(R.id.answerTextView);

		mShowAnswerButton = (Button)findViewById(R.id.showAnswerButton);
		mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mAnswer) {
					mAnswerTextView.setText(R.string.true_button);
				} else {
					mAnswerTextView.setText(R.string.false_button);
				}
				setAnswerShownResult(true);
			}
		});
	}
	
	public void setAnswerShownResult(boolean isAnswerShown) {
		Intent data = new Intent();
		data.putExtra(MainActivity.EXTRA_ANSWER_SHOWN, isAnswerShown);
		setResult(RESULT_OK, data);
	}
}
