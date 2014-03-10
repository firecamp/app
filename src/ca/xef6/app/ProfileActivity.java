package ca.xef6.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import ca.xef6.app.ui.Activity;
import ca.xef6.app.util.GraphUserParcelable;
import ca.xef6.app.util.ProfilePictureDownloadTask;

public class ProfileActivity extends Activity {

	ImageButton profilePictureButton;
	TextView userNameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_profile);
		profilePictureButton = (ImageButton) findViewById(R.id.profile_picture_button);
		userNameView = (TextView) findViewById(R.id.user_name_view);
		Intent intent = getIntent();
		GraphUserParcelable user = null;
		if (intent != null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				user = extras.getParcelable("user");
			}
		}
		Log.i("ProfileActivity", "GOT USER id = " + user.getId());
		new ProfilePictureDownloadTask(profilePictureButton, getResources()).execute(user.getId());
		userNameView.setText(user.getName());
	}

}
