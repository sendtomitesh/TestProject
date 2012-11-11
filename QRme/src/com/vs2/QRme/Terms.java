package com.vs2.QRme;



import com.airpush.android.Airpush;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Terms extends Activity {
Airpush airpush;
AdView adView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_up, R.anim.hold_y);
		setContentView(R.layout.terms);
		startAdmobAd();
	}
	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.hold_y, R.anim.slide_down);
		super.onPause();
		startAirpushAd();
	}
	public void startAirpushAd() {
		airpush = new Airpush(this);
		
		airpush.startSmartWallAd();
		// start icon ad.
		airpush.startIconAd();
		

	}
	public void startAdmobAd() {
		adView = new AdView(this, AdSize.BANNER, "a150990da66be0d");

		LinearLayout l = (LinearLayout) findViewById(R.id.linear);
		l.addView(adView);

		AdRequest request = new AdRequest();
		adView.loadAd(request);

	}
	public void gotoMain(View v) {

		finish();
	}
}
