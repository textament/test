/**
 * FileDownLoadUtils.java
 * com.zhubajie.client.utils.Log
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-8-8 		lihao
 *
 *  Copyright (c) 2012 zhubajie, TNT All Rights Reserved.
 */

package com.shanshengyuan.client.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.shanshengyuan.client.BaseApplication;

/**
 * ClassName:FileDownLoadUtils Function: 文件下载
 * 
 * 业务处理：通过文件下载路径，获取文件输入流， 然后将一个InputStream里面的数据写入到SD卡中
 * 
 * @author lihao
 * @version
 * @since Ver 2.0.0
 * @Date 2012-8-8
 * 
 * @see
 * 
 */
public class FileDownLoadUtils {

	private URL url = null;
	// 客户端定义的缓存资源存储目录
	private static String SDPATH = BaseApplication.SD_DIR;
	public static int FILESIZE = 4 * 1024;
	static byte[] data = null;
	
	private static String sPath = "";

	public static int downFile(String urlStr, String savePath) {

		File saveFile = new File(savePath);

		if (saveFile.exists()) {
			saveFile.delete();

		}

		try {
			saveFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sPath = savePath;
		try {
			readImage(urlStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return 0;
	}
	
	static HttpURLConnection conn = null;
	 static URL urls = null;
	 static InputStream inStream = null;

	public static void readImage(String path) throws Exception {
		
			urls = new URL(path);
		new Thread(){
			 @Override
			 public void run(){
				 try {
					conn = (HttpURLConnection) urls.openConnection();
					//处理UI
					 conn.setConnectTimeout(5 * 1000);
					 
					 inStream = conn.getInputStream();
					 
						ByteArrayOutputStream outSteam = new ByteArrayOutputStream();

						byte[] buffer = new byte[1024];

						int len = -1;

						while ((len = inStream.read(buffer)) != -1) {

							outSteam.write(buffer, 0, len);

						}

						data = outSteam.toByteArray();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 handler.sendEmptyMessage(0);
			 }
			 }.start();

	}
	
	private static Handler handler =new Handler(){
		 @Override
		 //当有消息发送出来的时候就执行Handler的这个方法
		 public void handleMessage(Message msg){
		 super.handleMessage(msg);
		
			
			OutputStream outputStream = null;
			try {
			

				// 调用readStream方法
				
			

				outputStream = new FileOutputStream(sPath);

				outputStream.write(data);

				outputStream.flush();
				// }
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (outputStream != null) {
						outputStream.close();
					}
					if(inStream !=null){
						inStream.close();
					}
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		 }
	};

	/**
	 * 
	 * @param urlStr
	 *            下载文件路径
	 * @param path
	 *            下载到哪个路径下
	 * @param fileName
	 *            下载文件完成名称 e.g : 111.txt
	 * @return -1:文件下载出错 0:文件下载成功 1:文件已经存在
	 */
	public int downFile(String urlStr, String path, String fileName) {
		InputStream inputStream = null;
		try {

			if (isFileExist(path + "/" + fileName)) {
				return 1;
			} else {
				inputStream = getInputStreamFromURL(urlStr);
				File resultFile = write2SDFromInput(path, fileName, inputStream);
				if (resultFile == null) {
					return -1;
				}
				inputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	/**
	 * 根据URL得到输入流
	 * 
	 * @param urlStr
	 * @return
	 */
	private InputStream getInputStreamFromURL(String urlStr) {
		HttpURLConnection urlConn = null;
		InputStream inputStream = null;
		try {
			url = new URL(urlStr);
			urlConn = (HttpURLConnection) url.openConnection();
			inputStream = urlConn.getInputStream();
			if (inputStream == null) { // 没有下载流
				throw new RuntimeException("无法获取文件");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return inputStream;
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private static File createSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + "/" + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 * @return
	 */
	private static File createSDDir(String dirName) {
		File dir = new File(SDPATH + "/" + dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean isFileExist(String fileName) {
		File file = new File(SDPATH + "/" + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * 
	 * @param path
	 * @param fileName
	 * @param input
	 * @return
	 */
	public static File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			createSDDir(path);
			file = createSDFile(path + "/" + fileName);
			output = new FileOutputStream(file);
			int length = 0;

			byte[] buffer = new byte[FILESIZE];
			while ((length = input.read(buffer)) != -1) {
				output.write(buffer, 0, length);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public static Bitmap getNetBitmap(String fileUrl) {
		InputStream is = null;
		Bitmap bitmap = null;
		try {
			URL url = new URL(fileUrl);
			is = url.openStream();
			bitmap = BitmapFactory.decodeStream(is);
			return bitmap;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 
	 * copyFile:复制文件
	 * 
	 * @param @param readFilePath 来源地址
	 * @param @param downPath //
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author
	 * @since zhubajie　Ver 2.0.0
	 */
	public static boolean copyFile(String readFilePath, String downPath) {

		OutputStream outputStream = null;
		FileInputStream inputStream = null;

		File readFile = new File(readFilePath);
		File saveFile = new File(downPath);

		if (saveFile.exists()) {
			saveFile.delete();

		}

		try {
			saveFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			inputStream = new FileInputStream(readFile);

			int length = (int) readFile.length();

			byte[] imgData = new byte[length];
			byte[] temp = new byte[512];
			int readLen = 0;
			int destPos = 0;
			while ((readLen = inputStream.read(temp)) > 0) {
				System.arraycopy(temp, 0, imgData, destPos, readLen);
				destPos += readLen;
			}

			outputStream = new FileOutputStream(saveFile);
			outputStream.write(imgData, 0, length);
			outputStream.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean setValidateFile(Context context, File file) {
		if (file.length() > (5 * 1024 * 1024)) {
			Toast.makeText(context, "上传文件大于5M,请重新上传", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;

	}
}
