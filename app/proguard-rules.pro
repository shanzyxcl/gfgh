# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
-obfuscationdictionary dictionary2.txt
-classobfuscationdictionary dictionary.txt
-packageobfuscationdictionary dictionary2.txt
-optimizationpasses 5
-mergeinterfacesaggressively
-dontoptimize

-keep class com.trilead.** { *; }
-keep class com.jcraft.jzlib.** { *; }
-keep class net.** { *; }
-keep class org.** { *; }
-keep class go.** { *; }
-keep class ooOoO.** { *; }
-keep class com.fenix.vpn.preference**{ *; }
-keep class com.google.android.gms.ads.** { *; }
-keep interface com.google.android.gms.ads.** { *; }

# Mantener clase JNI con sus métodos nativos (hev-socks5-tunnel)
-keep class hev.htproxy.TProxyService { *; }
-keepclassmembers class hev.htproxy.TProxyService {
    native <methods>;
    static <methods>;
}
-keep class com.v2ray.ang.service.TProxyService { *; }
-keepclasseswithmembers class com.v2ray.ang.service.TProxyService {
    native <methods>;
}
# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn com.android.org.conscrypt.SSLParametersImpl
-dontwarn org.apache.harmony.xnet.provider.jsse.SSLParametersImpl
-dontwarn sun.security.x509.X509Key
