package com.shanshengyuan.client.utils;

import java.io.IOException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class EncryptUtils {
	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
	static {
		System.loadLibrary("jni_encrypt");
	}
	private static EncryptUtils instance;

	private EncryptUtils() {
	}

	public static EncryptUtils getInstance() {
		if (instance == null) {
			instance = new EncryptUtils();
		}
		return instance;
	}

	public native String aesencrypt(byte[] msg);

	public native String encrypt(byte[] msg);

	public native String encrypturl(byte[] msg, byte[] t);

	/**
	 * 
	 * Description 根据键值进行加密
	 * 
	 * @param data
	 * 
	 * @param key
	 *            加密键byte数组
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */

	public String encrypt(String data, String key) throws Exception {

		return encode(key, data.getBytes());

	}

	/**
	 * 
	 * Description 根据键值进行解密
	 * 
	 * @param data
	 * 
	 * @param key
	 *            加密键byte数组
	 * 
	 * @return
	 * 
	 * @throws IOException
	 * 
	 * @throws Exception
	 */

	public String decrypt(String data, String key) {

		if (data == null)

			return null;

		byte[] buf = new byte[] {};
		byte[] bt = new byte[] {};
		try {
			buf = Base64.decode(data);
			bt = decode(key, buf);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new String(bt);

	}

	/**
	 * DES算法，加密
	 * 
	 * @param data
	 *            待加密字符串
	 * @param key
	 *            加密私钥，长度不能够小于8位
	 * @return 加密后的字节数组，一般结合Base64编码使用
	 * @throws CryptException
	 *             异常
	 */
	public static String encode(String key, byte[] data) throws Exception {
		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes());

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

			byte[] bytes = cipher.doFinal(data);

			return Base64.encodeBytes(bytes);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * DES算法，解密
	 * 
	 * @param data
	 *            待解密字符串
	 * @param key
	 *            解密私钥，长度不能够小于8位
	 * @return 解密后的字节数组
	 * @throws Exception
	 *             异常
	 */
	public static byte[] decode(String key, byte[] data) throws Exception {
		try {
			SecureRandom sr = new SecureRandom();
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
