package datx021512.chalmers.se.greenme.adapters;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import datx021512.chalmers.se.greenme.R;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ListViewHolder>{
    private ArrayList<ShopItem> mListData;
    //  private LayoutInflater mLayoutInflater;
    private View rootView;

    public ShoppingAdapter(ArrayList<ShopItem> items)
    {
        //  mLayoutInflater = LayoutInflater.from(context);
        mListData = items;
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
        Log.d("ADAPTER", "entering oncreateviewholder!");
        //View row = mLayoutInflater.inflate(R.layout.shopping_item, parent, false);
        //ShoppingAdapter.ViewHolder holder = new ShoppingAdapter.ViewHolder(row);
        View v = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.shopping_wrapper, parent,  false);
        //View v = mLayoutInflater.inflate(R.layout.shopping_item, parent, false);
        ListViewHolder vh = new ListViewHolder(v);
        return vh;
        //return holder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder viewHolder, final int position) {
        ShopItem data = mListData.get(position);
        viewHolder.textItem.setText(data.getmName());
        Log.d("ADAPTER", data.getmName());
        viewHolder.textCO2.setText(Double.toString(data.getmCO2())+" kg/co2");
        viewHolder.textQuantity.setText(Double.toString(data.getQuantity())+" kg");
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
        textTotal.setText(total+" kg/co2");
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
        //notifyDataSetChanged();
        notifyItemInserted(mListData.size());
    }

    public void removeItem(String item) {
        int position = mListData.indexOf(item);
        if (position != -1) {
            mListData.remove(item);
            notifyItemRemoved(position);
        }
    }

    public void removeItem(int position) {
        mListData.remove(position);
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
            buttonDelete = (ImageButton) itemView.findViewById(R.id.button_delete);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.verticalLayout);
        }
    }
}
