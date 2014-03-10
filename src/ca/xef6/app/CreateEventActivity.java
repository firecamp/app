package ca.xef6.app;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;
import ca.xef6.app.db.ContentProvider;
import ca.xef6.app.db.EventsTable;
import ca.xef6.app.ui.FragmentActivity;
import ca.xef6.app.util.DatePickerFragment;
import ca.xef6.app.util.TimePickerFragment;
import ca.xef6.widget.LocationPickerActivity;

import com.google.android.gms.maps.model.LatLng;

@SuppressLint("NewApi")
public class CreateEventActivity extends FragmentActivity {

    private static final class Data {
        public int    type;
        public String imageUrl;
        public double latitude;
        public double longitude;
    }

    private static final class Views {
        public ImageButton image;
        public EditText    name;
        public EditText    description;
        public Button      date;
        public Button      time;
        public Button      location;
    }
    
    boolean dateSelected;
    boolean timeSelected;

    private static final int RESULT_IMAGE_SELECTED    = 1;
    private static final int RESULT_LOCATION_SELECTED = 2;

    private final Data       data                     = new Data();
    private Uri              eventUri;
    private final Views      views                    = new Views();

    private void fillData(Uri uri) {
        String[] projection = EventsTable.ALL_COLUMNS;
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            views.image.setImageBitmap(BitmapFactory.decodeFile(data.imageUrl));
            views.name.setText(cursor.getString(cursor.getColumnIndexOrThrow(EventsTable.COL_NAME)));
            views.description.setText(cursor.getString(cursor.getColumnIndexOrThrow(EventsTable.COL_DESCRIPTION)));
            views.date.setText(cursor.getString(cursor.getColumnIndexOrThrow(EventsTable.COL_DATE)));
            views.time.setText(cursor.getString(cursor.getColumnIndexOrThrow(EventsTable.COL_TIME)));
            cursor.close();
        }
    }

    private void initializeViews() {
        setContentView(R.layout.create_event);
        views.image = (ImageButton) findViewById(R.id.image);
        views.name = (EditText) findViewById(R.id.name);
        views.description = (EditText) findViewById(R.id.description);
        views.date = (Button) findViewById(R.id.date);
        views.time = (Button) findViewById(R.id.time);
        views.location = (Button) findViewById(R.id.location);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RESULT_IMAGE_SELECTED) {
            if (resultCode == RESULT_OK && null != intent) {
                Uri selectedImage = intent.getData();
                final String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                data.imageUrl = cursor.getString(columnIndex);
                cursor.close();
                if (data.imageUrl != null) {
                    views.image.setImageBitmap(BitmapFactory.decodeFile(data.imageUrl));
                }
            }
        }
        if (requestCode == RESULT_LOCATION_SELECTED) {
            if (resultCode == RESULT_OK && null != intent) {
                LatLng latLng = (LatLng) intent.getParcelableExtra("selectedLocation");
                data.latitude = latLng.latitude;
                data.longitude = latLng.longitude;
                views.location.setText("Location: " + latLng.toString());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViews();
        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(R.layout.create_event_action_bar, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveState()){
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView);
        // Check from the saved Instance
        eventUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(ContentProvider.CONTENT_ITEM_TYPE);
        Bundle extras = getIntent().getExtras();
        // Or passed from the other activity
        if (extras != null) {
            eventUri = extras.getParcelable(ContentProvider.CONTENT_ITEM_TYPE);
            fillData(eventUri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //saveState();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //saveState();
        outState.putParcelable(ContentProvider.CONTENT_ITEM_TYPE, eventUri);
    }

    private boolean saveState() {
        String name = views.name.getText().toString();
        String description = views.description.getText().toString();
        String date = views.date.getText().toString();
        String time = views.time.getText().toString();
        if (name.length() == 0 || description.length() == 0
                || date.length() == 0 || time.length() == 0 || !timeSelected || !dateSelected) {
        	Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_LONG).show();
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(EventsTable.COL_NAME, name);
        values.put(EventsTable.COL_TYPE, data.type);
        values.put(EventsTable.COL_DESCRIPTION, description);
        values.put(EventsTable.COL_AUTHOR, "TODO <facebook id here>");
        values.put(EventsTable.COL_DATE, date);
        values.put(EventsTable.COL_TIME, time);
        values.put(EventsTable.COL_IMAGEURL, data.imageUrl);
        values.put(EventsTable.COL_LAT, data.latitude);
        values.put(EventsTable.COL_LNG, data.longitude);
        if (eventUri == null) {
           eventUri = getContentResolver().insert(ContentProvider.CONTENT_URI, values);
        } else {
        	Log.w("CreateEventActivity", eventUri.toString());
         getContentResolver().update(eventUri, values, null, null);
        }
        return true;
    }

    public void showDatePickerDialog(View view) {
        final Button button = (Button) view;
        new DatePickerFragment() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String text = String.format("%04d-%02d-%02d", year, monthOfYear, dayOfMonth);
                button.setText(text);
                dateSelected = true;
            }

        }.show(getSupportFragmentManager(), "datePicker");
    }

    public void showImagePickerDialog(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_IMAGE_SELECTED);
    }

    public void showLocationPicker(View view) {
        startActivityForResult(new Intent(this, LocationPickerActivity.class), RESULT_LOCATION_SELECTED);
    }

    public void showTimePickerDialog(View view) {
        final Button button = (Button) view;
        new TimePickerFragment() {

            @Override
            public void onTimeSet(TimePicker tp, int hourOfDay, int minute) {
                String text = String.format("%02d:%02d", hourOfDay, minute);
                button.setText(text);
                timeSelected = true;
            }

        }.show(getSupportFragmentManager(), "timePicker");
    }

}
