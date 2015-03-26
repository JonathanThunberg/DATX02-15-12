package datx021512.chalmers.se.greenme.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import datx021512.chalmers.se.greenme.R;
import datx021512.chalmers.se.greenme.adapters.ShoppingAdapter;


public class ShoppingFragment extends Fragment implements View.OnClickListener {
    private EditText mInput;
    private RecyclerView mRecycleView;
    private ShoppingAdapter mAdapter;
    private Button mAddButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shopping, container, false);

        //Init fields
        mInput = (EditText) rootView.findViewById(R.id.text_input);
        mAdapter = new ShoppingAdapter(rootView.getContext());
        mAddButton = (Button) rootView.findViewById(R.id.add_text);
        mAddButton.setOnClickListener(this);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.recyclerShoppingItems);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(llm);
        mRecycleView.setAdapter(mAdapter);
        setHasOptionsMenu(true);

        return rootView;
    }

    /*public void addItem(View view) {
        if (mInput.getText() != null) {
            String text = mInput.getText().toString();
            if (text != null && text.trim().length() > 0) {
                mAdapter.addItem(mInput.getText().toString());
            }
        }

    }*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_text:
                Log.d("GREEN", "Add button pressed!");
                if (mInput.getText() != null) {
                    String text = mInput.getText().toString();
                    if (text != null && text.trim().length() > 0) {
                        mAdapter.addItem(mInput.getText().toString());
                        mAdapter.notifyDataSetChanged();
                        Log.d("GREEN", "item added");
                        Log.d("GREEN", "items: " + mAdapter.getItemCount());
                    }
                }
                break;
            case R.id.OCR_add:
                Log.d("GREEN","OCR button pushed");
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
}
