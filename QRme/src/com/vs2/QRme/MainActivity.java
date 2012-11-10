package com.vs2.QRme;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.google.android.gcm.GCMRegistrar;



public class MainActivity extends Activity implements OnClickListener {
	String gcm_id;
	Facebook fb;
	ImageView pic, button;
	TextView welcome_msg;
	String Fb_Userid;
	String Fb_UserName;
	ProgressDialog pd;
	LinearLayout authLayout;
	Boolean isUserInDatabase = false;
	

	private final Context appContexts = this;
	String[] permissions = { "offline_access", "publish_stream", "user_photos",
			"publish_checkins", "photo_upload" };
	SharedPreferences sp;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.activity_main);
	
		gcm_id=setGCM_Id();

		if (Utility.hasConnection(appContexts) == false) 
			{
			showAlert();
			} 
		else {
			
			authLayout = (LinearLayout) findViewById(R.id.auth_layout);
			authLayout.setVisibility(View.GONE);
			// showAlert();

			fb = new Facebook(getString(R.string.app_id));
			sp = getPreferences(MODE_PRIVATE);
			String access_token = sp.getString("access_token", null);
			Long expires = sp.getLong("expires", 0);

			button = (ImageView) findViewById(R.id.login);
			button.setVisibility(View.GONE);
			pic = (ImageView) findViewById(R.id.user_pic);

			welcome_msg = (TextView) findViewById(R.id.welcome);
			button.setOnClickListener(this);

			if (access_token != null && expires != 0) {
				fb.setAccessToken(access_token);
				fb.setAccessExpires(expires);
				isUserInDatabase = true;
				LoadFacebookStatus lfs = new LoadFacebookStatus();
				lfs.execute("get status");
				button.setVisibility(View.GONE);

				// pic.setVisibility(View.VISIBLE);

			} else {
				button.setVisibility(View.VISIBLE);
				// pic.setVisibility(View.GONE);
			}

		}
	}

		


	private String setGCM_Id() {
		
			// TODO Auto-generated method stub
	    	
			if(GCMRegistrar.isRegistered(appContexts))GCMRegistrar.unregister(appContexts);
			
	    	Random random = new Random();
	    	long backoff = 2000 + random.nextInt(1000);
			String regId="";
			for(int i=0;i<=2;i++)
			{
				try
				{
					
					GCMRegistrar.checkDevice(MainActivity.this);
					GCMRegistrar.checkManifest(MainActivity.this);
					regId = GCMRegistrar.getRegistrationId(MainActivity.this);
					
					if (regId.equals("")) {
						
						if(!GCMRegistrar.isRegistered(appContexts))
						GCMRegistrar.register(MainActivity.this, "918369674738");
						
								  
					} 
					else 
					{
						
						return regId;
						  
					}
				}
				catch(Exception e)
				{
					 try {
		                    
		                    Thread.sleep(backoff);
		                } catch (InterruptedException e1) {
		                    // Activity finished before we complete - exit.
		                    
		                    Thread.currentThread().interrupt();
		                    return null;
		                }
					 
					
				}
				 
			}
			return regId;
		

	}




	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		fb.authorizeCallback(requestCode, resultCode, data);
	}

	private void showAlert() {
	
		AlertDialog.Builder builder = new AlertDialog.Builder(appContexts);
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

	private void moveToQR() {
		// TODO Auto-generated method stub

		Intent QRintent = new Intent(appContexts, MainQR.class);
		startActivity(QRintent);
		finish();

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		button.setVisibility(View.VISIBLE);

		fb.authorize(MainActivity.this, permissions, new DialogListener() {

			@Override
			public void onFacebookError(FacebookError e) {

				Toast.makeText(appContexts, getString(R.string.fbError), Toast.LENGTH_LONG)
						.show();
			}

			@Override
			public void onError(DialogError e) {

				Toast.makeText(appContexts, getString(R.string.fbError), Toast.LENGTH_LONG)
						.show();
			}

			@Override
			public void onComplete(Bundle values) {

				if (fb.isSessionValid()) {
					gcm_id=setGCM_Id();
					
					Editor editor = sp.edit();
					editor.putString("access_token", fb.getAccessToken());
					editor.putLong("expires", fb.getAccessExpires());
					editor.commit();
					
					
					LoadFacebookStatus lfs = new LoadFacebookStatus();
					lfs.execute();
					

				}
				// user logged in. Goto next Activity

			}

			@Override
			public void onCancel() {

				Toast.makeText(appContexts, getString(R.string.loginCancelledMsg),
						Toast.LENGTH_LONG).show();
			}
		});
	}

	public class LoadFacebookStatus extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// pd = new ProgressDialog(MainActivity.this);
			// pd.setMessage("Authenticating....");
			// pd.setIndeterminate(false);
			// pd.show();
			authLayout.setVisibility(View.VISIBLE);

		}

		@SuppressWarnings({ "deprecation", "unused" })
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			JSONObject json = null;
			URL img_url = null;
			try {
				String jsonUser = fb.request("me");
				json = Util.parseJson(jsonUser);

				Fb_Userid = json.optString("id");
				Fb_UserName = json.optString("name");
				if (!isUserInDatabase) {
					
					DatabaseFunctions.inserUserinDatabase(Fb_Userid,
							Fb_UserName,gcm_id,Utility.generateQrCode());
				}
				Utility.facebookId = Fb_Userid;
				Utility.facebookUsername = Fb_UserName;
				Utility.facebookAvatarUrl = "http://graph.facebook.com/" + Fb_Userid +"/picture?";
				//postMessageOnWall("Good afternoon friends");
			} catch (MalformedURLException e) {
				Log.d("MalformedURLException", e.toString());
				//e.printStackTrace();
			} catch (IOException e) {
				Log.d("IOException", e.toString());
				//e.printStackTrace();
			} catch (FacebookError e) {
				Log.d("FacebookError", e.toString());
				//e.printStackTrace();
			} catch (JSONException e) {
				Log.d("JSONException", e.toString());
				//e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			// pd.dismiss();
		//	authLayout.setVisibility(View.GONE);
			moveToQR();

		}

	}
	@SuppressWarnings({ "deprecation" })
	public void postMessageOnWall(String msg) {
		if (fb.isSessionValid()) {
		    Bundle parameters = new Bundle();
		    parameters.putString("message", msg);
		    try {
				
				String response = fb.request("me/feed", parameters,"POST");
				//System.out.println(response);
				Log.d("POST RESPONSE", "RESPONSE : " + response.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			//login();
			Log.d("POST LOGIN ERROR", "FACEBOOK USER NOT LOGGED IN");
		}
	}
}
