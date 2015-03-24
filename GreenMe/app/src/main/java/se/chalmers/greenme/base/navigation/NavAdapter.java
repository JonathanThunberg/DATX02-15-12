package se.chalmers.greenme.base.navigation;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import se.chalmers.greenme.base.R;

/**
 * Created by Fredrik on 2015-03-23.
 */
public class NavAdapter extends RecyclerView.Adapter<NavAdapter.ViewHolder> {
    private List<NavItem> mData;
    private NavCallback mDrawerCallbacks;
    private int mSelectedPosition;
    private int mTouchedPosition = -1;

    public NavAdapter(List<NavItem> data) {
        mData = data;
    }



    public void setNavigationDrawerCallbacks(NavCallback drawerCallbacks) {
        mDrawerCallbacks = drawerCallbacks;
    }

    @Override
    public NavAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nav_row, viewGroup, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(NavAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.textView.setText(mData.get(i).getText());
        viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(mData.get(i).getDrawable(), null, null, null);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {

                                                       if (mDrawerCallbacks != null)
                                                           mDrawerCallbacks.onNavigationDrawerItemSelected(i);
                                                   }
                                               }
        );


        if (mSelectedPosition == i || mTouchedPosition == i) {
            viewHolder.itemView.setBackgroundResource(0);
            viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(R.color.selection));

        } else {
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            viewHolder.itemView.setBackgroundResource(R.drawable.divider);
        }
    }


    public void selectPosition(int position) {
        int lastPosition = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(lastPosition);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_name);
        }
    }
}
