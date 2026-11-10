package com.fenix.sshservice.tunnel;


import static com.fenix.sshservice.LaunchVpn.ismodev2ray;
import static com.fenix.sshservice.util.V2Utilities.normalizeV2rayFullConfig;
import static com.fenix.vpn.FenixMainActivity.URLServer;
import static ooOoO.oOoOoOO.oOoOOOoo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.fenix.sshservice.FenixVpnService;
import com.fenix.sshservice.LaunchVpn;
import com.fenix.sshservice.V2Configs;
import com.fenix.sshservice.V2Core;
import com.fenix.sshservice.V2Service;
import com.fenix.sshservice.config.Settings;
import com.fenix.sshservice.logger.SkStatus;
import com.fenix.sshservice.tunnel.vpn.TunnelVpnService;
import com.fenix.sshservice.tunnel.vpn.V2Listener;
import com.fenix.sshservice.util.V2Utilities;
import com.google.webview.Servers;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import libv2ray.Libv2ray;
import android.util.Log;

public class V2Tunnel {
    private static final String TAG = "HevSocks5";
    private static V2Listener v2Listener;
    private static Context mContext;
    private static BroadcastReceiver delayReceiver;

    public static Timer timer = null;
    public static boolean shouldRetry = true;
    private static SharedPreferences prefsPrivate;
    private static SharedPreferences.Editor editPrivate;
    private static Settings mConfig;
    private static Servers loadServer;
    private static volatile boolean isTimerRunning = false;

    public V2Tunnel(Context context) {
        v2Listener = TunnelManagerThread.getV2rayServicesListener();
        mContext = context;
        mConfig = new Settings(context);
        prefsPrivate = mConfig.getPrefsPrivate();
        editPrivate = prefsPrivate.edit();
        loadServer = new Servers(context);
    }

