package datx021512.chalmers.se.greenme.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.location.LocationServices;


import datx021512.chalmers.se.greenme.MainActivity;
import datx021512.chalmers.se.greenme.R;

public class TravelFragment extends Fragment implements OnMapReadyCallback{
    private static final String TAG = "TravelFragment";

    private MapView mapView;
    private GoogleMap map;
    private boolean isButtonPressed = true;
    private  Button mapButton;
    private TextView textView;
    private MainActivity mainActivity;
    private LocationManager locationManager = null;
    private Location previousLocation = null;
    private double totalDistance;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 250; // 250 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minutes
    private GoogleApiClient mGoogleApiClient;


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

        mainActivity = (MainActivity)getActivity();
        mGoogleApiClient = mainActivity.getmGoogleApiClient();
        previousLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        mapView= (MapView)rootView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        map= mapView.getMap();
        map.getUiSettings().setZoomControlsEnabled(true);

        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        MapsInitializer.initialize(getActivity());
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);



        double myLatitude = previousLocation.getLatitude();
        double myLongitude = previousLocation.getLongitude();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(myLatitude,myLongitude),10);
        map.animateCamera(cameraUpdate);

        mapButton=(Button)rootView.findViewById(R.id.mapbutton);
        mapButton.setText("START");

        textView=(TextView)rootView.findViewById(R.id.textView);
        textView.setVisibility(View.INVISIBLE);

        mapButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // Enable or disable gps
                if (isButtonPressed) {
                    isButtonPressed = false;
                    map.clear();
                    totalDistance = 0;
                    mapButton.setText("STOP");
                    startTracking();

                }else{
                    isButtonPressed = true;
                    mapButton.setText("START");
                    stopTracking();
                }
            }
        });

        return rootView;
    }


    public void startTracking() {

        textView.setVisibility(View.VISIBLE);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Add new listeners with the given parameters (GPS or NETWORK)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER
                , MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener); // Network location
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
                , MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener); // Gps location

        previousLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        Log.d(TAG,previousLocation.toString());

        // Getting latitude of the current location
        double latitude = previousLocation.getLatitude();
        // Getting longitude of the current location
        double longitude = previousLocation.getLongitude();

        // Creating a LatLng object for the current location
        LatLng myPosition = new LatLng(latitude, longitude);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myPosition,17);
        map.animateCamera(cameraUpdate);


        map.addMarker(new MarkerOptions().position(myPosition).title("Start"));


    }

    public void stopTracking(){
        locationManager.removeUpdates(locationListener);
        map.clear();
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

        /*TODO Linjer
           Använda Polylines för att lägga till linje kanske? Risk att linjen går genom hus.
           Antingen så går det genom hus och visar rätt sträcka eller
           så visar den fel sträcka och ser bra ut eller så måste vi uppdatera oftare.
        */

        @Override
        public void onLocationChanged(Location newLocation)
        {
            Log.d(TAG, "Location är changed" + newLocation);

            float [] result = new float[3];
            String prefix;
            long reworkedDistance;

            // Getting latitude of the current location
            double newLatitude = newLocation.getLatitude();
            // Getting longitude of the current location
            double newLongitude = newLocation.getLongitude();

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(newLatitude,newLongitude),17);
            map.animateCamera(cameraUpdate);

            Location.distanceBetween(previousLocation.getLatitude(),previousLocation.getLongitude(),
                    newLatitude,newLongitude,result);

            totalDistance += result[0];

            previousLocation = newLocation;
            if (totalDistance > 1000) {
                reworkedDistance = Math.round(totalDistance);
                 reworkedDistance /= 1000;
                prefix = " Km";
            }
            else {
                reworkedDistance = Math.round(totalDistance);
                prefix = " m";
            }
            Log.d(TAG, "totaldistance: "+ totalDistance);
            textView.setText("Sträcka: " + reworkedDistance + prefix); //TODO Byta ut det till Co2? Roligare kanske? Lite annorlunda.

        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };
}
