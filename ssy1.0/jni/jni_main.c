#include "com_zhubajie_client_utils_EncryptUtils.h"
#include "stdio.h"
#include "mymd5.h"
#include <stdlib.h>
#include <jni.h>
#include <string.h>
#include <android/log.h> // 这个是输出LOG所用到的函数所在的路径

#define LOG_TAG    "JNILOG" // 这个是自定义的LOG的标识
#undef LOG // 取消默认的LOG

#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__) // 定义LOG类型
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__) // 定义LOG类型
#define LOGW(...)  __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__) // 定义LOG类型
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__) // 定义LOG类型
#define LOGF(...)  __android_log_print(ANDROID_LOG_FATAL,LOG_TAG,__VA_ARGS__) // 定义LOG类型

const char base[] =
		"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

char* base64_encode(const char* data, int data_len);
int myfind(const char *s, char c);
char * EnCrypt(char* src);
char * EnCryptToken(char* src, char* time);
char * getKey();
char *key;

JNIEXPORT jstring JNICALL Java_com_zhubajie_client_utils_EncryptUtils_encrypt(
		JNIEnv *env, jobject obj, jbyteArray jmsg) {
	//jstring to char*
	char* msg = NULL;
	jsize alen = (*env)->GetArrayLength(env, jmsg);
	jbyte* ba = (*env)->GetByteArrayElements(env, jmsg, JNI_FALSE);
	if (alen > 0) {
		msg = (char*) malloc(alen + 1);
		memcpy(msg, ba, alen);
		msg[alen] = 0;
	}

	(*env)->ReleaseByteArrayElements(env, jmsg, ba, 0);
	//加密
	char* dec = EnCrypt(msg);
	jstring res = (*env)->NewStringUTF(env, dec);
	free(msg);
	return res;
}

JNIEXPORT jstring JNICALL Java_com_zhubajie_client_utils_EncryptUtils_encrypturl(
		JNIEnv *env, jobject obj, jbyteArray jmsg, jbyteArray jtime) {
	//jstring to char*
	char* msg = NULL;
	jsize alen = (*env)->GetArrayLength(env, jmsg);
	jbyte* ba = (*env)->GetByteArrayElements(env, jmsg, JNI_FALSE);
	if (alen > 0) {
		msg = (char*) malloc(alen + 1);
		memcpy(msg, ba, alen);
		msg[alen] = 0;

	}
	(*env)->ReleaseByteArrayElements(env, jmsg, ba, 0);

	char* time = NULL;
	jsize alen1 = (*env)->GetArrayLength(env, jtime);
	jbyte* ba1 = (*env)->GetByteArrayElements(env, jtime, JNI_FALSE);
	if (alen > 0) {
		time = (char*) malloc(alen1 + 1);
		memcpy(time, ba1, alen1);
		time[alen1] = 0;

	}
	(*env)->ReleaseByteArrayElements(env, jtime, ba1, 0);
	//加密
	char *str = EnCryptToken(msg, time);
	jstring res = (*env)->NewStringUTF(env, str);
	free(msg);
	free(time);
	return res;
}

char * EnCryptToken(char* src, char* time) {
	char *key=getKey();
	char *base64EncodeSrc = base64_encode(key, strlen(key));
	char * result = (char *) malloc(
			strlen(src) + strlen(base64EncodeSrc) + strlen(time) + 1);
	strcpy(result, src);
	strcat(result, base64EncodeSrc);
	strcat(result, time);
	char *str = MDString(result);
	free(result);
	return str;
}

char *getKey() {
	return "87xP-M3d6+w4Y9";
}

char * EnCrypt(char* src) {
	//随机找一个数字，并从密锁串中找到一个密锁值
	const char* lockstream =
			"st=lDEFABCNOPyzghi_jQRST-UwxkVWXYZabcdef+IJK6/7nopqr89LMmGH012345uv.";
	int lockstreamLength = strlen(lockstream);
	srand((unsigned) time(NULL));
	int lockCount = rand() % lockstreamLength;
	char randomLock = lockstream[lockCount];
	char *key=getKey();
	char * passwordRand = (char *) malloc(strlen(key) + 1 + 1);
	strcpy(passwordRand, key);
	char tmp[2];
	tmp[0] = randomLock;
	tmp[1] = '\0';
	strcat(passwordRand, tmp);
	//md5加密passwordRand
	char *md5EncodePwd = MDString(passwordRand);
	//base64加密src
	char *base64EncodeSrc = base64_encode(src, strlen(src));
	int i = 0, j = 0, k = 0;
	int encrySrcLength = strlen(base64EncodeSrc) + 1;
	char* encryptSrc = (char *) malloc(encrySrcLength);
	memset(encryptSrc, 0, encrySrcLength);
	for (i = 0; i < strlen(base64EncodeSrc); i++) {
		k = (k == strlen(md5EncodePwd) ? 0 : k);
		int index = myfind(lockstream, base64EncodeSrc[i]);
		j = (index + lockCount + md5EncodePwd[k]) % lockstreamLength;
		encryptSrc[i] = lockstream[j];
		k++;
	}

	char * cstr = (char *) malloc(strlen(encryptSrc) + 1 + 1);
	strcpy(cstr, encryptSrc);
	strcat(cstr, tmp);
	free(encryptSrc);
	free(passwordRand);
	return cstr;
}

int myfind(const char *s, char c) {
	char *p = (char *) s;
	while (*s && *s != c)
		s++;
	if (*s == c)
		return s - p;
	else
		return -1;
}

/* */
char *base64_encode(const char* data, int data_len) {
//int data_len = strlen(data);
	int prepare = 0;
	int ret_len;
	int temp = 0;
	char *ret = NULL;
	char *f = NULL;
	int tmp = 0;
	char changed[4];
	int i = 0;
	ret_len = data_len / 3;
	temp = data_len % 3;
	if (temp > 0) {
		ret_len += 1;
	}
	ret_len = ret_len * 4 + 1;
	ret = (char *) malloc(ret_len);

	if (ret == NULL) {
		exit(0);
	}
	memset(ret, 0, ret_len);
	f = ret;
	while (tmp < data_len) {
		temp = 0;
		prepare = 0;
		memset(changed, '\0', 4);
		while (temp < 3) {
			//printf("tmp = %d\n", tmp);
			if (tmp >= data_len) {
				break;
			}
			prepare = ((prepare << 8) | (data[tmp] & 0xFF));
			tmp++;
			temp++;
		}
		prepare = (prepare << ((3 - temp) * 8));
		//printf("before for : temp = %d, prepare = %d\n", temp, prepare);
		for (i = 0; i < 4; i++) {
			if (temp < i) {
				changed[i] = 0x40;
			} else {
				changed[i] = (prepare >> ((3 - i) * 6)) & 0x3F;
			}
			*f = base[changed[i]];
			//printf("%.2X", changed[i]);
			f++;
		}
	}
	*f = '\0';

	return ret;

}
/* */
static char find_pos(char ch) {
	char *ptr = (char*) strrchr(base, ch); //the last position (the only) in base[]
	return (ptr - base);
}

