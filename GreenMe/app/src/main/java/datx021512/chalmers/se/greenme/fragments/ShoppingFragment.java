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


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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
    private static String mItem;

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
                integrator.addExtra("PROMPT_MESSAGE", "Skanna din vara");
                //integrator.setOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
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
        super.onActivityResult(request, result, i);
        IntentResult scan = IntentIntegrator.parseActivityResult(request, result, i);
        String read = scan.getContents();
        String URL = "http://api.ica.se/api/upclookup/?upc=";
        String text = "";
        if (scan!=null) {
            Log.d("OCR","ocr: " + read);
            try{

                text = JSONToString(getFromInternetz(URL + read));
                mAdapter.addItem(text);
                mAdapter.notifyDataSetChanged();
                mInput.setText(text);
            }catch (Exception e){
                e.printStackTrace();
            }

            Log.d("API","Nu da: " + text);
            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getActivity(), "No barcode is read!", Toast.LENGTH_SHORT).show();
        }
    }

    public String JSONToString(String result)
    {
        try
        {
            JSONObject obj = new JSONObject(result);
            JSONArray arr = obj.getJSONArray("Items");
            JSONObject jObj = arr.getJSONObject(0);
            return jObj.getString("ItemDescription");
            //Log.d("ICA","nya texten:" + mItem);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getFromInternetz(String url)
    {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            BufferedHttpEntity buf = new BufferedHttpEntity(entity);

            InputStream is = buf.getContent();

            BufferedReader r = new BufferedReader(new InputStreamReader(is));

            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line + "\n");
            }
            String result = total.toString();
            Log.i("Get URL", "Downloaded string: " + result);
            return result;
        } catch (Exception e) {
            Log.e("Get Url", "Error in downloading: " + e.toString());
        }
        return null;
    }
}
