package com.shanshengyuan.client.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.shanshengyuan.client.data.field.SearchHistoryField;
import com.shanshengyuan.client.data.field.UserAddressField;
import com.shanshengyuan.client.db.ClientDBHelper;
import com.shanshengyuan.client.db.Table;

public class UserAddress {

	public static final Table table = new Table("useraddress",UserAddressField.values());

	private long id;
	private String orderName = null;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

//	public String getHistoryName() {
//		return historyName;
//	}
//
//	public void setHistoryName(String historyName) {
//		this.historyName = historyName;
//	}

	public static SearchHistory insertNewRelease(SearchHistory release,
			ClientDBHelper dbHelper) {
		ContentValues values = new ContentValues();
		values.put(SearchHistoryField.historyName.name(),
				release.getHistoryName());
		long id = table.create(values, dbHelper);
		release.setId(id);
		return release;
	}

	public static List<SearchHistory> getRelease(String selection,
			String[] selectionArgs, ClientDBHelper dbHelper) {
		Cursor c = null;
		List<SearchHistory> categoryList = new ArrayList<SearchHistory>();
		try {
			c = table.query(selection, selectionArgs, SearchHistoryField._id
					+ " asc", dbHelper);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				SearchHistory release = cursor(c);
				categoryList.add(release);
				c.moveToNext();
			}
			return categoryList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (c != null) {
				c.close();
			}
		}
	}

	private static SearchHistory cursor(Cursor c) {
		SearchHistory release = new SearchHistory();
		release.setId(c.getLong(SearchHistoryField._id.index()));
		release.setHistoryName(c.getString(SearchHistoryField.historyName
				.index()));
		return release;
	}

	public static void deleteAll(ClientDBHelper dbHelper) {
		table.deleteAll(dbHelper);
	}
}
