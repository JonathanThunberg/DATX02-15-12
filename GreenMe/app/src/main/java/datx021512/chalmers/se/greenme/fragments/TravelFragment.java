package datx021512.chalmers.se.greenme.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import datx021512.chalmers.se.greenme.R;

public class TravelFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_items, container, false);

        TextView txt=(TextView)rootView.findViewById(R.id.txt);
        txt.setText("Rese delen");
        return rootView;
    }
}
