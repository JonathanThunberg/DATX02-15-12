package datx021512.chalmers.se.greenme.navigation;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import datx021512.chalmers.se.greenme.R;

public class NavFragment extends Fragment implements NavCallback {
    private RecyclerView recyclerviewDrawerList;
    private int navigationCurrentSelectedPosition;
    private NavCallback drawerCallbacks;
    private View drawerFragmentContainerView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nav, container, false);
        recyclerviewDrawerList = (RecyclerView) view.findViewById(R.id.drawerList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerviewDrawerList.setLayoutManager(layoutManager);
        recyclerviewDrawerList.setHasFixedSize(true);

//		RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);

        //	recyclerviewDrawerList.addItemDecoration(itemDecoration);

        final List<NavItem> navigationItems = getMenu();
        NavAdapter navigationAdapter = new NavAdapter(navigationItems);
        navigationAdapter.setNavigationDrawerCallbacks(this);
        recyclerviewDrawerList.setAdapter(navigationAdapter);
        selectItem(navigationCurrentSelectedPosition);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            drawerCallbacks = (NavCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }

    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        drawerFragmentContainerView = getActivity().findViewById(fragmentId);
        this.drawerLayout = drawerLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded())
                    return;
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded())
                    return;
                getActivity().invalidateOptionsMenu();
            }
        };

        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                actionBarDrawerToggle.syncState();
            }
        });

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    public void openDrawer() {
        drawerLayout.openDrawer(drawerFragmentContainerView);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(drawerFragmentContainerView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        drawerCallbacks = null;
    }

    public List<NavItem> getMenu() {
        List<NavItem> items = new ArrayList<NavItem>();
        items.add(new NavItem("Hem ", getResources().getDrawable(
                R.drawable.ic_home_grey600_48dp)));
        items.add(new NavItem("Inköpslistor", getResources().getDrawable(
                R.drawable.ic_shopping_cart_grey600_48dp)));
        items.add(new NavItem("Resa", getResources().getDrawable(
                R.drawable.ic_directions_car_grey600_48dp)));
        items.add(new NavItem("Statistik", getResources().getDrawable(
                R.drawable.ic_trending_up_grey600_48dp)));
        items.add(new NavItem("Topplistor", getResources().getDrawable(
                R.drawable.ic_group_grey600_48dp)));
        items.add(new NavItem("Logga ut", getResources().getDrawable(
                R.drawable.abc_ic_ab_back_mtrl_am_alpha)));
        return items;
    }

    void selectItem(int position) {
        if (position != 4) //if topplistor button is clicked then dont highlight it
        {
            navigationCurrentSelectedPosition = position;
        }
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(drawerFragmentContainerView);
        }
        if (drawerCallbacks != null) {
            drawerCallbacks.onNavigationDrawerItemSelected(position);
        }
        ((NavAdapter) recyclerviewDrawerList.getAdapter()).selectPosition(navigationCurrentSelectedPosition);
    }

    public boolean isDrawerOpen() {
        return drawerLayout != null
                && drawerLayout.isDrawerOpen(drawerFragmentContainerView);
    }

    public boolean isDrawerClosed()
    {
        return drawerLayout == null;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        selectItem(position);
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }
}
