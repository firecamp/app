package ca.xef6.app;

import org.json.JSONException;
import org.json.JSONObject;

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
import ca.xef6.app.util.PictureDownloadTask;
import ca.xef6.app.util.ProfilePictureDownloadTask;

import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

public class ProfileActivity extends Activity {

    HorizontalScrollView mutualFriendsScrollView;
    LinearLayout	 mutualFriendsLayout;

    ImageButton	  coverPhotoButton;
    ImageButton	  profilePictureButton;
    TextView	     userNameView;
    String	       userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.contact_profile);
	coverPhotoButton = (ImageButton) findViewById(R.id.profile_background_Button);
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
	new ProfilePictureDownloadTask(profilePictureButton, getResources())
		.execute(user.getId());
	userNameView.setText(user.getName());

	sendRequests();
    }

    private void sendRequests() {

	final Activity activity = this;
	final Session session = Session.getActiveSession();

	final RequestBatch batch = new RequestBatch();

	batch.add(Request.newGraphPathRequest(session, "/me/mutualfriends/" + userId + "/", new Callback() {
	    @Override
	    public void onCompleted(Response response) {

		GraphObjectList<GraphUser> users = response.getGraphObject().getPropertyAsList("data", GraphUser.class);
		for (GraphUser user : users) {
		    ProfilePictureView profilePictureView = new ProfilePictureView(activity);
		    profilePictureView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		    profilePictureView.setProfileId(user.getId());
		    mutualFriendsLayout.addView(profilePictureView);
		}

	    }
	}));

	// FIXME: If code in onCompleted() uses the response, this request
	// calls into ProfilePictureDownloadTask for some reason. I added a
	// null check in there so it doesn't crash but it shouldn't be called
	// at all. PPDT.onPostExecute() is called with sourceImage = null
	// and Java shows FileNotFoundException.
	// FIXME: This code is duplicated in ProfileFragment.

	Request loadCoverPhoto = Request.newGraphPathRequest(session, userId, new Callback() {
	    @Override
	    public void onCompleted(Response response) {
		JSONObject cover = (JSONObject) response.getGraphObject().getProperty("cover");
		String url = null;
		try {
		    url = cover.getString("source");
		} catch (JSONException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		if (url != null) {
		    new PictureDownloadTask(coverPhotoButton).execute(url);
		}
	    }
	});

	Bundle params = new Bundle();
	params.putString("fields", "cover");
	loadCoverPhoto.setParameters(params);

	batch.add(loadCoverPhoto);

	batch.executeAsync();
    }

    public void startChat(View v) {
	Uri uri = Uri.parse("fb://messaging/" + userId);
	startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

}
