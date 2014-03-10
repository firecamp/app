package ca.xef6.app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import ca.xef6.app.ui.Fragment;
import ca.xef6.app.util.ProfilePictureDownloadTask;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class ProfileFragment extends Fragment {

	ImageButton profilePictureButton;
	TextView userNameView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.profile, container, false);
		profilePictureButton = (ImageButton) view.findViewById(R.id.profile_picture_button);
		userNameView = (TextView) view.findViewById(R.id.user_name_view);
		setUserData(Session.getActiveSession());
		return view;
	}

	@Override
	public void onSessionStateChange(Session session, SessionState state, Exception exception) {
		setUserData(session);
	}

	private void setUserData(final Session session) {
		if (session == null) {
			Log.e("ProfileFragment", "session is null!!!!");
		}
		Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
			@Override
			public void onCompleted(GraphUser user, Response response) {
				if (session == Session.getActiveSession()) {
					if (user != null && profilePictureButton != null && userNameView != null) {
						new ProfilePictureDownloadTask(profilePictureButton, getActivity().getResources()).execute(user.getId());
						userNameView.setText(user.getName());
					}
				}
				if (response != null && response.getError() != null) {
					//handleError(response.getError());
				}
			}
		});
		request.executeAsync();

	}
}
