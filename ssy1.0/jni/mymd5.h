#ifndef MD5_H
#define MD5_H

/*
 * 带密钥的md5算法库md5lib.h共有下列函数：
 *
 */
char* MDString(char *);
/* 输入任意一个字符串，经过md5运算后，返回32个字符长的字符串
 *
 */
char* MDFile(char *);
/* 输入任意一个文件名，文件内容经过md5运算后，返回32个字符长的字符串
 *
 */
char* hmac_md5(char* text, char* key);
/* 输入任意一个字符串text,和一个用做密钥的字符串key,
 * 经过hmac_md5算法处理，返回处理结果：一个定长字符串（32个字符）
 */

#ifdef __cplusplus

}

#endif

#endif
