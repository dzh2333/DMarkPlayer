-optimizationpasses 5
-ignorewarnings
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-useuniqueclassmembernames
-allowaccessmodification
-dontpreverify
-verbose
-dontoptimize
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature
-keepattributes *Annotation*
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}
-keep public class **.R$*{
    public static final int *;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keepclassmembers class * {
    native <methods>;
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(***);
    public *** set*(***);
    public *** get*(***);
    public *** get*();
}
-keep class android.support.annotation.Keep
-keep @android.support.annotation.Keep class * {*;}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}
-keep public class * implements android.os.Parcelable{*;}
-keepclasseswithmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#EventBus bean
-keep class com.mark.cyberpunkplayer.event.**{*;}
#EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-keep class com.google.android.material.** {*;}

-keep class androidx.** {*;}

-keep public class * extends androidx.**

-keep interface androidx.** {*;}

-dontwarn com.google.android.material.**

-dontnote com.google.android.material.**

-dontwarn androidx.**


#Glide
-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.**{*;}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#GreenDao bean
-keep class com.mark.cyberpunkplayer.db.**{*;}
#GreenDao
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use RxJava:
-dontwarn rx.**
### greenDAO 2
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

-keep class com.orhanobut.**{*;}

-keep class com.mark.cyberpunkplayer.player.**{*;}

-keep public class org.cybergarage.**{*;}