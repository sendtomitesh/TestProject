package com.vs2.QRme;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;



import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class Messages extends Activity {
	LinearLayout layoutLoading;
	ListView listview;
	AdView adView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_up, R.anim.hold_y);
		setContentView(R.layout.messages);
		layoutLoading = (LinearLayout)findViewById(R.id.layout_loading);
		listview = (ListView) findViewById(R.id.listview_message);
		String url = Utility.getServerPath() + "messagelist.php?id=" + Utility.facebookId;
		LoadMessages messages = new LoadMessages();
		messages.execute(url);
		startAdmobAd();
		//loadMessageFromUrl(url);
	}
	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.hold_y, R.anim.slide_down);
		super.onPause();
		
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
	
	public class LoadMessages extends AsyncTask<String, Integer, String> {
		
		JSONObject jsonObj;
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();	
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			listview.setVisibility(View.VISIBLE);
			layoutLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub
			

			jsonObj = Utility.getJSONfromURL(urls[0]);

			try {
				
				JSONArray MessageList = jsonObj.getJSONArray("Messages");
				// Loop the Array
				for (int i = 0; i < MessageList.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject Restaurant = MessageList.getJSONObject(i);
					map.put("message", Restaurant.getString("message"));
					mylist.add(map);

				}
			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());

			}

			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			String[] from = new String[] { "message"};
			int[] to = new int[] { R.id.txt_message };
			//final ListView listview = (ListView) findViewById(R.id.listview_message);
			ListAdapter adapter = new SimpleAdapter(getApplicationContext(), mylist,R.layout.messages_list_item, from, to);
			
			listview.setAdapter(adapter);
			layoutLoading.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
			//Toast.makeText(getApplicationContext(), "Got Json", Toast.LENGTH_LONG).show();

		}

	}

}
