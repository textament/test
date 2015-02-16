/**
 * 
 */
package com.shanshengyuan.client.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.shanshengyuan.client.BaseApplication;

/**
 * @author 工具类
 * 
 */
public class ProjectUtils {

	public static final String tag = ProjectUtils.class.getSimpleName();

	public static final String getToImageUrl(String url) {
		url = url.replace("middle", "");
		url = url.replace("large", "");
		return url;
	}

	/**
	 * 
	 * getDeviceImageUrl:根据屏幕像素确定取服务器图片的大小
	 * 
	 * @param @param url
	 * @param @return
	 * @return String
	 * @throws
	 * @author
	 * @since zhubajie　Ver 2.0.0
	 */
	public static final String getDeviceImageUrl(String url) {

		switch (BaseApplication.DENSITY) {
		case 120:
			// url = url.replace("middle", "middle");
			url = url.replace("large", "middle");
			break;
		case 160:
			// url = url.replace("lagre", "middle");
			url = url.replace("middle", "large");
			break;
		case 240:
			break;
		case 320:
			url = url.replace("middle", "large");
			break;
		default:
			break;
		}

		return url;
	}

	/**
	 * 
	 * @param @param url 保存路径
	 * @param @param filename 保存文件名
	 * @param @return
	 * @return Intent
	 * @throws
	 * @author
	 * @since zhubajie　Ver 2.0.0
	 */
	public static Intent usesCamera(String url, String filename) {
		if (ProjectUtils.sdStatus()) {
			File desDir = new File(url);
			if (!desDir.exists()) {
				desDir.mkdir();
			}
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
					BaseApplication.SD_DIR + "/" + url, filename + ".jpg")));

			return intent;
		} else {
			return null;
		}
	}

	public static Intent usesAlbum() {
		Intent intent = new Intent();
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		return intent;
	}
	
	
	
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {

	        // Return the remote address
	        if (isGooglePhotosUri(uri))
	            return uri.getLastPathSegment();

	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	public static final String getJobStatus(int i) {
		String str = null;
		switch (i) {
		case 0: {
			str = "未发布";
			break;
		}
		case 1: {
			str = "待审核";
			break;
		}
		case 2: {
			str = "投标中";
			break;
		}
		case 3: {
			str = "选标中";
			break;
		}
		case 4: {
			str = "已结束";
			break;
		}
		default: {
			str = "";
			break;
		}
		}
		return str;
	}

	public static final String getOrderFromStatus(int i) {
		String str = null;
		switch (i) {
		case 1:
			str = "已付款";
			break;
		case 2:
			str = "等待买家付款";
			break;
		case 3:
			str = "交易完成";
			break;
		case 4:
			str = "交易关闭";
			break;
		case 5:
			str = "等待选标";
			break;
		case 6:
			str = "已付款,等待审核";
			break;
		case 7:
			str = "等待卖家上传作品";
			break;
		case 8:
			str = "等待买家确认作品";
			break;
		case 9:
			str = "等待系统付款";
			break;
		case 10:
			// 当条件是10时显示打款按钮
			str = "等待确认打款";
			break;
		case 11:
			str = "等待卖家完成工作";
			break;
		case 12:
			str = "等待买家确认工作";
			break;
		case 13:
			str = "等待确认协议";
			break;
		}
		return str;
	}

	/*
	 * {/if $task.list[task].state eq 2 AND $task.list[task].endtime|time:"1"
	 * neq '已结束' AND $task.list[task].ext_time eq 0/}投标中{/elseif
	 * $task.list[task].state eq 2 AND $task.list[task].endtime|time:"1" eq
	 * '已结束'/}选标中{/elseif $task.list[task].state eq 3/}已选标{/elseif
	 * $task.list[task].state eq 3 AND ispublicity eq 1 AND $nowtime gt
	 * $task.list[task].ext_selectime AND $nowtime lt
	 * $task.list[task].ext_selectime+259200/}公示中{/elseif $task.list[task].state
	 * eq 4/}已结束{/elseif $task.list[task].state lt 3 AND
	 * $task.list[task].ext_time gt 0/}加价继续投标{//if/}
	 */

	/**
	 * 传入两个时间戳，返回时间差
	 */
	public static final String getAboutTime(long beginTime, long endTime) {

		long time = endTime - beginTime;
		// long day = time / (3600 * 24);
		// long hour = time / (3600) - day * 24;
		// long min = time / (3600) - day * 24 - hour * 60;
		String strTime = "";
		if (time < 0) {
			strTime = "穿越";
			return " " + strTime + " ";
		} else if (time < 60 * 1000) {
			strTime = "3秒前";
		} else if (time < 3600 * 1000) {
			strTime = "5分钟前";
		} else if (time < 3600 * 24 * 1000) {
			strTime = "14小时前";
		} else if (time < 3600 * 24 * 30 * 1000) {
			strTime = "2天前";
		} else {
			Date date = new Date(beginTime);
			strTime = DateFormat.getDateTimeInstance().format(date).split(" ")[0];
		}
		return " " + strTime + " ";
	}

	public static final String getLastTime(long beginTime, long endTime) {

		new Date().getTime();

		long time = endTime - beginTime;
		long day = time / (3600 * 24);
		long hour = time / (3600) - day * 24;
		String strTime = "";

		if (time < 0) {
			strTime = "已到期";
			return " " + strTime + " ";
		} else if (day > 0) {
			strTime = day + "天";
		} else if (hour > 0) {
			strTime = hour + "小时";
		}
		return " " + strTime + " ";
	}

	public static final String getEvaluate(int i) {
		String str = null;
		switch (i) {
		case 0:
			str = "差评";
			break;
		case 1:
			str = "中评";
			break;
		case 2:
			str = "好评";
			break;
		default:
			str = "";
		}
		return str;
	}

	public static boolean sdStatus() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 此方法使用到网络下载，请在线程中使用
	 * 
	 * @throws
	 * @author
	 * @since zhubajie　Ver 2.0.0
	 */
	public static Bitmap getBitmapFromURL(String strUrl) {
		InputStream is = null;
		Bitmap bitmap = null;
		URL url;
		try {
			url = new URL(strUrl);
			is = url.openStream();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (is != null) {
			bitmap = BitmapFactory.decodeStream(is);
		}

		return bitmap;
	}

	public static Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		if (bitmap != null)
			return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
		else
			return null;
	}

	public static Bitmap getimageRlease(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 100f;// 这里设置高度为800f
		float ww = 100f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		if (bitmap != null)
			return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
		else
			return null;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static Bitmap getimageRlease1(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = null;// 此时返回bm为空

		newOpts.inJustDecodeBounds = true;

		newOpts.inSampleSize = computeSampleSize(newOpts, -1, 128 * 128);
		newOpts.inJustDecodeBounds = false;
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		if (bitmap != null)
			return bitmap;
		else
			return null;
	}

	public static Bitmap getSmallBitmap(String filePath) {
		return getSmallBitmap(filePath, 0);
	}

	public static Bitmap getSmallBitmap(String filePath, int angle) {
		Bitmap bitmap = null;// 此时返回bm为空
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = computeSampleSize(options, -1, 480 * 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		bitmap = BitmapFactory.decodeFile(filePath, options);
		if (angle != 0) {
			Matrix matrix = new Matrix();
			matrix.postRotate(angle);
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
					matrix, false);
			if (newBitmap != null)
				return newBitmap;
		}
		if (bitmap != null)
			return bitmap;
		else
			return null;
	}

	private static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 90, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		Log.e("start", baos.toByteArray().length / 1024 + "options:" + options);
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		Log.e("end", baos.toByteArray().length + "options:" + options);
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片

		return bitmap;
	}

	public static File saveBitmap(String url, Bitmap bit) {
		File dir = new File(url);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File f = new File(url);
		try {
			f.createNewFile();
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			bit.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			f = null;
			e1.printStackTrace();
		}

		return f;
	}
	
	public static File saveBitmapJPG(String url, Bitmap bit) {
		File dir = new File(url);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File f = new File(url);
		try {
			f.createNewFile();
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			bit.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			f = null;
			e1.printStackTrace();
		}

		return f;
	}

	public static Bitmap getBitmapFromPath(String path) {

		File file = new File(path);
		int scale = 1;

		if (file.length() > 1024 * 1024) {

			scale = (int) (file.length() / (1024 * 1024)) + 1;
		}

		Options options = new Options();
		options.inSampleSize = scale;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(path, options);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return Bitmap.createScaledBitmap(bitmap, 100, 100, true);
	}

	public static Bitmap getLocalBitmapFromPath(String path) {

		File file = new File(path);
		// int scale = 1;
		//
		// if (file.length() > 1024 * 1024) {
		//
		// scale = (int) (file.length() / (1024 * 1024)) + 1;
		// }
		//
		Options options = new Options();
		options.inSampleSize = 8;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(path, options);
			// bitmap.recycle();
		} catch (Exception e) {
			bitmap.recycle();
			// e.printStackTrace();
		}

		return bitmap;
	}

	// 把图片转换成圆形图片
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = bitmap.getWidth() / 2;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 
	 * getCutImage: 裁剪一个bitmap
	 * 
	 * @param bitmap
	 *            需要裁剪的图
	 * @param width
	 *            裁剪后的宽度
	 * @param height
	 *            裁剪后的高度
	 * @param isProportional
	 *            是否等比例缩放
	 * @return Bitmap
	 * @throws
	 * @author
	 * @since zhubajie　Ver 2.0.0
	 */
	public static Bitmap getCutImage(Bitmap bitmap, int width, int height,
			boolean isProportional) {

		// if (1 == 1) {
		// return bitmap;
		// }
		// 查看当前可用内存

		// ActivityManager manager =
		// (ActivityManager)getSystemService(Activity.ACTIVITY_SERVICE);

		//
		// Log.e("SystemMemory", "系统剩余内存:" + (outInfo.availMem >> 10) + "k");
		// Log.e("SystemMemory", "系统是否处于低内存运行：" + outInfo.lowMemory);
		// Log.e("SystemMemory", "当系统剩余内存低于" + outInfo.threshold + "时就看成低内存运行");

		int minEdge = 0;
		float scaleWidth = 0f;
		float scaleHeight = 0f;
		int beginPixW = 0;
		int beginPixH = 0;

		if (isProportional) {
			// 确定缩放标准，以长宽值小的为准
			minEdge = Math.min(bitmap.getWidth(), bitmap.getHeight());
			// 计算缩放率，新尺寸除原始尺寸
			scaleWidth = ((float) width) / bitmap.getWidth();
			scaleHeight = ((float) height) / bitmap.getHeight();

			beginPixW = bitmap.getWidth() - minEdge;
			beginPixH = bitmap.getHeight() - minEdge;

			// 缩放图片动作
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);

			if (beginPixW < 0) {
				beginPixW = 0;
			}
			if (beginPixH < 0) {
				beginPixH = 0;
			}

			if (width >= bitmap.getWidth()) {
				width = bitmap.getWidth() - 1;
			}

			if (height >= bitmap.getHeight()) {
				height = bitmap.getHeight() - 1;
			}

			// Bitmap newBitmap = Bitmap.createBitmap(bitmap, beginPixW / 2,
			// beginPixH / 2, width - beginPixW / 2, height
			// - beginPixH / 2, matrix, true);

			Bitmap newBitmap = Bitmap.createBitmap(bitmap, beginPixW / 2,
					beginPixH / 2, bitmap.getWidth() - beginPixW / 2 - 1,
					bitmap.getHeight() - beginPixH / 2 - 1, matrix, true);

			// bitmap.recycle();

			return newBitmap;
		} else {

			// 计算缩放率，新尺寸除原始尺寸
			scaleWidth = ((float) width) / bitmap.getWidth();
			scaleHeight = ((float) height) / bitmap.getHeight();

			// 缩放图片动作
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);

			if (beginPixW < 0) {
				beginPixW = 0;
			}
			if (beginPixH < 0) {
				beginPixH = 0;
			}

			Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					(bitmap.getWidth() - 1), (bitmap.getHeight() - 1), matrix,
					true);

			// bitmap.recycle();

			return newBitmap;
		}

	}

	/**
	 * 根据传入的长宽自动缩放或者拉伸图片。当图片完全覆盖传入长宽时，截取图片显示的范围及显示范围以下的部分。 高度最大为屏幕高度。
	 * 
	 * @param bitmap
	 *            需要截取的圖片
	 * @param width
	 *            需要裁剪到的宽度
	 * @param height
	 *            需要裁减到的高度
	 */

	public static Bitmap getBitmapInParentSize(Bitmap bitmap, int width,
			int height) {

		// 根据图片宽高拉伸或者收缩图片，必须是等比

		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();

		float scale = 0;

		// 计算压缩比
		float scaleWidth = ((float) width) / bitmapWidth;
		float scaleHeight = ((float) height) / bitmapHeight;

		scale = Math.max(scaleWidth, scaleHeight);

		// 缩放图片动作
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		// 是否需要裁减
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth,
				bitmapHeight, matrix, true);

		return newBitmap;
	}

	public static boolean saveImageToFile(Bitmap bitmap, String filePath) {

		File cover = new File(filePath);
		FileOutputStream fos = null;
		try {

			if (cover.exists()) {
				cover.delete();
			}

			if (!cover.createNewFile()) {
				return false;
			}

			fos = new FileOutputStream(cover);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 
	 * getDifferenceDate:日期相减时间差 格式为（X天X小时X分X秒）
	 * 
	 * @param nowDate
	 *            当前时间(时间戳) endDate 发布结束时间（时间戳）
	 * @return String 返回时间差
	 * @throws
	 * @author lihao
	 * @since zhubajie　Ver 2.0.0
	 */
	public static String getDifferenceDate(String nowDate, String endDate) {
		// 日期相减算出秒的算法
		String date = "";
		// 当前时间
		Date date1;
		// 发布结束时间
		Date date2;
		try {
			date1 = new Date(Long.parseLong(nowDate));
			date2 = new Date(Long.parseLong(endDate));

			long l = date1.getTime() - date2.getTime() > 0 ? date1.getTime()
					- date2.getTime() : date2.getTime() - date1.getTime();

			// System.out.println(l/1000+"秒");

			// 日期相减得到相差的日期
			long day = (date1.getTime() - date2.getTime())
					/ (24 * 60 * 60 * 1000) > 0 ? (date1.getTime() - date2
					.getTime()) / (24 * 60 * 60 * 1000)
					: (date2.getTime() - date1.getTime())
							/ (24 * 60 * 60 * 1000);
			// System.out.println("相差的日期: " +day);
			long hour = (date1.getTime() - date2.getTime()) / (60 * 60 * 1000) > 0 ? (date1
					.getTime() - date2.getTime()) / (60 * 60 * 1000)
					: (date2.getTime() - date1.getTime()) / (60 * 60 * 1000);
			long hour1 = (hour - day * 24);
			// System.out.println("相差的小时: " +(hour1-day*24));
			long min = (date1.getTime() - date2.getTime()) / (60 * 1000) > 0 ? (date1
					.getTime() - date2.getTime()) / (60 * 1000)
					: (date2.getTime() - date1.getTime()) / (60 * 1000);
			long min1 = (min - hour * 60);
			// System.out.println("相差的分钟: " +(min - hour1*60));
			long miao = (date1.getTime() - date2.getTime()) / (1000) > 0 ? (date1
					.getTime() - date2.getTime()) / (1000)
					: (date2.getTime() - date1.getTime()) / (1000);
			long miao1 = (miao - min * 60);
			// System.out.println("相差的秒: " +(miao - min*60));
			date = day + "天" + hour1 + "小时" + min1 + "分" + miao1 + "秒";
			return date;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public static final String DATE_TIME = "yyyy-MM-dd";

	/**
	 * 
	 * dateForMat:时间格式化
	 * 
	 * @param @param time 时间戳
	 * @param @return
	 * @return String 格式化后的时间
	 * @throws
	 * @author lihao
	 * @since zhubajie　Ver 2.0.0
	 */
	public static String dateForMat(String time) {
		long aa = Long.parseLong(time) * 1000;
		SimpleDateFormat dateformat1 = new SimpleDateFormat(DATE_TIME);
		String format = dateformat1.format(new Date(aa));
		return format;
	}

	/**
	 * 组合两张图（头像和图标）
	 * 
	 * @param @param head
	 * @param @param attach
	 * @param @param place
	 * @param @return
	 * @return Bitmap
	 * @throws
	 * @author
	 * @since zhubajie　Ver 2.0.0
	 */
	public static Bitmap assembleBitmap(Bitmap head, Bitmap attach, int place) {

		Canvas canvas = new Canvas(head);

		Paint paint = new Paint();

		int width = head.getWidth();
		int height = head.getHeight();

		if (attach != null) {

			switch (place) {
			case 0:
				canvas.drawBitmap(attach, 0, 0, paint);
				break;
			case 1:
				canvas.drawBitmap(attach, width / 2 - attach.getWidth() / 2, 0,
						paint);
				break;
			case 2:
				canvas.drawBitmap(attach, width - attach.getWidth(), 0, paint);
				break;
			case 3:
				canvas.drawBitmap(attach, 0, height / 2 - attach.getHeight()
						/ 2, paint);
				break;
			case 4:
				canvas.drawBitmap(attach, width / 2 - attach.getWidth() / 2,
						height / 2 - attach.getHeight() / 2, paint);
				break;
			case 5:
				canvas.drawBitmap(attach, width - attach.getWidth(), height / 2
						- attach.getHeight() / 2, paint);
				break;
			case 6:
				canvas.drawBitmap(attach, 0, height - 0, paint);
				break;
			case 7:
				canvas.drawBitmap(attach, width / 2 - attach.getWidth() / 2,
						height - attach.getHeight(), paint);
				break;
			case 8:

				canvas.drawBitmap(attach, width - attach.getWidth(), height
						- attach.getHeight(), paint);
				break;
			}
		}
		return head;
	}

	/**
	 * 
	 * seek对应的半径
	 * 
	 * @param @param seek 0:100米; 1:500米; 2:1000米; 3:2000米; 4:5000米
	 * @param @return
	 * @return String
	 * @throws
	 * @author
	 * @since zhubajie　Ver 2.0.0
	 */
	public static String getRadius(int seek) {

		switch (seek) {
		case 0:
			return "100";
		case 1:
			return "500";
		case 2:
			return "1000";
		case 3:
			return "2000";
		case 4:
			return "5000";
		default:
			return "100";
		}
	}

	/**
	 * 
	 * 把drawable 转换成 bitmap
	 * 
	 * @param @param drawable
	 * @param @return
	 * @return Bitmap
	 * @throws
	 * @author
	 * @since zhubajie　Ver 2.0.0
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap
					.createBitmap(
							drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight(),
							drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
									: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * 把drawable 转换成 bitmap
	 * 
	 * @param @param drawable
	 * @param @param width 转换出来的宽度
	 * @param @param height 转换出来的高度
	 * @param @return
	 * @return Bitmap
	 * @throws
	 * @author
	 * @since zhubajie　Ver 2.0.0
	 */
	public static Bitmap drawableToBitmap(Drawable drawable, int width,
			int height) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {

			if (width == 0) {
				width = drawable.getIntrinsicWidth();
			}

			if (height == 0) {
				height = drawable.getIntrinsicHeight();
			}

			Bitmap bitmap = Bitmap
					.createBitmap(
							width,
							height,
							drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
									: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);

			drawable.setBounds(0, 0, width, height);
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}

	/**
	 * 比较两个值的大小<br/>
	 * <ul>
	 * </ul>
	 * <strong>关于比较的结果</strong>
	 * <ul>
	 * <li>v1大于v2返回1</li>
	 * <li>v1等于v2返回0</li>
	 * <li>v1小于v2返回-1</li>
	 * </ul>
	 * <strong>关于比较的规则</strong>
	 * <ul>
	 * <li>若v1为null，v2为null，则相等</li>
	 * <li>若v1为null，v2不为null，则v1小于v2</li>
	 * <li>若v1不为null，v2为null，则v1大于v2</li>
	 * <li>若v1、v2均不为null，则利用v1的{@link Comparable#compareTo(Object)}判断，参数为v2</li>
	 * </ul>
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <V> int compare(V v1, V v2) {
		return v1 == null ? (v2 == null ? 0 : -1) : (v2 == null ? 1
				: ((Comparable) v1).compareTo(v2));
	}

	/**
	 * 比较两个对象是否相等
	 * 
	 * @param actual
	 * @param expected
	 * @return <ul>
	 *         <li>若两个对象都为null，则返回true</li>
	 *         <li>若{@code actual}对象不为null，则调用{@code actual}对象相应的
	 *         {@link Object#equals(Object)}函数进行判断，返回判断结果</li>
	 *         </ul>
	 * @see <ul>
	 *      <li>对于基本类实现了{@link Object#equals(Object)}
	 *      的话都会先判断类型是否匹配，类型不匹配返回false，可参考{@link String#equals(Object)}</li>
	 *      <li>关于如何利用此函数比较自定义对象可下载源代码，参考测试代码中的
	 *      {@link ObjectUtilsTest#testIsEquals()}</li>
	 *      </ul>
	 */
	public static boolean isEquals(Object actual, Object expected) {
		return actual == null ? expected == null : actual.equals(expected);
	}

	private static void findFileInDir(File file) {

		if (file.exists()) {

			if (file.isDirectory()) {
				File[] childrenFiles = file.listFiles();

				for (File child : childrenFiles) {
					findFileInDir(child);
				}

			} else {
				file.delete();
			}
		}

	}

	/**
	 * 等级转换为中文
	 */
	public static String convertNumber(int index) {
		String[] arrNum = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九",
				"十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九",
				"二十", "二十一", "二十二", "二十三", "二十四", "二十五", "二十六", "二十七", "二十八",
				"二十九", "三十", "三十一", "三十二" }; // 大写数字
		return arrNum[index];
	}
}
