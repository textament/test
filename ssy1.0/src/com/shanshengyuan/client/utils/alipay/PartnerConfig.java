package com.shanshengyuan.client.utils.alipay;

public class PartnerConfig {
    // -- Ali Pay --
	// 合作商户ID。用签约支付宝账号登录ms.alipay.com后，在账户信息页面获取。
	public static final String PARTNER = "2088001408629963";
	// 商户收款的支付宝账号
	public static final String SELLER = "zhubajie@zhubajie.com";
	// 商户（RSA）私钥
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL5IkkoW9RONT8O8+oufQj9vcAJ5t9UbcVnoyb5Ofshstpfl5LARZnRdbs4rBjiyhnGbueWhnwm5UWDexZx/kTNEW5DGD9SGvk7QM6vE/LD+/mb2y+1F3RmZyuWVLhZua+c2MAolAvXQNsEdlldzRtAFRBNWR9W4xkGeiqjaqQp5AgMBAAECgYBD2okgFnOqVzfcauVFKLNs/4YHu/PL8I9JKnPWgxM+0VWDcR1Nk8bfM/oxVrJPQYojtQ5jTnDx/L2CN65sEilqaxg30EMTu6+247Td/nIgxYvDukpKPkyZn9JakrsyC/NNVvAI4dfT852f+9aWDwgX5Wu+/Lv+NtGhZqIDzhLo6QJBAOgYZUui7ShpfJSk6ZGTdb5BV32VkQFTek6C8rWSeRdNawzXhqJcUUCetDh1qg8fyZ7vZOaOlBEqcQAJ5cbGPusCQQDR4busm5zT4Eiyp4/tsnU/gXo7j9QfTlEkXzYOAzBrw5KjDc7VN05x6aRJxMtjVxT5YlNsbTlMeKZEbAKLoSsrAkEA5hZZRVRtm6SHtqyOSJ1RGp2YaI1/xTrGg1LZ3hspJGrBd2eqtwaiHle2/knXn0q6DG/zLYWnUCUtBQbK3fXmpQJABa82dQtV6QsnDOeq50YxFMI9/ET9+scVPYXyodiGRDTs0Mu78VxpHH62e2UAGTWvlL2MAuDGb4LJBgti2kNINwJAMhD3vR57lxSFJqfVNSBbtCtljaqxO9c9Eba2AslcuQrFg5UonVk9sjtNZQwoKRdEd7fObSXFwAJ+yAa70XoCfg==";
	// 支付宝（RSA）公钥 用签约支付宝账号登录ms.alipay.com后，在密钥管理页面获取。
	public static final String RSA_ALIPAY_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCRx0ftBk441QaOxtBsjVwP9HxVR22LaiYUVzB/QALNc1j1DiRjIJ+l3iGzssPprc6zFJNVSNu/LZHUzCmY/YlCs9yMJNnCqgdCoDde6VZ60uEcIxz8decjzf+rEk06h9qIyt2EVPogFbG7UyI2WdPGjjCjsGaRaton+QhcXlJF9wIDAQAB";
	// 支付宝安全支付服务apk的名称，必须与assets目录下的apk名称一致
	public static final String ALIPAY_PLUGIN_NAME = "alipay_msp.apk";
	
	// -- YiJi Pay --
	public static final String PARTNER_ID = "20110722122621";
}
