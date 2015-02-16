package com.shanshengyuan.client.db;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.shanshengyuan.client.data.field.Field;

/**
 * @author Jelly
 */
public class Table {

    private final String tableName;
    private final Field[] fields;
    private String[] columns = null;
    protected Map<String, String> mMap = null;
    
    public Table(String tableName, Field[] fields) {
        this.tableName = tableName;
        this.fields = fields;
        mMap = new HashMap<String, String>();
    }

    public Field[] fields() {
        return fields;
    }

    public String tableName() {
        return tableName;
    }

    public String sql2Drop() {
        return "DROP TABLE IF EXISTS " + tableName() + ";";
    }

    public String sql2Create() {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE ").append(tableName()).append(" (");
        boolean first = true;
        for (Field t : fields) {
            if (!first) {
                sb.append(",");
            }
            first = false;
            sb.append(t.name()).append(" ").append(t.type());
        }
        return sb.append(")").toString();
    }

    public String[] columns() {
        if (columns == null) {
            columns = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                columns[i] = fields[i].name();
            }
        }
        return columns;
    }

    public void dropTable(SQLiteDatabase db) {
        db.execSQL(sql2Drop());
    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL(sql2Create());
    }

    public long create(ContentValues values, ClientDBHelper dbHelper) {
        return dbHelper.getWritableDatabase().insert(tableName, null, values);
    }

    public int update(ContentValues values, String whereClause, String[] whereArgs,
            ClientDBHelper dbHelper) {
        return dbHelper.getWritableDatabase().update(tableName, values, whereClause, whereArgs);
    }

    public Cursor query(String selection, String[] selectionArgs, Field orderBy,
            ClientDBHelper dbHelper) {
        return dbHelper.getWritableDatabase().query(tableName(), columns(), selection,
                selectionArgs, null, null, orderBy == null ? null : orderBy.name());
    }

    public Cursor query(String selection, String[] selectionArgs, String orderBy,
            ClientDBHelper dbHelper) {
        return dbHelper.getWritableDatabase().query(tableName(), columns(), selection,
                selectionArgs, null, null, orderBy == null ? null : orderBy);
    }

    public Cursor query(String selection, String[] selectionArgs, Field orderBy,
            ClientDBHelper dbHelper, String limit) {
        return dbHelper.getWritableDatabase().query(tableName(), columns(), selection,
                selectionArgs, null, null, orderBy == null ? null : orderBy.name(), limit);
    }

    public void deleteBy(Field aField, String value, ClientDBHelper dbHelper) {
        String sql = "DELETE from " + tableName() + " where " + aField.name() + " = '" + value
                + "'";
        dbHelper.getWritableDatabase().execSQL(sql);
    }

    public void deleteById(Field idField, Long id, ClientDBHelper dbHelper) {
        String sql = "DELETE from " + tableName() + " where " + idField.name() + " = " + id;
        dbHelper.getWritableDatabase().execSQL(sql);
    }

    public void deleteByIds(Field idField, String ids, ClientDBHelper dbHelper) {
        String sql = "DELETE from " + tableName() + " where " + idField.name() + " in (" + ids
                + ")";
        dbHelper.getWritableDatabase().execSQL(sql);
    }

    public void deleteAll(ClientDBHelper dbHelper) {
        try {
            String sql = "DELETE FROM " + tableName();
            dbHelper.getWritableDatabase().execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
