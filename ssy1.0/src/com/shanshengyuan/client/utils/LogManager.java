package com.shanshengyuan.client.utils;

import java.util.ArrayList;

public class LogManager {
	private static LogManager _me;
	private ArrayList<String> _logList = new ArrayList<String>();
	private boolean isDoWeb;
	private boolean isRecord;

	public boolean isRecord() {
		return isRecord;
	}

	public void setRecord(boolean isRecord) {
		this.isRecord = isRecord;
	}

	public ArrayList<String> get_logList() {
		return _logList;
	}

	public void set_logList(ArrayList<String> _logList) {
		this._logList = _logList;
	}

	public boolean isDoWeb() {
		return isDoWeb;
	}

	public void setDoWeb(boolean isDoWeb) {
		this.isDoWeb = isDoWeb;
	}

	private LogManager() {
	}

	public static LogManager getInstance() {
		if (_me == null) {
			_me = new LogManager();
			_me.setRecord(true);
		}
		return _me;
	}

	public void addLog(String log) {
		if(!isRecord){
			return;
		}
		_logList.add(log);
	}

	public void insertLog(String json) {
		if(!isRecord){
			return;
		}
		int index = _logList.size() - 1;
		String string = _logList.get(index);
		_logList.set(index, string.replace(
				"[请求]",
				"==========REQUEST==========<br><font color=\"#00ffff\">"
						+ JsonFormatTool.formatJson(json, " ").replace("\n",
								"<br>")));
	}

}
