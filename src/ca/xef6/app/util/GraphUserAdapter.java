package ca.xef6.app.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ca.xef6.app.R;

import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

public class GraphUserAdapter extends ArrayAdapter<GraphUser> {

    private LayoutInflater inflater;

    public GraphUserAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2); // FIXME: Why this layout here?
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.row_people, parent, false);
        } else {
            view = convertView;
        }
        GraphUser user = getItem(position);
        // Fill the view with user data:
        if (user != null) {
            ((ProfilePictureView) view.findViewById(R.id.profile_picture_button)).setProfileId(user.getId());
            ((TextView) view.findViewById(R.id.id1)).setText(user.getName());
            //((TextView) view.findViewById(R.id.status1)).setText(user.getLocation().getCity());
        }
        return view;
    }

    public void setData(List<GraphUser> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }

}
