package com.vs2.QRme;



import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class Terms extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_up, R.anim.hold_y);
		setContentView(R.layout.terms);
	}
	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.hold_y, R.anim.slide_down);
		super.onPause();
		
	}

	public void gotoMain(View v) {

		finish();
	}
}
