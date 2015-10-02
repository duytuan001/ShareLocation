package com.example.tuanpd.myapplication;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMapLoadedCallback {

    private Location mLastLocation;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FASTEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private GoogleMap mGoogleMap;
    private HashMap<String, Marker> mMarkersMap;

    private final float[] mColors = {BitmapDescriptorFactory.HUE_AZURE, BitmapDescriptorFactory.HUE_BLUE, BitmapDescriptorFactory.HUE_CYAN,
            BitmapDescriptorFactory.HUE_GREEN, BitmapDescriptorFactory.HUE_MAGENTA, BitmapDescriptorFactory.HUE_ORANGE,
            BitmapDescriptorFactory.HUE_ROSE, BitmapDescriptorFactory.HUE_VIOLET, BitmapDescriptorFactory.HUE_YELLOW};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (!MPreference.isGroupConnected(this)) {
//            openSetting();
//            finish();
//        }
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleMap = mapFragment.getMap();

        mGoogleMap.setOnMapLoadedCallback(this);

        mMarkersMap = new HashMap<String, Marker>();

        if (AppUtils.checkPlayServices(this)) {
            buildGoogleApiClient();
//            createLocationRequest(UPDATE_INTERVAL, FASTEST_INTERVAL, DISPLACEMENT);
        }

        findViewById(R.id.map_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.map_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCameraCoverAllMarker();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppUtils.checkPlayServices(this);
//        if (mGoogleApiClient.isConnected()) {
//            startLocationUpdates();
//        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        setupMap();
        showAllUsersOnMap();
    }

    @Override
    public void onMapLoaded() {
        setCameraCoverAllMarker();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = getLastLocation();
//        Marker m = createMarker(mLastLocation.getLatitude(), mLastLocation.getLongitude(), "I'm Here", BitmapDescriptorFactory.HUE_RED);
//        mMarkersMap.put("6", m);
    }

    @Override
    public void onConnectionSuspended(int result) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    //=+++++++++++++++++++++++++++++++++++++LOCATION++++++++++++++++++++++++++++++++++++++++++++++//

    private Location getLastLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    protected void createLocationRequest(int interval, int fastest_interval, int displacement) {
        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
        mLocationRequest.setInterval(interval);
        mLocationRequest.setFastestInterval(fastest_interval);
        mLocationRequest.setSmallestDisplacement(displacement);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    //=++++++++++++++++++++++++++++++++++++++++MAP++++++++++++++++++++++++++++++++++++++++++++++++//
    protected void setupMap() {
        //Set the map type
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //To enable the zoom controls (+/- buttons)
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        //To enable gestures
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);

        //To enable compass
        mGoogleMap.getUiSettings().setCompassEnabled(true);

        //Set to have a my location button which on clicked moves to your current location
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        //To enable rotation in your map
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
    }

    protected void setCameraAtLocation(double latitude, double longitude, int zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(zoom).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    protected Marker createMarker(double latitude, double longitude, String title, float color) {
        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(latitude, longitude)).title(title);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(color));
        // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker_icon)));
        Marker marker = mGoogleMap.addMarker(markerOptions);
        return marker;
    }

    protected void setCameraCoverAllMarker() {
        if (mMarkersMap == null || mMarkersMap.isEmpty()) {
            return;
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : mMarkersMap.values()) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 150; // offset from edges of the map in pixels
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mGoogleMap.animateCamera(cameraUpdate);
    }

    protected void showAllUsersOnMap() {
        List<LocationUser> list = ((MyApplication) getApplication()).getLocationGroup().getUserList();
        if (mMarkersMap == null) {
            mMarkersMap = new HashMap<String, Marker>();
        }
        LocationUser user;
        Marker marker;
        for (int i = 0; i < list.size(); i++) {
            user = list.get(i);
            marker = mMarkersMap.get(user.getPhoneNumber());
            if (marker == null) {
                mMarkersMap.put(user.getPhoneNumber(), createMarker(user.getLatitude(), user.getLongitude(), user.getUsername(), mColors[i]));
            } else {
                marker.setPosition(new LatLng(user.getLatitude(), user.getLongitude()));
            }
        }
    }

    private void openSetting() {
        Intent intent = new Intent(MapsActivity.this, SettingActivity.class);
        startActivity(intent);
    }

}
