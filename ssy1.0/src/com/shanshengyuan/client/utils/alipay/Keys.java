/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.shanshengyuan.client.utils.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	//合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088611765185581";

	//收款支付宝账号
	public static final String DEFAULT_SELLER = "sunshine@shanshengyuan.com";

	//商户私钥，自助生成
	public static final String PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOaoTHiivjiluUj6eCbYqPslWqI236e4sqn5vZy2ObwsO363Do/MwzXdaPVvgrQmn/Mw4NR354TWOgjhNkGy0JNu+kLhaas4MC6LXZT5l8QWoEu2CfDhrAsEDSVeyPdoYxklNZpkrGZMKXj1F7mpH4GCwmJvNNUADp28C1k+K3p1AgMBAAECgYEAikf3oJMgq2FfVYsjoVZe6EIatgljyG+IXTLmJB9zOYbjHDKuvjMqDQ7yL8Jsf4hweCGjiH1iBVdRb6VlG7oSJE38Vd3rs1wTcfYxQ5MLFFrAs3Zaw4zELrGhftpcizPGW8td/lcsazVdas0O9iw4J4KSrKjnuXwRJqpCfcR+yUECQQD5U6q4EPycNtI+l4Z2a4yeetiLppTm2TGdTu5gU27uPyfGrW7f/cTOKAPEOyWYs/d2fgnEtBXDE3GjmBunEYpdAkEA7NS2j9cn0/OJCepCWVMvKAyLrwGZXDwAmckRFjtt6s/aya07oWKajEVZjGHNa5W/2XTrRV1MoRVfmVyAMyke+QJAKatTB+pG3bDH2JAod9gnq0DqugRANIiuIxVS/6RSEN1bP3gy5Jqb7rx/TKd59Bh3zkToSwix7mVsg6PjUzu6PQJBAL+P5wM+Excbu9LrSzOZV2FblHjAIF48onfB7TJAa84sxlmu57bIXR3s0lv8l/3XqNCqA8udhegds7AG5CN9tXECQE4QAdw5fJuoJ5SQFof+rIG/MoLqIfIEMOeSzihasZ/69BWtIx3HZzWBXTzAZDqdFHLP3ywuxT8qAiiRJwpq7d8=";

	//支付宝公钥
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
}
