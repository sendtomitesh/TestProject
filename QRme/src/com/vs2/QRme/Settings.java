package com.vs2.QRme;



import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class Settings extends Activity {

	CheckBox checkAllowAdds, checkAllowPost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_up, R.anim.hold_y);
		setContentView(R.layout.settings);
		checkAllowPost = (CheckBox) findViewById(R.id.check_post);
		checkAllowAdds = (CheckBox) findViewById(R.id.check_adds);
		loadCheckbox();

	}

	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.hold_y, R.anim.slide_down);
		super.onPause();

	}

	public void gotoMain(View v) {
		finish();
	}

	public void loadCheckbox() {
		if (Utility.allowPostOnWall) {
			checkAllowPost.setChecked(true);
		} else {
			checkAllowPost.setChecked(false);

		}
		if (Utility.allowAdds) {
			checkAllowAdds.setChecked(true);
		} else {
			checkAllowAdds.setChecked(false);
		}

	}

	public void postOnWall(View v) {
		if (checkAllowPost.isChecked()) {
			Utility.allowPostOnWall = true;
			Toast.makeText(getApplicationContext(), "Wall Post :  TRUE", Toast.LENGTH_LONG).show();
		} else {
			Utility.allowPostOnWall = false;
			Toast.makeText(getApplicationContext(), "Wall Post :  FALSE", Toast.LENGTH_LONG).show();
		}
	}

	public void showAdds(View v) {
		if (checkAllowAdds.isChecked()) {
			Utility.allowAdds = true;
			Toast.makeText(getApplicationContext(), "ALLOW ADDS :  TRUE", Toast.LENGTH_LONG).show();
		} else {
			Utility.allowAdds = false;
			Toast.makeText(getApplicationContext(), "ALLOW ADDS :  FALSE", Toast.LENGTH_LONG).show();
		}
	}

}
