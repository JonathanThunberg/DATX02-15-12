package datx021512.chalmers.se.greenme.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import datx021512.chalmers.se.greenme.R;
import datx021512.chalmers.se.greenme.adapters.ShopItem;
import datx021512.chalmers.se.greenme.adapters.ShoppingListsAdapter;
import datx021512.chalmers.se.greenme.database.DatabaseHelper;


public class ShoppingListsFragment extends Fragment implements View.OnClickListener {
    private AutoCompleteTextView textView;
    private RecyclerView mRecycleView;
    private ShoppingListsAdapter mAdapter;
    private ImageButton mAddButton;
    private DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        db = new DatabaseHelper(rootView.getContext());
        mAdapter = new ShoppingListsAdapter(db.getShoppingLists(),this,(Button) rootView.findViewById(R.id.total_text_list));

        mAddButton = (ImageButton) rootView.findViewById(R.id.add_text);
        mAddButton.setOnClickListener(this);

        mRecycleView = (RecyclerView) rootView.findViewById(R.id.recyclerShoppingItems);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        mRecycleView.setLayoutManager(llm);
        mRecycleView.setAdapter(mAdapter);

        setHasOptionsMenu(true);

        getActivity().setTitle("Dina Inköpslistor");
        textView = (AutoCompleteTextView)
                rootView.findViewById(R.id.text_input);

        textView.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == KeyEvent.KEYCODE_ENTER)||actionId == EditorInfo.IME_ACTION_DONE) {
                    addNewList();
                    return true;
                }
                return false;
            }
        });
        Button totalText = (Button) rootView.findViewById(R.id.total_text_list);
        double total=0;
        for(ShopItem s :db.getShoppingLists()){
            total+=s.getmCO2();
        }
        totalText.setText("  "+Math.round(total*1000)/1000.0+"Kg Co2");
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_text:
                addNewList();
        }
    }

    private void addNewList() {
        if (textView.getText().toString().trim().length() != 0 && !mAdapter.contains(textView.getText().toString()) && !mAdapter.contains(textView.getText().toString().replaceAll("\\s",""))) {
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            createNewList(textView.getText().toString(), date.format(new Date()));

            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().getBaseContext().INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

    }

    private void createNewList(String s, String format) {
        FragmentManager fragmentManager = getFragmentManager();
        final Bundle bundle = new Bundle();
        bundle.putString("Shopping_Name", s);
        ShoppingFragment sh= new ShoppingFragment();
        sh.setArguments(bundle);
        db.createNewList(s,format);
        fragmentManager.beginTransaction().replace(R.id.container,sh).commit();

    }

    @Override
    public void onPause(){
        super.onPause();

    }

}
