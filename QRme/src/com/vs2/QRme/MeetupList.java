package com.vs2.QRme;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.airpush.android.Airpush;

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


public class MeetupList extends Activity {
	LinearLayout layoutLoading;
	ListView listview;
	AdView adView;
	//Airpush airpush;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_up, R.anim.hold_y);
		setContentView(R.layout.meetup_list);
		layoutLoading = (LinearLayout)findViewById(R.id.layout_loading);
		listview = (ListView) findViewById(R.id.listview_meetup);
		String url = Utility.getServerPath() + "scanlist.php?id=" + Utility.facebookId;
		LoadMessages messages = new LoadMessages();
		messages.execute(url);
		
	//	startAirpushAd();
		//loadMessageFromUrl(url);
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
				
				JSONArray MeetupList = jsonObj.getJSONArray("Meetup");
				// Loop the Array
				for (int i = 0; i < MeetupList.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject MeetUp = MeetupList.getJSONObject(i);
					map.put("Fb_Username", MeetUp.getString("Fb_Username"));
					map.put("count","MeetUp (" + MeetUp.getString("count") + ")");
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
			String[] from = new String[] { "Fb_Username","count"};
			int[] to = new int[] { R.id.txt_username,R.id.txt_count };
			//final ListView listview = (ListView) findViewById(R.id.listview_message);
			ListAdapter adapter = new SimpleAdapter(getApplicationContext(), mylist,R.layout.meetup_list_item, from, to);
			
			listview.setAdapter(adapter);
			layoutLoading.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
			startAdmobAd();
			//Toast.makeText(getApplicationContext(), "Got Json", Toast.LENGTH_LONG).show();

		}

	}

}
