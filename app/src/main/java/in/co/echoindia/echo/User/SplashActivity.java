package in.co.echoindia.echo.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import in.co.echoindia.echo.HomePage.HomePageActivity;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    private class splash implements Runnable{

        @Override
        public void run() {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        mContentView = findViewById(R.id.fullscreen_content);

        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // Set up the user interaction to manually show or hide the system UI.

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        Thread splash = new Thread(new splash());
        splash.start();
        gotoNextActivity();
    }

    void gotoNextActivity(){

        sharedpreferences = AppUtil.getAppPreferences(this);
        if(sharedpreferences.getBoolean(Constants.SETTINGS_IS_LOGGED,false)) {
            Intent intentHomePage = new Intent(SplashActivity.this, HomePageActivity.class);
            Toast.makeText(SplashActivity.this, "Welcome "+sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_USER_CODE,""), Toast.LENGTH_SHORT).show();
            startActivity(intentHomePage);

        }
        else{
            Intent intentWalkThrough = new Intent(SplashActivity.this, WalkthroughActivity.class);
            startActivity(intentWalkThrough);
        }
        SplashActivity.this.finish();

    }

}
