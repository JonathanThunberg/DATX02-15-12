package datx021512.chalmers.se.greenme.fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import datx021512.chalmers.se.greenme.R;
import datx021512.chalmers.se.greenme.database.DatabaseHelper;

public class VehicleFragment extends Fragment {

    ImageButton walkingButton;
    ImageButton bikingButton;
    ImageButton busButton;
    ImageButton carButton;
    ImageButton trainButton;
    private DatabaseHelper db;
    TextView manualTextView;
    TextView automaticTextView;
    Switch inputSwitch;
    Button addButton;
    TextView kmTextView;
    EditText kilometerInput;
    private static final String TAG="debug";
    boolean buttonClicked= false;
    private static final int SHORT_DELAY = 2000;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Fordonstyp");
        View rootView = inflater.inflate(R.layout.fragment_vehicle, container, false);

        busButton = (ImageButton) rootView.findViewById(R.id.busButton);
        busButton.setOnClickListener(myButtonListener);
        trainButton = (ImageButton) rootView.findViewById(R.id.trainButton);
        trainButton.setOnClickListener(myButtonListener);
        carButton = (ImageButton) rootView.findViewById(R.id.carButton);
        carButton.setOnClickListener(myButtonListener);
        bikingButton = (ImageButton) rootView.findViewById(R.id.bikingButton);
        bikingButton.setOnClickListener(myButtonListener);
        walkingButton = (ImageButton) rootView.findViewById(R.id.walkingButton);
        walkingButton.setOnClickListener(myButtonListener);

        manualTextView=(TextView) rootView.findViewById(R.id.manualTextView);
        automaticTextView=(TextView) rootView.findViewById(R.id.automaticTextView);
        inputSwitch=(Switch) rootView.findViewById(R.id.inputSwitch);
        addButton=(Button) rootView.findViewById(R.id.addButton);
        addButton.setVisibility(View.INVISIBLE);
        kmTextView=(TextView) rootView.findViewById(R.id.kmTextView);
        kmTextView.setVisibility(View.INVISIBLE);
        kilometerInput=(EditText) rootView.findViewById(R.id.kilometerInput);
        kilometerInput.setVisibility(View.INVISIBLE);


        inputSwitch.setOnCheckedChangeListener(switchListener);
        return rootView;
    }

    private CompoundButton.OnCheckedChangeListener switchListener= new CompoundButton.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                addButton.setVisibility(View.VISIBLE);
                kmTextView.setVisibility(View.VISIBLE);
                kilometerInput.setVisibility(View.VISIBLE);
            }else{
                addButton.setVisibility(View.INVISIBLE);
                kmTextView.setVisibility(View.INVISIBLE);
                kilometerInput.setVisibility(View.INVISIBLE);
            }
        }
    };

    private View.OnClickListener myButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            TravelFragment newFragment = new TravelFragment();

            switch (v.getId()) {

                case R.id.busButton:
                    if(!inputSwitch.isChecked()) {
                        fragmentTransaction.replace(R.id.container, newFragment);
                        fragmentTransaction.commit();
                        getActivity().setTitle("Buss");

                    }else{
                        trainButton.setAlpha(0.5f);
                        carButton.setAlpha(0.5f);
                        bikingButton.setAlpha(0.5f);
                        walkingButton.setAlpha(0.5f);
                        buttonClicked=true;


                        /*image_button.setAlpha(0f) // to make it full transparent
                        image_button.setAlpha(0.5f) // to make it half transparent
                        image_button.setAlpha(0.6f) // to make it (40%) transparent
                        image_button.setAlpha(1f) // to make it opaque*/
                    }
                    break;

                case R.id.trainButton:
                    if(!inputSwitch.isChecked()) {
                        fragmentTransaction.replace(R.id.container, newFragment);
                        fragmentTransaction.commit();
                        getActivity().setTitle("Tåg");
                    }else{
                        busButton.setAlpha(0.5f);
                        carButton.setAlpha(0.5f);
                        bikingButton.setAlpha(0.5f);
                        walkingButton.setAlpha(0.5f);
                        buttonClicked=true;
                    }
                    break;

                case R.id.carButton:
                    if(!inputSwitch.isChecked()) {
                        fragmentTransaction.replace(R.id.container, newFragment);
                        fragmentTransaction.commit();
                    }else{
                        busButton.setAlpha(0.5f);
                        trainButton.setAlpha(0.5f);
                        bikingButton.setAlpha(0.5f);
                        walkingButton.setAlpha(0.5f);
                        buttonClicked=true;
                    }
                    break;

                case R.id.bikingButton:
                    if(!inputSwitch.isChecked()) {
                        fragmentTransaction.replace(R.id.container, newFragment);
                        fragmentTransaction.commit();
                        getActivity().setTitle("Cykel");
                    }else{
                        busButton.setAlpha(0.5f);
                        carButton.setAlpha(0.5f);
                        trainButton.setAlpha(0.5f);
                        walkingButton.setAlpha(0.5f);
                        buttonClicked=true;
                    }
                    break;

                case R.id.walkingButton:
                    if(!inputSwitch.isChecked()) {
                        fragmentTransaction.replace(R.id.container, newFragment);
                        fragmentTransaction.commit();
                        getActivity().setTitle("Gång");
                    }else{
                        busButton.setAlpha(0.5f);
                        carButton.setAlpha(0.5f);
                        bikingButton.setAlpha(0.5f);
                        trainButton.setAlpha(0.5f);
                        buttonClicked=true;
                    }
                    break;
                case R.id.addButton:
                    if(inputSwitch.isChecked() && !buttonClicked){
                        Context context = getActivity().getApplicationContext();
                        CharSequence text = "Du måste välja fordonstyp";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }

                default:
                    break;
            }

        }
    };

    /*private void create(String s, String format){
        FragmentManager fragmentManager = getFragmentManager();
        final Bundle bundle = new Bundle();
        //bundle.putString("Shopping_Name", s);
        TravelFragment tf= new TravelFragment();
        tf.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.container,tf).commit();
        db.create(s,format);
    }*/
}
