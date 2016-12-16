#include <jni.h>
#include <string>
#include <sstream>
#include <cstdlib>



extern "C"
jstring
Java_eu_aplusteam_jni_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {

    int number = rand();

    std::stringstream message;
    message << "Hello from C++ World" << number;

    return env->NewStringUTF(message.str().c_str());
}

extern "C"
void
Java_eu_aplusteam_jni_MainActivity_initJNI(
        JNIEnv *env,
        jobject /* this */) {

    srand (time(NULL));
}