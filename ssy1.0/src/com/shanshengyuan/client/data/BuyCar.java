/**
 * 
 */
package com.shanshengyuan.client.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.shanshengyuan.client.data.field.BuyCarField;
import com.shanshengyuan.client.db.ClientDBHelper;
import com.shanshengyuan.client.db.Table;

/**
 * @author lihao
 *
 */
public class BuyCar {

	
	public static final Table table = new Table("buycar",BuyCarField.values());
	
	private long id;
	private String shopId = null;
	private String shopImg = null;
	private String shopName = null;
	private String shopContent = null;
	private String shopPrice = null;
	private String shopDishType = null;
	private String shopNum = null;
	
	
	
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getShopImg() {
		return shopImg;
	}
	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopContent() {
		return shopContent;
	}
	public void setShopContent(String shopContent) {
		this.shopContent = shopContent;
	}
	public String getShopPrice() {
		return shopPrice;
	}
	public void setShopPrice(String shopPrice) {
		this.shopPrice = shopPrice;
	}
	public String getShopDishType() {
		return shopDishType;
	}
	public void setShopDishType(String shopDishType) {
		this.shopDishType = shopDishType;
	}
	public String getShopNum() {
		return shopNum;
	}
	public void setShopNum(String shopNum) {
		this.shopNum = shopNum;
	}
	
  	public static BuyCar insertBuyCar(BuyCar release, ClientDBHelper dbHelper) {
		ContentValues values = new ContentValues();
		values.put(BuyCarField.shopImg.name(), release.getShopImg());
		values.put(BuyCarField.shopName.name(), release.getShopName());
		values.put(BuyCarField.shopContent.name(), release.getShopContent());
		values.put(BuyCarField.shopPrice.name(), release.getShopPrice());
		values.put(BuyCarField.shopDishType.name(), release.getShopDishType());
		values.put(BuyCarField.shopNum.name(), release.getShopNum());
		values.put(BuyCarField.shopId.name(), release.getShopId());
		long id = table.create(values, dbHelper);
		release.setId(id);
		return release;
	}
  	
  	public static void updateBuyCar(BuyCar release, ClientDBHelper dbHelper){
  		ContentValues values = new ContentValues();
		values.put(BuyCarField.shopNum.name(), release.getShopNum());
		String whereClause = " shopId=? ";
		String[] whereArgs = {release.getShopId()};
		table.update(values, whereClause, whereArgs, dbHelper);
  	}
  	
  	public static void updateBuyCarPrice(BuyCar release, ClientDBHelper dbHelper){
  		ContentValues values = new ContentValues();
		values.put(BuyCarField.shopPrice.name(), release.getShopPrice());
		String whereClause = " shopId=? ";
		String[] whereArgs = {release.getShopId()};
		table.update(values, whereClause, whereArgs, dbHelper);
  	}
	
	public static List<BuyCar> getBuyCar(String selection,
			String[] selectionArgs, ClientDBHelper dbHelper) {
		Cursor c = null;
		List<BuyCar> categoryList = new ArrayList<BuyCar>();
		try {
			c = table.query(selection, selectionArgs, BuyCarField._id +" asc",
					dbHelper);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				BuyCar release = cursor(c);
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
	
	private static BuyCar cursor(Cursor c) {
		BuyCar release = new BuyCar();
		release.setId(c.getLong(BuyCarField._id.index()));
		release.setShopImg(c.getString(BuyCarField.shopImg.index()));
		release.setShopName(c.getString(BuyCarField.shopName.index()));
		release.setShopContent(c.getString(BuyCarField.shopContent.index()));
		release.setShopPrice(c.getString(BuyCarField.shopPrice.index()));
		release.setShopDishType(c.getString(BuyCarField.shopDishType.index()));
		release.setShopNum(c.getString(BuyCarField.shopNum.index()));
		release.setShopId(c.getString(BuyCarField.shopId.index()));
		return release;
	}
	
	public static void deleteBuyCar(String id,ClientDBHelper dbHelper){
		table.deleteByIds(BuyCarField.shopId,id, dbHelper);
	}
	
	public static void deleteAll(ClientDBHelper dbHelper) {
		table.deleteAll(dbHelper);
	}
}
