package com.vs2.QRme;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import com.airpush.android.Airpush;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class Messages extends Activity {
	LinearLayout layoutLoading;
	ListView listview;
	AdView adView;
	Airpush airpush;
	ListAdapter adapter;
	String msgUrl = Utility.getServerPath() + "messagelist.php?id="
			+ Utility.facebookId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_up, R.anim.hold_y);
		setContentView(R.layout.messages);
		layoutLoading = (LinearLayout) findViewById(R.id.layout_loading);
		listview = (ListView) findViewById(R.id.listview_message);

		LoadMessages messages = new LoadMessages();
		messages.execute(msgUrl);
		 startAdmobAd();
		 startAirpushAd();
		// loadMessageFromUrl(url);
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

	public class LoadMessages extends
			AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {

		JSONObject jsonObj;

		// ArrayList<HashMap<String, String>> mylist = new
		// ArrayList<HashMap<String, String>>();
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			listview.setVisibility(View.VISIBLE);
			layoutLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... urls) {
			// TODO Auto-generated method stub

			jsonObj = Utility.getJSONfromURL(urls[0]);
			ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
			try {

				JSONArray MessageList = jsonObj.getJSONArray("Messages");
				// Loop the Array
				for (int i = 0; i < MessageList.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject Restaurant = MessageList.getJSONObject(i);
					map.put("message", Restaurant.getString("message"));
					map.put("id", Restaurant.getString("id"));
					mylist.add(map);

				}
			} catch (JSONException e) {

				Log.e("log_tag", "Error parsing data " + e.toString());
				return null;

			}

			return mylist;
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {

			super.onPostExecute(result);

			// final ListView listview = (ListView)
			// findViewById(R.id.listview_message);
			if (result.size() > 0) {
				setmsgList(result);
			} else {
				TextView msg = (TextView) findViewById(R.id.loadingtxt);
				msg.setText("No messages in your inbox");
			}

			 startAdmobAd();
			 startAirpushAd();

		}

	}

	public class DeleteMessage extends	AsyncTask<String, Integer, Integer> {

		@Override
		protected Integer doInBackground(
				String... urls) {
			// TODO Auto-generated method stub
			return DatabaseFunctions.delteMessage(urls[0]);
		}

		@Override
		protected void onPostExecute(Integer result) {

			super.onPostExecute(result);
			if(result == 0){
			//	Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
				new LoadMessages().execute(msgUrl);
			}
			else{
				Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
			}
			
		}

	}

	public void setmsgList(ArrayList<HashMap<String, String>> mylist) {
		String[] from = new String[] { "message", "id" };
		int[] to = new int[] { R.id.txt_message, R.id.txt_message_id };
		adapter = new SimpleAdapter(getApplicationContext(), mylist,
				R.layout.messages_list_item, from, to);
		listview.setAdapter(adapter);
		layoutLoading.setVisibility(View.GONE);
		listview.setVisibility(View.VISIBLE);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final TextView textId = (TextView) view
						.findViewById(R.id.txt_message_id);

				AlertDialog multichoice;
				multichoice = new AlertDialog.Builder(Messages.this)

				.setTitle("Delete this message?")
						.setPositiveButton("OK", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								new DeleteMessage().execute(textId.getText()
										.toString());
								

							}
						}).setNegativeButton("Cancel", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						})

						.create();

				multichoice.show();

			}
		});
	}

}
