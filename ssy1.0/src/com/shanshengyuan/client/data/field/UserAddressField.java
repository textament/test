package com.shanshengyuan.client.data.field;

public enum UserAddressField implements Field {
	
	_id( "INTEGER NOT NULL primary key" ),
	orderName("STRING"),
	orderAddress("STRING"),
	orderPhone("STRING")
	;

	private String type = null;

	private UserAddressField() {
		this("TEXT");
	}

	private UserAddressField(String type) {
		this.type = type;
	}

	public int index() {
		return this.ordinal();
	}

	public String type() {
		return type;
	}

}
