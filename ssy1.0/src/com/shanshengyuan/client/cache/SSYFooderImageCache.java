package com.shanshengyuan.client.cache;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.shanshengyuan.client.R;

public class SSYFooderImageCache {

	private static SSYFooderImageCache instance;
	
    DisplayImageOptions options = null;
	
	
	public static SSYFooderImageCache getInstance() {
		if (instance == null) {
			instance = new SSYFooderImageCache();
		}
		return instance;
	}
	
	public DisplayImageOptions initOption(){
		
		options = new DisplayImageOptions.Builder()
		      //  .showImageOnLoading(R.drawable.defaule_img)            //加载图片时的图片
		        .showImageForEmptyUri(R.drawable.defaule_img)         //没有图片资源时的默认图片
		        .showImageOnFail(R.drawable.defaule_img)              //加载失败时的图片
		        .cacheInMemory(true)                               //启用内存缓存
		        .cacheOnDisk(true)                                 //启用外存缓存
		        .considerExifParams(true)                          //启用EXIF和JPEG图像格式
		     //   .displayer(new RoundedBitmapDisplayer(20))         //设置显示风格这里是圆角矩形
		        .build();
		return options;
	}
}
