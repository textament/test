/**
 * YoukuUrlForMP4.java
 * com.zhubajie.client.utils.Log
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-12-13 		lihao
 *
 *  Copyright (c) 2013 zhubajie, TNT All Rights Reserved.
 */

package com.shanshengyuan.client.utils;

import java.sql.Date;
import java.util.Random;

import android.text.method.DateTimeKeyListener;

/**
 * ClassName:YoukuUrlForMP4 Function: TODO ADD FUNCTION Reason: TODO ADD REASON
 * 
 * @author lihao
 * @version
 * @since Ver 2.0.0
 * @Date 2013-12-13
 * 
 * @see
 * 
 */
public class YoukuUrlForMP4 {

	public static final String tagId = "XNjQwMjM0NDY4";

	public static String genSid() {
		int i1 = (int) (1000 + Math.floor((double) (new Random().nextInt(999))));
		int i2 = (int) (1000 + Math.floor((double) (new Random().nextInt(9000))));
		return System.currentTimeMillis() + "" + i1 + "" + i2;
	}

	public static String getFileID(String fileid, double seed) {
		String mixed = getFileIDMixString(seed);
		String[] ids = fileid.split("\\*");
		StringBuilder realId = new StringBuilder();
		int idx;
		for (int i = 0; i < ids.length; i++) {
			idx = Integer.parseInt(ids[i]);
			realId.append(mixed.charAt(idx));
		}
		return realId.toString();
	}
	
	private static String getFileIDMixString(double seed) {
		StringBuilder mixed = new StringBuilder();
		StringBuilder source = new StringBuilder(
				"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ/\\:._-1234567890");
		int index, len = source.length();
		for (int i = 0; i < len; ++i) {
			seed = (seed * 211 + 30031) % 65536;
			index = (int) Math.floor(seed / 65536 * source.length());
			mixed.append(source.charAt(index));
			source.deleteCharAt(index);
		}
		return mixed.toString();
	}

	public static String getKey(String key1, String key2) {
		int key = Long.valueOf(key1, 16).intValue();
		Log.i("tag", key + "");
		key ^= 0xA55AA5A5;
		return key2 + Long.toHexString(key);
	}
}