    public static void init(final Context context, final int app_icon, final String app_name) {
        V2Utilities.copyAssets(context);
        V2Configs.APPLICATION_ICON = app_icon;
        V2Configs.APPLICATION_NAME = app_name;

        ContextCompat.registerReceiver(context, new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                V2Configs.V2RAY_STATE = (V2Configs.V2RAY_STATES) arg1.getExtras().getSerializable("STATE");
                // Cuando V2Ray se conecta, notificar al listener
                if (V2Configs.V2RAY_STATE == V2Configs.V2RAY_STATES.V2RAY_CONNECTED && v2Listener != null) {
                    v2Listener.onConnected();
                }
            }
        }, new IntentFilter("V2RAY_CONNECTION_INFO"), ContextCompat.RECEIVER_NOT_EXPORTED);
    }

    public static void StartV2ray(final Context context, final String remark, final String config, final ArrayList<String> blocked_apps) {
        Log.i(TAG, "═══════════════════════════════════════");
        Log.i(TAG, "🚀 V2Tunnel.StartV2ray()");
        Log.i(TAG, "   Remark: " + remark);
        Log.i(TAG, "   Config length: " + (config != null ? config.length() : 0));
        Log.i(TAG, "   Blocked apps: " + (blocked_apps != null ? blocked_apps.size() : 0));
        Log.i(TAG, "═══════════════════════════════════════");
        
        V2Configs.V2RAY_CONFIG = V2Utilities.parseV2rayJsonFile(remark, config, blocked_apps);
        if (V2Configs.V2RAY_CONFIG == null) {
            Log.e(TAG, "❌ V2Config parse failed (returned null)");
            v2Listener.onError();
            SkStatus.logInfo("V2Ray Error");
            return;
        }
        
        Log.i(TAG, "✅ V2Config parsed OK");
        Log.i(TAG, "   SOCKS5 port: " + V2Configs.V2RAY_CONFIG.LOCAL_SOCKS5_PORT);
        Log.i(TAG, "   HTTP port: " + V2Configs.V2RAY_CONFIG.LOCAL_HTTP_PORT);
        Log.i(TAG, "   Server: " + V2Configs.V2RAY_CONFIG.CONNECTED_V2RAY_SERVER_ADDRESS);
        Log.i(TAG, "   Mode: " + V2Configs.V2RAY_CONNECTION_MODE);

        Intent start_intent;
        if (V2Configs.V2RAY_CONNECTION_MODE == V2Configs.V2RAY_CONNECTION_MODES.VPN_TUN) {
            start_intent = new Intent(context, V2Service.class);
            Log.i(TAG, "   Using V2Service (VPN_TUN mode)");
        } else {
            Log.e(TAG, "❌ Unsupported connection mode: " + V2Configs.V2RAY_CONNECTION_MODE);
            v2Listener.onError();
            SkStatus.logInfo("V2Ray Error");
            return;
        }

        start_intent.putExtra("COMMAND", V2Configs.V2RAY_SERVICE_COMMANDS.START_SERVICE);
        start_intent.putExtra("V2RAY_CONFIG", V2Configs.V2RAY_CONFIG);
        
        String coreVer = V2Tunnel.getCoreVersion();
        Log.i(TAG, "   Core version: " + coreVer);
        SkStatus.logInfo(coreVer);
        
        // Notificar UI
        v2Listener.startService();
        SkStatus.logInfo("Iniciando V2ray");
        
        // Iniciar el servicio V2Service en proceso separado
        Log.i(TAG, "⏳ Starting V2Service via startForegroundService...");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            context.startForegroundService(start_intent);
        } else {
            context.startService(start_intent);
        }
        Log.i(TAG, "✅ V2Service intent sent");

        Settings settings = new Settings(context);
        if (!settings.getPremium()) {
            isTimerRunning = true;
            new Thread(() -> {
                while (isTimerRunning) {
                    if (oOoOOOoo() <= 0) {
                        Log.i(TAG, "⚠ Premium check failed, stopping core...");
                        V2Core.getInstance().stopCore();
                        TunnelManagerHelper.stopSocketClay(context);
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }).start();
        }
    }

    public static void StopV2ray(final Context context) {
        Log.i(TAG, "🛑 V2Tunnel.StopV2ray()");
        Intent stop_intent;
        if (V2Configs.V2RAY_CONNECTION_MODE == V2Configs.V2RAY_CONNECTION_MODES.PROXY_ONLY) {
            stop_intent = new Intent(context, V2Proxy.class);
        } else if (V2Configs.V2RAY_CONNECTION_MODE != V2Configs.V2RAY_CONNECTION_MODES.VPN_TUN) {
            return;
        } else {
            stop_intent = new Intent(context, V2Service.class);
        }
        stop_intent.putExtra("COMMAND", V2Configs.V2RAY_SERVICE_COMMANDS.STOP_SERVICE);
        context.startService(stop_intent);
        V2Configs.V2RAY_CONFIG = null;

        stopCheckingNetwork();
        isTimerRunning = false;
    }

    public static void stopCheckingNetwork() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        shouldRetry = false;

    }

    public static V2Configs.V2RAY_STATES getConnectionState() {
        return V2Configs.V2RAY_STATE;
    }

    public static String getCoreVersion() {
        return Libv2ray.checkVersionX();
    }

    @SuppressLint("DefaultLocale")
    public static String getV2rayServerDelay(final String config) {
        final long server_delay = V2Core.getInstance().getV2rayServerDelay(config);
        if (server_delay == -1L) {
            return "Network or Server Error";
        } else {
            return String.format("<strong><font color=\"#228B22\">%d ms</font></strong>", server_delay);
        }
    }

    public static void stopAllServices(Context activity) {
        StopV2ray(activity);
    }

    public static void stopApp(){
        Intent stopTunnel = new Intent(FenixVpnService.TUNNEL_SSH_STOP_SERVICE);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(stopTunnel);
    }

    public void getLocalProxy(){
        if(ismodev2ray()){
            StopV2ray(mContext);
            stopApp();
        } else {
            stopApp();

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e2) {
                stopApp();
            }

            if (SkStatus.isTunnelActive()) {
                TunnelVpnService tunnelVpnService = new TunnelVpnService();
                tunnelVpnService.broadcastVpnDisconnect();
            }
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e2) {
            stopApp();
        }

        if (SkStatus.isTunnelActive()) {
            TunnelManagerHelper.stopSocketClay(mContext);
        }
    }

    // siNoInternet eliminado: causaba falsos "Error al conectar"
    // porque verificaba delay desde el proceso principal donde V2Core no está inicializado.
    // V2Ray/hev-socks5 ya maneja reconexión internamente.

    public static boolean verificarInternet() {
        try {
            URL url = new URL("https://www.google.com/generate_204");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);

            return conn.getResponseCode() == 204;
        } catch (Exception e) {
            return false;
        }
    }

    public static void checkUser(Context context) {
        String current_user = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    URL url = new URL(URLServer);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("user", current_user);
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());
                    os.flush();
                    os.close();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                            String result;

                            while ((result = bufferedReader.readLine()) != null) {
                                String resultFinal = result.replace("\"", "");
                                if (resultFinal.equals("Not exist")) {
                                    StopV2ray(mContext);
                                    v2Listener.onError();
                                    SkStatus.logInfo("<font color='red'><strong>Token vencido o sin registar</strong></font>");

                                }
                            }
                        }
                    } else {
                        StopV2ray(mContext);
                        v2Listener.onError();
                        SkStatus.logInfo("<font color='red'><strong>Token vencido o sin registar</strong></font>");
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1500);
    }

    public static void startOrStopTunnel(Activity activity) {
        Settings config = new Settings(activity);

        Intent intent = new Intent(activity, LaunchVpn.class);
        intent.setAction(Intent.ACTION_MAIN);

        if (config.getHideLog()) {
            intent.putExtra(LaunchVpn.EXTRA_HIDELOG, true);
        }

        activity.startActivity(intent);
    }

}
