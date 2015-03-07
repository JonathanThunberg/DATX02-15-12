package datx021512.chalmers.se.greenme;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import datx021512.chalmers.se.greenme.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class GroceryFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MENU_POSITION = "position";


    // TODO: Rename and change types of parameters
    private int mParam1;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static GroceryFragment newInstance(int position) {
        //Log.d("abc","begening of new instance");
        GroceryFragment fragment = new GroceryFragment();
        Bundle args = new Bundle();
        args.putInt(MENU_POSITION, position);
        fragment.setArguments(args);
        //Log.d("abc","end of new instance");
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroceryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getInt(MENU_POSITION);
        }
        // Set title of fragment page
        getActivity().getActionBar().setTitle(R.string.title_section2);
        // TODO: Change Adapter to display your content
        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),R.layout.fragment_shopping, android.R.id.empty, DummyContent.ITEMS));
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            ((MainActivity) activity).onSectionAttached(getArguments().getInt(MENU_POSITION));
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
