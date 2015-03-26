package datx021512.chalmers.se.greenme.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import datx021512.chalmers.se.greenme.R;

public class ShoppingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<ShopItem> mListData;
    private LayoutInflater mLayoutInflater;

    public ShoppingAdapter(Context context, ArrayList<ShopItem> items)
    {
        mLayoutInflater = LayoutInflater.from(context);
        mListData = items;
    }

    @Override
    public ShoppingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("ADAPTER", "entering oncreateviewholder!");
        //View row = mLayoutInflater.inflate(R.layout.shopping_item, parent, false);
        //ShoppingAdapter.ViewHolder holder = new ShoppingAdapter.ViewHolder(row);
        View v = mLayoutInflater.inflate(R.layout.shopping_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
        //return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if(viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            Log.d("ADAPTER", "entering onbindviewholder!");

            ShopItem data = mListData.get(position);
            holder.textItem.setText(data.getmName());
            holder.textCO2.setText(Double.toString(data.getmCO2()));
            holder.textQuantity.setText("1");
            holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(position);
                }
            });
        }
    }

    public void addItem(ShopItem item) {
        Log.d("ADAPTER", "entering addItem!");
        mListData.add(item);
        notifyDataSetChanged();
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

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textItem;
        TextView textCO2;
        TextView textQuantity;
        ImageButton buttonDelete;
        ImageButton buttonPlus;
        ImageButton buttonMinus;

        public ViewHolder(View itemView) {
            super(itemView);
            textItem = (TextView) itemView.findViewById(R.id.text_item);
            textCO2 = (TextView) itemView.findViewById(R.id.text_co2);
            buttonPlus = (ImageButton) itemView.findViewById(R.id.button_plus);
            textQuantity = (TextView) itemView.findViewById(R.id.text_quantity);
            buttonMinus = (ImageButton) itemView.findViewById(R.id.button_minus);
            buttonDelete = (ImageButton) itemView.findViewById(R.id.button_delete);

        }
    }
}
