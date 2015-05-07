package datx021512.chalmers.se.greenme.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import datx021512.chalmers.se.greenme.MainActivity;
import datx021512.chalmers.se.greenme.R;
import datx021512.chalmers.se.greenme.database.DatabaseHelper;

/**
 * Created by Fredrik on 2015-03-23.
 */
public class Home extends Fragment implements View.OnClickListener{

    public String TAG = "Home";
    private DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "HomeFragment");
        getActivity().setTitle("Startskärm");

        View rootView = inflater.inflate(R.layout.fragment_items, container, false);
        db = new DatabaseHelper(rootView.getContext());


        TextView text_total_home=(TextView)rootView.findViewById(R.id.text_total_home);
        ImageButton mShoppingButton = (ImageButton) rootView.findViewById(R.id.button_grocery_bag);
        ImageButton mTransportButton = (ImageButton) rootView.findViewById(R.id.button_transport_icon);
        mShoppingButton.setOnClickListener(this);
        mTransportButton.setOnClickListener(this);

        int i2 = db.getTotalImpact();
        text_total_home.setText(i2 + " Kg C02"); //TODO Leta metod!
        return rootView;
    }

    @Override
    public void onClick(View v) {

        FragmentManager fragmentManager = getActivity().getFragmentManager();;
        switch (v.getId()){
            case R.id.button_grocery_bag:
                fragmentManager.beginTransaction().replace(R.id.container, (new ShoppingListsFragment())).commit();
                break;
            case R.id.button_transport_icon:
                fragmentManager.beginTransaction().replace(R.id.container, (new TravelFragment())).commit();
                break;

        }

    }
}