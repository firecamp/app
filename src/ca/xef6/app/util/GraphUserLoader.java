package ca.xef6.app.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class GraphUserLoader extends AsyncTaskLoader<List<GraphUser>> {

	private List<GraphUser> users;

	public GraphUserLoader(Context context) {
		super(context);
	}

	public GraphUser getUser(int id) {
		if (users == null) {
			return null;
		}
		if (id < 0 || id >= users.size()) {
			Log.e("GraphUserLoader", "getUser: invalid id");
			return null;
		}
		return users.get(id);
	}

	@Override
	public void deliverResult(List<GraphUser> users) {
		if (isStarted()) {
			super.deliverResult(users);
		}
	}

	@Override
	public List<GraphUser> loadInBackground() {
		final List<GraphUser> usersTT = new ArrayList<GraphUser>();
		Session session = Session.getActiveSession();
		if (session == null || !session.isOpened()) {
			users = null;
			return usersTT;
		} else {
			users = usersTT;
		}
		Request request = Request.newMyFriendsRequest(session, new Request.GraphUserListCallback() {

			@Override
			public void onCompleted(List<GraphUser> users, Response response) {
				for (GraphUser user : users) {
					if (user != null) {
						usersTT.add(user);
					}
				}
			}

		});
		Request.executeAndWait(request);
		return usersTT;
	}

	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();
	}

	@Override
	protected void onStartLoading() {
		if (users != null) {
			deliverResult(users);
		} else {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

}
