package com.shanshengyuan.client.data.field;
/**
 * 
 * @author lihao
 *
 */
public interface Field {
	
	public static class Status{
		
		public final static int DELETED_YES = 1;
		public final static int DELETED_NO = 0;	
		
		public final static int HIDDEN_YES = 1;
		public final static int HIDDEN_NO = 0;

		public static final int SYSTEM_TYPE = 0;
		public static final int CUSTOM_TYPE = 1;
		
		public static final int Remind_ON = 1;
		public static final int Remind_OFF = 0;
		
		public static final int DEFAULT_NATIVE = 1;
		
	}
	
	String name();
	int index();
	String type();
}
