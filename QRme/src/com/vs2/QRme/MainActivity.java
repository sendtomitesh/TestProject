package com.vs2.QRme;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.FacebookActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.google.android.gcm.GCMRegistrar;



public class MainActivity extends FacebookActivity  {
	String gcm_id="";
	ImageView pic;
	
	ProgressDialog pd;
	
	Boolean isUserInDatabase = false;
	boolean terms=false;

	private final Context appContexts = this;
//	String[] permissions121 = { "offline_access", "publish_stream", "user_photos",
	//		"publish_checkins", "photo_upload" };
	List<String> fbPerm = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fbPerm.add("offline_access");
		fbPerm.add("publish_stream");
		fbPerm.add("photo_upload");
		fbPerm.add("publish_actions");
		
		if(getGCMfromSP()==null )
	  		gcm_id=setGCM_Id();
		else
		{
			gcm_id=getGCMfromSP();
			terms=true;
		}
		
		
		if (Utility.hasConnection(appContexts) == false) 
			{
			showAlert();
			} 
		else 
			{
			   
			 	if(this.isSessionOpen())
				{
					if(getSessionState().isOpened())
					{
						login();
					}
				}
				else
				{
					if(terms)
						initiateLogin();
					else
						showTermsOfUse();
					
					//	this.openSessionForPublish(getString(R.string.app_id), fbPerm);
					
				
				}
			
			}  
	}

	

	private void showTermsOfUse() {
		// TODO Auto-generated method stub
		 TextView msg1=new TextView(MainActivity.this);
		 msg1.setText(getString(R.string.termOfUseText));
		 msg1.setGravity(Gravity.CENTER);
		 LinearLayout dialogLayout = new LinearLayout(MainActivity.this);
         dialogLayout.addView(msg1);
		 dialogLayout.setOrientation(LinearLayout.VERTICAL);
		 ScrollView sv= new ScrollView(MainActivity.this);
		 sv.addView(dialogLayout);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		// Add the buttons
		builder.setTitle("Terms and Conditions");
		
		builder.setView(sv);
		builder.setPositiveButton("I accept", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User clicked OK button
		        	   initiateLogin();
		           }
		       });
		builder.setNegativeButton("I do not accept", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User cancelled the dialog
		        	   finish();
		           }
		       });
		// Set other dialog properties
		
        
		// Create the AlertDialog
		AlertDialog dialog = builder.create();	
		dialog.show();
	}

    private void initiateLogin()
    {
    	this.openSessionForPublish(getString(R.string.app_id), fbPerm);
    }

	private void login()
	{
		Request request = Request.newMeRequest(
			      this.getSession(),
			      new Request.GraphUserCallback() {
			        // callback after Graph API response with user object
			        @Override
			        public void onCompleted(GraphUser user, Response response) {
			          if (user != null) {
			        	
			        	  setValues(user);
			        	  
			        	  moveToQR();
			        	
			          }
			        }
			      }
			    );
			    Request.executeBatchAsync(request);
	}
	
	@Override
	protected void onSessionStateChange(SessionState state, Exception exception) {
	  // user has either logged in or not ...
	  if (state.isOpened()) {
	    // make request to the /me API
	    Request request = Request.newMeRequest(
	      this.getSession(),
	      new Request.GraphUserCallback() {
	        // callback after Graph API response with user object
	        @Override
	        public void onCompleted(GraphUser user, Response response) {
	          if (user != null) {
	        	  if(gcm_id=="")
	        	  	{
		        	  	if(getGCMfromSP()==null )
		        	  		gcm_id=setGCM_Id();
		      			else
		      				gcm_id=getGCMfromSP();
	        	  	}
	        	  	storeGCMtoSP(gcm_id);
	        	  	setValues(user);
	        	  	new storeUserInDatabase().execute(user.getId(),user.getName(),gcm_id);
	        	  
	           
	        	  	
	          }
	        }
	      }
	    );
	    Request.executeBatchAsync(request);
	  }
	}		


	private void setValues(GraphUser user)
	{
		Utility.facebookId=user.getId();
	  	Utility.facebookUsername=user.getName();
	  	Utility.facebookAvatarUrl=user.getLink();
	  	Utility.s_gcmId=gcm_id;
	  
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

	private void storeGCMtoSP(String gcm)
	{
		SharedPreferences sp= getSharedPreferences("gcm",MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("gcm_id", gcm);
		editor.commit();
		
	}
	private String getGCMfromSP()
	{
		SharedPreferences sp=getSharedPreferences("gcm",MODE_PRIVATE);
		return sp.getString("gcm_id", null);
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
	private class storeUserInDatabase extends AsyncTask<String, Integer, Integer>
	{

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			return DatabaseFunctions.inserUserinDatabase(params[0],params[1],params[2],Utility.generateQrCode());
					
			
		}
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			moveToQR();
			super.onPostExecute(result);
			
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
	
}
