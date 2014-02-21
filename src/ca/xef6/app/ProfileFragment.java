package ca.xef6.app;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

public class ProfileFragment extends Fragment {

    ImageButton               profilePictureButton;
    TextView                  userNameView;
    private UiLifecycleHelper uiLifecycleHelper;

    private void initialize() {
        uiLifecycleHelper = new UiLifecycleHelper(getActivity(), new StatusCallback() {

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
        initialize();
        uiLifecycleHelper.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);
        profilePictureButton = (ImageButton) view.findViewById(R.id.profile_picture_button);
        userNameView = (TextView) view.findViewById(R.id.user_name_view);
        setUserData(Session.getActiveSession());
        return view;
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
        setUserData(session);
    }

    @Override
    public void onStop() {
        super.onStop();
        uiLifecycleHelper.onStop();
    }

    private void setUserData(final Session session) {
        Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (session == Session.getActiveSession()) {
                    if (user != null) {
                        new ProfilePictureDownloadTask(profilePictureButton).execute(user.getId());
                        userNameView.setText(user.getName());
                    }
                }
                if (response.getError() != null) {
                    //handleError(response.getError());
                }
            }
        });
        request.executeAsync();

    }
}

class ProfilePictureDownloadTask extends AsyncTask<String, Void, Bitmap> {

    private final ImageView imageView;

    public ProfilePictureDownloadTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... userIds) {
        return downloadProfilePicture(userIds[0]);
    }

    private Bitmap downloadProfilePicture(String userId) {
        URL url = null;
        try {
            url = new URL("http://graph.facebook.com/me/picture?type=thumbnail");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

}
