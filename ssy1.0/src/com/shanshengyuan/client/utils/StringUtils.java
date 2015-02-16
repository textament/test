package com.shanshengyuan.client.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.json.JSONException;
import org.json.JSONObject;

public class StringUtils {

	private static final String tag = StringUtils.class.getSimpleName();

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			if (StringUtils.isEmpty(sb.toString())) {
				Log.i(tag, "converStreamToString:" + is.read());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public static String mathPrice(String price){
		 float aa = (float) ((Float.parseFloat(price))*0.98);
		 int   scale   =   1;//设置位数  
		 int   roundingMode   =   1;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.  
		 BigDecimal   bd   =   new   BigDecimal((double)aa);  
		 bd   =   bd.setScale(scale,roundingMode);  
		 aa   =   bd.floatValue(); 
		 return aa+"";
	}

	public static boolean checkEmail(String mail) {
		String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mail);
		return m.find();
	}

	public static String assemblingAuthentity(String email, String pws) {
		JSONObject object = new JSONObject();
		String result = null;
		try {
			object.put("user_email", email);
			object.put("user_pws", pws);
			result = object.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e(tag, e.getMessage());
		}
		return result;
	}

	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static String quote(String str) {
		return "\"" + str + "\"";
	}

	public static int parseInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static int strToInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}
