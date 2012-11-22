package com.vs2.QRme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.FacebookError;
import com.facebook.android.Util;

import android.util.Log;

public class DatabaseFunctions {

	public static boolean isUserExistInDatabase(String FacebookUserId) {
		return false;
		// check for user in database here
	}

	public static int inserUserinDatabase(String FbUserId, String FbUserName,String gcd_id,
			String QrCode) {
		
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(Utility.getServerPath()+"insert.php");

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("id", FbUserId));
			nameValuePairs.add(new BasicNameValuePair("username", FbUserName));
			nameValuePairs.add(new BasicNameValuePair("gcd_id", gcd_id));
			nameValuePairs.add(new BasicNameValuePair("qrcode", QrCode));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			String responseBody = EntityUtils.toString(response.getEntity());
			Log.e("INSERT RESPONSE", responseBody.toString());
			return 0;
		} catch (ClientProtocolException e) {
			Log.e("Client error", e.toString());
			
		} catch (IOException e) {
			Log.e("IO Error", e.toString());
			
		}
		return 1;

	}

	
	public static void sendCashRequest(String amount,String source,String type) {
		// Create a new HttpClient and Post Header
		Log.d("CASH REQUEST", "Sending request");
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(Utility.getServerPath()+"cashrequest.php");
		
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("id", Utility.facebookId));
			nameValuePairs.add(new BasicNameValuePair("amt", amount));
			nameValuePairs.add(new BasicNameValuePair("source", source));
			nameValuePairs.add(new BasicNameValuePair("type", type));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			String responseBody = EntityUtils.toString(response.getEntity());
			Log.d("CASH REQUEST RESPONSE", responseBody.toString());
		} catch (ClientProtocolException e) {
			Log.e("Client error", e.toString());
		} catch (IOException e) {
			Log.e("IO Error", e.toString());
		}

	}

	
	public static String getExchangeRate() {
		// Create a new HttpClient and Post Header
		JSONObject jsonObject = Utility.getJSONfromURL(Utility.getServerPath()+"exchange.php");
		String result = "";
		try {
			result = jsonObject.getString("result").toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
		

	public static int updateQrCode(String QrCode) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(
				Utility.getServerPath()+"updateqrcode.php");
		int check = 0;
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs
					.add(new BasicNameValuePair("id", Utility.facebookId));
			nameValuePairs.add(new BasicNameValuePair("qrcode", QrCode));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			JSONObject json = null;
			String responseBody = EntityUtils.toString(response.getEntity());

			try {
				json = Util.parseJson(responseBody);
				check = json.getInt("Result");

			} catch (FacebookError e) {
				Log.e("Facebook error", e.toString());
			} catch (JSONException e) {
				Log.e("JSONException", e.toString());
			}

		} catch (ClientProtocolException e) {
			Log.e("Client error", e.toString());
		} catch (IOException e) {
			Log.e("IO Error", e.toString());
		}
		return check;

	}

	
	public static String[] scanCode(String QrCode) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
		Utility.getServerPath()+"scaninsert.php");
		String[] check = new String[5];
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs
			.add(new BasicNameValuePair("id", Utility.facebookId));
			nameValuePairs.add(new BasicNameValuePair("qrcode", QrCode));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			JSONObject json = null;
			String responseBody = EntityUtils.toString(response.getEntity());

			try {
				json = Util.parseJson(responseBody);
				 
				check[0] = json.getString("Result");
				check[1] = json.getString("Fb_id");
				check[2] = json.getString("Username");
				check[3] = json.getString("earn_point");
				check[4] = json.getString("User_point");
				//Log.d("RESPONSE","Result : " + check[0] );
				//Log.d("RESPONSE","FACEBOOK ID : "+check[1] );
				//Log.d("RESPONSE","FACEBOOK USERNAME : " + check[2] );

			} catch (FacebookError e) {
				Log.e("Facebook error", e.toString());
			} catch (JSONException e) {
				Log.e("JSONException", e.toString());
			}

		} catch (ClientProtocolException e) {
			Log.e("Client error", e.toString());
		} catch (IOException e) {
			Log.e("IO Error", e.toString());
		}

		return check;

	}

}
