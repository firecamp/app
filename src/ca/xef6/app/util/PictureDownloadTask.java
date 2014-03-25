package ca.xef6.app.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class PictureDownloadTask extends AsyncTask<String, Void, Bitmap> {

    private final ImageView imageView;

    public PictureDownloadTask(ImageView imageView) {
	this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
	return downloadPicture(urls[0]);
    }

    private Bitmap downloadPicture(String stringUrl) {
	URL url = null;
	try {
	    url = new URL(stringUrl);
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
	Log.i("PictureDownloadTask", "sourceImage = "
		+ ((sourceImage == null) ? "<null>" : sourceImage.toString()));
	if (sourceImage == null)
	    return;

	imageView.setImageBitmap(sourceImage);
    }

}
