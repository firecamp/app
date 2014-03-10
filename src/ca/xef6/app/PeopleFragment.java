package ca.xef6.app;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import ca.xef6.app.ui.ListFragment;
import ca.xef6.app.util.GraphUserAdapter;
import ca.xef6.app.util.GraphUserLoader;
import ca.xef6.app.util.GraphUserParcelable;

import com.facebook.model.GraphUser;

public class PeopleFragment extends ListFragment implements LoaderCallbacks<List<GraphUser>> {

	private GraphUserAdapter adapter;
	private GraphUserLoader loader;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new GraphUserAdapter(getActivity());
		setListAdapter(adapter);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<List<GraphUser>> onCreateLoader(int id, Bundle args) {
		if (loader == null) {
			loader = new GraphUserLoader(getActivity());
		}
		return loader;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.people, container, false);
		return view;
	}

	@Override
	public void onLoadFinished(Loader<List<GraphUser>> loader, List<GraphUser> data) {
		adapter.setData(data);
	}

	@Override
	public void onLoaderReset(Loader<List<GraphUser>> loader) {
		adapter.setData(null);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		boolean ok = false;
		Log.i("PeopleFragment", "onListItemClick, id = " + id);
		if (loader != null) {
			GraphUser user = loader.getUser((int) id);
			if (user != null) {
				Log.i("PeopleFragment", "user id = " + user.getId());
				Intent intent = new Intent(getActivity(), ProfileActivity.class);
				ok = true;
				GraphUserParcelable gup = new GraphUserParcelable(user);
				intent.putExtra("user", gup);
				startActivity(intent);
			}
		}
		if (!ok) {
			Toast.makeText(getActivity(), "Contact the developer if you see this!", Toast.LENGTH_SHORT).show();
		}
	}

}
