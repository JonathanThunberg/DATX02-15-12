package datx021512.chalmers.se.greenme.fragments;

import android.app.Activity;
import android.app.Fragment;

import android.location.Location;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;

    private MapView mapView;
    private GoogleMap map;

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

        return rootView;


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    /**
     * This method will return a straight line between the locations
     * @return distance in kilometers
     */
    public double getDistanceInKiloMeters(){
        Location loc1 = new Location("");
        loc1.setLatitude(startLatitude);
        loc1.setLongitude(startLongitude);

        Location loc2 = new Location("");
        loc2.setLatitude(endLatitude);
        loc2.setLongitude(endLongitude);

        float distanceInMeters = loc1.distanceTo(loc2);
        float distanceInKiloMeters = distanceInMeters * 1000;
        return distanceInKiloMeters;
    }

}
