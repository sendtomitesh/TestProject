package com.vs2.QRme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.Facebook;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class RedeemCash extends Activity {

	Spinner spinnerSource;
	EditText textSource, textAmount;
	TextView textPoints,textMinimumCashout,textMinimumRecharge;
	Button btnSendRequest;
	String sourceType;
	int points;
	Double amount;
	SharedPreferences sp;
	AdView adView;
	Facebook fb;
	LinearLayout layoutCircle, layoutOperator;
	Spinner spinnerCircle, spinnerOperator;
	String operatorId;
	String circleId; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_up, R.anim.hold_y);
		setContentView(R.layout.redeemcash);
		startAdmobAd();

		globalInitailize();
		loadSource();

		// Utility.postMessageOnWall("Good afternoon friends!");
	}

	public void startAdmobAd() {
		adView = new AdView(this, AdSize.BANNER, "a150990da66be0d");

		LinearLayout l = (LinearLayout) findViewById(R.id.linear);
		l.addView(adView);

		AdRequest request = new AdRequest();
		adView.loadAd(request);

	}

	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.hold_y, R.anim.slide_down);
		super.onPause();

	}

	public void globalInitailize() {
		textSource = (EditText) findViewById(R.id.txt_payment_type);
		textPoints = (TextView) findViewById(R.id.txt_points);
		textAmount = (EditText) findViewById(R.id.txt_amount);
		textMinimumCashout = (TextView) findViewById(R.id.txt_minimum_cashout);
		textMinimumRecharge = (TextView) findViewById(R.id.txt_minimum_recharge);
		btnSendRequest = (Button) findViewById(R.id.btn_send_request);
		layoutCircle = (LinearLayout) findViewById(R.id.layout_circle);
		layoutOperator = (LinearLayout) findViewById(R.id.layout_operator);
		spinnerCircle = (Spinner) findViewById(R.id.spinner_circle);
		spinnerOperator = (Spinner) findViewById(R.id.spinner_operator);
		new LoadPoints().execute();
		new LoadOperator().execute();
		new LoadCircle().execute();
		new LoadExchangeRate().execute();
	}

		public void gotoMain(View v) {

		finish();
	}

	public void sendRequest(View v) {

		try {

			if (amount < 50) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.inSufBalMsg), Toast.LENGTH_LONG)
						.show();
			} else {

				Double amt = Double
						.parseDouble(textAmount.getText().toString());
				if (amt <= amount) {
					String source = textSource.getText().toString();
					if(sourceType == "Mobile"){
						SendCashRequest request = new SendCashRequest();
						request.execute(amt.toString(), source, sourceType,
								Utility.s_gcmId,operatorId,circleId);
						finish();
					}
					else{
						SendCashRequest request = new SendCashRequest();
						request.execute(amt.toString(), source, sourceType,
								Utility.s_gcmId,null,null);
						finish();
					}
					

				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.inSufBalMsg), Toast.LENGTH_LONG)
							.show();
				}

			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.errInReqMsg), Toast.LENGTH_LONG).show();
		}

	}

	@SuppressWarnings("unused")
	private void showAlert() {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				getApplicationContext());
		// Add the buttons
		builder.setMessage(getString(R.string.noInternetMsg));
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public void loadSelectedSource(int type) {
		if (type == 1) {
			layoutCircle.setVisibility(View.VISIBLE);
			layoutOperator.setVisibility(View.VISIBLE);
			textMinimumRecharge.setVisibility(View.VISIBLE);
			textMinimumCashout.setVisibility(View.GONE);
			textSource.setHint("Mobile No");
			textSource.setInputType(InputType.TYPE_CLASS_PHONE);
			sourceType = "Mobile";
		} else {
			layoutCircle.setVisibility(View.GONE);
			layoutOperator.setVisibility(View.GONE);
			textMinimumRecharge.setVisibility(View.GONE);
			textMinimumCashout.setVisibility(View.VISIBLE);
			textSource.setHint("Email ");
			textSource
					.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			sourceType = "Paypal";
		}
	}

	public void loadSource() {
		spinnerSource = (Spinner) findViewById(R.id.spinner_source);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.source_array,
				android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerSource.setAdapter(adapter);
		spinnerSource.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				Log.d("Selected Item", "Position : " + position);
				int pos = position + 1;
				loadSelectedSource(pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});

	}

	
	
	public class SendCashRequest extends AsyncTask<String, Integer, String> {

		JSONObject jsonObject;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			btnSendRequest.setText(getString(R.string.sndReqMsg));
		}

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub

			DatabaseFunctions.sendCashRequest(urls[0], urls[1], urls[2],
					urls[3],urls[4],urls[5]);

			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			Toast.makeText(getApplicationContext(),
					getString(R.string.reqSentMsg), Toast.LENGTH_LONG).show();
			Utility.isPointsChanged = true;
			btnSendRequest.setText(getString(R.string.send_request));
			if (Utility.allowPostOnWall) {
				//Toast.makeText(getApplicationContext(), "comming in allow",
					//	Toast.LENGTH_LONG).show();
				fb = new Facebook(getString(R.string.app_id));
				sp = getPreferences(MODE_PRIVATE);

				fb.setAccessToken(sp.getString("access_token", null));
				fb.setAccessExpires(sp.getLong("expires", 0));
				Utility.postMessageOnWall(fb,
						"I have just earned by using QRme Android app. Its awesome!");
			}

		}

	}
	
	public class LoadExchangeRate extends AsyncTask<String, Integer, JSONObject> {

		

		@Override
		protected JSONObject doInBackground(String... urls) {
			// TODO Auto-generated method stub
			JSONObject jsonObject = null;
			try {
				jsonObject = DatabaseFunctions.getCurrentExchangeRate();
				return jsonObject;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {

			super.onPostExecute(result);
			try {
				if(result != null){
					textMinimumRecharge.setText("Min. Recharge = " + result.getString("rechargeamt").toString() + " INR");
					textMinimumCashout.setText("Min. Cashout = " + result.getString("cashamt").toString() + " USD");
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}

	}



	public class LoadOperator extends
			AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... urls) {
			// TODO Auto-generated method stub
			return DatabaseFunctions.getOperators();
		}

		
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {

			

			final SpinnerData items[] = new SpinnerData[result.size()];
			for (int i = 0; i < items.length; i++) {

				items[i] = new SpinnerData(result.get(i).get("Operator"),
						result.get(i).get("id"));
			}

			ArrayAdapter<SpinnerData> adapter = new ArrayAdapter<SpinnerData>(
					getApplicationContext(),
					R.layout.spinneritem, items);
			adapter.setDropDownViewResource(R.layout.spinneritem);
			spinnerOperator.setAdapter(adapter);
			spinnerOperator
					.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							SpinnerData data = items[position];
							operatorId = data.getValue();
						//	Toast.makeText(
							//		getApplicationContext(),
								//	"Value : " + data.getValue() + "\n Text : "
									//		+ data.getSpinnerText(),
									//Toast.LENGTH_SHORT).show();
						}

						public void onNothingSelected(AdapterView<?> parent) {

						}
					});

			super.onPostExecute(result);
		} 
		
 
	}

	public class LoadCircle extends
			AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... urls) {
			// TODO Auto-generated method stub
			return DatabaseFunctions.getCircle();
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {

			super.onPostExecute(result);

			final SpinnerData items[] = new SpinnerData[result.size()];
			for (int i = 0; i < items.length; i++) {

				items[i] = new SpinnerData(result.get(i).get("state"),
						result.get(i).get("id"));
			}

			ArrayAdapter<SpinnerData> adapter = new ArrayAdapter<SpinnerData>(
					getApplicationContext(),
					R.layout.spinneritem, items);
			adapter.setDropDownViewResource(R.layout.spinneritem);
			spinnerCircle.setAdapter(adapter);
			spinnerCircle
					.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							SpinnerData data = items[position];
							circleId = data.getValue();
						}

						public void onNothingSelected(AdapterView<?> parent) {

						}
					});

		}

	}

	public class LoadPoints extends AsyncTask<String, Integer, String> {

		JSONObject jsonObject;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			textPoints.setText(getString(R.string.LoadingMsg));
		}

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub
			jsonObject = Utility.getJSONfromURL(Utility.getServerPath()
					+ "/calculatepoints.php?id=" + Utility.facebookId);

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			try {
				points = jsonObject.getInt("point");
				amount = jsonObject.getDouble("amt");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			textPoints.setText(points + " Pts = " + amount + " INR");

		}

	}

	@SuppressWarnings({ "deprecation" })
	public void postMessageOnWall(String msg) {
		if (fb.isSessionValid()) {
			Bundle parameters = new Bundle();
			parameters.putString("message", msg);
			try {

				String response = fb.request("me/feed", parameters, "POST");
				// System.out.println(response);
				
				Log.d("POST RESPONSE", "RESPONSE : " + response.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// login();
			Log.d("POST LOGIN ERROR", "FACEBOOK USER NOT LOGGED IN");
		}
	}

	class SpinnerData {
		public SpinnerData(String spinnerText, String value) {
			this.spinnerText = spinnerText;
			this.value = value;
		}

		public String getSpinnerText() {
			return spinnerText;
		}

		public String getValue() {
			return value;
		}

		public String toString() {
			return spinnerText;
		}

		String spinnerText;
		String value;
	}
}
