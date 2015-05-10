package datx021512.chalmers.se.greenme.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;


import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.android.gms.games.GamesStatusCodes;

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

import datx021512.chalmers.se.greenme.MainActivity;
import datx021512.chalmers.se.greenme.R;
import datx021512.chalmers.se.greenme.adapters.ShopItem;
import datx021512.chalmers.se.greenme.adapters.ShoppingAdapter;
import datx021512.chalmers.se.greenme.database.DatabaseHelper;
import datx021512.chalmers.se.greenme.ocr.IntentIntegrator;
import datx021512.chalmers.se.greenme.ocr.IntentResult;


public class ShoppingFragment extends Fragment implements View.OnClickListener{
    private AutoCompleteTextView mAutoCompleteField;
    private RecyclerView mRecycleView;
    private ShoppingAdapter mAdapter;
    private ImageButton mOCRButton;
    private ImageButton mAddButton;
    private DatabaseHelper db;
    private View rootView;
    private MainActivity mainActivity;
    private String name;
    public String TAG = "ShoppingFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shopping, container, false);
        mainActivity = (MainActivity)getActivity();
        mainActivity.setTitle("Shopping Lista");
        ArrayList<ShopItem> items = new ArrayList<ShopItem>();
        mAdapter = new ShoppingAdapter(rootView);
        db = new DatabaseHelper(rootView.getContext());
        Bundle args = getArguments();
        if (args  != null && args.containsKey("Shopping_Name")){
            this.name = args.getString("Shopping_Name");
        }
        getActivity().setTitle(name);
        mAdapter = new ShoppingAdapter(db.getSavedList(name), rootView);
        mAddButton = (ImageButton) rootView.findViewById(R.id.add_text);
        mAddButton.setOnClickListener(this);
        mOCRButton = (ImageButton) rootView.findViewById(R.id.OCR_add);
        mOCRButton.setOnClickListener(this);
        mOCRButton = (ImageButton) rootView.findViewById(R.id.upload_button);
        mOCRButton.setOnClickListener(this);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.recyclerShoppingItems);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(llm);
        mRecycleView.setAdapter(mAdapter);
        Log.d("GREEN","Setting adapter!");
        setHasOptionsMenu(true);




        ArrayList<String> categories = db.getCategories();
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_item, categories);

        textView = (AutoCompleteTextView) rootView.findViewById(R.id.text_input);
        textView.setAdapter(itemsAdapter);
        textView.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_DONE) {
                    addNewItem();
                    return true;
                }
                return false;
            }
        });

        this.rootView = rootView;
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_text:
                Log.d("GREEN", "Add button pressed!");
                addNewItem();
                break;
            case R.id.OCR_add:
                Log.d("GREEN","OCR button pushed");
                IntentIntegrator integrator = new IntentIntegrator(ShoppingFragment.this);
                integrator.addExtra("PROMPT_MESSAGE", "Skanna din vara");
                integrator.initiateScan();
                break;
            case R.id.upload_button:
                Log.d("GREEN", "upload button pushed");
                updateLeaderboard();
                break;
        }
    }

    private void addNewItem() {
        if (textView.getText() != null) {
            final String text = textView.getText().toString();
            if (text != null && text.trim().length() > 0) {
                if (db.getImpact(text).size() == 1 && db.getImpactName(text).get(0).equals(text) ) {
                    addItemToList(db.getImpactName(text).get(0), Double.parseDouble(db.getImpact(text).get(0)),"",1,(db.getEco(text).get(0)));
                    Log.d("TEST","!!!!!!!!!!!!!!!!GetALL1: " + mAdapter.getAllitems());
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
                                addItemToList(db.getImpactName(text).get(position-1), Double.parseDouble(db.getImpact(text).get(position-1)),"",1,(db.getEco(text).get(position-1)));
                                Log.d("TEST","!!!!!!!!!!!!!!!!GetALL2: " + mAdapter.getAllitems());
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

    }

    @Override
    public void onPause(){
        super.onPause();
        db.saveList(mAdapter.getAllitems(),name);
    }


    private void createNewItem(String text) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.dialog_newitem, null);
            final EditText userInput = (EditText)
                    convertView.findViewById(R.id.username);
            final EditText userImpact = (EditText)
                    convertView.findViewById(R.id.userimpact);
            final EditText userCountry = (EditText)
                convertView.findViewById(R.id.usercountry);
            final CheckBox userEkological = (CheckBox)
                convertView.findViewById(R.id.userEkological);
            userInput.setText(text);

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String text = userInput.getText().toString();
                    String text2 = userImpact.getText().toString();
                    String county = userCountry.getText().toString();
                    int eco = 0;
                    db.createNewItem(text, Integer.parseInt(text2));
                    if (((CheckBox) userEkological).isChecked()) {
                        eco = 1;

                    }
                    addItemToList(text, Double.parseDouble(text2),county,1,eco);

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

    private void createNewOCRItem(String text, final double weight) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = inflater.inflate(R.layout.dialog_newitem, null);
        final EditText userInput = (EditText) convertView.findViewById(R.id.username);
        final EditText userImpact = (EditText) convertView.findViewById(R.id.userimpact);
        userInput.setText(text);
        userImpact.setHint("skriv in kg/co2 värde");

    public void addItemToList(String text,double position,String country, double quant, int eco){
        if(!mAdapter.contains(text)) {
           //if(eco == null)
            mAdapter.addItem(new ShopItem(text, position,country, quant, eco));
            textView.setText("");
            updateTotal();
        }
    }

    public void addOCRToList(String text, int amount, double weight, double co2)
    {
        if(!mAdapter.contains(text)) {
            mAdapter.addItem(new ShopItem(text, amount, weight, co2));
            mAutoCompleteField.setText("");
            updateTotal();
        }
    }

    public void updateTotal(){
        Double total = mAdapter.getTotalImpact();
        TextView textTotal = (TextView) getActivity().findViewById(R.id.text_total);
        textTotal.setText(total+" kg/co2");

    }
    public void updateLeaderboard(){
        final int newEco = mAdapter.getNewEco();
        Log.d(TAG,"det vi ska lägga till är: " + newEco);


        Games.Leaderboards.submitScoreImmediate(mainActivity.getmGoogleApiClient(),mainActivity.getResources()
                .getString(R.string.Leaderboard_Ekologiskt),0)
                .setResultCallback(new ResultCallback<Leaderboards.SubmitScoreResult>() {

                    @Override
                    public void onResult(Leaderboards.SubmitScoreResult submitScoreResult) {

                        if (submitScoreResult.getStatus().getStatusCode() == GamesStatusCodes.STATUS_OK) {

                            Games.Leaderboards.loadCurrentPlayerLeaderboardScore(mainActivity.getmGoogleApiClient(),
                                    mainActivity.getResources()
                                            .getString(R.string.Leaderboard_Ekologiskt),LeaderboardVariant.TIME_SPAN_ALL_TIME,LeaderboardVariant.COLLECTION_SOCIAL)
                                    .setResultCallback(new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {

                                        @Override
                                        public void onResult(Leaderboards.LoadPlayerScoreResult loadPlayerScoreResult) {
                                            Long currScore = loadPlayerScoreResult.getScore().getRawScore();
                                            Long score = currScore + newEco;
                                            Log.d(TAG,score.toString());
                                            Games.Leaderboards.submitScore(mainActivity.getmGoogleApiClient(),
                                                    mainActivity.getResources().getString(R.string.Leaderboard_Ekologiskt), score);
                                        }
                                    });
                        }
                        else{
                            Log.d("GREEN", " Something went wrong, the LeaderboardStatus is not OK. " );
                        }
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:
                Toast.makeText(getActivity(),"Sparar listan",Toast.LENGTH_SHORT).show();

                return true;
            case R.id.action_load:
                Toast.makeText(getActivity(),"Laddar in en lista",Toast.LENGTH_SHORT).show();

                return true;
            case R.id.action_reset:
                Toast.makeText(getActivity(),"Rensar listan",Toast.LENGTH_SHORT).show();
                mAdapter.removeAllItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int request, int result, Intent i) {
        super.onActivityResult(request, result, i);
        IntentResult scan = IntentIntegrator.parseActivityResult(request, result, i);
        String read = scan.getContents();
        Log.d("OCR","read: " + read);
        String URL = "http://api.ica.se/api/upclookup/?upc=";
        String text = "";
        if (scan!=null && read != null) {
            Log.d("OCR","ocr: " + read);
            try{
                text = JSONToString(getFromInternetz(URL + read));
                if(text != "" && text != null && text != "null")
                {
                    String[] split = text.split(" ");
                    String last = split[split.length-1];
                    Log.d("OCR","Weight: " + last);
                    String last2 = split[split.length-2];
                    Log.d("OCR","Weight2: " + last2);
                    double numWeight = 0;
                    if(last2.matches(".*\\d.*")){
                        if(last.contains("kg")){
                            numWeight = Double.parseDouble(last2);
                        }
                        else if(last.contains("g")){
                            last2 = last2.replaceAll("[^\\d]", "");
                            numWeight = Double.parseDouble(last2);
                            numWeight /= 1000;
                        }
                    }
                    else
                    {
                        if(last.contains("kg")){
                            last = last.replaceAll("[^\\d]", "");
                            numWeight = Double.parseDouble(last);
                        }
                        else if(last.contains("g")){
                            last = last.replaceAll("[^\\d]", "");
                            numWeight = Double.parseDouble(last);
                            numWeight /= 1000;
                        }
                    }


                    Log.d("OCR","Inside: " + numWeight);
                    addItemToList(text,1337,"",numWeight,1);
//                    ShopItem item = new ShopItem(text,1337,1);
//                    mAdapter.addItem(item);
//                    mAdapter.notifyDataSetChanged();
//                    updateTotal();
                    checkDbForItem(text,numWeight);
                }
                else
                    Toast.makeText(getActivity(), "Produkten hittades ej!", Toast.LENGTH_SHORT).show();


            }catch (Exception e){
                e.printStackTrace();
            }

            if(text == null)
                Toast.makeText(getActivity(), "Ingen produkt scannades", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            Log.d("API","Nu da: " + text);

        }
        else
        {
            Toast.makeText(getActivity(), "Kunde ej hitta streckkod!", Toast.LENGTH_SHORT).show();
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
    public String checkIfInDb(String scanned)
    {
        return db.getImpactName(scanned).get(0);
    }

    public void checkDbForItem(String text, final double weight)
    {
        final String inputText = text;
        if (db.getImpact(text).size() == 1 && db.getImpactName(text).get(0).equals(text) ) {
            addOCRToList(db.getImpactName(text).get(0), 1, weight, Double.parseDouble(db.getImpact(text).get(0)));
            Log.d("SHOPPING","!!!!!!!!!!!!!!!!GetALL1: " + mAdapter.getAllitems());
            Log.d("TEST1","Weight: " + weight);
        } else {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View convertView = inflater.inflate(R.layout.list_alert, null);
            alertDialog.setView(convertView);
            alertDialog.setTitle("Menade du detta?");
            ListView lv = (ListView) convertView.findViewById(R.id.listView1);
            ArrayList<String> suggestions = new ArrayList<>();
            suggestions.add("Skapa nytt objekt");
            suggestions.addAll(db.getImpactName(text));
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,suggestions );
            lv.setAdapter(arrayAdapter);
            final AlertDialog mdialog = alertDialog.create();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position==0){
                        createNewOCRItem(inputText, weight);
                    }else{
                        addOCRToList(db.getImpactName(inputText).get(0), 1, weight, Double.parseDouble(db.getImpact(inputText).get(0)));
                        Log.d("TEST","!!!!!!!!!!!!!!!!GetALL2: " + mAdapter.getAllitems());
                        Log.d("TEST2","Weight: " + weight);
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
