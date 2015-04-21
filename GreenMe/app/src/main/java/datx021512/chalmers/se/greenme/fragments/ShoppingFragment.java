package datx021512.chalmers.se.greenme.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.ArrayList;

import datx021512.chalmers.se.greenme.R;
import datx021512.chalmers.se.greenme.adapters.ShopItem;
import datx021512.chalmers.se.greenme.adapters.ShoppingAdapter;
import datx021512.chalmers.se.greenme.ocr.IntentIntegrator;
import datx021512.chalmers.se.greenme.ocr.IntentResult;
import datx021512.chalmers.se.greenme.database.databaseHelper;


public class ShoppingFragment extends Fragment implements View.OnClickListener {
    private AutoCompleteTextView textView;
    private RecyclerView mRecycleView;
    private ShoppingAdapter mAdapter;
    private ImageButton mOCRButton;
    private ImageButton mAddButton;
    private databaseHelper db;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shopping, container, false);


        ArrayList<ShopItem> items = new ArrayList<ShopItem>();
        mAdapter = new ShoppingAdapter(rootView);
        mAddButton = (ImageButton) rootView.findViewById(R.id.add_text);
        mAddButton.setOnClickListener(this);
        mOCRButton = (ImageButton) rootView.findViewById(R.id.OCR_add);
        mOCRButton.setOnClickListener(this);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.recyclerShoppingItems);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(llm);
        mRecycleView.setAdapter(mAdapter);
        Log.d("GREEN","Setting adapter!");
        setHasOptionsMenu(true);



        db = new databaseHelper(rootView.getContext());
        ArrayList<String> categories = db.getCategories();
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_item, categories);

        textView = (AutoCompleteTextView)
                rootView.findViewById(R.id.text_input);
        textView.setAdapter(itemsAdapter);
        this.rootView = rootView;
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_text:
                Log.d("GREEN", "Add button pressed!");
                if (textView.getText() != null) {
                    final String text = textView.getText().toString();
                    if (text != null && text.trim().length() > 0) {
                       if (/*db.getImpact(text).size() == 1 &&*/ db.getImpactName(text).get(0).equals(text) ) {
                               addItemToList(db.getImpactName(text).get(0), Double.parseDouble(db.getImpact(text).get(0)));
                              x
                           } else {
                               final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                               LayoutInflater inflater = getActivity().getLayoutInflater();
                               View convertView = (View) inflater.inflate(R.layout.list_alert, null);
                               alertDialog.setView(convertView);
                               alertDialog.setTitle("Menade du detta?");
                               ListView lv = (ListView) convertView.findViewById(R.id.listView1);
                               ArrayList<String> suggestions = new ArrayList<>();
                               suggestions.add("Skapa nytt objekt");
                               suggestions.addAll(db.getImpactName(text));
                               ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,suggestions );
                               lv.setAdapter(arrayAdapter);
                               final AlertDialog mdialog = alertDialog.create();
                               lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                       if(position==0){
                                            createNewItem(text);
                                       }else{
                                            addItemToList(db.getImpactName(text).get(position-1), Double.parseDouble(db.getImpact(text).get(position-1)));
                                       }
                                       mdialog.dismiss();
                                   }
                               });
                               mdialog.show();

                           }
                           View view = getActivity().getCurrentFocus();
                            if (view != null) {
                                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().getBaseContext().INPUT_METHOD_SERVICE);
                                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                       }
                    }

                break;
            case R.id.OCR_add:
                Log.d("GREEN","OCR button pushed");
                IntentIntegrator integrator = new IntentIntegrator(ShoppingFragment.this);
                integrator.addExtra("PROMPT_MESSAGE", "Skanna din vara");
                integrator.initiateScan();
                break;
        }
    }

    private void createNewItem(String text) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.dialog_newitem, null);
            final EditText userInput = (EditText)
                    convertView.findViewById(R.id.username);
            final EditText userImpact = (EditText)
                    convertView.findViewById(R.id.userimpact);

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String text = userInput.getText().toString();
                    String text2 = userImpact.getText().toString();
                    db.createNewItem(text, Integer.parseInt(text2));
                    addItemToList(text, Double.parseDouble(text2));

                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });


            alertDialog.setView(convertView);
            alertDialog.setTitle("Nytt Föremål");

            final AlertDialog mdialog = alertDialog.create();

            mdialog.show();
        }

    public void addItemToList(String text,double position){
        if(!mAdapter.contains(text)) {
            mAdapter.addItem(new ShopItem(text, position));
            textView.setText("");
            updateTotal();
        }
    }

    public void updateTotal(){
        Double total = mAdapter.getTotalImpact();
        TextView textTotal = (TextView) getActivity().findViewById(R.id.text_total);
        textTotal.setText(total+" kg/co2");

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
                ShopItem item = new ShopItem(text,1337);
                mAdapter.addItem(item);
                mAdapter.notifyDataSetChanged();
                updateTotal();
                //textView.setText(text);
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
