#include <jni.h>
#include <string>
#include <sstream>
#include <cstdlib>
#include <Core.hpp>


extern "C"
jstring
Java_eu_aplusteam_jni_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {

    return env->NewStringUTF(Core::Invoke());
}