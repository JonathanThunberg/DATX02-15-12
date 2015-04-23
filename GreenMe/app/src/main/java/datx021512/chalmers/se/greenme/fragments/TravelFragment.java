package datx021512.chalmers.se.greenme.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


import datx021512.chalmers.se.greenme.R;

public class TravelFragment extends Fragment implements OnMapReadyCallback{

    private static final String TAG = "test";

    private MapView mapView;
    private GoogleMap map;
    private boolean isButtonPressed = false;
    private  Button mapButton;
    private TextView textView;

    private LocationManager locationManager = null;
    private Location previousLocation = null;
    private double totalDistance;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 250; // 250 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minutes

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        //todo change view
        View rootView = inflater.inflate(R.layout.fragment_travel, container, false);

        int statusCode = com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity());
        switch (statusCode) {
            case ConnectionResult.SUCCESS:
                Toast.makeText(this.getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
                break;
            case ConnectionResult.SERVICE_MISSING:
                Toast.makeText(this.getActivity(), "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Toast.makeText(this.getActivity(), "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                break;
            default: Toast.makeText(this.getActivity(), "Play Service result " + statusCode, Toast.LENGTH_SHORT).show();

        }

        mapView= (MapView)rootView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        map= mapView.getMap();
        map.getUiSettings().setZoomControlsEnabled(true);

        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        MapsInitializer.initialize(getActivity());
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(57.708870,11.974560),10);
        map.animateCamera(cameraUpdate);

        mapButton=(Button)rootView.findViewById(R.id.mapbutton);
        mapButton.setText("START");

        textView=(TextView)rootView.findViewById(R.id.textView);

        mapButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // Enable or disable gps
                if (!isButtonPressed) {
                    startTracking();
                }else{
                    stopTracking();
                    mapButton.setText("START");
                    textView.setText("totdist" + totalDistance);
                }
                isButtonPressed = !isButtonPressed;
            }
        });
        return rootView;
    }

    public void startTracking() {
        mapButton.setText("STOP");

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Add new listeners with the given parameters (GPS or NETWORK)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener); // Network location
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener); // Gps location
    }

    public void stopTracking(){
        locationManager.removeUpdates(locationListener);

    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location newLocation)
        {
            if (previousLocation != null)
            {
                double latitude = newLocation.getLatitude() + previousLocation.getLatitude();
                latitude *= latitude;
                double longitude = newLocation.getLongitude() + previousLocation.getLongitude();
                longitude *= longitude;
                double altitude = newLocation.getAltitude() + previousLocation.getAltitude();
                altitude *= altitude;
                totalDistance += Math.sqrt(latitude + longitude + altitude);
            }

            previousLocation = newLocation;

            Log.d(TAG, "totaldistance: "+ totalDistance);

        }


        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

}
