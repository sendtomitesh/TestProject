package com.vs2.QRme;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airpush.android.Airpush;
import com.facebook.FacebookActivity;
import com.facebook.widget.ProfilePictureView;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class MainQR extends FacebookActivity {

	ImageView qrCodeImage;
	TextView welcomeText, pointsText;
	ProgressBar progressPoints;
	LinearLayout progressQrImage;
	private Boolean flag = false;
	ProgressDialog pd;
	int qrCodeWidth = 230;
	int qrCodeHeight = 230;
	AdView adView;
	Airpush airpush;
	private final Context appContext=this;
	
	private ProfilePictureView profilePictureView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_qr);
		if (Utility.hasConnection(appContext) == false) {
			showAlert();
		} else {
			globalInitialize();
			if (!Utility.isPointsChanged)
				Utility.isPointsChanged = true;
			if (!Utility.isQrRefreshed)
				Utility.isQrRefreshed = true;
			loadData();
			flag = true;
			startAdmobAd();
			startNotificationAd();

		}

	}
	
	

	@Override
	protected void onPause() {
		super.onPause();
		flag = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Utility.hasConnection(appContext) == false) {
			showAlert();
		} else {
			if (flag == false) {
				loadData();
			}
			startAdmobAd();
			//startAirpushAd();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_qr, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_settings:
			// Do Something
			Intent settingsIntent = new Intent(appContext,
					Settings.class);
			startActivity(settingsIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void startAdmobAd() {
		adView = new AdView(this, AdSize.BANNER, "a150990da66be0d");

		LinearLayout l = (LinearLayout) findViewById(R.id.linear);
		l.addView(adView);

		AdRequest request = new AdRequest();
		adView.loadAd(request);

	}

	public void startNotificationAd()
	{
		airpush = new Airpush(this);
		
		airpush.startPushNotification(false);
		
	}

	private void showAlert() {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				appContext);
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

	public void refreshCode(View v) {
		if (Utility.hasConnection(appContext) == false) {
			showAlert();
		} else {
			Utility.isQrRefreshed = true;
			RefreshCode code = new RefreshCode();
			code.execute();

		}
	}

	public void globalInitialize() {
		qrCodeImage = (ImageView) findViewById(R.id.qr_code);
		welcomeText = (TextView) findViewById(R.id.welcome);
		pointsText = (TextView) findViewById(R.id.your_points);
		progressPoints = (ProgressBar) findViewById(R.id.progress_points);
		progressQrImage = (LinearLayout) findViewById(R.id.progress_qr_image);
		welcomeText.setText(Utility.facebookUsername.toString() + "!");
		profilePictureView = (ProfilePictureView)findViewById(R.id.prof_pic);
		profilePictureView.setProfileId(Utility.facebookId);

	}

	public void loadData() {
		LoadData data = new LoadData();
		data.execute();
	}

	public void gotoScan(View v) {
		Intent intent = new Intent(appContext, Scanner.class);
		startActivity(intent);
	}

	public void gotoExchange(View v) {
		Intent exchangeIntent = new Intent(appContext,
				ExchangeRate.class);
		startActivity(exchangeIntent);
	}

	public void gotoMeetups(View v) {
		Intent meetupIntent = new Intent(appContext,
				MeetupList.class);
		startActivity(meetupIntent);
	}

	public void gotoMessages(View v) {
		Intent messageIntent = new Intent(appContext,
				Messages.class);
		startActivity(messageIntent);
	}

	public void gotoReadings(View v) {
		Intent readingsIntent = new Intent(appContext,
				RedeemCash.class);
		startActivity(readingsIntent);
	}

	public void howtouseClicked(View v) {
		Intent termsIntent = new Intent(appContext, Howtouse.class);
		startActivity(termsIntent);
	}

	public class LoadData extends AsyncTask<String, Integer, String> {

		JSONObject object;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (Utility.isQrRefreshed) {
				hideQrImage();
			} else {
				showQrImage();
			}

			if (Utility.isPointsChanged) {
				hidePoints();
			} else {
				showPoints();
			}

		}

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub

			String url = Utility.getServerPath() + "userinfo.php?id="
					+ Utility.facebookId;
			Log.d("USERINFO", url);

			object = Utility.getJSONfromURL(url);

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			try {
				JSONObject json;
				json = (JSONObject) new JSONTokener(object.toString())
						.nextValue();

				// progressPoints.setVisibility(View.INVISIBLE);
				// pointsText.setVisibility(View.VISIBLE);

				if (Utility.isPointsChanged) {
					pointsText.setText("" + json.getInt("Points"));
					Utility.earnedPoints = json.getInt("Points");

					Utility.isPointsChanged = false;
				}

				if (Utility.isQrRefreshed) {
					new encodeQR(qrCodeImage).execute(json.getString("Qrcode"));
					Utility.isQrRefreshed = false;
				}
				showPoints();
				// loadQrImage(qrImageUrl+json.getString("Qrcode"));

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public void showQrImage() {
		progressQrImage.setVisibility(View.INVISIBLE);
		qrCodeImage.setVisibility(View.VISIBLE);

	}

	public void hideQrImage() {
		qrCodeImage.setVisibility(View.INVISIBLE);
		progressQrImage.setVisibility(View.VISIBLE);
	}

	public void showPoints() {
		progressPoints.setVisibility(View.INVISIBLE);
		pointsText.setVisibility(View.VISIBLE);

	}

	public void hidePoints() {
		pointsText.setVisibility(View.INVISIBLE);
		progressPoints.setVisibility(View.VISIBLE);
	}

	public class RefreshCode extends AsyncTask<String, Integer, String> {

		int resultCode;

		@Override
		protected void onPreExecute() {

			hideQrImage();

		}

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub

			resultCode = DatabaseFunctions.updateQrCode(Utility
					.generateQrCode());

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			if (resultCode == 0) {
				loadData();

			} else {
				Toast.makeText(appContext,
						getString(R.string.CodeNotRefMsg), Toast.LENGTH_LONG)
						.show();
			}
			// pd.dismiss();
		}

	}

	final class encodeQR extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public encodeQR(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String qrCode = urls[0];
			Bitmap mIcon11 = Bitmap.createBitmap(qrCodeWidth, qrCodeHeight,
					Config.RGB_565);
			QRCodeWriter qr = new QRCodeWriter();
			try {
				BitMatrix bm = qr.encode(qrCode, BarcodeFormat.QR_CODE,
						qrCodeWidth, qrCodeHeight);
				for (int i = 0; i < qrCodeWidth; i++) {
					for (int j = 0; j < qrCodeHeight; j++) {
						mIcon11.setPixel(i, j, bm.get(i, j) ? Color.BLACK
								: Color.WHITE);
					}
				}

			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {

			bmImage.setImageBitmap(result);
			showQrImage();
		}
	}

}
