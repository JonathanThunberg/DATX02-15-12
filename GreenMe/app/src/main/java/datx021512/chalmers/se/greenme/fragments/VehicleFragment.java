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

import java.util.ArrayList;
import java.util.List;

import datx021512.chalmers.se.greenme.R;
import datx021512.chalmers.se.greenme.database.DatabaseHelper;

public class VehicleFragment extends Fragment {

    ImageButton walkingButton;
    ImageButton bikingButton;
    ImageButton busButton;
    ImageButton carButton;
    ImageButton trainButton;
    Button smallestCarButton;
    Button smallerCarButton;
    Button biggerCarButton;
    Button biggestCarButton;
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
    boolean carTypeVisible=false;
    private ArrayList<ImageButton> buttonList= new ArrayList<ImageButton>();
    private ArrayList<Button> carTypeButtonList=new ArrayList<Button>();

    private double smallestCarCarbon = 158.12;
    private double smallerCarCarbon = 190.28;
    private double biggerCarCarbon = 230.48;
    private double biggestCarCarbon = 270.68;

    private double busCarbon = 60.68;

    private double trainCarbon = 0.01;

    private double walkingCarbon = 0;
    private double bikingCarbon = 0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Logga resa");
        View rootView = inflater.inflate(R.layout.fragment_vehicle, container, false);

        busButton = (ImageButton) rootView.findViewById(R.id.busButton);
        trainButton = (ImageButton) rootView.findViewById(R.id.trainButton);
        carButton = (ImageButton) rootView.findViewById(R.id.carButton);
        bikingButton = (ImageButton) rootView.findViewById(R.id.bikingButton);
        walkingButton = (ImageButton) rootView.findViewById(R.id.walkingButton);

        smallestCarButton=(Button) rootView.findViewById(R.id.smallestCar);
        smallerCarButton=(Button) rootView.findViewById(R.id.smallerCar);
        biggerCarButton=(Button) rootView.findViewById(R.id.biggerCar);
        biggestCarButton=(Button) rootView.findViewById(R.id.biggestCar);

        manualTextView=(TextView) rootView.findViewById(R.id.manualTextView);
        automaticTextView=(TextView) rootView.findViewById(R.id.automaticTextView);
        inputSwitch=(Switch) rootView.findViewById(R.id.inputSwitch);

        buttonList.add(busButton);
        buttonList.add(trainButton);
        buttonList.add(carButton);
        buttonList.add(bikingButton);
        buttonList.add(walkingButton);

        carTypeButtonList.add(smallerCarButton);
        carTypeButtonList.add(smallestCarButton);
        carTypeButtonList.add(biggerCarButton);
        carTypeButtonList.add(biggestCarButton);

        for(ImageButton i:buttonList){
            i.setOnClickListener(myButtonListener);
        }

        for(Button i:carTypeButtonList){
            i.setOnClickListener(myButtonListener);
            i.setVisibility(View.INVISIBLE);
        }

