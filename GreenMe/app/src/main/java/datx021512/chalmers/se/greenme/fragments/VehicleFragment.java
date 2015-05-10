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

    private VehicleType vehicleType;



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
            carTypeInvisible();
        }else{
            getActivity().setTitle("Välj fordonstyp");

            addButton.setVisibility(View.INVISIBLE);
            kmTextView.setVisibility(View.INVISIBLE);
            kilometerInput.setVisibility(View.INVISIBLE);
            for(ImageButton i: buttonList){
                i.setAlpha(1f);
            }
            carTypeInvisible();

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
                        vehicleType = VehicleType.BUS;
                    }
                    carTypeInvisible();

                    break;

                case R.id.trainButton:
                    if(!inputSwitch.isChecked()) {
                        getActivity().setTitle("Tåg");
                        nextFragment(trainCarbon);

                    }else{
                        vehicleTransparent(trainButton);
                        vehicleType = VehicleType.TRAIN;
                    }
                    carTypeInvisible();
                    break;

                case R.id.carButton:
                    carTypeVisible();
                    vehicleTransparent(carButton);
                    carNoTransparent();
                    vehicleType = null;
                    break;

                case R.id.smallestCar:
                    if(!inputSwitch.isChecked()) {
                        nextFragment(smallestCarCarbon);
                    }else{
                        vehicleType = VehicleType.SMALLESTCAR;
                        carTransparent(smallestCarButton);
                    }
                    break;
                case R.id.smallerCar:
                    if(!inputSwitch.isChecked()) {
                        nextFragment(smallerCarCarbon);
                    }else{
                        vehicleType = VehicleType.SMALLERCAR;
                        carTransparent(smallerCarButton);
                    }
                    break;
                case R.id.biggerCar:
                    if(!inputSwitch.isChecked()) {
                        nextFragment(biggerCarCarbon);
                    }else{
                        vehicleType = VehicleType.BIGGERCAR;
                        carTransparent(biggerCarButton);
                    }
                    break;
                case R.id.biggestCar:
                    if(!inputSwitch.isChecked()) {
                        nextFragment(biggestCarCarbon);
                    }else{
                        vehicleType = VehicleType.BIGGESTCAR;
                        carTransparent(biggestCarButton);
                    }
                    break;

                case R.id.bikingButton:
                    if(!inputSwitch.isChecked()) {
                        nextFragment(bikingCarbon);
                        getActivity().setTitle("Cykel");


                    }else{
                        vehicleTransparent(bikingButton);
                        vehicleType = VehicleType.BIKE;
                    }
                    carTypeInvisible();
                    break;

                case R.id.walkingButton:
                    if(!inputSwitch.isChecked()) {
                        nextFragment(walkingCarbon);
                        getActivity().setTitle("Gång");
                    }else{
                        vehicleTransparent(walkingButton);
                        vehicleType = VehicleType.WALK;
                    }
                    carTypeInvisible();
                    break;
                case R.id.addButton:
                    if(vehicleType == null){
                        Context context = getActivity().getApplicationContext();
                        CharSequence text = "Du måste välja fordonstyp";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }else if(kilometerInput.getText().toString().trim().length()==0){
                        Context context = getActivity().getApplicationContext();
                        CharSequence text = "Du måste skriva in hur långt du åkt";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }else{
                        double temp=0;
                        switch (vehicleType){
                            case WALK:
                                temp = walkingCarbon;
                                break;
                            case TRAIN:
                                temp = trainCarbon;
                                break;
                            case BUS:
                                temp = busCarbon;
                                break;
                            case BIKE:
                                temp = bikingCarbon;
                                break;
                            case BIGGERCAR:
                                temp = biggerCarCarbon;
                                break;
                            case BIGGESTCAR:
                                temp = biggestCarCarbon;
                                break;
                            case SMALLERCAR:
                                temp = smallerCarCarbon;
                                break;
                            case SMALLESTCAR:
                                temp = smallestCarCarbon;
                                break;

                        }
                        double totalimpact = temp * Double.parseDouble(kilometerInput.getText().toString());


                        Context context = getActivity().getApplicationContext();
                        CharSequence text = "Denna resan har blivit tillagd och det blev "+totalimpact+" kg CO2" ;
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }

                break;
            }

        }
    };

    public void carTypeInvisible(){
        for(Button i:carTypeButtonList){
                i.setVisibility(View.INVISIBLE);

        }
    }
    public void carTypeVisible() {
      for (Button i : carTypeButtonList) {
                i.setVisibility(View.VISIBLE);
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


    public void carTransparent(Button button){
        for(Button i:carTypeButtonList){
            if(button!=i) {
                i.setAlpha(0.5f);
            }
            if(button==i){
                i.setAlpha(1f);
            }
      }
    }

    public void carNoTransparent(){
        for(Button i:carTypeButtonList){
              i.setAlpha(1f);
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
    private enum VehicleType{
        TRAIN, BUS, BIKE, WALK, BIGGESTCAR,BIGGERCAR, SMALLESTCAR, SMALLERCAR

    }
}
