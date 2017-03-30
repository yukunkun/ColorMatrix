-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-ignorewarnings
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes Signature

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}
-keep public class * extends android.view.View
-keep class **.R$* {
 *;
}

-keep class com.letv.recorder.** { *; }
-keep class com.le.utils.gles.** { *; }
-keep class com.le.filter.gles.**{ *; }
-keep class com.le.utils.common.**{ *;}
-keep class com.le.utils.format.**{*;}
-keep class com.le.share.streaming.**{*;}
-dontwarn com.le.utils.format.**

-assumenosideeffects class android.util.Log{
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}