        addButton=(Button) rootView.findViewById(R.id.addButton);
        addButton.setOnClickListener(myButtonListener);
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
            if(inputSwitch.isChecked()) {
                getActivity().setTitle("Lägg till antal kilometer");

                addButton.setVisibility(View.VISIBLE);
                kmTextView.setVisibility(View.VISIBLE);
                kilometerInput.setVisibility(View.VISIBLE);

                for(ImageButton i: buttonList){
                    i.setAlpha(1f);
                }




            }else{
                getActivity().setTitle("Välj fordonstyp");

                addButton.setVisibility(View.INVISIBLE);
                kmTextView.setVisibility(View.INVISIBLE);
                kilometerInput.setVisibility(View.INVISIBLE);
                for(ImageButton i: buttonList){
                    i.setAlpha(1f);
                }
                for(Button i:carTypeButtonList){
                    i.setAlpha(0f);
                }

            }
        }
    };

    private View.OnClickListener myButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.busButton:
                    if(!inputSwitch.isChecked()) {
                        nextFragment(busCarbon);
                        getActivity().setTitle("Buss");
                    }else{
                        vehicleTransparent(busButton);
                        carTypeInvisible(busButton);
                    }
                    if(carTypeVisible){
                        carTypeInvisible(busButton);
                    }

                    break;

                case R.id.trainButton:
                    if(!inputSwitch.isChecked()) {

                        getActivity().setTitle("Tåg");
                        nextFragment(trainCarbon);

                    }else{
                        vehicleTransparent(trainButton);
                        carTypeInvisible(trainButton);
                    }
                    if(carTypeVisible){
                        carTypeInvisible(trainButton);
                    }
                    break;

                case R.id.carButton:
                    if(!inputSwitch.isChecked()) {
                        carTypeVisible(carButton);
                        switch (v.getId()) {
                            case R.id.smallestCar:
                                allButtonsTransparent(smallestCarButton);
                                nextFragment(smallestCarCarbon);
                                break;
                            case R.id.smallerCar:
                                allButtonsTransparent(smallerCarButton);
                                nextFragment(smallerCarCarbon);
                                break;
                            case R.id.biggerCar:
                                allButtonsTransparent(biggerCarButton);
                                nextFragment(biggerCarCarbon);
                                break;
                            case R.id.biggestCar:
                                allButtonsTransparent(biggestCarButton);
                                nextFragment(biggestCarCarbon);
                                break;
                            default:
                                break;
                        }
                    }else{

                        carTypeVisible(carButton);
                        vehicleTransparent(carButton);
                    }
                    break;

                case R.id.bikingButton:
                    if(!inputSwitch.isChecked()) {
                        nextFragment(bikingCarbon);
                        getActivity().setTitle("Cykel");


                    }else{
                        vehicleTransparent(bikingButton);
                        carTypeInvisible(bikingButton);

                    }
                    if(carTypeVisible){
                        carTypeInvisible(bikingButton);
                    }
                    break;

                case R.id.walkingButton:
                    if(!inputSwitch.isChecked()) {
                        nextFragment(walkingCarbon);
                        getActivity().setTitle("Gång");
                    }else{
                        vehicleTransparent(walkingButton);
                        carTypeInvisible(walkingButton);
                    }
                    if(carTypeVisible){
                        carTypeInvisible(walkingButton);
                    }
                    break;
                case R.id.addButton:
                    if(inputSwitch.isChecked() && !buttonClicked){
                        Context context = getActivity().getApplicationContext();
                        CharSequence text = "Du måste välja fordonstyp";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }else{
                        switch(v.getId()){
                            case R.id.biggestCar:
                                double totalImpact=Integer.parseInt(kilometerInput.getText().toString())* biggestCarCarbon;
                                Context context = getActivity().getApplicationContext();
                                CharSequence text = "totalt: " + totalImpact;
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                        }
                    }

                default:

                    break;
            }

        }
    };

    public void carTypeInvisible(ImageButton button){
        for(ImageButton i:buttonList){
            if(button!=i) {
                for (Button j : carTypeButtonList) {
                    j.setVisibility(View.INVISIBLE);
                }
                carTypeVisible = false;
            }

        }
        for(Button i:carTypeButtonList){
                i.setVisibility(View.INVISIBLE);

        }
    }
    public void carTypeVisible(ImageButton button) {
        if(button==carButton){
            for (Button i : carTypeButtonList) {
                i.setVisibility(View.VISIBLE);
            }
            carTypeVisible= true;
        }

    }
    public void vehicleTransparent(ImageButton button){
        for(ImageButton i:buttonList){
            if(button!=i) {
                i.setAlpha(0.5f);
            }
            if(button==i){
                i.setAlpha(1f);
            }

        }
    }
    public void vehicleNonTransparent(ImageButton button){
        for(ImageButton i:buttonList){
            if(button!=i) {
                i.setAlpha(1f);
            }
        }
    }
    public void vehicleFullTransparent(ImageButton button) {
        for(ImageButton i:buttonList){
            if(button!=i) {
                i.setAlpha(0f);
            }
        }
    }
    public void allButtonsTransparent(Button button){
        for(Button i:carTypeButtonList){
            if(button!=i){
                i.setAlpha(0f);
            }
            if(button==i){
                i.setAlpha(1f);
            }
        }
    }

    public void nextFragment(Double id){
        FragmentManager fragmentManager = getFragmentManager();
        final Bundle bundle = new Bundle();
        bundle.putDouble("Vehicle", id);
        TravelFragment tf = new TravelFragment();
        tf.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.container,tf).commit();

    }
}
