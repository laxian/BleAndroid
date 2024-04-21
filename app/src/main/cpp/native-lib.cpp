#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

jint JNI_OnLoad(JavaVM* vm, void*) {
   JNIEnv* env;
   if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
       return JNI_ERR; // JNI版本检查失败
   }

   // 定义一个本地方法描述数组，用于存储自定义方法的信息
   JNINativeMethod methods[] = {
       {"stringFromJNI", "()Ljava/lang/String;", reinterpret_cast<void*>(stringFromJNI)},
   };

   // 获取MainActivity类的引用
   jclass clazz = env->FindClass("com/example/bleandroid/MainActivity");
   if (clazz == nullptr) {
       return JNI_ERR; // 类未找到，返回错误
   }

   // 动态注册方法
   int result = env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0]));
   if (result < 0) {
       return JNI_ERR; // 注册失败，返回错误
   }

   return JNI_VERSION_1_6; // 返回支持的JNI版本号
}
