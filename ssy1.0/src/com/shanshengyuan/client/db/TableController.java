/**
 * TableController.java
 * com.shanshengyuan.client.db
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2014-1-7 		lihao
 *
 *  Copyright (c) 2014 zhubajie, TNT All Rights Reserved.
*/

package com.shanshengyuan.client.db;

import com.shanshengyuan.client.data.field.Field;
import com.shanshengyuan.client.utils.Log;

import android.database.sqlite.SQLiteDatabase;

/**
 * 集成一些简单的数据库操作类
 * @author   lihao
 * @version  
 * @since    Ver 2.0.0
 * @Date	 2014-1-7
 *
 * @see 	 
 * 
 */
public class TableController {

	public static String tag = TableController.class.getSimpleName();

	private SQLiteDatabase db = null;
	StringBuilder sql = null;

	// 数据
	String tableName = null;
	
	public void add(Table... table) {

		for (Table t : table) {

			sql = new StringBuilder();
			StringBuilder values = new StringBuilder();
			boolean first = true;

			sql.append("insert into ").append(t.tableName()).append("(");

			Field temp = null;
			for (int i = 0; i < t.fields().length; i++) {
				temp = t.fields()[i];
				if (!first) {
					sql.append(",");
					values.append(",");
				}
				sql.append(temp.name());
				values.append("'" + t.mMap.get(temp.name()) + "'");
				first = false;
			}

			sql.append(")").append(" values(").append(values.toString())
					.append(")");

			Log.i("SQLITE_ADD", sql.toString());

//			db.execSQL(sql.toString());
		}
	}
	
}

