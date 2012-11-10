package com.vs2.QRme;

import com.google.android.gcm.GCMBaseIntentService;




import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GCMIntentService extends GCMBaseIntentService {
    public static String TAG = "GCMIntentService";
    int count=0;
    
   
  //  public GCMIntentService(String senderId) {
  //      super(senderId);
  //      Log.d("GCMIntentService", senderId);
  //  }

    @Override
    protected void onError(Context arg0, String arg1) {
        Log.d("onError", arg1);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId){
        Log.d("onRecoverableError", errorId);
        return false;
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.d("onMessage", String.valueOf(intent));
        
        String message = intent.getExtras().getString("qrme_message");
		long timestamp = intent.getLongExtra("timestamp", -1);
		
		
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.push_icon)
		        .setContentTitle("Message from QRme!")
		        .setContentText(message);
				
				
		Intent resultIntent = new Intent(this, Messages.class);

		
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		mBuilder.setSound(uri);
		mBuilder.setAutoCancel(true);
		mBuilder.setNumber(count);
		
		mNotificationManager.notify(0,mBuilder.build());
 
	   count++;
	        
	   //     Intent notificationIntent = new Intent(context, Messages.class);
	   //     notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
	   //            Intent.FLAG_ACTIVITY_SINGLE_TOP);
	   //     PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
	      
	  //      note.setLatestEventInfo(context, "App Notification", message, pendingIntent);
	  //      note.number = count++;
	  //      note.defaults |= Notification.DEFAULT_SOUND;
//		note.defaults |= Notification.DEFAULT_VIBRATE;
	//	note.defaults |= Notification.DEFAULT_LIGHTS;
	//	note.flags |= Notification.FLAG_AUTO_CANCEL;
	 //       notificationManager.notify(0, note);
    }

    @Override
    protected void onRegistered(Context arg0, String arg1) {
        Log.d("onRegistered", arg1);
    }

    @Override
    protected void onUnregistered(Context arg0, String arg1) {
        Log.d("onUnregistered", arg1);
    }

	
}
