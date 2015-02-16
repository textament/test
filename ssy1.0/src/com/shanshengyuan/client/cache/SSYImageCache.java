package com.shanshengyuan.client.cache;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.shanshengyuan.client.utils.Log;

public class SSYImageCache {
	private static final int THREAD_COUNT = 1;
	// 保存正在下载的图片URL集合，避免重复下载用
	private static HashSet<String> sDownloadingSet;
	// 线程池相关
	private static ExecutorService sExecutorService;
	// 通知UI线程图片获取ok时使用
	private static Handler handler;
	private static SSYImageCache instance;
	private static ImageMemoryCache memoryCache;
	private static ImageFileCache fileCache;
	private static HashMap<String, ImageView> sameMap;

	public static SSYImageCache getInstance() {
		if (instance == null) {
			instance = new SSYImageCache();
		}
		return instance;
	}

	/**
	 * 异步加载图片完毕的回调接口
	 */
	public interface ImageCallback {
		/**
		 * 回调函数
		 * 
		 * @param bitmap
		 *            : may be null!
		 * @param imageUrl
		 */
		public void onImageLoaded(Bitmap bitmap, String imageUrl);
	}

	static {
		sDownloadingSet = new HashSet<String>();
	}

	public void init(Context context) {
		memoryCache = new ImageMemoryCache(context);
		fileCache = new ImageFileCache();
		handler = new Handler();
		sameMap = new HashMap<String, ImageView>();
		startThreadPoolIfNecessary();

	}

	/** 开启线程池 */
	public static void startThreadPoolIfNecessary() {
		if (sExecutorService == null || sExecutorService.isShutdown()
				|| sExecutorService.isTerminated()) {
			sExecutorService = Executors.newFixedThreadPool(THREAD_COUNT);
			// sExecutorService = Executors.newSingleThreadExecutor();
		}
	}

	/**
	 * 异步下载图片，并缓存到memory中
	 * 
	 * @param url
	 * @param callback
	 *            see ImageCallback interface
	 */
	public void downloadImage(HashMap<String, ImageView> mfaceMap,
			ImageView view, final String url, final ImageCallback callback) {
		downloadImage(mfaceMap, view, url, true, callback);
	}

	/**
	 * 
	 * @param url
	 * @param cache2Memory
	 *            是否缓存至memory中
	 * @param callback
	 */
	public void downloadImage(final HashMap<String, ImageView> mfaceMap,
			final ImageView view, final String url, final boolean saveFile,
			final boolean isList, final ImageCallback callback) {
		if (url == null) {
			return;
		}
		final String key = url + "";
		view.setTag(url);
		// 从内存缓存中获取图片
		Bitmap result = memoryCache.getBitmapFromCache(url);
		if (result == null || result.isRecycled()) {
			// 文件缓存中获取
			result = fileCache.getImage(url);
			if (result == null) {
//				if (sDownloadingSet.contains(url)) {
//					sameMap.put(url + "|" + key, view);
//					Log.i("ZBJImageCache" + url, "###该图片正在下载，不能重复下载！");
//					return;
//				}
				// 从网络端下载图片
				sDownloadingSet.add(url);
				sExecutorService.submit(new Runnable() {
					@Override
					public void run() {
						final Bitmap [] bitmaps=new Bitmap[1];
						int i=0;
						do {
							bitmaps[0] = ImageGetFromHttp.downloadBitmap(
									url, isList);
							i++;
							Log.i("download img:" + url, "retry:"+i);
						} while (bitmaps[0]==null&&i<=3);
						
						if (bitmaps[0]!= null) {
							if (saveFile) {
								fileCache.saveBitmap(bitmaps[0], url);
							}
							memoryCache.addBitmapToCache(url, bitmaps[0]);
						}
						sDownloadingSet.remove(url);
						handler.post(new Runnable() {
							@Override
							public void run() {
								if (view.getTag().equals(key)) {
									view.setImageBitmap(bitmaps[0]);
									view.setBackgroundDrawable(null);
								}
								Iterator<String> it = sameMap.keySet()
										.iterator();
								while (it.hasNext()) {
									String next = it.next();
									if (next.contains(url)) {
										sameMap.get(next)
												.setImageBitmap(bitmaps[0]);
										it.remove();
									}
								}
							}
						});
					}
				}); 
			} else {
				if (view.getTag().equals(key)) {
					view.setImageBitmap(result);
					view.setBackgroundDrawable(null);
				}
				// 添加到内存缓存
				memoryCache.addBitmapToCache(url, result);
			}
		} else {
			if (view.getTag().equals(key)) {
				view.setImageBitmap(result);
				view.setBackgroundDrawable(null);
			}
		}

	}

	public void downloadImage(HashMap<String, ImageView> mfaceMap,
			ImageView view, final String url, final boolean isList,
			final ImageCallback callback) {
		downloadImage(mfaceMap, view, url, true, isList, callback);
	}

	public void clearAll() {
		String path = fileCache.getDirectory();
		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			File[] listFiles = file.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				listFiles[i].delete();
			}
			file.delete();
		}
	}
}
