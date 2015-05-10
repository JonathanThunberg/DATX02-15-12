package datx021512.chalmers.se.greenme.tests;

import android.support.v7.widget.Toolbar;
import android.test.ActivityInstrumentationTestCase2;
import static com.google.android.apps.common.testing.ui.espresso.contrib.DrawerActions.closeDrawer;
import static com.google.android.apps.common.testing.ui.espresso.contrib.DrawerActions.openDrawer;
import static com.google.android.apps.common.testing.ui.espresso.contrib.DrawerMatchers.isClosed;
import static com.google.android.apps.common.testing.ui.espresso.contrib.DrawerMatchers.isOpen;


import datx021512.chalmers.se.greenme.MainActivity;
import datx021512.chalmers.se.greenme.R;
import datx021512.chalmers.se.greenme.navigation.NavFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


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

    public void testOpenAndCloseDrawer() {
        // NOT WORKING
        // Drawer should not be open to start.
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));

        openDrawer(R.id.drawer_layout);

        // The drawer should now be open.
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));

        closeDrawer(R.id.drawer_layout);

        // Drawer should be closed again.
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mMainActivity = getActivity();


    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }


}
