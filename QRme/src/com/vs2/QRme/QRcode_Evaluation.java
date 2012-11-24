package com.vs2.QRme;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class QRcode_Evaluation extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrevaluation);
		startAnimation();
		new codeCheck().execute(getIntent().getStringExtra("ScannedCode"));
	}

	public void getResponse(String[] check,String qrCode) {
		String resultString = "";
		int checkResult = Integer.parseInt(check[0].toString());
		
		
		switch (checkResult) {
		case 0:
			resultString = getString(R.string.scnSuccMsg);
			Utility.isPointsChanged = true;
			Intent scannedUserIntent = new Intent(getApplicationContext(),ScannedUser.class);
			scannedUserIntent.putExtra("ScannedUserInfo", check);
			startActivity(scannedUserIntent);
			finish();
			break;
		case 1:
			resultString = getString(R.string.scnOwnCodeMsg);
			resultIntent(resultString);
			break;
		case 2:
			resultString = getString(R.string.alreadyScnMsg);
			resultIntent(resultString);
			break;
		case 3:
			resultString =getString(R.string.scnErrMsg);
			resultIntent(resultString);
			break;
		case 4:
			//resultString = getString(R.string.invalidCodeMsg);
			resultString = "You scanned : " + qrCode;
			Utility.isPointsChanged = true;
			Intent scannedUserIntent1 = new Intent(getApplicationContext(),ScannedUser.class);
			scannedUserIntent1.putExtra("OtherScan", check);
			startActivity(scannedUserIntent1);
			finish();
			break;
		default:
			resultString = getString(R.string.sysConfuseMsg);
			resultIntent(resultString);
			break;
		}
		
	}
	
	public void resultIntent(String result)
	{
		Intent scannedUserIntent = new Intent(getApplicationContext(),ScannedUser.class);
		scannedUserIntent.putExtra("ScanError", result);
		startActivity(scannedUserIntent);
		finish();
	}

	private void startAnimation()
	{
		Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotation);
		ImageView img =(ImageView)findViewById(R.id.qrLogo);
        img.startAnimation(rotation);
        
        Animation animation=new ScaleAnimation(0, 2, 0, 2); 

        animation.setDuration(2000);

        animation.setRepeatMode(Animation.RESTART);

        animation.setRepeatCount(Animation.INFINITE);
        TextView text = (TextView)findViewById(R.id.textView1);
        text.startAnimation(animation);
	}
	
	
	
	
	
	private class codeCheck extends AsyncTask<String, Integer, String[]>
	{
		private String qrCode=null;
		@Override
		protected String[] doInBackground(String... params) {
			// TODO Auto-generated method stub
			qrCode=params[0];
			return DatabaseFunctions.scanCode(params[0]);
			
		}
		@Override
		protected void onPostExecute(String[] result) {
			// TODO Auto-generated method stub
			getResponse(result,qrCode);
			
			super.onPostExecute(result);
		}
	}
	

}
