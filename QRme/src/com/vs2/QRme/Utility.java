package com.vs2.QRme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class Utility {
	
	public static Boolean isPointsChanged = true;
	public static Boolean isQrRefreshed = true;
	public static int earnedPoints = 0;
	public static Boolean allowPostOnWall = true;
	public static Boolean allowAdds = true;
	public static String facebookId,facebookUsername,facebookAvatarUrl;
	public static String s_gcmId;
	private static SecureRandom random = new SecureRandom();

	public static String getServerPath()
	{
		return "http://108.161.130.243/~vs2/qrme/";
	}
	public static String generateQrCode() {
		return new BigInteger(130, random).toString(32);
	}
	
	public static boolean hasConnection(Context cont) {
	    ConnectivityManager cm = (ConnectivityManager) cont.getApplicationContext().getSystemService(
	        Context.CONNECTIVITY_SERVICE);

	    NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    if (wifiNetwork != null && wifiNetwork.isConnected()) {
	      return true;
	    }

	    NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    if (mobileNetwork != null && mobileNetwork.isConnected()) {
	      return true;
	    }

	    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	    if (activeNetwork != null && activeNetwork.isConnected()) {
	      return true;
	    }

	    return false;
	}

	
	// Get JSON Object from URL
	public static JSONObject getJSONfromURL(String url) {
		
		// initialize
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jArray = new JSONObject(result);
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		return jArray;
	}
	
	
	//Get URL encoded string
	public static String getEncodedString(String str)
	{
		String query = "";
		try {
			query = URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return query;
	}
	
	
	//I used First one
	//Post on user's wall Function 1
	@SuppressWarnings("deprecation")
	public static void postMessageOnWall(Facebook facebook,String msg) {
		if (facebook.isSessionValid()) {
		    Bundle parameters = new Bundle();
		    parameters.putString("message", msg);
		    

		    try {
				
				String response = facebook.request("me/feed", parameters,"POST");
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

	//Post on user's wall Function 2
	@SuppressWarnings("unused")
	private void postToWall(Context context,Facebook facebook) {
	    Bundle parameters = new Bundle();
	    parameters.putString("name", "Battery Monitor");
	    parameters.putString("link", "https://play.google.com/store/apps/details?id=com.ck.batterymonitor");
	    //parameters.putString("picture", "link to the picture");
	    //parameters.putString("display", "page");
	    // parameters.putString("app_id", "228476323938322");

	    facebook.dialog(context, "feed", parameters, new DialogListener() {

	        @Override
	        public void onFacebookError(FacebookError e) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void onError(DialogError e) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void onComplete(Bundle values) {
	            // TODO Auto-generated method stub
	        }

	        @Override
	        public void onCancel() {
	            // TODO Auto-generated method stub
	        }
	    });
	}

}
