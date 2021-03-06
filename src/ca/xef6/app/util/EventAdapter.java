package ca.xef6.app.util;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.ResourceCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import ca.xef6.app.R;
import ca.xef6.app.db.EventsTable;

public class EventAdapter extends ResourceCursorAdapter {

    private final Map<String, Bitmap> bitmapCache = new HashMap<String, Bitmap>();

    public EventAdapter(Context context, int layout, Cursor c, int flags) {
	super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

	String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(EventsTable.COL_IMAGEURL));

	Log.w("EventAdapter", "imageUrl = " + imageUrl);
	ImageView iv = ((ImageView) view.findViewById(R.id.event_row_image));
	if (imageUrl != null) {
	    if (!bitmapCache.containsKey(imageUrl)) {
		bitmapCache.put(imageUrl, BitmapFactory.decodeFile(imageUrl));
	    }
	    iv.setImageBitmap(bitmapCache.get(imageUrl));
	} else {
	    iv.setVisibility(View.GONE);
	}

	((TextView) view.findViewById(R.id.event_row_name)).setText(
		cursor.getString(cursor.getColumnIndexOrThrow(EventsTable.COL_NAME)));

	((TextView) view.findViewById(R.id.event_row_description)).setText(
		cursor.getString(cursor.getColumnIndexOrThrow(EventsTable.COL_DESCRIPTION)));

	((TextView) view.findViewById(R.id.event_row_date)).setText(
		cursor.getString(cursor.getColumnIndexOrThrow(EventsTable.COL_DATE)));

	((TextView) view.findViewById(R.id.event_row_time)).setText(
		cursor.getString(cursor.getColumnIndexOrThrow(EventsTable.COL_TIME)));

	TextView locationTV = ((TextView) view.findViewById(R.id.event_row_location));

	String location = cursor.getString(cursor.getColumnIndexOrThrow(EventsTable.COL_LOC_ADDRESS));
	if (location == null) {
	    location = "Lat: " + cursor.getFloat(cursor.getColumnIndexOrThrow(EventsTable.COL_LAT)) + ", "
		    + "lng: " + cursor.getFloat(cursor.getColumnIndexOrThrow(EventsTable.COL_LNG));
	}
	locationTV.setText(location);

    }
}
