package com.vs2.QRme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;


public class Scanner extends CaptureActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scanner);
	}

	@Override
	public void handleDecode(Result rawResult, Bitmap barcode) {

		super.handleDecode(rawResult, barcode);
		getResponse(DatabaseFunctions.scanCode(rawResult.toString()),rawResult.toString());

	}

	public void getResponse(String[] check,String qrCode) {
		String resultString = "";
		int checkResult = Integer.parseInt(check[0].toString());
		
		//Log.d("RESPONSE","Result : " + check[0]);
		//Log.d("RESPONSE","FACEBOOK ID : " + check[1]);
		//Log.d("RESPONSE","FACEBOOK USERNAME : " + check[2]);
		//Log.d("RESPONSE","POINTS EARNED : " + check[3]);
		//Log.d("RESPONSE","TOTAL POINTS : " + check[4]);
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
		if(resultString != "")
		{
			Toast.makeText(getApplicationContext(), resultString, Toast.LENGTH_LONG).show();
		}
	}
	
	public void resultIntent(String result)
	{
		Intent scannedUserIntent = new Intent(getApplicationContext(),ScannedUser.class);
		scannedUserIntent.putExtra("ScanError", result);
		startActivity(scannedUserIntent);
		finish();
	}

}
