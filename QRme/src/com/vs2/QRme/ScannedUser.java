package com.vs2.QRme;



import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookActivity;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;


public class ScannedUser extends FacebookActivity {
	LinearLayout layoutLoading;
	TextView textScannedUsername,textPointsEarned,textTotalPoints;
	String[] scannedInfo;
	String[] otherScanInfo;
	AdView adView;
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final int REAUTH_ACTIVITY_CODE = 100;
	@SuppressWarnings("unused")
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	@SuppressWarnings("unused")
	private boolean pendingPublishReauthorization = false;
	private static final String TAG = "MainFragment";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_up, R.anim.hold_y);
		setContentView(R.layout.scanned_user);
		
		startAdmobAd();
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
			publishStory("I met with "+scannedInfo[2]+" using QRme and earned "+scannedInfo[3]+" Points!");
			
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
			publishStory("I scanned a QRcode using QRme and earned "+otherScanInfo[3]+"Points!");
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
	private void publishStory(String title) {
	    Session session = Session.getActiveSession();

	    if (session != null){

	        // Check for publish permissions    
	        List<String> permissions = session.getPermissions();
	        if (!isSubsetOf(PERMISSIONS, permissions)) {
	            pendingPublishReauthorization = true;
	            Session.ReauthorizeRequest reauthRequest = new Session
	                    .ReauthorizeRequest(this, PERMISSIONS)
	                    .setRequestCode(REAUTH_ACTIVITY_CODE);
	        session.reauthorizeForPublish(reauthRequest);
	            return;
	        }

	        Bundle postParams = new Bundle();
	        postParams.putString("name", title);
	        postParams.putString("caption", "QRme is a Great app earn Mobile recharge with fun.");
	        postParams.putString("description", "QR codes/barcodes are available on various products. Using android phone with camera and QRme app you can scan codes. Upon scanning codes you will be rewarded with points. Points can be redeemed in terms of mobile recharge and cash");
	        postParams.putString("link", "http://www.facebook.com/QRmeCommunity");
	        postParams.putString("picture", "http://108.161.130.243/~vs2/qrme/qrme512.png");

	        Request.Callback callback= new Request.Callback() {
	            @SuppressWarnings("unused")
				public void onCompleted(Response response) {
	                JSONObject graphResponse = response
	                                           .getGraphObject()
	                                           .getInnerJSONObject();
	                String postId = null;
	                try {
	                    postId = graphResponse.getString("id");
	                } catch (JSONException e) {
	                    Log.i(TAG,
	                        "JSON error "+ e.getMessage());
	                }
	                FacebookRequestError error = response.getError();
	                if (error != null) {
	                    Toast.makeText(getApplicationContext(),
	                         error.getErrorMessage(),
	                         Toast.LENGTH_SHORT).show();
	                    } 
	                }
	            
	        };

	        Request request = new Request(session, "me/feed", postParams, 
	                              HttpMethod.POST, callback);

	        RequestAsyncTask task = new RequestAsyncTask(request);
	        task.execute();
	    }

	}
	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == REAUTH_ACTIVITY_CODE) {
	        Session.getActiveSession().onActivityResult(ScannedUser.this, 
	            requestCode, resultCode, data);
	    }
	 }
}
