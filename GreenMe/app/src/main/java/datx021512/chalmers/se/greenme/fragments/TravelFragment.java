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
import android.widget.Button;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.location.LocationServices;


import java.text.SimpleDateFormat;
import java.util.Date;

import datx021512.chalmers.se.greenme.MainActivity;
import datx021512.chalmers.se.greenme.R;
import datx021512.chalmers.se.greenme.database.DatabaseHelper;

public class TravelFragment extends Fragment implements OnMapReadyCallback{
    private static final String TAG = "TravelFragment";

    private double totalused;
    private MapView mapView;
    private GoogleMap map;
    private boolean isButtonPressed = true;
    private  Button mapButton;
    private TextView text_usedtot;
    private TextView text_distancetot;
    private MainActivity mainActivity;
    private LocationManager locationManager = null;
    private Location previousLocation = null;
    private double totalDistance;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 25; // 25 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 20 * 1; // 20 sec
    private GoogleApiClient mGoogleApiClient;
    private PolylineOptions polylineOpti;
    private Polyline poly;
    private DatabaseHelper db;
    private double vehicle;

        @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {



        //todo change view
        View rootView = inflater.inflate(R.layout.fragment_travel, container, false);

        Bundle args = getArguments();
        if (args  != null && args.containsKey("Vehicle")){ //TODO Match with VehicleFragment passed
           this.vehicle = args.getDouble("Vehicle");
        }

       /* int statusCode = com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity());
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
            */

        db = new DatabaseHelper(rootView.getContext());

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

        text_distancetot =(TextView)rootView.findViewById(R.id.text_distancetot);
        text_distancetot.setVisibility(View.INVISIBLE);

        text_usedtot =(TextView)rootView.findViewById(R.id.text_usedtot);
        text_usedtot.setVisibility(View.INVISIBLE);



        mapButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // Enable or disable gps
                if (isButtonPressed) {
                    isButtonPressed = false;
                    map.clear();
                    /*        poly.remove(); Kanske använda
                         map.clear();*/
                    totalDistance = 0;
                    totalused = 0;
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

        text_usedtot.setVisibility(View.VISIBLE);
        text_distancetot.setVisibility(View.VISIBLE);

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

        polylineOpti = new PolylineOptions()
                .add(myPosition)
                .color(Color.RED);

        poly = map.addPolyline(polylineOpti);
    }

    public void stopTracking(){
        locationManager.removeUpdates(locationListener);

        LatLng prevLatLng = new LatLng(previousLocation.getLatitude(),previousLocation.getLongitude());

        map.addMarker(new MarkerOptions().position(prevLatLng).title("Mål"));

        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        db.saveTravelUsed(totalused,date.format(new Date()) );

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
            Log.d(TAG, "Location är changed" + newLocation);
         //   Toast.makeText(getActivity(),"Location är changed: " + newLocation, Toast.LENGTH_LONG).show();

            float [] result = new float[3];

            // Getting latitude of the current location
            double newLatitude = newLocation.getLatitude();
            // Getting longitude of the current location
            double newLongitude = newLocation.getLongitude();

            LatLng newLatLng = new LatLng(newLatitude,newLongitude);

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(newLatLng,17);
            map.animateCamera(cameraUpdate);

            Location.distanceBetween(previousLocation.getLatitude(), previousLocation.getLongitude(),
                    newLatitude, newLongitude, result);

            totalDistance += result[0];

            polylineOpti.add(newLatLng);
            poly.remove();
            poly = map.addPolyline(polylineOpti);


            if (totalDistance > 1000) {
                double reworkedDistanceKm = Math.round(totalDistance/100)/10;
                text_distancetot.setText("Sträcka: " + reworkedDistanceKm + " Km");
            }
            else {
                long reworkedDistanceM = Math.round(totalDistance);
                text_distancetot.setText("Sträcka: " + reworkedDistanceM + " m");
            }

            totalused = totalDistance*vehicle;

            text_usedtot.setText(totalused + " Kg CO2");
            Log.d(TAG, "totaldistance: " + totalDistance);

            previousLocation = newLocation;
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };
}
