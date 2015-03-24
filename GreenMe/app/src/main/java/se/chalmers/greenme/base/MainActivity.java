package se.chalmers.greenme.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import se.chalmers.greenme.base.fragments.Home;
import se.chalmers.greenme.base.fragments.ShoppingFragment;
import se.chalmers.greenme.base.fragments.StatisticsFragment;
import se.chalmers.greenme.base.fragments.TravelFragment;
import se.chalmers.greenme.base.navigation.NavCallback;
import se.chalmers.greenme.base.navigation.NavFragment;


public class MainActivity extends ActionBarActivity implements NavCallback {

    private Toolbar toolBar;

    private NavFragment navigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        navigationDrawerFragment = (NavFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        navigationDrawerFragment.setup(R.id.fragment_drawer,(DrawerLayout) findViewById(R.id.drawer), toolBar);

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new Home();
                break;
            case 1:
                fragment = new ShoppingFragment();
                break;
            case 2:
                fragment = new TravelFragment();
                break;
            case 3:
                fragment = new StatisticsFragment();
                break;
            default:
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

        }

    }

    @Override
    public void onBackPressed() {
        if (navigationDrawerFragment.isDrawerOpen())
            navigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // finish();
    }

}