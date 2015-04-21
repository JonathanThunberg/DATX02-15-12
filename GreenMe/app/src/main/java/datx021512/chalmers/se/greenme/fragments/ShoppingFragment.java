package datx021512.chalmers.se.greenme.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

import datx021512.chalmers.se.greenme.R;
import datx021512.chalmers.se.greenme.adapters.ShopItem;
import datx021512.chalmers.se.greenme.adapters.ShoppingAdapter;
import datx021512.chalmers.se.greenme.database.databaseHelper;


public class ShoppingFragment extends Fragment implements View.OnClickListener {
    private AutoCompleteTextView textView;
    private RecyclerView mRecycleView;
    private ShoppingAdapter mAdapter;
    private Button mAddButton;
    private databaseHelper db;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shopping, container, false);

        //Init fields
      //  mInput = (EditText) rootView.findViewById(R.id.text_input);
        ArrayList<ShopItem> items = new ArrayList<ShopItem>();
        mAdapter = new ShoppingAdapter(items,rootView);
        mAddButton = (Button) rootView.findViewById(R.id.add_text);
        mAddButton.setOnClickListener(this);
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
                if (textView.getText() != null) {
                    final String text = textView.getText().toString();
                    if (text != null && text.trim().length() > 0) {
                       if (db.getImpact(text).size() == 1 && db.getImpactName(text).get(0).equals(text) ) {
                               addItemToList(text,0);
                               View view = getActivity().getCurrentFocus();
                               if (view != null) {
                                   InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().getBaseContext().INPUT_METHOD_SERVICE);
                                   inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                               }
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
                                            addItemToList(text,position-1);
                                       }
                                       mdialog.dismiss();
                                   }
                               });
                               mdialog.show();

                           }
                       }
                    }

                break;
            case R.id.OCR_add:
                Log.d("GREEN","OCR button pushed");
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
                        db.createNewItem(text,Integer.parseInt(text2));

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

    public void addItemToList(String text,int position){
        if(!mAdapter.contains(text)) {
            mAdapter.addItem(new ShopItem(db.getImpactName(text).get(position), Double.parseDouble(db.getImpact(text).get(position))));
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
}
