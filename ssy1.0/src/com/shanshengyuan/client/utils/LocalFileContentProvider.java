package com.shanshengyuan.client.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

public class LocalFileContentProvider extends ContentProvider {
	private static final String URI_PREFIX = "content://im.zhubajie";

	public static String constructUri(String url) {
		Uri uri = Uri.parse(url);
		return uri.isAbsolute() ? url : URI_PREFIX + url;
	}

	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode)
			throws FileNotFoundException {
		File file = new File(uri.getPath());
		ParcelFileDescriptor parcel = ParcelFileDescriptor.open(file,
				ParcelFileDescriptor.MODE_READ_ONLY);
		return parcel;
	}

	@Override
	public AssetFileDescriptor openAssetFile(Uri uri, String mode)
			throws FileNotFoundException {
		String path = uri.getPath().substring(1);

		// 系统盘有没有
		String tpath = getContext().getFilesDir() + "/res/" + path;
		File file = new File(tpath);
		if (file.exists()) {
			Uri turi = Uri.parse(URI_PREFIX + tpath);
			return super.openAssetFile(turi, mode);
		}

		return super.openAssetFile(uri, mode);
	}

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public int delete(Uri uri, String s, String[] as) {
		throw new UnsupportedOperationException(
				"Not supported by this provider");
	}

	@Override
	public String getType(Uri uri) {
		throw new UnsupportedOperationException(
				"Not supported by this provider");
	}

	@Override
	public Uri insert(Uri uri, ContentValues contentvalues) {
		throw new UnsupportedOperationException(
				"Not supported by this provider");
	}

	@Override
	public Cursor query(Uri uri, String[] as, String s, String[] as1, String s1) {
		throw new UnsupportedOperationException(
				"Not supported by this provider");
	}

	@Override
	public int update(Uri uri, ContentValues contentvalues, String s,
			String[] as) {
		throw new UnsupportedOperationException(
				"Not supported by this provider");
	}

}