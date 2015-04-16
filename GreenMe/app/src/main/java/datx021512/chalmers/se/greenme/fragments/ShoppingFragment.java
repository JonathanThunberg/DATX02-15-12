package datx021512.chalmers.se.greenme.fragments;

import android.app.Fragment;
import android.content.Intent;
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
import android.widget.Toast;


import datx021512.chalmers.se.greenme.R;
import datx021512.chalmers.se.greenme.adapters.ShoppingAdapter;
import datx021512.chalmers.se.greenme.ocr.IntentIntegrator;
import datx021512.chalmers.se.greenme.ocr.IntentResult;


public class ShoppingFragment extends Fragment implements View.OnClickListener {
    private EditText mInput;
    private RecyclerView mRecycleView;
    private ShoppingAdapter mAdapter;
    private Button mAddButton;
    private Button mOCRButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shopping, container, false);

        //Init fields
        mInput = (EditText) rootView.findViewById(R.id.text_input);
        mAdapter = new ShoppingAdapter(rootView.getContext());
        mAddButton = (Button) rootView.findViewById(R.id.add_text);
        mAddButton.setOnClickListener(this);
        mOCRButton = (Button) rootView.findViewById(R.id.OCR_add);
        mOCRButton.setOnClickListener(this);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.recyclerShoppingItems);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                IntentIntegrator integrator = new IntentIntegrator(ShoppingFragment.this);
                integrator.initiateScan();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onActivityResult(int request, int result, Intent i) {
        IntentResult scan = IntentIntegrator.parseActivityResult(request, result, i);

        if (scan!=null) {
            Toast.makeText(getActivity(), scan.getContents(), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getActivity(), "No barcode is read!", Toast.LENGTH_SHORT).show();
        }
    }
}
