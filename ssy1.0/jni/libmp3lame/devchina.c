#include <stdio.h>
#include <jni.h>
#include "lame.h"
#include "com_devchina_lame_DevchinaActivity.h"

JNIEXPORT jstring JNICALL Java_com_devchina_lame_DevchinaActivity_getLameVesion
  (JNIEnv * env, jobject obj)
{

	return (env*)->NewStringUTF(env,get_lame_version());
}
