package com.fenix.vpn;

import static com.fenix.sshservice.util.SkProtect.FILE_NAME;
import static com.fenix.sshservice.util.SkProtect.isClass;
import static com.fenix.sshservice.util.SkProtect.oOoOoO;
import static com.fenix.sshservice.util.SkProtect.oOoOoOo;
import static com.fenix.vpn.activities.BaseActivity.v7;
import static com.fenix.vpn.activities.BaseActivity.v8;
import static ooOoO.oOoOoOO.oOoOOo;
import static ooOoO.oOoOoOO.oOoOOoO;
import static ooOoO.oOoOoOO.ooOoOOOOo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.fenix.sshservice.FenixCore;
import com.fenix.sshservice.V2Core;
import com.fenix.sshservice.config.Settings;
import com.fenix.sshservice.logger.SkStatus;
import com.fenix.sshservice.tunnel.V2Tunnel;
import com.fenix.sshservice.util.SkProtect;
import ooOoO.ConfigUtil;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;


/**
 * App
 */
public class FenixApp extends Application implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private static final String TAG = FenixApp.class.getSimpleName();
    public static final String PREFS_GERAL = "SocketClayGERAL";
    private final String ooOoooOO = new Object() {
        int wkkdev;
        byte[] s = new byte[]{67, 61, -48, -125, 40, 125};
        public String toString() {
            byte[] buf = new byte[22];
            wkkdev = -1946086252;
            buf[0] = (byte) ((~((wkkdev ^ 1928926356) >>> 19)) ^ s[0]);
            wkkdev = -1152928908;
            buf[1] = (byte) ((~((wkkdev ^ 1150366580) >>> 15)) ^ s[1]);
            wkkdev = 1236705777;
            buf[2] = (byte) ((((wkkdev ^ 1236492785) >>> 14) ^ 0xAA) ^ s[2]);
            wkkdev = -792495978;
            buf[3] = (byte) ((~((wkkdev ^ 798062742) >>> 16)) ^ s[3]);
            wkkdev = -1890878098;
            buf[4] = (byte) ((((wkkdev ^ -1884324498) >>> 16) ^ 0xAA) ^ s[4]);
            wkkdev = -1053187381;
            buf[5] = (byte) ((((wkkdev ^ -1053654325) >>> 12) ^ 0xAA) ^ s[5]);
            wkkdev = -834933417;
            buf[6] = (byte) ((((wkkdev ^ -826479273) >>> 16) ^ 0xAA) ^ s[0]);
            wkkdev = -256227853;
            buf[7] = (byte) ((~((wkkdev ^ 256318963) >>> 11)) ^ s[1]);
            wkkdev = 1335377700;
            buf[8] = (byte) ((~((wkkdev ^ -1335013596) >>> 12)) ^ s[2]);
            wkkdev = 1591533306;
            buf[9] = (byte) ((((wkkdev ^ 1591535354) >>> 11) ^ 0xAA) ^ s[3]);
            wkkdev = -2067810882;
            buf[10] = (byte) ((~((wkkdev ^ 2067870142) >>> 10)) ^ s[4]);
            wkkdev = -701457540;
            buf[11] = (byte) ((((wkkdev ^ -701127812) >>> 11) ^ 0xAA) ^ s[5]);
            wkkdev = -1375014767;
            buf[12] = (byte) ((((wkkdev ^ -1375567727) >>> 12) ^ 0xAA) ^ s[0]);
            wkkdev = 1956662137;
            buf[13] = (byte) ((((wkkdev ^ 1957419897) >>> 12) ^ 0xAA) ^ s[1]);
            wkkdev = 667214105;
            buf[14] = (byte) ((((wkkdev ^ 657776921) >>> 18) ^ 0xAA) ^ s[2]);
            wkkdev = -1714316216;
            buf[15] = (byte) ((((wkkdev ^ -1714377656) >>> 10) ^ 0xAA) ^ s[3]);
            wkkdev = -1126586639;
            buf[16] = (byte) ((((wkkdev ^ -1126557967) >>> 10) ^ 0xAA) ^ s[4]);
            wkkdev = 492725004;
            buf[17] = (byte) ((~((wkkdev ^ -492776692) >>> 12)) ^ s[5]);
            wkkdev = -1113004403;
            buf[18] = (byte) ((((wkkdev ^ -1112890739) >>> 10) ^ 0xAA) ^ s[0]);
            wkkdev = -180439460;
            buf[19] = (byte) ((~((wkkdev ^ 180402780) >>> 10)) ^ s[1]);
            wkkdev = -438267624;
            buf[20] = (byte) ((~((wkkdev ^ 438168856) >>> 10)) ^ s[2]);
            wkkdev = 607320541;
            buf[21] = (byte) ((((wkkdev ^ 606386653) >>> 14) ^ 0xAA) ^ s[3]);
            return new String(buf);
        }
    }.toString();//com,fenix.vpn.FenixApp

    @SuppressLint("StaticFieldLeak")
    private static FenixApp mApp;
    private AppOpenAdManager appOpenAdManager;
    private Activity currentActivity;
    private static String filePath;
    private static long millis;
    private static Settings mConfig;
    public static final String TOKEN_PASS = (new Object() {
        int MDevz;
        public String toString() {
            byte[] buf = new byte[14];
            MDevz = 1806065113;
            buf[0] = (byte) (MDevz >>> 13);
            MDevz = -1291382809;
            buf[1] = (byte) (MDevz >>> 13);
            MDevz = -1440061660;
            buf[2] = (byte) (MDevz >>> 9);
            MDevz = -196705289;
            buf[3] = (byte) (MDevz >>> 13);
            MDevz = 1778096806;
            buf[4] = (byte) (MDevz >>> 7);
            MDevz = -1687166570;
            buf[5] = (byte) (MDevz >>> 3);
            MDevz = -1616222098;
            buf[6] = (byte) (MDevz >>> 9);
            MDevz = -1039591105;
            buf[7] = (byte) (MDevz >>> 7);
            MDevz = 1853995196;
            buf[8] = (byte) (MDevz >>> 17);
            MDevz = 977418133;
            buf[9] = (byte) (MDevz >>> 11);
            MDevz = 1701125060;
            buf[10] = (byte) (MDevz >>> 24);
            MDevz = 2033540014;
            buf[11] = (byte) (MDevz >>> 6);
            MDevz = -1660302939;
            buf[12] = (byte) (MDevz >>> 2);
            MDevz = -283127362;
            buf[13] = (byte) (MDevz >>> 21);
            return new String(buf);
        }
    }.toString());

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        mConfig = new Settings(this);
        FenixCore.init(this);
        SkProtect.init(this);

        filePath = getFilesDir().getAbsolutePath() + "/" + FILE_NAME;
        ooOoOOOOo(filePath, FenixApp.class, mApp, getApplicationInfo().nativeLibraryDir, oOOoO());

        setModoNoturno(this);
        this.registerActivityLifecycleCallbacks(this);
        if (isMainProcess()) {
            MobileAds.initialize(this, initializationStatus -> {});
        }
        SkProtect.BaseApp(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        appOpenAdManager = new AppOpenAdManager();

        if (!oOoOOo("ooOoOOOOo")) {
            oOoOOoO();
        }

        if(!isClass(ooOoooOO)){
            oOoOOoO();
            throw new RuntimeException("");
        }

    }

    private boolean isMainProcess() {
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return getPackageName().equals(processInfo.processName);
            }
        }
        return false;
    }

    public static String getTolito(Context c, String s) {
        Properties props = new Properties();
        try {
            Resources resources = c.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.dnsd_conf);
            props.load(inputStream);
            return props.getProperty(s);
        } catch (IOException e) {
            throw new RuntimeException("");
        }
    }

    public String oOOoO() {
        String abi = Build.SUPPORTED_ABIS[0];
        if (abi.equals(v8)) {
            return oOoOoOo;
        } else if (abi.equals(v7)) {
            return oOoOoO;
        }
        return "0";
    }

    @Keep
    public static boolean oOOoOOo() {
        return SkStatus.isTunnelActive();
    }

    @Keep
    public static void OoOOoOOo(Context c) {
        V2Tunnel.StopV2ray(c);
        V2Core.getInstance().stopCore();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //LocaleHelper.setLocale(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //LocaleHelper.setLocale(this);
    }

    private void setModoNoturno(Context context) {
        boolean is = new Settings(context).getModoNoturno().equals("on");

        int night_mode = is ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(night_mode);

    }

    public static FenixApp getApp() {
        return mApp;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeGround() {
        appOpenAdManager.showAdIfAvailable(currentActivity);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity;
        }
        Log.d(TAG, "onActivityStarted");

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Log.d(TAG, "onActivityResumed");

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        Log.d(TAG, "onActivityPaused");

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        Log.d(TAG, "onActivityStopped");

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        Log.d(TAG, "onActivitySaveInstanceState");

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        Log.d(TAG, "onActivityDestroyed");

    }

    public void showAdIfAvailabel(@NonNull Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener);
    }


    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    private static class AppOpenAdManager {


        private AppOpenAd appOpenAd = null;
        private boolean isLoadingAd = false;
        private boolean isShowingAd = false;

        private long loadTime = 0;


        public AppOpenAdManager() {
        }

        private void loadAd(Context context) {
            if (!mConfig.getPremium()) {
                if (isLoadingAd || isAdAvailable()) {
                    return;
                }
                isLoadingAd = true;
                ConfigUtil configUtil = new ConfigUtil(context);
                AdRequest request = new AdRequest.Builder().build();
                AppOpenAd.load(context, configUtil.OpenApp(), request,
                        new AppOpenAd.AppOpenAdLoadCallback() {
                            @SuppressLint("ShowToast")
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                                Log.d(TAG, "onAdFailedToLoad:" + loadAdError.getMessage());
                                //Toast.makeText(context,"Failed to load ad due to"+ loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdLoaded(@NonNull AppOpenAd ad) {
                                super.onAdLoaded(appOpenAd);
                                appOpenAd = ad;
                                isLoadingAd = false;
                                loadTime = (new Date()).getTime();

                                Log.d(TAG, "onAdLoaded: ");
                                //Toast.makeText(context,"Add Loaded...", Toast.LENGTH_SHORT).show();

                            }
                        }

                );
            } else {
                isLoadingAd = false;
                appOpenAd = null;
            }
        }

        private boolean wassLoadTimeLessThanNHoursAgo() {

            long dateDifference = (new Date()).getTime() - loadTime;
            long numMilliSecondsPerHour = 3600000;
            return (dateDifference < (numMilliSecondsPerHour));
        }


        private boolean isAdAvailable() {
            return appOpenAd != null && wassLoadTimeLessThanNHoursAgo();
        }

        private void showAdIfAvailable(@NonNull final Activity activity) {
            showAdIfAvailable(activity, () -> Log.d(TAG, "onShowAdComplete"));


        }


        private void showAdIfAvailable(@NonNull final Activity activity, @NonNull OnShowAdCompleteListener OnShowAdCompleteListener) {

            if (isShowingAd) {
                Log.d(TAG, "showAdIfAvailable: The app open ad is already showing...");
                return;
            }
            if (!isAdAvailable()) {
                Log.d(TAG, "showAdIfAvailable: The app open ad is not yet ready...");
                OnShowAdCompleteListener.onShowAdComplete();
                loadAd(activity);
                return;
            }

            appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();

                    Log.d(TAG, "onAdClicked:");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    Log.d(TAG, "onAdDismissedFullScreenContent:");
                    appOpenAd = null;
                    isShowingAd = false;

                    OnShowAdCompleteListener.onShowAdComplete();
                    loadAd(activity);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);

                    Log.d(TAG, "onAdFailedToShowFullScreenContent:" + adError.getMessage());
                    appOpenAd = null;
                    isShowingAd = false;

                    OnShowAdCompleteListener.onShowAdComplete();
                    loadAd(activity);

                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();

                    Log.d(TAG, "onAdImpression:");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();

                    Log.d(TAG, "onAdShowedFullScreenContent:");
                }

            });

            isShowingAd = true;
            appOpenAd.show(activity);

        }

    }

}
