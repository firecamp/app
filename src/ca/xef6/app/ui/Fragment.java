package ca.xef6.app.ui;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

/**
 * Fragment class which handles Facebook session state changes automatically.
 * Use this instead of {@link android.support.v4.app.Fragment}. Override
 * {@link #onSessionStateChange(Session, SessionState, Exception)} to receive
 * Facebook state updates.
 */
public class Fragment extends android.support.v4.app.Fragment implements SessionStateChangeListener {

    private UiLifecycleHelper uiLifecycleHelper;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiLifecycleHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiLifecycleHelper = new UiLifecycleHelper(getActivity(), new StatusCallback() {

            @Override
            public void call(Session session, SessionState state, Exception exception) {
                onSessionStateChange(session, state, exception);
            }

        });
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

    @Override
    public void onSessionStateChange(Session session, SessionState state, Exception exception) {
    }

    @Override
    public void onStop() {
        super.onStop();
        uiLifecycleHelper.onStop();
    }

}
