package datx021512.chalmers.se.greenme.adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import datx021512.chalmers.se.greenme.R;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ListViewHolder>{
    private ArrayList<ShopItem> mListData = new ArrayList();
    private View rootView;


    public ShoppingAdapter(View rootView)
    {
        this.rootView = rootView;
    }

    public ShoppingAdapter(ArrayList<ShopItem> items, View rootView) {
        mListData = items;
        this.rootView = rootView;
    }

    public boolean contains(String s){
        for(ShopItem i: mListData){
            if(i.getmName().equals(s)){
                return true;
            }
        }
        return false;
    }

    @Override
    public ShoppingAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.shopping_wrapper, parent, false);

        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListViewHolder viewHolder, final int position) {
        ShopItem data = mListData.get(position);
        viewHolder.textItem.setText(data.getmName() + " X " + (int)data.getQuantity());
        viewHolder.textCO2.setText(Double.toString(data.getmCO2())+" kg/co2");
        viewHolder.textQuantity.setText(Double.toString(data.getWeight())+" kg");
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });
        viewHolder.buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseQuantity(position);
            }
        });
        viewHolder.buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseQuantity(position);
            }
        });
        if(position%2==0){
            viewHolder.linearLayout.setBackgroundColor(Color.LTGRAY);
        }else {
            viewHolder.linearLayout.setBackgroundColor(Color.WHITE);
        }
    }

    private void decreaseQuantity(int position) {
        ShopItem temp = mListData.get(position);
        if(temp.getQuantity()-1>0){
            temp.setQuantity(temp.getQuantity()-1);
            mListData.remove(position);
            mListData.add(position, temp);
            notifyDataSetChanged();
        }
        Double total = getTotalImpact();
        TextView textTotal = (TextView) rootView.findViewById(R.id.text_total);
        textTotal.setText(total + " kg/co2");
    }


    private void increaseQuantity(int position) {
        ShopItem temp = mListData.get(position);
            temp.setQuantity(temp.getQuantity() + 1);
            mListData.remove(position);
            mListData.add(position, temp);
            notifyDataSetChanged();
        Double total = getTotalImpact();
        TextView textTotal = (TextView) rootView.findViewById(R.id.text_total);
        textTotal.setText(total+" kg/co2");
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
        mListData.remove(position);
        notifyDataSetChanged();
        notifyItemRemoved(position);
    }

    public void removeAllItems()
    {
        mListData.clear();
        notifyDataSetChanged();

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
    public int getNewEco() {
       int totalNewEco = 0;
        for(ShopItem i: mListData) {
            Log.d("TAG","gasdasdas: " + i.getEco());
            totalNewEco += (i.getEco()*i.getQuantity()); //Asuming standard for LiteSQL where 1 = true and 0 = false
        }

        Log.d("TAG","fadsdsa "+ totalNewEco);
        return totalNewEco;
    }

    public List<ShopItem> getAllitems() {
        return(List<ShopItem>) mListData.clone();
    }


    public static class ListViewHolder extends RecyclerView.ViewHolder {
        TextView textItem;
        TextView textCO2;
        TextView textQuantity;
        ImageButton buttonDelete;
        ImageButton buttonPlus;
        ImageButton buttonMinus;
        LinearLayout linearLayout;

        public ListViewHolder(View itemView) {
            super(itemView);
            textItem = (TextView) itemView.findViewById(R.id.text_item);
            textCO2 = (TextView) itemView.findViewById(R.id.text_co2);
            buttonPlus = (ImageButton) itemView.findViewById(R.id.button_plus);
            buttonPlus.setColorFilter(Color.argb(255, 0, 255, 0));
            textQuantity = (TextView) itemView.findViewById(R.id.text_quantity);
            buttonMinus = (ImageButton) itemView.findViewById(R.id.button_minus);
            buttonMinus.setColorFilter(Color.argb(255, 255, 0, 0));
            buttonDelete = (ImageButton) itemView.findViewById(R.id.button_delete);
            buttonDelete.setColorFilter(Color.argb(255, 0, 0, 0));
            linearLayout = (LinearLayout) itemView.findViewById(R.id.verticalLayout);
        }
    }
}
