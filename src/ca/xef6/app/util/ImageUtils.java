package ca.xef6.app.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;

public class ImageUtils {

    public static Bitmap getMask(Resources resources, int id)
    {
	return BitmapFactory.decodeResource(resources, id);
    }

    public static Bitmap applyMask(Bitmap image, Bitmap mask) {

	if (image == null || mask == null) {
	    throw new IllegalArgumentException("ImageUtils.roundCorners :: image or mask is null");
	}

	Bitmap destinationImage = Bitmap.createBitmap(mask.getWidth(),
						      mask.getHeight(),
						      Bitmap.Config.ARGB_8888);

	Paint paint = new Paint();
	paint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));

	Canvas c = new Canvas();

	c.setBitmap(destinationImage);
	c.drawBitmap(Bitmap.createScaledBitmap(image,
					       destinationImage.getWidth(),
					       destinationImage.getHeight(),
					       false),
		     0,
		     0,
		     null);

	c.drawBitmap(mask, 0, 0, paint);

	return destinationImage;
    }

}
