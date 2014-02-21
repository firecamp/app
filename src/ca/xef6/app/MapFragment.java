package ca.xef6.app;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Session;
import com.facebook.SessionState;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends ca.xef6.app.ui.MapFragment
        implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener, OnMyLocationButtonClickListener {

    private LocationClient locationClient;
    private GoogleMap      map;

    @Override
    public void onConnected(Bundle connectionHint) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        GoogleMap map = getMap();
        if (map != null) {
            map.addMarker(new MarkerOptions().position(new LatLng(45.5, 73.6)).title("My Title"));
        }
        return view;
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onSessionStateChange(Session session, SessionState state, Exception exception) {
    }

}
