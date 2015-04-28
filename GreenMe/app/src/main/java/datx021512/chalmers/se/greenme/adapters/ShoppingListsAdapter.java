package datx021512.chalmers.se.greenme.adapters;


import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import datx021512.chalmers.se.greenme.R;
import datx021512.chalmers.se.greenme.database.DatabaseHelper;
import datx021512.chalmers.se.greenme.fragments.ShoppingFragment;
import datx021512.chalmers.se.greenme.fragments.ShoppingListsFragment;

public class ShoppingListsAdapter extends RecyclerView.Adapter<ShoppingListsAdapter.ListViewHolder>{
    private final ShoppingListsFragment shoppingListsFragment;
    private ArrayList<ShopItem> mListData = new ArrayList();
    private View rootView;



    public ShoppingListsAdapter(ArrayList<ShopItem> shoppingLists, ShoppingListsFragment shoppingListsFragment) {
        mListData = shoppingLists;
        this.shoppingListsFragment = shoppingListsFragment;
    }

    public boolean contains(String s){
        for(ShopItem i: mListData){
            if(i.getmName().equals(s) || i.getmName().replaceAll("\\s","").equals(s)){
                return true;
            }
        }
        return false;
    }

    @Override
    public ShoppingListsAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.shopping_lists_item, parent, false);

        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder viewHolder, final int position) {
        ShopItem data = mListData.get(position);
        viewHolder.textItem.setText(data.getmName());
        viewHolder.textCO2.setText(Double.toString(data.getmCO2()) + " kg/co2");
        viewHolder.textDate.setText(data.getDate());
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = shoppingListsFragment.getFragmentManager();
                final Bundle bundle = new Bundle();
                bundle.putString("Shopping_Name", viewHolder.textItem.getText().toString());
                ShoppingFragment sh= new ShoppingFragment();
                sh.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.container,sh).commit();
            }
        });
        if(position%2==0){
            viewHolder.linearLayout.setBackgroundColor(Color.LTGRAY);
        }else {
            viewHolder.linearLayout.setBackgroundColor(Color.WHITE);
        }
    }

    public void addItem(ShopItem item) {
        mListData.add(item);
        notifyItemInserted(mListData.size());
    }

    public void removeItem(ShopItem item) {
        int position = mListData.indexOf(item);
        if (position != -1) {
            mListData.remove(item);
            notifyItemRemoved(position);
        }
    }

    public void removeItem(int position) {
        DatabaseHelper db = new DatabaseHelper(shoppingListsFragment.getView().getContext());
        db.removeShoppingList(mListData.get(position).getmName());
        mListData.remove(position);
        notifyDataSetChanged();
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public Double getTotalImpact() {
        double totalImpact =0;
        for(ShopItem i: mListData){
            totalImpact += (i.getmCO2()*i.getQuantity());
        }
        return totalImpact;
    }

    public List<ShopItem> getAllitems() {
        return(List<ShopItem>) mListData.clone();
    }


    public static class ListViewHolder extends RecyclerView.ViewHolder {
        TextView textItem;
        TextView textCO2;
        ImageButton buttonDelete;
        LinearLayout linearLayout;
        TextView textDate;

        public ListViewHolder(View itemView) {
            super(itemView);
            textItem = (TextView) itemView.findViewById(R.id.text_Lists_item);
            textCO2 = (TextView) itemView.findViewById(R.id.text_Lists_co2);
            buttonDelete = (ImageButton) itemView.findViewById(R.id.button_Lists_delete);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.verticalListsLayout);
            textDate =  (TextView) itemView.findViewById(R.id.text_Lists_Date);
        }
    }
}
