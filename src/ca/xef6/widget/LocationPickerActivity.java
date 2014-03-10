package ca.xef6.widget;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import ca.xef6.app.R;
import ca.xef6.app.ui.FragmentActivity;
import ca.xef6.app.ui.MapFragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("NewApi")
public class LocationPickerActivity extends FragmentActivity
        implements ConnectionCallbacks, LocationListener, OnConnectionFailedListener, OnMyLocationButtonClickListener, OnMapClickListener {

    private static final class Views {
        public TextView    detailsTextView;
        public MapFragment mapFragment;
    }

    private LatLng         currentLocation;
    private LocationClient locationClient;
    private GoogleMap      map;
    private LatLng         selectedLocation;
    private final Views    views = new Views();

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

    private void initializeActionBar() {
        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(R.layout.location_picker_action_bar, null);

        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLocation != null) {
                    Intent intent = new Intent();
                    intent.putExtra("selectedLocation", selectedLocation);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView);
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
                map.setOnMyLocationButtonClickListener(this);
            }
        }
    }

    private void initializeViews() {
        setContentView(R.layout.location_picker);
        views.detailsTextView = (TextView) findViewById(R.id.details_text_view);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        views.mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map_fragment);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViews();
        initializeActionBar();
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

    @Override
    protected void onPause() {
        disconnect();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeLocationClient();
        initializeMap();
        connect();
    }

    @Override
    public void onMapClick(LatLng point) {
        selectedLocation = new LatLng(point.latitude, point.longitude);
        Log.w("onMapClick", selectedLocation.toString());
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.title("Selected Location");
        if (map != null) {
            map.clear();
            map.addMarker(markerOptions);
        }
        views.detailsTextView.setText(point.toString());
    }
}
