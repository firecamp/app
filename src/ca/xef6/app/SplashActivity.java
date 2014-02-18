package ca.xef6.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

public class SplashActivity extends Activity {

    private static final int  MAIN_ACTIVITY_START_DELAY = 1500;

    private LinearLayout      loginLayout;
    private ImageView         splashImageView;
    private LinearLayout      splashLayout;

    private UiLifecycleHelper uiLifecycleHelper;

    private void initialize() {
        uiLifecycleHelper = new UiLifecycleHelper(this, new StatusCallback() {

            @Override
            public void call(Session session, SessionState state, Exception exception) {
                onSessionStateChange(session, state, exception);
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiLifecycleHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        loginLayout = (LinearLayout) findViewById(R.id.login_layout);
        splashImageView = (ImageView) findViewById(R.id.splash_image_view);
        splashLayout = (LinearLayout) findViewById(R.id.splash_layout);
        initialize();
        uiLifecycleHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiLifecycleHelper.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiLifecycleHelper.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        uiLifecycleHelper.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiLifecycleHelper.onSaveInstanceState(outState);
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
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

    @Override
    public void onStop() {
        super.onStop();
        uiLifecycleHelper.onStop();
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
