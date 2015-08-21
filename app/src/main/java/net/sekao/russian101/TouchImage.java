package net.sekao.russian101;

import java.io.IOException;
import java.io.InputStream;

import android.widget.ImageView;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;

public class TouchImage extends ImageView {	
	public TouchImage(Context context) {
		super(context);
		init(context);
	}
	
	public TouchImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public TouchImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context) {		
		setScaleType(ImageView.ScaleType.FIT_CENTER);
		setAdjustViewBounds(true);
		setPadding(8, 8, 8, 8);
	}
	
	public void setLessonPage(Bitmap image) {
		setImageBitmap(image);
	}
	
	public static Bitmap getLessonPage(Context context, int lesson, int page, boolean isThumbnail) {
		String path = "lesson" + (lesson+1) + "/" + (page+1) +
			(isThumbnail ? "t" : "") + ".webp";

		Bitmap image = null;
		try {
			image = getBitmapFromAsset(context, path);
		} catch (Exception e) {}

		return image;
	}
	
	private static Bitmap getBitmapFromAsset(Context context, String strName) throws IOException {
		AssetManager assetManager = context.getAssets();

		InputStream istr = assetManager.open(strName);
		Bitmap bitmap = BitmapFactory.decodeStream(istr);

		return bitmap;
	}
}