package com.sststore.client.utils;

import com.sststore.client.Config;

public class Log {

	public static final boolean IS_DUG = Config.DEBUG;

	public static final void e(String tag, String msg) {
		if (IS_DUG)
			android.util.Log.e(tag, msg);
	}

	public static final void e(String tag, String msg, Throwable e) {
		if (IS_DUG)
			android.util.Log.e(tag, msg, e);
	}

	public static final void v(String tag, String msg) {
		if (IS_DUG)
			android.util.Log.v(tag, msg);
	}

	public static final void v(String tag, String msg, Throwable e) {
		if (IS_DUG)
			android.util.Log.v(tag, msg, e);
	}

	public static final void i(String tag, String msg) {
		if (IS_DUG)
			android.util.Log.i(tag, msg);
	}

	public static final void i(String tag, String msg, Throwable e) {
		if (IS_DUG)
			android.util.Log.i(tag, msg, e);
	}

	public static final void d(String tag, String msg) {
		if (IS_DUG)
			android.util.Log.d(tag, msg);
	}

	public static final void d(String tag, String msg, Throwable e) {
		if (IS_DUG)
			android.util.Log.d(tag, msg, e);
	}

	public static final void w(String tag, String msg) {
		if (IS_DUG)
			android.util.Log.w(tag, msg);
	}

	public static final void w(String tag, String msg, Throwable e) {
		if (IS_DUG)
			android.util.Log.w(tag, msg, e);
	}
}
