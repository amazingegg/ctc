# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


#下面是常见的proguard.cfg配置项
#指定代码的压缩级别
-optimizationpasses 5
#包名不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
# 指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers
#优化  不优化输入的类文件
-dontoptimize
#预校验
-dontpreverify
#混淆时是否记录日志
-verbose
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*

#忽略警告
-ignorewarning

##记录生成的日志数据,gradle build时在本项目根目录输出##
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射-printmapping mapping.txt
########记录生成的日志数据，gradle build时 在本项目根目录输出-end#####

#需要保留的东西
# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class com.android.vending.licensing.ILicensingService

#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment

##########JS接口类不混淆，否则执行不了
-dontwarn com.android.JsInterface.**
-keep class com.android.JsInterface.** {*; }

#极光推送和百度lbs android sdk一起使用proguard 混淆的问题#http的类被混淆后，导致apk定位失败，保持apache 的http类不被混淆就好了
-dontwarn org.apache.**
-keep class org.apache.**{ *; }

-keep public class * extends android.view.View {
  public <init>(android.content.Context);
  public <init>(android.content.Context, android.util.AttributeSet);
  public <init>(android.content.Context, android.util.AttributeSet, int);
  public void set*(...);
 }

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
  native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
  public <init>(android.content.Context, android.util.AttributeSet);
}

#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
  public void *(android.view.View);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
-keepclassmembers enum * {
      public static **[] values();
      public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
      public void *ButtonClicked(android.view.View);
}

#不混淆资源类
-keepclassmembers class **.R$* {
      public static <fields>;
}

#避免混淆泛型 如果混淆报错建议关掉
#–keepattributes Signature

######===========================混淆保护自己项目的部分代码以及引用的第三方jar包library=======================########
#如果引用了v4或者v7包
-dontwarn android.support.**

#gson
#-libraryjars libs/gson-2.2.2.jar
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.** { *;}
#过滤掉存放所有JavaBean（实体类)的包不进行混淆编译
-keep class com.android.model.** {*;}
-keep class com.atkj.ctc.bean** {*;}

#butterknife 的混淆代码
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
   @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
   @butterknife.* <methods>;
}

#网络相关混淆代码
#okhttputils
-dontwarn com.zhy.http.**
-keep class com.zhy.http.**{*;}
#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
#okio
-dontwarn okio.**
-keep class okio.**{*;}

#loadsir 的混淆代码
-dontwarn com.kingja.loadsir.**
-keep class com.kingja.loadsir.** {*;}


# glide 的混淆代码
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
   **[] $VALUES;
   public *;
 }

# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }

##Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

##ShareSDK
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}

##EvenBus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
#-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
#    <init>(java.lang.Throwable);
#}

####============================混淆保护自己项目的部分代码以及引用的第三方jar包library==========================-end####