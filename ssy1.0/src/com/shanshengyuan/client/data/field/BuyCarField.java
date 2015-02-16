package com.shanshengyuan.client.data.field;

public enum BuyCarField implements Field {
	_id( "INTEGER NOT NULL primary key" ),
	shopId("STRING"),
	shopImg("STRING"),
	shopName("STRING"),
	shopContent("STRING"),
	shopPrice("STRING"),
	shopDishType("STRING"),
	shopNum("STRING")
	;
	
	private String type = null;

	private BuyCarField() {
		this("TEXT");
	}

	private BuyCarField(String type) {
		this.type = type;
	}

	public int index() {
		return this.ordinal();
	}

	public String type() {
		return type;
	}

}
