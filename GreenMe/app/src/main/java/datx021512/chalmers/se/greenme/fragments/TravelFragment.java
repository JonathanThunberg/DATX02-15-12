package datx021512.chalmers.se.greenme.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import datx021512.chalmers.se.greenme.R;

public class TravelFragment extends Fragment implements LocationListener {

    private static final String TAG = "test";
    private TextView latitudeField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;
    private double latitude;
    private double longitude;


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        //todo change view
        View rootView = inflater.inflate(R.layout.fragment_travel, container, false);
        latitudeField = (TextView) rootView.findViewById(R.id.TextView02);
        longitudeField = (TextView) rootView.findViewById(R.id.TextView04);

        // provides access to the system location services
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //criteria for how to select a location provider
        Criteria criteria = new Criteria();
        // a provider provides periodic reports on the geographical location of the device.

        provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider); //does not have any lastknownlocation since the app is closed down
        // Initialize the location fields

        if(location!=null){
            Log.d(TAG, "location!=null: "+ location);
        }
        else if (location == null) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    400, 1, this);
            Log.d(TAG, "location==null");

            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.d(TAG, "locationmanager är inte null och location är: " + location);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.d(TAG, "latitude: " + latitude);
                    Log.d(TAG, "longitude: " + longitude);
                    latitudeField.setText(String.valueOf(latitude));
                    longitudeField.setText(String.valueOf(longitude));
                }
            }
        }

        return rootView;


    }

    @Override
    public void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);

    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);

    }

    @Override
    public void onLocationChanged(Location location) {
         /*if (location != null) {
            onLocationChanged(location);
        } else {
            latitudeField.setText("Location not available");
            longitudeField.setText("Location not available");
        }*/
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
