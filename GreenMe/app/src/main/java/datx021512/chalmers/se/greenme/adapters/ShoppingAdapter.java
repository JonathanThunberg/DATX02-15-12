package datx021512.chalmers.se.greenme.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import datx021512.chalmers.se.greenme.R;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.Holder>{
    private ArrayList<ShopItem> mListData = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public ShoppingAdapter(Context context)
    {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = mLayoutInflater.inflate(R.layout.shopping_item, parent, false);
        Holder holder = new Holder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
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

    public void addItem(ShopItem item) {
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

    public static class Holder extends RecyclerView.ViewHolder {
        TextView textItem;
        TextView textCO2;
        TextView textQuantity;
        ImageButton buttonDelete;
        ImageButton buttonPlus;
        ImageButton buttonMinus;

        public Holder(View itemView) {
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
