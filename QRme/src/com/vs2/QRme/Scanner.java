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
		Intent scannedUserIntent = new Intent(getApplicationContext(),QRcode_Evaluation.class);
		scannedUserIntent.putExtra("ScannedCode", rawResult.toString());
		startActivity(scannedUserIntent);
		finish();
		//getResponse(DatabaseFunctions.scanCode(rawResult.toString()),rawResult.toString());

	}

	
}
