package com.vs2.QRme;

import com.airpush.android.Airpush;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExchangeRate extends Activity {

	private TextView txtExchangeRate;
	AdView adView;
	Airpush airpush;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_up, R.anim.hold_y);
		setContentView(R.layout.exchange_rate);
		if(Utility.hasConnection(getApplicationContext()) == false)
		{
			showAlert();
		}
		else
		{
			txtExchangeRate = (TextView) findViewById(R.id.txt_exchange_rate);
			loadCurrentRate();
		}
		
		
	}
	public void startAdmobAd()
	{
		adView = new AdView(this, AdSize.BANNER,"a150990da66be0d");
		
        LinearLayout l=(LinearLayout)findViewById(R.id.linear);
        l.addView(adView);

        AdRequest request = new AdRequest();
        adView.loadAd(request);

	}
	public void startAirpushAd() {
		airpush = new Airpush(this);
		
		airpush.startPushNotification(false);
		// start icon ad.
		airpush.startIconAd();
		

	}
	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.hold_y, R.anim.slide_down);
		super.onPause();
		
	}
	
	private void showAlert() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
		// Add the buttons
		builder.setMessage(getString(R.string.noInternetMsg));
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				finish();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public void gotoMain(View v) {
		
		finish();
	}
	
	public void loadCurrentRate()
	{
		LoadExchangeRate rate = new LoadExchangeRate();
		rate.execute();
	}

	public class LoadExchangeRate extends AsyncTask<String, Integer, String> {

		String currentRate;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			txtExchangeRate.setText(getString(R.string.LoadingMsg));
		}

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub

			currentRate = DatabaseFunctions.getExchangeRate();

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			txtExchangeRate.setText(currentRate);
			startAdmobAd();
			startAirpushAd();
			
		}

	}

}
