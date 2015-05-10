package datx021512.chalmers.se.greenme.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import datx021512.chalmers.se.greenme.LoginActivity;
import datx021512.chalmers.se.greenme.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * Created by Fredrik on 2015-05-05.
 */
public class TestLoginActivity extends ActivityInstrumentationTestCase2<LoginActivity> {
    private LoginActivity mActivity;

    public TestLoginActivity()
    {
        super(LoginActivity.class);
    }
    public TestLoginActivity(Class<LoginActivity> activityClass) {
        super(activityClass);
    }

    public void testGoogleApi()
    {

        assertTrue("is NOT logged in", mActivity.getApiClient().isConnected());
    }
    @Override
    public void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
