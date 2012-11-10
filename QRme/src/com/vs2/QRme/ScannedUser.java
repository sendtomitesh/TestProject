package com.vs2.QRme;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ScannedUser extends Activity {
	LinearLayout layoutLoading;
	TextView textScannedUsername,textPointsEarned,textTotalPoints;
	String[] scannedInfo;
	String[] otherScanInfo;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_up, R.anim.hold_y);
		setContentView(R.layout.scanned_user);
		textScannedUsername = (TextView) findViewById(R.id.txt_scanned_username);
		textPointsEarned = (TextView) findViewById(R.id.txt_points_earned);
		textTotalPoints = (TextView) findViewById(R.id.txt_total_points);
		Intent scannedUserIntent = getIntent();
		
		if(scannedUserIntent.hasExtra("ScannedUserInfo"))
		{
			scannedInfo = scannedUserIntent.getStringArrayExtra("ScannedUserInfo");
			String username = "You Scanned " + scannedInfo[2];
			String pointsEarned = "Points earned +" + scannedInfo[3];
			String totalPoints = "Total Points = " + scannedInfo[4];
			textScannedUsername.setText(username);
			textPointsEarned.setText(pointsEarned);
			textTotalPoints.setText(totalPoints);
			
		}
		if(scannedUserIntent.hasExtra("OtherScan"))
		{
			otherScanInfo = scannedUserIntent.getStringArrayExtra("OtherScan");
			String codeScanned = "Scanned Information \n" + otherScanInfo[2];
			String pointsEarned = "Points earned +" + otherScanInfo[3];
			String totalPoints = "Total Points = " + otherScanInfo[4];
			textScannedUsername.setText(codeScanned);
			textPointsEarned.setText(pointsEarned);
			textTotalPoints.setText(totalPoints);
			
		}
		if(scannedUserIntent.hasExtra("ScanError"))
		{
			   textScannedUsername.setText(scannedUserIntent.getStringExtra("ScanError"));
		}
		
	}
	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.hold_y, R.anim.slide_down);
		super.onPause();
		
	}

	public void gotoMeetupList(View v)
	{
		Intent settingsIntent = new Intent(getApplicationContext(),MeetupList.class);
		startActivity(settingsIntent);
		finish();
	}
	
	public void gotoMain(View v) {
		finish();
	}
}
