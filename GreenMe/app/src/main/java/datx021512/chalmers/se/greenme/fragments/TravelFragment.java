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

       // criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        boolean gps = locationManager.isProviderEnabled("gps");
        boolean passive = locationManager.isProviderEnabled("passive");
        boolean network = locationManager.isProviderEnabled("network");

        // provider =locationManager.getBestProvider(criteria, false); //provider == null :(
        Log.d(TAG, "passive: "+ passive + "gps: " + gps + "network: " + network);

        Location location = locationManager.getLastKnownLocation(provider); //does not have any lastknownlocation since the app is closed down
        // Initialize the location fields



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
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        latitudeField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));

        if (location != null) {
            onLocationChanged(location);
        } else {
            latitudeField.setText("Location not available");
            longitudeField.setText("Location not available");
        }
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
