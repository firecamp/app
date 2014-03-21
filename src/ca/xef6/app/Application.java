package ca.xef6.app;

import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class Application extends android.app.Application {

	private String	userId;

	public void logOut(boolean clearTokenInformation) {
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			if (clearTokenInformation) {
				session.closeAndClearTokenInformation();
			} else {
				session.close();
			}
		}
	}

	public String getUserId() {
		return userId;
	}

	public void updateUserId() {
		Session session = Session.getActiveSession();
		Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
			@Override
			public void onCompleted(GraphUser user, Response response) {
				userId = user.getId();
				Log.i("Application", "updateUserId(): set userId to " + userId);
			}
		});
		request.executeAsync();
	}

}
