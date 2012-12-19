package com.vs2.QRme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class DatabaseFunctions {

	public static boolean isUserExistInDatabase(String FacebookUserId) {
		return false;
		// check for user in database here
	}

	public static int inserUserinDatabase(String FbUserId, String FbUserName,
			String gcd_id, String QrCode) {

		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(Utility.getServerPath() + "insert.php");

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
			@SuppressWarnings("unused")
			String responseBody = EntityUtils.toString(response.getEntity());
			//Log.e("INSERT RESPONSE", responseBody.toString());
			return 0;
		} catch (ClientProtocolException e) {
			//Log.e("Client error", e.toString());

		} catch (IOException e) {
			//Log.e("IO Error", e.toString());

		}
		return 1;

	}

	public static void sendCashRequest(String amount, String source,
			String type, String gcm_id,String operator,String circle) {
		// Create a new HttpClient and Post Header
		//Log.d("CASH REQUEST", "Sending request");
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(Utility.getServerPath()
				+ "cashrequest.php");

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs
					.add(new BasicNameValuePair("id", Utility.facebookId));
			nameValuePairs.add(new BasicNameValuePair("amt", amount));
			nameValuePairs.add(new BasicNameValuePair("source", source));
			nameValuePairs.add(new BasicNameValuePair("type", type));
			nameValuePairs.add(new BasicNameValuePair("gcm_id", gcm_id));
			if(operator != null){
				nameValuePairs.add(new BasicNameValuePair("operator_id", operator));
			}
			if(circle != null){
				nameValuePairs.add(new BasicNameValuePair("circle_id", circle));
			}
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			@SuppressWarnings("unused")
			String responseBody = EntityUtils.toString(response.getEntity());
			//Log.d("CASH REQUEST RESPONSE", responseBody.toString());
		} catch (ClientProtocolException e) {
			//Log.e("Client error", e.toString());
		} catch (IOException e) {
			//Log.e("IO Error", e.toString());
		}

	}
	
	

	public static String getExchangeRate() {
		// Create a new HttpClient and Post Header
		JSONObject jsonObject = Utility.getJSONfromURL(Utility.getServerPath()
				+ "exchange.php");
		String result = "";
		try {
			result = jsonObject.getString("result").toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return result;
	}
	
	public static JSONObject getCurrentExchangeRate() throws JSONException {
		// Create a new HttpClient and Post Header
		JSONObject jsonObject;
		jsonObject = Utility.getJSONfromURL(Utility.getServerPath()
				+ "exchange.php");
		return jsonObject;
	}

	public static int updateQrCode(String QrCode) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(Utility.getServerPath()
				+ "updateqrcode.php");
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
				//Log.e("Facebook error", e.toString());
			} catch (JSONException e) {
				//Log.e("JSONException", e.toString());
			}

		} catch (ClientProtocolException e) {
			//Log.e("Client error", e.toString());
		} catch (IOException e) {
			//Log.e("IO Error", e.toString());
		}
		return check;

	}

	public static String[] scanCode(String QrCode) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(Utility.getServerPath()
				+ "scaninsert.php");
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
				// Log.d("RESPONSE","Result : " + check[0] );
				// Log.d("RESPONSE","FACEBOOK ID : "+check[1] );
				// Log.d("RESPONSE","FACEBOOK USERNAME : " + check[2] );

			} catch (FacebookError e) {
				//Log.e("Facebook error", e.toString());
			} catch (JSONException e) {
				//Log.e("JSONException", e.toString());
			}

		} catch (ClientProtocolException e) {
			//Log.e("Client error", e.toString());
		} catch (IOException e) {
			//Log.e("IO Error", e.toString());
		}

		return check;

	}

	public static ArrayList<HashMap<String, String>> getOperators() {
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

		JSONObject jo = null;
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(Utility.getServerPath() + "/opeatorlist.php");

		try {

			// Execute HTTP Post Request

			HttpResponse response = httpclient.execute(httppost);
			jo = Utility.getjsonFromInputStream(response.getEntity()
					.getContent());

			JSONArray GroupsList = jo.getJSONArray("Operator");
			for (int i = 0; i < GroupsList.length(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject Groups = GroupsList.getJSONObject(i);
				map.put("id", Groups.getString("id"));
				map.put("Operator", Groups.getString("Operator"));
				mylist.add(map);

			}
			// if(jo.getString("error").equals("0"))return jo;
			// else return 1;
		} catch (ClientProtocolException e) {

			//Log.e("Client error", e.toString());
			// return 2;
		} catch (IOException e) {

			//Log.e("IO Error", e.toString());
			// return 3;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		return mylist;
	}
	
	public static ArrayList<HashMap<String, String>> getCircle() {
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

		JSONObject jo = null;
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(Utility.getServerPath() + "/circlelist.php");

		try {

			// Execute HTTP Post Request

			HttpResponse response = httpclient.execute(httppost);
			jo = Utility.getjsonFromInputStream(response.getEntity()
					.getContent());

			JSONArray GroupsList = jo.getJSONArray("Circle");
			for (int i = 0; i < GroupsList.length(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject Groups = GroupsList.getJSONObject(i);
				map.put("id", Groups.getString("id"));
				map.put("state", Groups.getString("state"));
				mylist.add(map);

			}
			// if(jo.getString("error").equals("0"))return jo;
			// else return 1;
		} catch (ClientProtocolException e) {

			//Log.e("Client error", e.toString());
			// return 2;
		} catch (IOException e) {

			//Log.e("IO Error", e.toString());
			// return 3;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		return mylist;
	}
	
	
	public static int delteMessage(String messageId) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(Utility.getServerPath()
				+ "messagedelete.php");
		int result = 0;
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs
			.add(new BasicNameValuePair("user_id", Utility.facebookId));
			nameValuePairs.add(new BasicNameValuePair("id", messageId));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			String responseBody = EntityUtils.toString(response.getEntity());
			
			JSONObject json = null;


			try {
				json = Util.parseJson(responseBody);

				 result = Integer.parseInt(json.getString("Result"));
				
			} catch (FacebookError e) {
				//Log.e("Facebook error", e.toString());
				return 1;
			} catch (JSONException e) {
				//Log.e("JSONException", e.toString());
				return 1;
			}

			//Log.d("CASH REQUEST RESPONSE", responseBody.toString());
		} catch (ClientProtocolException e) {
			//Log.e("Client error", e.toString());
			return 1;
		} catch (IOException e) {
			//Log.e("IO Error", e.toString());
			return 1;
		}
		
		return result;

	}


}
