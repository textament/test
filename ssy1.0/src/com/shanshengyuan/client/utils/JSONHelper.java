/**
 * 
 */
package com.shanshengyuan.client.utils;

import java.lang.Character.UnicodeBlock;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.ErrorResponse;

/**
 * @author Action.Tan
 * 
 */
public class JSONHelper {
	public static String objToJson(Object obj) {
		try {
			Gson gson = new Gson();
			return gson.toJson(obj);
		} catch (Exception e) {
			Log.e(NetworkHelper.class.getSimpleName(), e.getMessage());
		}

		return null;

	}

	/**
	 * unicode 转换成 utf-8
	 * 
	 * @author fanhui 2007-3-15
	 * @param theString
	 * @return
	 */
	public static String unicodeToUtf8(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

	/**
	 * utf-8 转换成 unicode
	 * 
	 * @author fanhui 2007-3-15
	 * @param inStr
	 * @return
	 */
	public static String utf8ToUnicode(String inStr) {
		char[] myBuffer = inStr.toCharArray();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < inStr.length(); i++) {
			UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
			if (ub == UnicodeBlock.BASIC_LATIN) {
				// 英文及数字等
				sb.append(myBuffer[i]);
			} else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
				// 全角半角字符
				int j = (int) myBuffer[i] - 65248;
				sb.append((char) j);
			} else {
				// 汉字
				short s = (short) myBuffer[i];
				String hexS = Integer.toHexString(s);
				String unicode = "\\u" + hexS;
				sb.append(unicode.toLowerCase());
			}
		}
		return sb.toString();
	}

	public static BaseResponse jsonToObj(String json, Type t) {
		BaseResponse value = null;
		try {
			Gson gson = new Gson();
			value = gson.fromJson(json, ErrorResponse.class);
			if (value != null && ((ErrorResponse) value).getResult() != 0) {
				return value;
			}
			value = gson.fromJson(json, t);
		} catch (Exception e) {
			value = new ErrorResponse("服务器异常");
			Log.e(NetworkHelper.class.getSimpleName(), e.getMessage());
		}
		if(value==null){
			value = new ErrorResponse("服务器异常");
		}
		return value;
	}
}
