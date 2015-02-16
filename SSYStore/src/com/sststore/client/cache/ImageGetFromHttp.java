package com.sststore.client.cache;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

import com.sststore.client.BaseApplication;

public class ImageGetFromHttp {

	public static Bitmap downloadBitmap(String url, boolean isList) {
		Bitmap bitmap = null;
		try {
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setConnectTimeout(30000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream is = conn.getInputStream();
				Options options = new Options();
				options.inPurgeable = true;
				options.inInputShareable = true;
				bitmap = BitmapFactory.decodeStream(is, null, options);
				is.close();
				conn.disconnect();
				if (bitmap != null) {
					Bitmap scaleBitmap = scaleBitmap(bitmap, isList);
					return scaleBitmap;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap scaleBitmap(Bitmap img, boolean isList) {
		int width = img.getWidth();
		int height = img.getHeight();
		int screenWidth = BaseApplication.WIDTH;
		if (width <= 200) {
			return img;
		}
		int newWidth = 0;
		int newHeight = height;
		newWidth = screenWidth;
		BitmapDrawable drawable = new BitmapDrawable(img);
		newHeight = (int) (screenWidth * 1.0f * drawable.getIntrinsicHeight() / drawable
				.getIntrinsicWidth());
		if (isList) {
			newHeight = (int) (screenWidth * 0.618f);
		}
		Bitmap result = Bitmap.createBitmap(newWidth, newHeight,
				Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(result);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));

		canvas.drawBitmap(img, null, new Rect(0, 0, newWidth, newHeight), null);
		return result;
	}

}
