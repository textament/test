package com.shanshengyuan.client.data.field;

public enum SearchHistoryField implements Field {
	_id("INTEGER NOT NULL primary key"), historyName("STRING");

	private String type = null;

	private SearchHistoryField() {
		this("TEXT");
	}

	private SearchHistoryField(String type) {
		this.type = type;
	}

	public int index() {
		return this.ordinal();
	}

	public String type() {
		return type;
	}
}
