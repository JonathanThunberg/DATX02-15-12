package datx021512.chalmers.se.greenme.fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import datx021512.chalmers.se.greenme.R;

public class VehicleFragment extends Fragment {

    ImageButton walkingButton;
    ImageButton bikingButton;
    ImageButton busButton;
    ImageButton carButton;
    ImageButton trainButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_vehicle, container, false);

        busButton = (ImageButton) rootView.findViewById(R.id.busButton);
        busButton.setOnClickListener(myButtonListener);
        trainButton = (ImageButton) rootView.findViewById(R.id.trainButton);
        trainButton.setOnClickListener(myButtonListener);
        /*carButton = (ImageButton) rootView.findViewById(R.id.carButton);
        carButton.setOnClickListener(myButtonListener);
        bikingButton = (ImageButton) rootView.findViewById(R.id.bikingButton);
        bikingButton.setOnClickListener(myButtonListener);
        walkingButton = (ImageButton) rootView.findViewById(R.id.walkingButton);
        walkingButton.setOnClickListener(myButtonListener);*/




        return rootView;
    }
    private View.OnClickListener myButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.busButton:
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    TravelFragment newFragment = new TravelFragment();
                    fragmentTransaction.replace(R.id.container, newFragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.trainButton:
                    break;
                /*case R.id.carButton:
                    break;
                case R.id.bikingButton:
                    break;
                case R.id.walkingButton:
                    break;*/
                default:
                    break;
            }
        }
    };

}
