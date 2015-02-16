package com.shanshengyuan.client.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.Config;
import com.shanshengyuan.client.ErrorResponse;
import com.ta.util.http.AsyncHttpClient;

/**
 * @author lihao
 * @date 2012-5-11
 */
public class NetworkHelper {
	private static HttpClient client = null;
	private static final int REQUEST_TIMEOUT = 30 * 1000;
	private static final String TAG = NetworkHelper.class.getSimpleName();
	public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
	static {
		asyncHttpClient.setTimeout(20000);
	}

	private static void createHttpClient() {
		client = new DefaultHttpClient();
		final HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, REQUEST_TIMEOUT);
		ConnManagerParams.setTimeout(params, REQUEST_TIMEOUT);
	}

	private static String appendUrl(String urlString) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Config.GATEWAY_URL).append("/");
		stringBuilder.append(urlString);

		return stringBuilder.toString();
	}

	// public synchronized static BaseResponse doSNSPost(String urlString,
	// String data, Type t) {
	//
	// if (client == null) {
	// createHttpClient();
	// }
	// try {
	// String url = urlString;
	// HttpPost post = new HttpPost(url);
	// post.setHeader("Content-Type", "application/json; charset=utf-8");
	//
	// HttpEntity entity = new ByteArrayEntity(data.getBytes());
	// post.setEntity(entity);
	// HttpResponse response;
	//
	// Log.d(TAG + "====>", data);
	// response = client.execute(post);
	//
	// StatusLine sl = response.getStatusLine();
	// Log.d(TAG, "URL:" + url);
	// Log.d(TAG, "CODE:" + sl.getStatusCode());
	//
	// if (sl.getStatusCode() == 200) {
	// if (response.getEntity() != null) {
	// String responseBody = EntityUtils.toString(response.getEntity(),
	// "UTF-8");
	// JSONObject obj = new JSONObject(responseBody);
	// int isBind = obj.optInt("isbind");
	// IsBindOpenIdResponse r = null;
	// if (isBind == 0) {
	// r = new IsBindOpenIdResponse();
	// r.setResult(0);
	// r.setIsbind("0");
	// return r;
	// } else {
	// r = (IsBindOpenIdResponse) JSONHelper.jsonToObj(responseBody,
	// IsBindOpenIdResponse.class);
	// return r;
	// }
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e(NetworkHelper.class.getSimpleName(), e.getMessage());
	// }
	//
	// return new ErrorResponse("社会化账号登录失败");
	// }
	public static String executeUrl(String urlString) {
		String url;
		if (urlString.contains("113.31.22.234:16383")) {
			url = urlString;
		} else if (urlString.contains("113.31.20.67:16383")) {
			url = urlString;
		} else if (urlString.contains("http://social")) {
			url = urlString;
		} else if (urlString.contains("http://i.api.zhubajie.com")) {
			url = urlString;
		} else if (urlString.contains("mt.zhubajie.la/backend/songtao")) {
			url = urlString;
		} else {
			url = appendUrl(urlString);
		}
		return url;
	}

	public static BaseResponse doObject(String url, String responseBody, Type t) {
		BaseResponse resp = null;
		if (url.contains("113.31.22.234:16383")
				|| url.contains("113.31.20.67:8197")) {
			String a = "{\"data\":" + responseBody + "}";
			String b = a.replace("\"sendtime\":{},", "\"sendtime\":\"\",");
			Log.i(TAG + " Response: =====>", b);
			resp = JSONHelper.jsonToObj(b, t);
		} else if (url.contains("https://api.weixin.qq.com")) {
			String a = "{\"data\":" + responseBody + "}";
			resp = JSONHelper.jsonToObj(a, t);
		} else {
			resp = JSONHelper.jsonToObj(responseBody, t);
		}
		if (Config.DEBUG && !Config.GATEWAY_URL.contains("i.api.zhubajie.com")) {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			String time = format.format(new Date(System.currentTimeMillis()));
			String urf8body = JSONHelper.unicodeToUtf8(responseBody);
			String response = "";
			if (!urf8body.equals("")) {
				if (urf8body.startsWith("{")) {
					response = JsonFormatTool.formatJson(urf8body, " ")
							.replace("\n", "<br>");
				} else {
					response = urf8body.replace("\n", "<br>");
				}
			}
			LogManager
					.getInstance()
					.addLog("<p>"
							+ "==========URL=========="
							+ time
							+ "<br><font color=\"#ff0000\">"
							+ url
							+ "</p><p>[请求]</p><p> ==========RESPONSE==========<br><font color=\"#00ff00\">"
							+ response + "</p>");
		}
		return resp;
	}

	public synchronized static BaseResponse doPost(String urlString,
			String data, Type t) {

		if (client == null) {
			createHttpClient();
		}
		try {
			String url = executeUrl(urlString);

			HttpPost post = new HttpPost(url);
			post.setHeader("Content-Type", "application/json; charset=utf-8");

			HttpEntity entity = new ByteArrayEntity(data.getBytes());
			post.setEntity(entity);
			HttpResponse response;
			Log.d(TAG + " Request: ====>", data);
			response = client.execute(post);

			StatusLine sl = response.getStatusLine();
			Log.d(TAG, "URL:" + url);
			Log.d(TAG, "CODE:" + sl.getStatusCode());

			if (sl.getStatusCode() == 200) {
//				ImageBaseResponse value = new ImageBaseResponse();
//				value.setMsg("图片验证码");
//				value.setResult(0);
//				value.setmImageStream(response.getEntity().getContent());
				return null;
			} else if (sl.getStatusCode() == 400) {
				return new ErrorResponse("无效的请求");
			} else {
				return new ErrorResponse("与服务器连接超时，请稍后再试..");
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}

		return new ErrorResponse("数据加载出错，请稍后再试..");
	}

	public synchronized static JSONObject doYoukuGet(String urlString) {

		if (client == null) {
			createHttpClient();
		}

		HttpGet get = new HttpGet();
		Log.d(TAG + " Request youku: ====>", urlString);
		try {
			get.setURI(new URI(urlString));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		get.setHeader("Content-Type", "application/json; charset=utf-8");
		HttpResponse response;
		try {
			response = client.execute(get);
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == 200) {
				String data = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				JSONObject jsonData = new JSONObject(data).getJSONArray("data")
						.getJSONObject(0);
				return jsonData;
			} else {
				return null;
			}

		} catch (JSONException e) {
			JSONObject object = new JSONObject();
			try {
				object.put("errormsg", "服务器故障或视频已被删除");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return object;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// public synchronized static String doPostJson(String urlString, String
	// data,
	// Type t) {
	//
	// if (client == null) {
	// createHttpClient();
	// }
	// try {
	// String url;
	// if (urlString.contains("113.31.20.67:8197")) {
	// url = urlString;
	// } else {
	// url = appendUrl(urlString);
	// }
	//
	// HttpPost post = new HttpPost(url);
	// post.setHeader("Content-Type", "application/json; charset=utf-8");
	//
	// HttpEntity entity = new ByteArrayEntity(data.getBytes());
	// post.setEntity(entity);
	// HttpResponse response;
	//
	// Log.d(TAG + " Request: ====>", data);
	// response = client.execute(post);
	//
	// StatusLine sl = response.getStatusLine();
	// Log.d(TAG, "URL:" + url);
	// Log.d(TAG, "CODE:" + sl.getStatusCode());
	//
	// if (sl.getStatusCode() == 200) {
	// return doResponseJson(response, t);
	// } else if (sl.getStatusCode() == 400) {
	// return "400";
	// } else {
	// return sl.getStatusCode() + "";
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e(NetworkHelper.class.getSimpleName(), e.getMessage());
	// }
	//
	// return "1";
	// }

	// private static BaseResponse doObject(HttpResponse response, Type t) {
	// String responseBody = null;
	// BaseResponse resp = null;
	// if (response.getEntity() != null) {
	// try {
	// responseBody = EntityUtils.toString(response.getEntity(),
	// "UTF-8");
	// String a = "{\"data\":" + responseBody + "}";
	// String b = a.replace("\"sendtime\":{},", "\"sendtime\":\"\",");
	// Log.i(TAG + " Response: =====>", b);
	// resp = JSONHelper.jsonToObj(b, t);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return resp;
	// }

	// public static synchronized BaseResponse doGet(String urlString, Type t) {
	//
	// if (client == null) {
	// createHttpClient();
	//
	// }
	// String url = null;
	// if (urlString.contains("113.31.20.67:16383")) {
	// url = urlString;
	// } else if (urlString.contains("http://i.api.zhubajie.com")) {
	// url = urlString;
	// }
	// HttpGet get = new HttpGet();
	// Log.d(TAG + " Request: ====>", url);
	// try {
	// get.setURI(new URI(url));
	// } catch (URISyntaxException e) {
	// e.printStackTrace();
	// }
	// get.setHeader("Content-Type", "application/json; charset=utf-8");
	// HttpResponse response;
	// try {
	//
	// response = client.execute(get);
	// StatusLine sl = response.getStatusLine();
	//
	// if (sl.getStatusCode() == 200) {
	// if (urlString.contains("113.31.20.67:16383")) {
	// return doObject(response, t);
	// } else
	// return doResponse(response, t);
	// } else {
	// return new ErrorResponse("网络错误，请稍后再试..");
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return new ErrorResponse("数据加载出错，请稍后再试..");
	//
	// }

	public synchronized static BaseResponse doPostWithFiles(String urlString,
			String data, Type t, File file) {
		String end = System.getProperty("line.separator");
		String twoHyphens = "--";
		String boundary = "*****";

		BaseResponse resp = null;
		String urlStr = appendUrl(urlString);

		try {
			URL url = new URL(urlStr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);
			con.connect();

			DataOutputStream ds = new DataOutputStream(con.getOutputStream());

			// 写入基本数据
			Map<Object, Object> objectMap = new HashMap<Object, Object>();
			JSONObject object = new JSONObject(data);
			Iterator<String> iterator = object.keys();
			String tempName = null;
			while (iterator.hasNext()) {
				tempName = iterator.next();
				objectMap.put(tempName, object.getString(tempName));
			}

			StringBuilder sb = new StringBuilder();
			for (Map.Entry<Object, Object> param : objectMap.entrySet()) {
				if (param.getKey().toString().equalsIgnoreCase("face")
						|| param.getKey().toString()
								.equalsIgnoreCase("hasProcessDialog")) {
					continue;
				}
				sb.append(twoHyphens + boundary + end);
				sb.append("Content-Disposition: form-data; name=\""
						+ param.getKey() + "\"" + end);
				sb.append(end);
				sb.append(param.getValue().toString() + end);
			}

			ds.write(sb.toString().getBytes("UTF-8"));// 发送表单字段数据
			Log.i(TAG + "=====>", sb.toString());
			ds.flush();

			// 写入文件

			StringBuilder split = new StringBuilder();
			split.append(twoHyphens + boundary + end);
			split.append("Content-Disposition: form-data; name=\"face\"; filename=\""
					+ file.getName() + "\"" + end);

			split.append(end);

			Log.i("....", split.toString());

			FileInputStream fis = null;
			try {
				// 发送图片数据
				ds.writeBytes(split.toString());
				fis = new FileInputStream(file);
				byte[] b = new byte[1024];
				int length = 0;
				while ((length = fis.read(b)) != -1) {
					ds.write(b, 0, length);
				}
				ds.writeBytes(end);
			} catch (IOException e) {
				e.printStackTrace();
			}

			finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			ds.flush();
			// ds.writeBytes(twoHyphens + boundary + end);
			// ds.writeBytes("Content-Disposition: form-data; " +
			// "name=\"face\";filename=\""
			// + file.getName() + "\"" + end);
			// ds.writeBytes(end);
			//
			// FileInputStream fStream = new FileInputStream(file);
			// int bufferSize = 1024;
			// byte[] buffer = new byte[bufferSize];
			// int length = -1;
			// while ((length = fStream.read(buffer)) != -1) {
			// ds.write(buffer, 0, length);
			// }
			// ds.writeBytes(end);
			// // ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			// fStream.close();
			// ds.flush();
			ds.close();

			Log.d(TAG, "URL:" + urlStr);
			Log.d(TAG, "CODE:" + con.getResponseCode());

			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

				InputStream is = con.getInputStream();
				int ch;
				StringBuffer b = new StringBuffer();
				while ((ch = is.read()) != -1) {
					b.append((char) ch);
				}
				String responseBody = b.toString();

				Log.i(TAG + "=====>", responseBody);
				resp = JSONHelper.jsonToObj(responseBody, t);

				if (Config.DEBUG
						&& Config.GATEWAY_URL.contains("mt.zhubajie.la")) {
					SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
					LogManager
							.getInstance()
							.addLog("<p>"
									+ "==========URL=========="
									+ format.format(new Date(System
											.currentTimeMillis()))
									+ "<br><font color=\"#ff0000\">"
									+ urlStr
									+ "</p><p>"
									+ "==========REQUEST==========<br><font color=\"#00ffff\">"
									+ sb.toString()
									+ "</p><p> ==========REPONSE==========<br><font color=\"#00ff00\">"
									+ JsonFormatTool
											.formatJson(
													JSONHelper
															.unicodeToUtf8(responseBody),
													" ").replace("\n", "<br>")
									+ "</p>");
				}
			} else {
				return new ErrorResponse("上传文件失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resp;
	}

	public synchronized static BaseResponse doPostWithMoreFiles(
			String urlString, String data, Type t, Map<String, File> filesMap) {
		String end = System.getProperty("line.separator");
		String twoHyphens = "--";
		String boundary = "*****";

		BaseResponse resp = null;
		String urlStr = appendUrl(urlString);

		try {
			URL url = new URL(urlStr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);
			con.connect();

			DataOutputStream ds = new DataOutputStream(con.getOutputStream());

			// 写入基本数据
			Map<Object, Object> objectMap = new HashMap<Object, Object>();
			JSONObject object = new JSONObject(data);
			Iterator<String> iterator = object.keys();
			String tempName = null;
			while (iterator.hasNext()) {
				tempName = iterator.next();
				objectMap.put(tempName, object.getString(tempName));
			}

			StringBuilder sb = new StringBuilder();
			for (Map.Entry<Object, Object> param : objectMap.entrySet()) {
				if (param.getKey().toString().equalsIgnoreCase("face")) {
					continue;
				}
				sb.append(twoHyphens + boundary + end);
				sb.append("Content-Disposition: form-data; name=\""
						+ param.getKey() + "\"" + end);
				sb.append(end);
				sb.append(param.getValue().toString() + end);
			}

			ds.write(sb.toString().getBytes("UTF-8"));// 发送表单字段数据
			Log.i(TAG + "=====>", sb.toString());
			ds.flush();

			// 写入文件

			Object[] keys = filesMap.keySet().toArray();

			for (Object key : keys) {

				File file = filesMap.get(key);

				StringBuilder split = new StringBuilder();
				split.append(twoHyphens + boundary + end);
				split.append("Content-Disposition: form-data; name=\""
						+ key.toString() + "\"; filename=\"" + file.getName()
						+ "\"" + end);

				split.append(end);

				Log.i("....", split.toString());

				FileInputStream is = null;
				try {
					// 发送图片数据
					ds.writeBytes(split.toString());
					is = new FileInputStream(file);
					byte[] b = new byte[1024];
					int length = 0;
					while ((length = is.read(b)) != -1) {
						ds.write(b, 0, length);
					}
					ds.writeBytes(end);
				} catch (IOException e) {
					e.printStackTrace();
				}

				finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}

			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			ds.flush();
			// ds.writeBytes(twoHyphens + boundary + end);
			// ds.writeBytes("Content-Disposition: form-data; " +
			// "name=\"face\";filename=\""
			// + file.getName() + "\"" + end);
			// ds.writeBytes(end);
			//
			// FileInputStream fStream = new FileInputStream(file);
			// int bufferSize = 1024;
			// byte[] buffer = new byte[bufferSize];
			// int length = -1;
			// while ((length = fStream.read(buffer)) != -1) {
			// ds.write(buffer, 0, length);
			// }
			// ds.writeBytes(end);
			// // ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			// fStream.close();
			// ds.flush();
			ds.close();

			Log.d(TAG, "URL:" + urlStr);
			Log.d(TAG, "CODE:" + con.getResponseCode());

			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

				InputStream is = con.getInputStream();
				int ch;
				StringBuffer b = new StringBuffer();
				while ((ch = is.read()) != -1) {
					b.append((char) ch);
				}
				String responseBody = b.toString();

				Log.i(TAG + "=====>", responseBody);
				resp = JSONHelper.jsonToObj(responseBody, t);
				if (Config.DEBUG
						&& Config.GATEWAY_URL.contains("mt.zhubajie.la")) {
					SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
					LogManager
							.getInstance()
							.addLog("<p>"
									+ "==========URL=========="
									+ format.format(new Date(System
											.currentTimeMillis()))
									+ "<br><font color=\"#ff0000\">"
									+ urlStr
									+ "</p><p>"
									+ "==========REQUEST==========<br><font color=\"#00ffff\">"
									+ sb.toString().replace("\n", "<br>")
									+ "</p><p> ==========RESPONSE==========<br><font color=\"#00ff00\">"
									+ JsonFormatTool
											.formatJson(
													JSONHelper
															.unicodeToUtf8(responseBody),
													" ").replace("\n", "<br>")
									+ "</p>");
				}
			} else {
				return new ErrorResponse("上传文件失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resp;
	}

	// private static String doResponseJson(HttpResponse response, Type t) {
	// String responseBody = null;
	// BaseResponse resp = null;
	// if (response.getEntity() != null) {
	// try {
	// responseBody = EntityUtils.toString(response.getEntity(),
	// "UTF-8");
	// Log.i(TAG + " Response: =====>", responseBody);
	// // resp = JSONHelper.jsonToObj(responseBody, t);
	// // if(resp instanceof ErrorResponse) {
	// // if(resp.getResult() <=50 && resp.getResult() != 22) {
	// // resp.setMsg("系统错误");
	// // }
	// // }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// return responseBody;

	// }

	// private static BaseResponse doResponseStream(HttpResponse response, Type
	// t) {
	// InputStream responseBody = null;
	// BaseResponse resp = null;
	// if (response.getEntity() != null) {
	// try {
	// responseBody = response.getEntity().getContent();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// ImageBaseResponse value = new ImageBaseResponse();
	// value.setMsg("图片验证码");
	// value.setResult(0);
	// value.setmImageStream(responseBody);
	// return value;
	//
	// }

	// private static BaseResponse doResponse(HttpResponse response, Type t) {
	// String responseBody = null;
	// BaseResponse resp = null;
	// if (response.getEntity() != null) {
	// try {
	// responseBody = EntityUtils.toString(response.getEntity(),
	// "UTF-8");
	// Log.i(TAG + " Response: =====>", responseBody);
	// resp = JSONHelper.jsonToObj(responseBody, t);
	// // if(resp instanceof ErrorResponse) {
	// // if(resp.getResult() <=50 && resp.getResult() != 22) {
	// // resp.setMsg("系统错误");
	// // }
	// // }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// return resp;
	//
	// }

	public static boolean checkNetwork(final Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static Drawable loadImageFromUrl(String url) {
		if (url.equalsIgnoreCase("")) {
			return null;
		}
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			URLConnection conexion = m.openConnection();
			conexion.setRequestProperty("User-Agent",
					"Mozilla/5.0 ( compatible ) ");
			conexion.setRequestProperty("Accept", "*/*");
			conexion.connect();
			i = conexion.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Drawable d = Drawable.createFromStream(i, "src");
		return d;
	}

	public static Bitmap loadImageFromUrlToBitmap(String url) {
		if (url == null) {
			return null;
		}
		URL m;
		Bitmap bmp = null;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
			bmp = BitmapFactory.decodeStream(i);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmp;
	}

}
