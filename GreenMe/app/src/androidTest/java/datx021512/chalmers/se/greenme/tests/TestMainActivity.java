package datx021512.chalmers.se.greenme.tests;

import android.support.v7.widget.Toolbar;
import android.test.ActivityInstrumentationTestCase2;
import datx021512.chalmers.se.greenme.MainActivity;
import datx021512.chalmers.se.greenme.R;
import datx021512.chalmers.se.greenme.navigation.NavFragment;


/**
 * Created by Fredrik on 2015-05-05.
 */
public class TestMainActivity extends ActivityInstrumentationTestCase2<MainActivity>
{
    private MainActivity mMainActivity;
    private Toolbar mToolbar;
    private NavFragment navigationDrawerFragment;

    public TestMainActivity()
    {
        super(MainActivity.class);
    }

    public void testActivity()
    {
        assertNotNull("MainActivity is null", mMainActivity);
    }

    public void testGoogle()
    {
        assertTrue("Google connection is NOT working", mMainActivity.getmGoogleApiClient().isConnected());
    }

    public void testToolbar()
    {
        assertTrue("Toolbar is showing", mMainActivity.getSupportActionBar().isShowing());
    }

    public void testMenu()
    {
        navigationDrawerFragment.openDrawer();
        assertTrue("Menu not open", navigationDrawerFragment.isDrawerOpen());
        navigationDrawerFragment.closeDrawer();
        assertFalse("Menu not closed", navigationDrawerFragment.isVisible());
    }
    @Override
    public void setUp() throws Exception {
        super.setUp();
        mMainActivity = getActivity();
        navigationDrawerFragment = (NavFragment) mMainActivity.getFragmentManager().findFragmentById(R.id.fragment_drawer);

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
