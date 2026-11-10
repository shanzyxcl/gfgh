package com.fenix.sshservice;

import static com.fenix.sshservice.util.V2Utilities.getDeviceIdForXUDPBaseKey;
import static com.fenix.sshservice.util.V2Utilities.getUserAssetsPath;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.fenix.sshservice.config.V2Config;
import com.fenix.sshservice.tunnel.vpn.V2Listener;
import com.fenix.sshservice.util.V2Utilities;
import com.fenix.vpn.R;

import org.json.JSONObject;

import java.util.Objects;
import go.Seq;
import libv2ray.*;

/**
 * V2Ray core manager - singleton.
 * 
 * IMPORTANTE: Cada vez que se inicia el core se crea un NUEVO CoreController,
 * igual que HZV2ray hace en V2rayCoreExecutor.kt.
 */
public final class V2Core {
    private static final String TAG = "HevSocks5";
    private volatile static V2Core INSTANCE;
    public V2Listener v2Listener = null;
    private boolean isLibV2rayCoreInitialized = false;
    public V2Configs.V2RAY_STATES V2RAY_STATE = V2Configs.V2RAY_STATES.V2RAY_DISCONNECTED;
    private CountDownTimer countDownTimer;
    private int seconds, minutes, hours;
    private long totalDownload, totalUpload, uploadSpeed, downloadSpeed;
    private String SERVICE_DURATION = "00:00:00";
    private NotificationManager mNotificationManager = null;
    
    // Ahora NO es final - se crea nuevo en cada startCore() como HZV2ray
    private CoreController coreController = null;
    
    private static final String CHANNEL_ID = "WakkoServiceChannel";

    private V2Core() {
        Log.i(TAG, "═══════════════════════════════════════");
        Log.i(TAG, "⏳ V2Core singleton created");
        Log.i(TAG, "   (coreController se crea en startCore, no aquí)");
        Log.i(TAG, "═══════════════════════════════════════");
    }

    public static V2Core getInstance() {
        if (INSTANCE == null) {
            synchronized (V2Core.class) {
                if (INSTANCE == null) {
                    INSTANCE = new V2Core();
                }
            }
        }
        return INSTANCE;
    }

    private void makeDurationTimer(final Context context, final boolean enable_traffic_statics) {
        countDownTimer = new CountDownTimer(7200, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onTick(long millisUntilFinished) {
                seconds++;
                if (seconds == 59) {
                    minutes++;
                    seconds = 0;
                }
                if (minutes == 59) {
                    minutes = 0;
                    hours++;
                }
                if (hours == 23) {
                    hours = 0;
                }
                if (enable_traffic_statics && coreController != null) {
                    try {
                        downloadSpeed = coreController.queryStats("block", "downlink") + coreController.queryStats("proxy", "downlink");
                        uploadSpeed = coreController.queryStats("block", "uplink") + coreController.queryStats("proxy", "uplink");
                        totalDownload = totalDownload + downloadSpeed;
                        totalUpload = totalUpload + uploadSpeed;
                    } catch (Exception e) {
                        // ignore
                    }
                }
                SERVICE_DURATION = V2Utilities.convertIntToTwoDigit(hours) + ":" + V2Utilities.convertIntToTwoDigit(minutes) + ":" + V2Utilities.convertIntToTwoDigit(seconds);
                Intent connection_info_intent = new Intent("V2RAY_CONNECTION_INFO");
                connection_info_intent.putExtra("STATE", V2Core.getInstance().V2RAY_STATE);
                connection_info_intent.putExtra("DURATION", SERVICE_DURATION);
                connection_info_intent.putExtra("UPLOAD_SPEED", V2Utilities.parseTraffic(uploadSpeed, false, true));
                connection_info_intent.putExtra("DOWNLOAD_SPEED", V2Utilities.parseTraffic(downloadSpeed, false, true));
                connection_info_intent.putExtra("UPLOAD_TRAFFIC", V2Utilities.parseTraffic(totalUpload, false, false));
                connection_info_intent.putExtra("DOWNLOAD_TRAFFIC", V2Utilities.parseTraffic(totalDownload, false, false));
                context.sendBroadcast(connection_info_intent);
            }

            public void onFinish() {
                countDownTimer.cancel();
                if (V2Core.getInstance().isV2rayCoreRunning())
                    makeDurationTimer(context, enable_traffic_statics);
            }
        }.start();
    }

