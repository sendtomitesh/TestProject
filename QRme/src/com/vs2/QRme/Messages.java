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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class Messages extends Activity {
	LinearLayout layoutLoading;
	ListView listview;
	AdView adView;
	Airpush airpush;
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

	public void startAirpushAd() {
		airpush = new Airpush(this);
		airpush.startSmartWallAd(); // launch smart wall on App start
		

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
			
			//final ListView listview = (ListView) findViewById(R.id.listview_message);
			if(mylist.size()>0)
			{
				setmsgList();
			}
			else
			{
				TextView msg =(TextView)findViewById(R.id.loadingtxt);
				msg.setText("No messages in your inbox");
			}
			
			startAdmobAd();
			startAirpushAd();
		

		}
		public void setmsgList()
		{
			String[] from = new String[] { "message"};
			int[] to = new int[] { R.id.txt_message };
			ListAdapter adapter = new SimpleAdapter(getApplicationContext(), mylist,R.layout.messages_list_item, from, to);
			listview.setAdapter(adapter);
			layoutLoading.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
			listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					
					
					AlertDialog multichoice;
		    		multichoice=new AlertDialog.Builder(Messages.this)
		    			
		               .setTitle( "Delete this message?" )
		               .setPositiveButton("OK", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					})
		    		.setNegativeButton("Cancel", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					})
		             
		               .create();
		    			
		    		multichoice.show();

					
				}
			});
		}

	}
	

}
