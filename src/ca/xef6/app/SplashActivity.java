package ca.xef6.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import ca.xef6.app.ui.Activity;

import com.facebook.Session;
import com.facebook.SessionState;

public class SplashActivity extends Activity {

    private static final int MAIN_ACTIVITY_START_DELAY = 1500;

    private LinearLayout     loginLayout;
    private ImageView        splashImageView;
    private LinearLayout     splashLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        loginLayout = (LinearLayout) findViewById(R.id.login_layout);
        splashImageView = (ImageView) findViewById(R.id.splash_image_view);
        splashLayout = (LinearLayout) findViewById(R.id.splash_layout);
    }

    @Override
    public void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i("SplashActivity", "onSessionStateChange opened");
            loginLayout.setVisibility(View.GONE);
            splashLayout.setVisibility(View.VISIBLE);
            startMainActivity();
        } else {
            Log.i("SplashActivity", "onSessionStateChange closed");
            loginLayout.setVisibility(View.VISIBLE);
            splashLayout.setVisibility(View.GONE);
        }
    }

    private void startMainActivity() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }

        }, MAIN_ACTIVITY_START_DELAY);
    }

}