    public void setUpListener(Service targetService) {
        try {
            Log.i(TAG, "═══════════════════════════════════════");
            Log.i(TAG, "🔄 setUpListener() from " + targetService.getClass().getSimpleName());
            Log.i(TAG, "═══════════════════════════════════════");
            
            v2Listener = (V2Listener) targetService;
            Seq.setContext(targetService);
            
            String assetsPath = getUserAssetsPath(targetService.getApplicationContext());
            String deviceId = getDeviceIdForXUDPBaseKey();
            Log.i(TAG, "   Assets path: " + assetsPath);
            Log.i(TAG, "   Device ID (base64): " + deviceId);
            
            Libv2ray.initCoreEnv(assetsPath, deviceId);
            isLibV2rayCoreInitialized = true;
            
            SERVICE_DURATION = "00:00:00";
            seconds = 0;
            minutes = 0;
            hours = 0;
            uploadSpeed = 0;
            downloadSpeed = 0;
            
            Log.i(TAG, "✅ setUpListener completed OK");
            Log.i(TAG, "   Core version: " + Libv2ray.checkVersionX());
        } catch (Exception e) {
            Log.e(TAG, "❌ setUpListener failed => ", e);
            isLibV2rayCoreInitialized = false;
        }
    }

    public boolean startCore(final V2Config v2Config) {
        Log.i(TAG, "═══════════════════════════════════════");
        Log.i(TAG, "🚀 V2Core.startCore()");
        Log.i(TAG, "   Remark: " + v2Config.REMARK);
        Log.i(TAG, "   SOCKS5 port: " + v2Config.LOCAL_SOCKS5_PORT);
        Log.i(TAG, "   Traffic statics: " + v2Config.ENABLE_TRAFFIC_STATICS);
        Log.i(TAG, "   Core initialized: " + isLibV2rayCoreInitialized);
        Log.i(TAG, "═══════════════════════════════════════");
        
        if (v2Listener == null) {
            Log.e(TAG, "❌ startCore failed => v2Listener is null");
            return false;
        }
        
        makeDurationTimer(v2Listener.getService().getApplicationContext(), v2Config.ENABLE_TRAFFIC_STATICS);
        V2RAY_STATE = V2Configs.V2RAY_STATES.V2RAY_CONNECTING;
        
        if (!isLibV2rayCoreInitialized) {
            Log.e(TAG, "❌ startCore failed => LibV2rayCore not initialized.");
            return false;
        }
        
        // Si ya hay un core corriendo, pararlo primero
        if (coreController != null) {
            try {
                if (coreController.getIsRunning()) {
                    Log.w(TAG, "⚠ Core already running, stopping first...");
                    coreController.stopLoop();
                }
            } catch (Exception e) { /* ignore */ }
        }
        
        try {
            // --- CREAR NUEVO CoreController como HZV2ray ---
            Log.i(TAG, "🔧 Creating new CoreController...");
            final V2Listener currentListener = v2Listener; // capturar el listener actual
            
            CoreController newController = Libv2ray.newCoreController(new CoreCallbackHandler() {
                @Override
                public long shutdown() {
                    Log.i(TAG, "🛑 CoreCallbackHandler.shutdown() called");
                    if (currentListener == null) {
                        Log.e(TAG, "shutdown failed => can`t find initial service.");
                        return -1;
                    }
                    try {
                        currentListener.stopService();
                        return 0;
                    } catch (Exception e) {
                        Log.e(TAG, "shutdown failed =>", e);
                        return -1;
                    }
                }
                @Override
                public long startup() {
                    Log.i(TAG, "🚀 CoreCallbackHandler.startup() called - V2Ray está listo!");
                    if (currentListener != null) {
                        try {
                            currentListener.startService();
                        } catch (Exception e) {
                            Log.e(TAG, "setup failed => ", e);
                            return -1;
                        }
                    } else {
                        Log.w(TAG, "⏳ startup() called but listener is null");
                    }
                    return 0;
                }
                @Override
                public long onEmitStatus(long l, String s) {
                    if (s != null && !s.isEmpty()) {
                        Log.d(TAG, "📊 CoreCallbackHandler.onEmitStatus(" + l + ", '" + s + "')");
                    }
                    return 0;
                }
            });
            
            coreController = newController;
            
            Log.i(TAG, "⏳ Calling coreController.startLoop()...");
            Log.i(TAG, "📋 FULL V2RAY CONFIG:\n" + v2Config.V2RAY_FULL_JSON_CONFIG);
            coreController.startLoop(v2Config.V2RAY_FULL_JSON_CONFIG, -1);
            
            V2RAY_STATE = V2Configs.V2RAY_STATES.V2RAY_CONNECTED;
            Log.i(TAG, "✅ startLoop returned successfully");
            Log.i(TAG, "   IsRunning: " + isV2rayCoreRunning());
            
            if (isV2rayCoreRunning()) {
                showNotification(v2Config);
                return true;
            }
            Log.w(TAG, "⚠ startLoop returned but core reports not running");
            return true;
        } catch (Exception e) {
            sendDisconnectedBroadCast();
            Log.e(TAG, "❌ startCore failed =>", e);
            return false;
        }
    }

