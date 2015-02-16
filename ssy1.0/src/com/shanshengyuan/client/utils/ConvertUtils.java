package com.shanshengyuan.client.utils;

import java.text.DecimalFormat;

import android.content.Context;


public class ConvertUtils {

	private static DecimalFormat unitFormat = new DecimalFormat("0.00");

	public static class FeedUnitType {
		public static final int FEED_ML = 1;
		public static final int FEED_US = 2;
		public static final int FEED_UK = 3;
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static String[] lengthMetricToImperial(String cm) {
		double getcm = 0;
		if (cm.trim().length() != 0) {
			getcm = Double.parseDouble(cm);
		}
		String[] imperial = new String[2];
		imperial[0] = String.valueOf((int) (getcm / 12));
		imperial[1] = String.valueOf(unitFormat
				.format((double) ((getcm % 12) * 0.3937)));
		return imperial;
	}

	public static String lengthImperialToMetric(String feet, String inches) {
		double dfeet = 0;
		double dinches = 0;
		if (feet.trim().length() != 0) {
			dfeet = Double.parseDouble(feet) * 12;
		}
		if (inches.trim().length() != 0) {
			dinches = Double.parseDouble(inches) / 0.3937;
		}
		return String.valueOf(unitFormat.format((double) (dfeet + dinches)));
	}

	public static String[] weightMetricToImperial(String kg) {
		double getkg = 0;
		if (kg.trim().length() != 0) {
			getkg = Double.parseDouble(kg);
		}
		String[] imperial = new String[2];
		imperial[0] = String.valueOf((int) (getkg / 16));
		imperial[1] = String.valueOf(unitFormat
				.format((double) ((getkg % 16) * 35.27)));
		return imperial;
	}

	public static String weightImperialToMetric(String lbs, String oz) {
		double dlbs = 0;
		double doz = 0;
		if (lbs.trim().length() != 0) {
			dlbs = Double.parseDouble(lbs) * 16;
		}
		if (oz.trim().length() != 0) {
			doz = Double.parseDouble(oz) / 35.27;
		}
		return String.valueOf(unitFormat.format((double) ((dlbs + doz))));
	}

}
