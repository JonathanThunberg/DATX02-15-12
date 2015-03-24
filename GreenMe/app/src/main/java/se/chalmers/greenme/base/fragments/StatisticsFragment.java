package se.chalmers.greenme.base.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.chalmers.greenme.base.R;

/**
 * Created by Fredrik on 2015-03-23.
 */
public class StatisticsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_items, container, false);

        TextView txt=(TextView)rootView.findViewById(R.id.txt);
        txt.setText("Statestik");
        return rootView;
    }
}