//
//	public static String MD5(String text) {
//		try {
//			MessageDigest md;
//			md = MessageDigest.getInstance("MD5");
//			byte[] md5hash = new byte[32];
//		//	md.update(text.getBytes());
//			md.update(text.getBytes("iso-8859-1"), 0, text.length());
//			md5hash = md.digest();
//			return convertToHex(md5hash);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
	public  static String MD5(String value) {
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(value.getBytes("UTF-8"));
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	// 是否是电话号码
	public static boolean isPhoneNumber(String phoneNumber) {
		try {
			return phoneNumber.matches("^(?:13|15|18|14)[0-9]\\d{8}$");
		} catch (Exception e) {
			return false;
		}
	}

	public static final String key = "a6#43d%1f064@eb";

	// 客户端加密
	public static String xirEnCode(String str) {
		StringBuffer result = new StringBuffer();
		StringBuffer sb = new StringBuffer(str);
		StringBuffer sbKey = new StringBuffer(key);
		for (int i = 0; i < sb.length(); i++) {
			int cr = sb.charAt(i) ^ sbKey.charAt(i % sbKey.length());
			result.append((char) cr);
		}
		return new String(Base64.encodeBytes(result.toString().getBytes()));
	}

	// 转换时间
	public static String date(Date time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String SDateTime = formatter.format(time);
		return SDateTime;
	}

	public static String date(Date time, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String SDateTime = formatter.format(time);
		return SDateTime;
	}

	/**
	 * 把yyyy-mm-dd转换成yyyy年mm月dd日
	 * 
	 * @param date
	 * @return date
	 */
	public static String dateToDate(String date) {
		String str = date.replaceFirst("-", "年").replaceFirst("-", "月") + "日";
		return str;

	}

	/**
	 * 把GZIP流解压缩
	 * 
	 * @param response
	 * @return
	 */
	public static String isStringGZIP(InputStream is) {
		String jsonString = null;
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(is);
			bis.mark(2);
			// 取前两个字节
			byte[] header = new byte[2];
			int result = bis.read(header);
			// reset输入流到开始位置
			bis.reset();
			// 判断是否是GZIP格式
			int headerData = (int) ((header[0] << 8) | header[1] & 0xFF);
			// Gzip 流 的前两个字节是 0x1f8b

			if (result != -1 && headerData == 0x1f8b) {
				Log.d("HttpTask", " use GZIPInputStream  ");
				is = new GZIPInputStream(bis);
			} else {
				Log.d("HttpTask", " not use GZIPInputStream");
				is = bis;
			}
			InputStreamReader reader = new InputStreamReader(is, "utf-8");
			char[] data = new char[100];
			int readSize;
			StringBuffer sb = new StringBuffer();
			while ((readSize = reader.read(data)) > 0) {
				sb.append(data, 0, readSize);
			}
			jsonString = sb.toString();
			bis.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return jsonString;
	}

	/**
	 * 获取一个字符串的长度。不同之处在于，一个汉字等于2个字符长度
	 * 
	 * @param @param s
	 * @param @return
	 * @return int
	 * @throws
	 * @author
	 * @since zhubajie　Ver 2.0.0
	 */
	public static int getGBKStringLength(String s) {

		int length = 0;

		byte[] bytes = s.getBytes();
		int stringLength = bytes.length;

		for (int i = 0; i < stringLength; i++) {

			if (bytes[i] < 0) { // 如果字节码为负数，则为汉字
				length = length + 2;
				i = i + 2;
			} else {
				length = length + 1;
			}

		}

		return length;
	}

	/**
	 * 
	 * isLetter:验证是否字母
	 * 
	 * @param @param s
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author
	 * @since zhubajie　Ver 2.0.1
	 */
	public static boolean isLetter(String s) {
		char c = s.charAt(0);
		int i = (int) c;
		if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * isNumeric:是否包含数字
	 *
	 * @param  @param str
	 * @param  @return    
	 * @return boolean    
	 * @throws 
	 * @author  
	 * @since  zhubajie　Ver 2.0.1
	 */
	public static boolean isNumeric(String str){ 
		String regex="[0-9]+?";
	    Pattern pattern = Pattern.compile(regex); 
	    return pattern.matcher(str).find();    
	} 

	/**
	 * 验证是否是允许上传的图片格式
	 */
	public static boolean isImage(String s) {

		if(s == null) {
			return false;
		}
		
		int length = s.lastIndexOf(".");

		String fileType = null;

		if (length != -1) {
			fileType = s.substring(length, s.length());
		}

		if (".jpg".equalsIgnoreCase(fileType) || ".png".equalsIgnoreCase(fileType)) {
			return true;
		}

		return false;
	}
	
	/**
	 * 
	 * isSpecialNum:特殊字符验证
	 *
	 * @param  @param str
	 * @param  @return    
	 * @return boolean    
	 * @throws 
	 * @author  
	 * @since  zhubajie　Ver 2.0.1
	 */
	public static boolean isSpecialNum(String str) {
		boolean result = false;
		 if(str.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*", "").length()==0)  
			 result = true;
		 else
			 result = false;
		return result;
	}
	
	
	/**
     * 等级转换为中文
     */
    public static String convertNumber(int index) {
        String[] arrNum = { "新手", "猪一戒", "猪二戒", "猪三戒", "猪四戒", "猪五戒", "猪六戒", "猪七戒", "猪八戒", "猪九戒", "猪十戒", 
                "猪十一戒", "猪十二戒","猪十三戒", "猪十四戒", "猪十五戒", "猪十六戒", "猪十七戒", "猪十八戒", "猪十九戒", "猪二十戒", 
                "猪二十一戒", "猪二十二戒", "猪二十三戒", "猪二十四戒", "二十五戒", "猪二十六戒","猪二十七戒", "猪二十八戒", 
                "猪二十九戒", "猪三十戒", "猪三十一戒", "猪三十二戒" }; // 大写数字
        return arrNum[index];
    }
    
	public static boolean parseList(HashMap<String, Object> res,String name) {
		if (res == null || res.size() <= 0) {
			return false;
		}

		boolean hasData = false;
		if ("SinaWeibo".equals(name)) {
			// users[id, name, description]
			@SuppressWarnings("unchecked")
			ArrayList<HashMap<String, Object>> users
					= (ArrayList<HashMap<String,Object>>) res.get("users");
			for (HashMap<String, Object> user : users) {
//				Following following = new Following();
//				following.uid = String.valueOf(user.get("id"));
//				following.screeName = String.valueOf(user.get("name"));
//				following.description = String.valueOf(user.get("description"));
//				if (!map.containsKey(following.uid)) {
//					map.put(following.uid, following);
//					follows.add(following);
//					hasData = true;
//				}
			}
			//hasNext = (Integer) res.get("total_number") > follows.size();
		}
		else if ("TencentWeibo".equals(name)) {
			//hasNext = ((Integer)res.get("hasnext") == 0);
			// info[nick, name, tweet[text]]
			@SuppressWarnings("unchecked")
			ArrayList<HashMap<String, Object>> infos
					= (ArrayList<HashMap<String,Object>>) res.get("info");
			for (HashMap<String, Object> info : infos) {
//				Following following = new Following();
//				following.screeName = String.valueOf(info.get("nick"));
//				following.uid = String.valueOf(info.get("name"));
//				@SuppressWarnings("unchecked")
//				ArrayList<HashMap<String, Object>> tweets
//						= (ArrayList<HashMap<String,Object>>) info.get("tweet");
//				for (int i = 0; i < tweets.size();) {
//					HashMap<String, Object> tweet = tweets.get(i);
//					following.description = String.valueOf(tweet.get("text"));
//					break;
//				}
//				if (!map.containsKey(following.uid)) {
//					map.put(following.uid, following);
//					follows.add(following);
//					hasData = true;
//				}
			}
		}
		
		return hasData;
	}

}
