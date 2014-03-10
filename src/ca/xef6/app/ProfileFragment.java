package ca.xef6.app;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import ca.xef6.app.ui.Fragment;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class ProfileFragment extends Fragment {

    ImageButton profilePictureButton;
    TextView    userNameView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);
        profilePictureButton = (ImageButton) view.findViewById(R.id.profile_picture_button);
        userNameView = (TextView) view.findViewById(R.id.user_name_view);
        setUserData(Session.getActiveSession());
        return view;
    }

    @Override
    public void onSessionStateChange(Session session, SessionState state, Exception exception) {
        setUserData(session);
    }

    private void setUserData(final Session session) {
        Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (session == Session.getActiveSession()) {
                    if (user != null && profilePictureButton != null && userNameView != null) {
                        new ProfilePictureDownloadTask(profilePictureButton, getActivity().getResources()).execute(user.getId());
                        userNameView.setText(user.getName());
                    }
                }
                if (response != null && response.getError() != null) {
                    //handleError(response.getError());
                }
            }
        });
        request.executeAsync();

    }
}

class ProfilePictureDownloadTask extends AsyncTask<String, Void, Bitmap> {

    private final ImageView imageView;
    private final Resources resources;

    public ProfilePictureDownloadTask(ImageView imageView, Resources resources) {
        this.imageView = imageView;
        this.resources = resources;
    }

    @Override
    protected Bitmap doInBackground(String... userIds) {
        return downloadProfilePicture(userIds[0]);
    }

    private Bitmap downloadProfilePicture(String userId) {
        URL url = null;
        try {
            url = new URL("https://graph.facebook.com/" + userId + "/picture?type=large");
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
    protected void onPostExecute(Bitmap sourceImage) {

        Bitmap mask = BitmapFactory.decodeResource(resources, R.drawable.mask);
        Bitmap image = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
        Canvas c = new Canvas();
        c.setBitmap(image);
        c.drawBitmap(Bitmap.createScaledBitmap(sourceImage, image.getWidth(), image.getHeight(), false), 0, 0, null);
        c.drawBitmap(mask, 0, 0, paint);

        imageView.setImageBitmap(image);
    }

}
