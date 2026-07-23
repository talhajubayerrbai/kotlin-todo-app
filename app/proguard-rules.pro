# Add project-specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in the Android SDK's default proguard-android-optimize.txt file.

# Keep Room entity and DAO classes
-keep class com.example.kotlintodo.data.** { *; }

# Keep ViewModel classes
-keep class com.example.kotlintodo.viewmodel.** { *; }

# Keep Kotlin coroutine classes
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
