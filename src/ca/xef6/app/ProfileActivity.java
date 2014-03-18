package ca.xef6.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import ca.xef6.app.ui.Activity;
import ca.xef6.app.util.GraphUserParcelable;
import ca.xef6.app.util.ProfilePictureDownloadTask;

import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

public class ProfileActivity extends Activity {

	HorizontalScrollView mutualFriendsScrollView;
	LinearLayout mutualFriendsLayout;

	ImageButton profilePictureButton;
	TextView userNameView;
	String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_profile);
		profilePictureButton = (ImageButton) findViewById(R.id.profile_picture_button);
		userNameView = (TextView) findViewById(R.id.user_name_view);

		mutualFriendsScrollView = (HorizontalScrollView) findViewById(R.id.mutual_friends_scroll_view);
		mutualFriendsLayout = (LinearLayout) findViewById(R.id.mutual_friends_layout);

		Intent intent = getIntent();
		GraphUserParcelable user = null;
		if (intent != null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				user = extras.getParcelable("user");
			}
		}
		Log.i("ProfileActivity", "GOT USER id = " + user.getId());
		userId = user.getId();
		new ProfilePictureDownloadTask(profilePictureButton, getResources()).execute(user.getId());
		userNameView.setText(user.getName());
		loadMutualFriends(mutualFriendsLayout);
	}

	private void loadMutualFriends(final LinearLayout layout) {
		final Activity activity = this;
		Session session = Session.getActiveSession();
		Request request = Request.newGraphPathRequest(session, "/me/mutualfriends/" + userId + "/", new Callback() {

			@Override
			public void onCompleted(Response response) {
				GraphObjectList<GraphUser> users = response.getGraphObject().getPropertyAsList("data", GraphUser.class);
				for (GraphUser user : users) {
					ProfilePictureView profilePictureView = new ProfilePictureView(activity);
					profilePictureView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
					profilePictureView.setProfileId(user.getId());
					layout.addView(profilePictureView);
				}
			}

		});
		request.executeAsync();
	}

	public void startChat(View v) {
		Uri uri = Uri.parse("fb://messaging/" + userId);
		startActivity(new Intent(Intent.ACTION_VIEW, uri));
	}

}
