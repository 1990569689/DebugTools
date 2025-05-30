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
#指定代码的压缩级别
-optimizationpasses 5

#包明不混合大小写
-dontusemixedcaseclassnames

#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

 #优化  不优化输入的类文件
-dontoptimize

#不混淆输入的类文件
-dontobfuscate

 #预校验
-dontpreverify

 # 有了verbose这句话，混淆后就会生成映射文件
 # 包含有类名->混淆后类名的映射关系
 # 然后使用printmapping指定映射文件的名称
 -verbose
 -printmapping priguardMapping.txt

 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#保护注解
-keepattributes *Annotation*

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference


#如果引用了v4或者v7包
-dontwarn android.support.**

# 保留Activity中的方法参数是view的方法，
    # 从而我们在layout里面编写onClick就不会影响
    -keepclassmembers class * extends android.app.Activity {
        public void * (android.view.View);
    }

    -keep public class * extends android.view.View {
            public <init>(android.content.Context);
            public <init>(android.content.Context, android.util.AttributeSet);
            public <init>(android.content.Context, android.util.AttributeSet, int);
            public void set*(...);
            *** get* ();
        }

        #保持 native 方法不被混淆
    -keepclasseswithmembernames class * {
            native <methods>;
        }

        #保持自定义控件类不被混淆
    -keepclasseswithmembers class * {
            public <init>(android.content.Context, android.util.AttributeSet);
        }
    #保持 Parcelable 不被混淆
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

    #保持 Serializable 不被混淆
    -keep public class * implements java.io.Serializable {
        public *;
    }

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

    #不混淆资源类
    -keepclassmembers class **.R$* {
        public static <fields>;
    }

    # 对于带有回调函数onXXEvent的，不能混淆
    -keepclassmembers class * {
        void *(**On*Event);
    }

#移除log 测试了下没有用还是建议自己定义一个开关控制是否输出日志
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

-dontwarn com.squareup.**
-keep class com.squareup.**{*; }

-dontwarn org.apache.**
-keep class org.apache.**{*;}

-dontwarn com.tencent.**
-keep class com.tencent.**{*;}

-dontwarn com.sina.**
-keep class com.sina.**{*;}

-dontwarn com.baidu.**
-keep class com.baidu.**{*;}

-dontwarn com.alipay.**
-keep class com.alipay.**{*;}

-dontwarn org.json.alipay.**
-keep class org.json.alipay.**{*;}

-dontwarn org.apache.http.entity.mime.**
-keep class org.apache.http.entity.mime.**{*;}

-dontwarn com.rongkecloud.**
-keep class com.rongkecloud.**{*;}

-dontwarn com.rongke.**
-keep class com.rongke.**{*;}

-dontwarn org.**
-keep class org.**{*;}

-dontwarn a.a.a.a.a.**
-keep class a.a.a.a.a.**{*;}

-dontwarn chat.demo.entity.**
-keep class chat.demo.entity.**{*;}

-dontwarn av.demo.entity.**
-keep class av.demo.entity.**{*;}

 -dontwarn com.google.zxing.**
 -keep class com.google.zxing.**{*;}

-dontwarn com.lking.demo.entity.**
-keep class com.lking.demo.entity.**{*;}

-keep class com.youth.banner.** {
    *;
 }

-keepattributes *JavascriptInterface*


-keepattributes *JavascriptInterface*
-keepclassmembers class com.lking.androidlib.widget.WebView$*{
    *;
}


# Do not strip any method/class that is annotated with @DoNotStrip

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
