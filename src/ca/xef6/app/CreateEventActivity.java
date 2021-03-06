package ca.xef6.app;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import ca.xef6.app.db.ContentProvider;
import ca.xef6.app.db.EventsTable;
import ca.xef6.app.ui.FragmentActivity;
import ca.xef6.app.ui.MapFragment;
import ca.xef6.app.util.DatePickerFragment;
import ca.xef6.app.util.TimePickerFragment;
import ca.xef6.widget.AddressSuggestionDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("NewApi")
public class CreateEventActivity extends FragmentActivity implements ConnectionCallbacks, LocationListener, OnConnectionFailedListener,
        OnMyLocationButtonClickListener, OnMapClickListener, OnMapLongClickListener {

    private static final class Data {
        public int    type;
        public String imageUrl;
        public double latitude;
        public double longitude;
        public String selectedAddress;
        public int    zoomLevel;
    }

    private static final class Views {
        public ImageButton image;
        public EditText    name;
        public EditText    description;
        public Button      date;
        public Button      time;
        public TextView    location;
        public MapFragment mapFragment;
        public SeekBar     zoomLevel;
        public TextView    zoomLevelValue;
    }

    private LatLng           currentLocation;
    private LocationClient   locationClient;
    private GoogleMap        map;

    boolean                  dateSelected;
    boolean                  timeSelected;

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
        views.location = (TextView) findViewById(R.id.loc_address);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        views.mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map_fragment);

        views.zoomLevel = (SeekBar) findViewById(R.id.zoom_level);
        views.zoomLevelValue = (TextView) findViewById(R.id.zoom_level_value);
        views.zoomLevel.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                data.zoomLevel = progress + 2;
                views.zoomLevelValue.setText(String.valueOf(data.zoomLevel));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });
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
                    views.image.setImageResource(0);
                    views.image.setScaleType(ScaleType.CENTER_CROP);
                    views.image.setBackground(null);
                }
            }
        }
        /*if (requestCode == RESULT_LOCATION_SELECTED) {
        	if (resultCode == RESULT_OK && null != intent) {
        		LatLng latLng = (LatLng) intent.getParcelableExtra("selectedLocation");
        		data.latitude = latLng.latitude;
        		data.longitude = latLng.longitude;
        		views.location.setText("Location: " + latLng.toString());
        	}
        }*/
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
                saveState();
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
        disconnect();
        //saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeLocationClient();
        initializeMap();
        connect();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //saveState();
        outState.putParcelable(ContentProvider.CONTENT_ITEM_TYPE, eventUri);
    }

    private void showAddressSuggestionDialog(LatLng position) {
        AddressSuggestionDialog asd = new AddressSuggestionDialog();
        asd.setCallback(new AddressSuggestionDialog.Callback() {
            @Override
            public void onAddressSelected(String selectedAddress) {
                data.selectedAddress = selectedAddress;
                if (views.location != null && selectedAddress != null) {
                    views.location.setText(selectedAddress);
                }
            }
        });
        asd.setPosition(position);
        asd.show(getSupportFragmentManager(), null);
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
        values.put(EventsTable.COL_LOC_ADDRESS, data.selectedAddress);
        values.put(EventsTable.COL_ZOOM_LEVEL, data.zoomLevel);
        if (eventUri == null) {
            eventUri = getContentResolver().insert(ContentProvider.CONTENT_URI, values);
        } else {
            Log.w("CreateEventActivity", eventUri.toString());
            getContentResolver().update(eventUri, values, null, null);
        }
        setResult(RESULT_OK);
        finish();
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

    //public void showLocationPicker(View view) {
    //    startActivityForResult(new Intent(this, LocationPickerActivity.class), RESULT_LOCATION_SELECTED);
    //}

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

    @Override
    public void onConnected(Bundle connectionHint) {
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(16);
        locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationClient.requestLocationUpdates(locationRequest, this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (currentLocation != null) {
            onMapClick(currentLocation);
        }
        return false;
    }

    private void connect() {
        if (locationClient != null) {
            locationClient.connect();
        }
    }

    private void disconnect() {
        if (locationClient != null) {
            locationClient.disconnect();
        }
    }

    private void initializeLocationClient() {
        if (locationClient == null) {
            locationClient = new LocationClient(this, this, this);
        }
    }

    private void initializeMap() {
        if (map == null && views.mapFragment != null) {
            map = views.mapFragment.getMap();
            if (map != null) {
                map.setMyLocationEnabled(true);
                map.setOnMapClickListener(this);
                map.setOnMapLongClickListener(this);
                map.setOnMyLocationButtonClickListener(this);
            }
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        LatLng selectedLocation = new LatLng(point.latitude, point.longitude);
        data.latitude = selectedLocation.latitude;
        data.longitude = selectedLocation.longitude;
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.title("Selected Location");
        if (map != null) {
            map.clear();
            map.addMarker(markerOptions);
        }
    }

    @Override
    public void onMapLongClick(LatLng point) {
        onMapClick(point);
        showAddressSuggestionDialog(new LatLng(data.latitude, data.longitude));
    }

}
