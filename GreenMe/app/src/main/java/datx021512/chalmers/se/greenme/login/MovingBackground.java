package datx021512.chalmers.se.greenme.login;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import datx021512.chalmers.se.greenme.R;

/**
 * Created by Patrik on 2015-05-09.
 */

public class MovingBackground extends SurfaceView implements
        SurfaceHolder.Callback {


    private Bitmap backGround;
    private Bitmap logo;
    private Bitmap loginButton;
    private String TAG = "Test";
    private int login_left;
    private int login_top;


    public MovingBackground(Context context) {
        super(context);

        backGround = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.signin_background);

        Bitmap tmplogo = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.kandidatloga);

        Bitmap tmplogin = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.signin);

        int widthLogo = (int)Math.round(tmplogo.getWidth()*0.5);
        int heightLogo = (int)Math.round(tmplogo.getHeight()*0.5);

        int widthLogin = (int)Math.round(tmplogin.getWidth()*0.65);
        int heightLogin = (int)Math.round(tmplogin.getHeight()*0.6);

        logo = Bitmap.createScaledBitmap(tmplogo, widthLogo, heightLogo, false);
        loginButton = Bitmap.createScaledBitmap(tmplogin, widthLogin, heightLogin, false);

        login_left = 0;
        login_top = 0;

        //  If this view doesn't do any drawing on its own, set this flag to allow further optimizations.
        setWillNotDraw(false);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        doDrawRunning(canvas);
        //invalidate the whole view
        invalidate();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * Draw current state of the canvas.
     */

    //so this actually does only work if the image is bigger than the canvas,
    // since background.getWidth() > getWidth().
    private int x = 0;
    private double ix = -1;

    private void doDrawRunning(Canvas canvas) {

        login_left = this.getWidth()/2-loginButton.getWidth()/2;
        login_top = getHeight()/4+logo.getHeight()+loginButton.getHeight()/2;


        canvas.drawBitmap(backGround, -x, 0, null);
        canvas.drawBitmap(logo,this.getWidth()/2-logo.getWidth()/2,this.getHeight()/6,null);
        canvas.drawBitmap(loginButton,login_left,login_top,null);

        if (x == 0 || backGround.getWidth() - x == getWidth())
            ix = -ix;

        x += ix;
    }
    public int getLoginLeft(){
        return login_left;
    }

    public int getLoginTop() {
        return login_top;
    }

    public int getLoginButtonWidth() {
        return loginButton.getWidth();
    }
    public int getLoginButtonHeight() {
        return loginButton.getHeight();
    }
}
