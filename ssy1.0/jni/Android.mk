LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

# 打算编译出的动态库的名字
LOCAL_MODULE:= jni_encrypt


LOCAL_SRC_FILES := \
./jni_main.c \
./mymd5.c


# 如果不包含这一句的话，会提示：__android_log_print 未定义
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
 
LOCAL_MODULE    	:= libmp3lame

LOCAL_SRC_FILES 	:= \
./libmp3lame/bitstream.c \
./libmp3lame/encoder.c \
./libmp3lame/fft.c \
./libmp3lame/gain_analysis.c \
./libmp3lame/id3tag.c \
./libmp3lame/lame.c \
./libmp3lame/mpglib_interface.c \
./libmp3lame/newmdct.c \
./libmp3lame/presets.c \
./libmp3lame/psymodel.c \
./libmp3lame/quantize.c \
./libmp3lame/quantize_pvt.c \
./libmp3lame/reservoir.c \
./libmp3lame/set_get.c \
./libmp3lame/tables.c \
./libmp3lame/takehiro.c \
./libmp3lame/util.c \
./libmp3lame/vbrquantize.c \
./libmp3lame/VbrTag.c \
./libmp3lame/version.c \
./wrapper.c 

LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)