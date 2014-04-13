package ca.xef6.app;

import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import ca.xef6.app.db.ContentProvider;
import ca.xef6.app.db.EventsTable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment
        extends
        ca.xef6.app.ui.MapFragment
        implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LoaderCallbacks<Cursor>,
        LocationListener,
        OnMyLocationButtonClickListener,
        OnCameraChangeListener {

    private static final LocationRequest LOCATION_REQUEST = LocationRequest.create()
                                                                  .setInterval(5000)
                                                                  .setFastestInterval(16)
                                                                  .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    private float                        zoomLevel;

    private LocationClient               locationClient;
    private GoogleMap                    map;

    private void initialize() {
        initializeLocationClient();
        initializeMap();
        locationClient.connect();
    }

    private void initializeLocationClient() {
        if (locationClient == null) {
            locationClient = new LocationClient(getActivity(), this, this);
        }
    }

    private void initializeMap() {
        if (map == null) {
            map = getMap();
            if (map != null) {
                map.setMyLocationEnabled(true);
                map.setOnMyLocationButtonClickListener(this);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        locationClient.requestLocationUpdates(LOCATION_REQUEST, this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ContentProvider.CONTENT_URI, EventsTable.ALL_COLUMNS, null, null, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.w("MapFragment", "adding markers... current zoom level = " + zoomLevel);
        if (data.moveToFirst()) {
            do {
                final float latitude = data.getFloat(data.getColumnIndexOrThrow(EventsTable.COL_LAT));
                final float longitude = data.getFloat(data.getColumnIndexOrThrow(EventsTable.COL_LNG));
                final String title = data.getString(data.getColumnIndexOrThrow(EventsTable.COL_NAME));
                final int markerZoomLevel = data.getInt(data.getColumnIndexOrThrow(EventsTable.COL_ZOOM_LEVEL));
                if (map != null && markerZoomLevel > zoomLevel) {
                    map.addMarker(new MarkerOptions().title(title).position(new LatLng(latitude, longitude)));
                }
            } while (data.moveToNext());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (locationClient != null && locationClient.isConnected()) {
            String msg = "Location = " + locationClient.getLastLocation();
            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationClient != null) {
            locationClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    @Override
    public void onCameraChange(CameraPosition position) {
        zoomLevel = position.zoom;
    }

}
