package datx021512.chalmers.se.greenme;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;

import datx021512.chalmers.se.greenme.fragments.Home;
import datx021512.chalmers.se.greenme.fragments.ShoppingFragment;
import datx021512.chalmers.se.greenme.fragments.ShoppingListsFragment;
import datx021512.chalmers.se.greenme.fragments.StatisticsFragment;
import datx021512.chalmers.se.greenme.fragments.TravelFragment;
import datx021512.chalmers.se.greenme.navigation.NavCallback;
import datx021512.chalmers.se.greenme.navigation.NavFragment;


public class MainActivity extends ActionBarActivity implements NavCallback {

    private Toolbar toolBar;
    public String TAG = "MainActivity";
    private static final int RC_UNUSED = 5001;
    private NavFragment navigationDrawerFragment;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        navigationDrawerFragment = (NavFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        navigationDrawerFragment.setup(R.id.fragment_drawer,(DrawerLayout) findViewById(R.id.drawer), toolBar);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .addApi(LocationServices.API)
                .build();

    }
    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    public GoogleApiClient getmGoogleApiClient()
    {
        return mGoogleApiClient;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new Home();
                break;
            case 1:
                fragment = new ShoppingListsFragment();
                break;
            case 2:
                fragment = new TravelFragment();
                break;
            case 3:
                fragment = new StatisticsFragment();
                break;
            case 4:
                if(mGoogleApiClient.isConnected())
                {
                    Log.d(TAG,"Show Leaderboard");
                    startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient),
                            RC_UNUSED);
                }
                else{
                    Log.d(TAG,"Cant show Leaderboard because user is not connected");
                }
                break;
            case 5:
                if(mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                this.finish();
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