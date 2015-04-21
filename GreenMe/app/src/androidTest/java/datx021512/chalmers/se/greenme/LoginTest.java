package datx021512.chalmers.se.greenme;

import datx021512.chalmers.se.greenme.LoginActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;


public class LoginTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private LoginActivity LoginActivity;
    private SignInButton SignInButton;

    public LoginTest() {
        super(LoginActivity.class);
    }


    @Override
    protected void setUp() throws Exception{

        super.setUp();
        setActivityInitialTouchMode(true);

        LoginActivity = getActivity();
        SignInButton = (SignInButton) LoginActivity.findViewById(R.id.sign_in_button);
    }

    @MediumTest
    public void testPreconditions() {
        //Try to add a message to add context to your assertions. These messages will be shown if
        //a tests fails and make it easy to understand why a test failed
        assertNotNull("LoginActivity is null", LoginActivity);
        assertNotNull("SignInButton is null", SignInButton);
    }

    @MediumTest
    public void testSignInButton_layout() {
        //Retrieve the top-level window decor view
        final View decorView = LoginActivity.getWindow().getDecorView();

        //Verify that the mClickMeButton is on screen
        ViewAsserts.assertOnScreen(decorView, SignInButton);

        //Verify width and heights
        final ViewGroup.LayoutParams layoutParams = SignInButton.getLayoutParams();
        assertNotNull(layoutParams);
        assertEquals(layoutParams.width, getPx(195));
        assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public int getPx(int dp){
        float scale = LoginActivity.getResources().getDisplayMetrics().density;
        return((int) (dp * scale + 0.5f));
    }
}
