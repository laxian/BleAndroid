#include <jni.h>
#include <jni.h>


extern "C"
JNIEXPORT void JNICALL
Java_com_example_bleandroid_RustBridge_scanWrapper(JNIEnv *env, jobject thiz, jobjectArray filter,
                                                   jobject callback);

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_bleandroid_RustBridge_initWrapper(JNIEnv *env, jobject thiz);

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_bleandroid_RustBridge_add(JNIEnv *env, jobject thiz, jint n1, jint n2);