    public void stopCore() {
        Log.i(TAG, "═══════════════════════════════════════");
        Log.i(TAG, "🛑 V2Core.stopCore()");
        Log.i(TAG, "═══════════════════════════════════════");
        try {
            // Detener foreground notification
            try {
                if (v2Listener != null) {
                    v2Listener.getService().stopForeground(true);
                }
            } catch (Exception ignored) {}
            
            if (coreController != null && coreController.getIsRunning()) {
                Log.i(TAG, "  Stopping V2Ray loop...");
                coreController.stopLoop();
                if (v2Listener != null) {
                    Log.i(TAG, "  Calling v2Listener.stopService()...");
                    v2Listener.stopService();
                }
                Log.i(TAG, "✅ stopCore success => v2ray core stopped.");
            } else {
                Log.i(TAG, "  Core not running, nothing to stop.");
            }
            coreController = null;
            sendDisconnectedBroadCast();
        } catch (Exception e) {
            Log.e(TAG, "❌ stopCore failed =>", e);
        }
    }

    private NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            try {
                mNotificationManager = (NotificationManager) v2Listener.getService().getSystemService(Context.NOTIFICATION_SERVICE);
            } catch (Exception e) {
                return null;
            }
        }
        return mNotificationManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String createNotificationChannelID(final String Application_name) {
        String notification_channel_id = CHANNEL_ID;
        NotificationChannel notificationChannel = new NotificationChannel(notification_channel_id, "V2ray Service", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setLightColor(socketclay.compatstyle.R.color.blue);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationChannel.setImportance(NotificationManager.IMPORTANCE_NONE);
        Objects.requireNonNull(getNotificationManager()).createNotificationChannel(notificationChannel);
        return notification_channel_id;
    }
    private int judgeForNotificationFlag() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT;
        } else {
            return PendingIntent.FLAG_UPDATE_CURRENT;
        }
    }
    private void showNotification(final V2Config v2Config) {
        if (v2Listener == null) {
            return;
        }
        Intent launchIntent = v2Listener.getService().getPackageManager().getLaunchIntentForPackage(v2Listener.getService().getApplicationInfo().packageName);
        launchIntent.setAction("FROM_DISCONNECT_BTN");
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notificationContentPendingIntent = PendingIntent.getActivity(v2Listener.getService(), 0, launchIntent, judgeForNotificationFlag());
        String notificationChannelID = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannelID = createNotificationChannelID(v2Config.APPLICATION_NAME);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(v2Listener.getService(), notificationChannelID);
        mBuilder.setSmallIcon(R.drawable.ic_wkk_bw2)
                .setContentTitle("V2Ray Conectado")
                .setContentText("HTTP Fenix V2Ray - " + v2Config.REMARK)
                .setOngoing(true)
                .setShowWhen(false)
                .setOnlyAlertOnce(true)
                .setContentIntent(notificationContentPendingIntent)
                .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE);
        v2Listener.getService().startForeground(1, mBuilder.build());
    }

    private void sendDisconnectedBroadCast() {
        V2RAY_STATE = V2Configs.V2RAY_STATES.V2RAY_DISCONNECTED;
        SERVICE_DURATION = "00:00:00";
        seconds = 0;
        minutes = 0;
        hours = 0;
        uploadSpeed = 0;
        downloadSpeed = 0;
        if (v2Listener != null) {
            Intent connection_info_intent = new Intent("V2RAY_CONNECTION_INFO");
            connection_info_intent.putExtra("STATE", V2Core.getInstance().V2RAY_STATE);
            connection_info_intent.putExtra("DURATION", SERVICE_DURATION);
            connection_info_intent.putExtra("UPLOAD_SPEED", V2Utilities.parseTraffic(0, false, true));
            connection_info_intent.putExtra("DOWNLOAD_SPEED", V2Utilities.parseTraffic(0, false, true));
            connection_info_intent.putExtra("UPLOAD_TRAFFIC", V2Utilities.parseTraffic(0, false, false));
            connection_info_intent.putExtra("DOWNLOAD_TRAFFIC", V2Utilities.parseTraffic(0, false, false));
            try {
                v2Listener.getService().getApplicationContext().sendBroadcast(connection_info_intent);
            } catch (Exception e) {
                //ignore
            }
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public boolean isV2rayCoreRunning() {
        if (coreController != null) {
            try {
                return coreController.getIsRunning();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public Long getConnectedV2rayServerDelay() {
        if (coreController == null) return -1L;
        try {
            return coreController.measureDelay("");
        } catch (Exception e) {
            return -1L;
        }
    }

    public Long getV2rayServerDelay(final String config) {
        try {
            try {
                JSONObject config_json = new JSONObject(config);
                JSONObject new_routing_json = config_json.getJSONObject("routing");
                new_routing_json.remove("rules");
                config_json.remove("routing");
                config_json.put("routing", new_routing_json);
                return Libv2ray.measureOutboundDelay(config_json.toString(), "");
            } catch (Exception json_error) {
                Log.e("getV2rayServerDelay", json_error.toString());
                return Libv2ray.measureOutboundDelay(config, "");
            }
        } catch (Exception e) {
            Log.e("getV2rayServerDelayCore", e.toString());
            return -1L;
        }
    }
}
