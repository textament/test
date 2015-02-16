package com.shanshengyuan.client.ziguang;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import android.util.Log;

public class HttpUtil {
	private final static String TAG="HttpUtil";
	private static final int CONN_TIMEOUT=6000;
	private static final int READ_TIMEOUT=10000;
	private static final String POST="POST";

	//同步方式，即不启动新线程，在外面启动,返回XML字符串
	public static String sendTbXmlPostMessage(final String rqurl,final Map<String,Object> params) throws Exception {
		StringBuffer sBuffer = map2StringBuffer(params);
		final byte[] buffer = sBuffer.toString().getBytes(); 
		Log.i(TAG, "rqurl---"+rqurl+",sBuffer="+sBuffer.toString());
		try {
//			if(!SystemContext.isnetopen())
//			{
//				throw new Exception("请打开网络连接！");
//			}
			URL url = new URL(rqurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			//addHttpURLConnection(conn);
			conn.setConnectTimeout(CONN_TIMEOUT);//设置连接超时
			conn.setReadTimeout(READ_TIMEOUT);//设置读取超时
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod(POST);  
			conn.setRequestProperty("Content-Length", buffer.length + "");  
			OutputStream out = conn.getOutputStream();  
			out.write(buffer);//参数
			out.close();  
			if (conn.getResponseCode() != 200) 
			{
				throw new Exception("返回错误："+conn.getResponseCode());
			}else
			{
				InputStream is = conn.getInputStream();//得到网络返回的输入流
				String result = readData(is, "utf-8");
				conn.disconnect();
				return result;
			}
		}catch (Exception e) {
			e.printStackTrace();
				throw e;
		}
	}
	//第一个参数为输入流,第二个参数为字符集编码
	public static String readData(InputStream inSream, String charsetName) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while( (len = inSream.read(buffer)) != -1 ){
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inSream.close();
		return new String(data, charsetName);
	}
	public static StringBuffer map2StringBuffer(Map<String, Object> params) {  
		StringBuffer buf = new StringBuffer("");  
		if (params != null && params.size() > 0) {
			Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();  
			while (it.hasNext()) {  
				Map.Entry<String, Object> entry = it.next();  
				buf.append(entry.getKey()).append("=").append(entry.getValue()).toString();  
				buf.append("&");  
			}
		}
		if (buf.length() > 1)  
			buf.deleteCharAt(buf.length() - 1);  
		return buf;  
	} 

}
