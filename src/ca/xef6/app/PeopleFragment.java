package ca.xef6.app;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ca.xef6.app.ui.ListFragment;
import ca.xef6.app.util.GraphUserAdapter;
import ca.xef6.app.util.GraphUserLoader;

import com.facebook.model.GraphUser;

public class PeopleFragment extends ListFragment implements LoaderCallbacks<List<GraphUser>> {

    private GraphUserAdapter adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new GraphUserAdapter(getActivity());
        setListAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<List<GraphUser>> onCreateLoader(int id, Bundle args) {
        return new GraphUserLoader(getActivity());
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

}
