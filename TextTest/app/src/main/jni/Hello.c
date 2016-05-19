#include <jni.h>
jstring java_cn_tobeing_texttest_MainActivity_gethello(JNIEnv* env, jobject thiz){
 return (*env)->NewStringUTF(env, "Hello from C World");
 }