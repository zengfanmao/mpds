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





-ignorewarnings

-dontoptimize
-dontusemixedcaseclassnames
-verbose
-dontshrink
-dontoptimize
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers

-dontnote **
-dontwarn **

-dontwarn dalvik.**

-dontwarn android.support.**
-keep class android.support.** { *; }
-keep interface android.support.** { *; }

-keep public class **.R$*{
   public static final int *;
}


#BaseRecyclerViewAdapterHelper
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}


 #EventBus
 -keepattributes *Annotation*
 -keepclassmembers class ** {
     @org.greenrobot.eventbus.Subscribe <methods>;
 }
 -keep enum org.greenrobot.eventbus.ThreadMode { *; }

 # Only required if you use AsyncExecutor
 -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
     <init>(java.lang.Throwable);
 }



#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
#okio
-dontwarn okio.**
-keep class okio.**{*;}

-dontwarn android.support.**
-keep class android.support.** { *; }
-keep interface android.support.** { *; }

#3D 地图 V5.0.0之后：
#-keep   class com.amap.api.maps.**{*;}
#-keep   class com.autonavi.**{*;}
#-keep   class com.amap.api.trace.**{*;}

#百度地图
-keep class com.baidu.** {*;}
-keep class mapsdkvi.com.** {*;}
-dontwarn com.baidu.**



# greenDAO 3.2
-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
# 数据库加密
-keep class net.sqlcipher.database.**{*;}
-keep public interface net.sqlcipher.database.**
-keep class data.db.dao.*$Properties {
    public static <fields>;
}
-keepclassmembers class data.db.dao.** {
    public static final <fields>;
 }
-keep public class net.sqlcipher.**{*;}
-keep public class net.sqlcipher.database.**{*;}


#RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-dontwarn     com.squareup.picasso.*
-keep class   com.squareup.picasso.** { *;}

-dontwarn     com.aimissu.ptt.entity.*
-keep public class   com.aimissu.ptt.entity.** { *;}

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

-keep public class * implements java.lang.annotation.Annotation
-keep public class * extends java.lang.annotation.**{*;}

-dontwarn com.tencent.**
-keep public class com.tencent.**{*;}
-keep interface com.tencent.** { *;}

-dontwarn org.eclipse.paho.client.mqttv3.**
-keep public class org.eclipse.paho.client.mqttv3.**{*;}


-dontwarn com.grgbanking.video.**
-keep  class com.grgbanking.video.**{*;}
-keep  interface com.grgbanking.video.**{*;}
-dontwarn org.linphone.**
-keep  interface org.linphone.**{*;}
-keep  class org.linphone.**{*;}
-keep  class org.apache.commons.**{*;}


#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

#PictureSelector 2.0
-keep class com.luck.picture.lib.** { *; }

-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

 #rxjava
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#rxandroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule


-dontwarn com.mabeijianxi.smallvideorecord2**
-keep class com.mabeijianxi.smallvideorecord2** { *; }

#GSYVideoPlayer 混淆
-keep class com.shuyu.gsyvideoplayer.video.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.**
-keep class com.shuyu.gsyvideoplayer.video.base.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.base.**
-keep class com.shuyu.gsyvideoplayer.utils.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.utils.**
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}