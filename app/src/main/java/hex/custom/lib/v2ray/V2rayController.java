package hex.custom.lib.v2ray;

import static android.Manifest.permission.POST_NOTIFICATIONS;
import static android.content.Context.RECEIVER_EXPORTED;
import static com.fenix.sshservice.tunnel.V2Tunnel.stopApp;
import static hex.custom.lib.v2ray.utils.V2rayConstants.V2RAY_SERVICE_COMMAND_EXTRA;
import static hex.custom.lib.v2ray.utils.V2rayConstants.V2RAY_SERVICE_COMMAND_INTENT;
import static hex.custom.lib.v2ray.utils.V2rayConstants.V2RAY_SERVICE_CONFIG_EXTRA;
import static hex.custom.lib.v2ray.utils.V2rayConstants.SERVICE_CONNECTION_STATE_BROADCAST_EXTRA;
import static hex.custom.lib.v2ray.utils.V2rayConstants.V2RAY_SERVICE_CURRENT_CONFIG_DELAY_BROADCAST_EXTRA;
import static hex.custom.lib.v2ray.utils.V2rayConstants.V2RAY_SERVICE_CURRENT_CONFIG_DELAY_BROADCAST_INTENT;
import static hex.custom.lib.v2ray.utils.V2rayConstants.V2RAY_SERVICE_STATICS_BROADCAST_INTENT;
import static hex.custom.lib.v2ray.utils.V2rayConstants.SERVICE_TYPE_BROADCAST_EXTRA;
import static hex.custom.lib.v2ray.utils.V2rayConfigs.connectionState;
import static hex.custom.lib.v2ray.utils.V2rayConfigs.currentConfig;
import static hex.custom.lib.v2ray.utils.V2rayConfigs.serviceMode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.VpnService;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;


import com.fenix.sshservice.config.Settings;
import com.fenix.sshservice.logger.SkStatus;
import com.fenix.sshservice.tunnel.V2Tunnel;
import com.fenix.vpn.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import hex.custom.lib.v2ray.interfaces.LatencyDelayListener;
import hex.custom.lib.v2ray.services.V2rayVPNService;
import hex.custom.lib.v2ray.services.V2rayProxyService;
import hex.custom.lib.v2ray.utils.V2rayConfigs;
import hex.custom.lib.v2ray.utils.Utilities;
import hex.custom.lib.v2ray.utils.V2rayConstants;
import libv2ray.Libv2ray;

public class V2rayController {
    static final BroadcastReceiver stateUpdaterBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                connectionState = (V2rayConstants.CONNECTION_STATES) Objects.requireNonNull(intent.getExtras()).getSerializable(SERVICE_CONNECTION_STATE_BROADCAST_EXTRA);
                if (Objects.equals(intent.getExtras().getString(SERVICE_TYPE_BROADCAST_EXTRA), V2rayProxyService.class.getSimpleName())) {
                    V2rayConfigs.serviceMode = V2rayConstants.SERVICE_MODES.PROXY_MODE;
                } else {
                    V2rayConfigs.serviceMode = V2rayConstants.SERVICE_MODES.VPN_MODE;
                }
            } catch (Exception ignore) {}
        }
    };

    public static void init(final AppCompatActivity activity, final int app_icon, final String app_name) {
        Utilities.copyAssets(activity);
        currentConfig.applicationIcon = app_icon;
        currentConfig.applicationName = app_name;
        registerReceivers(activity);

    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    public static void registerReceivers(final Activity activity){
        try {
            activity.unregisterReceiver(stateUpdaterBroadcastReceiver);
        }catch (Exception ignore){}
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.registerReceiver(stateUpdaterBroadcastReceiver, new IntentFilter(V2RAY_SERVICE_STATICS_BROADCAST_INTENT), RECEIVER_EXPORTED);
        } else {
            activity.registerReceiver(stateUpdaterBroadcastReceiver, new IntentFilter(V2RAY_SERVICE_STATICS_BROADCAST_INTENT));
        }
    }

    public static V2rayConstants.CONNECTION_STATES getConnectionState() {
        return connectionState;
    }

    public static boolean isPreparedForConnection(final Context context) {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS) != PermissionChecker.PERMISSION_GRANTED) {
                return false;
            }
        }
        Intent vpnServicePrepareIntent = VpnService.prepare(context);
        return vpnServicePrepareIntent == null;
    }


    public static void startV2ray(final Context activity, final String remark, final String config, final ArrayList<String> blocked_apps) {
        if (!Utilities.parseV2rayJsonFile(remark, config, blocked_apps)) {
            SkStatus.logInfo("Config JSON Invalida");
            return;
        }
            startTunnel(activity);
    }

    public static void stopV2ray(final Context context) {
        Intent stop_intent = new Intent(V2RAY_SERVICE_COMMAND_INTENT);
        stop_intent.setPackage(context.getPackageName());
        stop_intent.putExtra(V2RAY_SERVICE_COMMAND_EXTRA, V2rayConstants.SERVICE_COMMANDS.STOP_SERVICE);
        context.sendBroadcast(stop_intent);
        Settings config = new Settings(context);
            stopAutoPing();
            stopApp();
        SkStatus.updateStateString(SkStatus.SSH_DESCONECTADO, context.getString(R.string.state_disconnected));
        SkStatus.logInfo("<font color='#FFOOOO'><strong>V2Ray Desconectado</strong></font>");
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    public static void getConnectedV2rayServerDelay(final Context context, final LatencyDelayListener latencyDelayCallback) {
        if (getConnectionState() != V2rayConstants.CONNECTION_STATES.CONNECTED) {
            latencyDelayCallback.OnResultReady(-1);
            return;
        }
        BroadcastReceiver connectionLatencyBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    int delay = Objects.requireNonNull(intent.getExtras()).getInt(V2RAY_SERVICE_CURRENT_CONFIG_DELAY_BROADCAST_EXTRA);
                    latencyDelayCallback.OnResultReady(delay);
                } catch (Exception ignore) {
                    latencyDelayCallback.OnResultReady(-1);
                }
                context.unregisterReceiver(this);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(connectionLatencyBroadcastReceiver, new IntentFilter(V2RAY_SERVICE_CURRENT_CONFIG_DELAY_BROADCAST_INTENT), RECEIVER_EXPORTED);
        } else {
            context.registerReceiver(connectionLatencyBroadcastReceiver, new IntentFilter(V2RAY_SERVICE_CURRENT_CONFIG_DELAY_BROADCAST_INTENT));
        }
        Intent get_delay_intent = new Intent(V2RAY_SERVICE_COMMAND_INTENT);
        get_delay_intent.setPackage(context.getPackageName());
        get_delay_intent.putExtra(V2RAY_SERVICE_COMMAND_EXTRA, V2rayConstants.SERVICE_COMMANDS.MEASURE_DELAY);
        context.sendBroadcast(get_delay_intent);
    }

    private static void updatePingColor(int delayMs) {
        int color;

        if (delayMs <= 100) {
            color = Color.parseColor("#4CAF50");
        } else if (delayMs < 400) {
            color = Color.parseColor("#FFEB3B");
        } else if (delayMs < 700) {
            color = Color.parseColor("#FF9800");
        } else {
            color = Color.parseColor("#F44336");
        }

        SkStatus.logInfo("V2Ray Ping : <font color='" + color + "'>" + delayMs + "</font> ms");
    }

    public static String getCoreVersion() {
        return Libv2ray.checkVersionX();
    }

    public static void toggleConnectionMode() {
        if (serviceMode == V2rayConstants.SERVICE_MODES.PROXY_MODE) {
            serviceMode = V2rayConstants.SERVICE_MODES.VPN_MODE;
        } else {
            serviceMode = V2rayConstants.SERVICE_MODES.PROXY_MODE;
        }
    }

    public static void toggleTrafficStatics() {
        if (currentConfig.enableTrafficStatics) {
            currentConfig.enableTrafficStatics = false;
            currentConfig.enableTrafficStaticsOnNotification = false;
        } else {
            currentConfig.enableTrafficStatics = true;
            currentConfig.enableTrafficStaticsOnNotification = true;
        }
    }

    private static void startTunnel(final Context context) {
        Intent start_intent;
        if (serviceMode == V2rayConstants.SERVICE_MODES.PROXY_MODE) {
            start_intent = new Intent(context, V2rayProxyService.class);
        } else {
            start_intent = new Intent(context, V2rayVPNService.class);
        }
        start_intent.setPackage(context.getPackageName());
        start_intent.putExtra(V2RAY_SERVICE_COMMAND_EXTRA, V2rayConstants.SERVICE_COMMANDS.START_SERVICE);
        SkStatus.logInfo(V2Tunnel.getCoreVersion());
        SkStatus.updateStateString(SkStatus.SSH_INICIANDO, "INICIANDO");
        SkStatus.logInfo("Iniciando V2ray");
        SkStatus.logInfo("Tipo De Conexion : " + detectProtocol(currentConfig.fullJsonConfig));
        SkStatus.logInfo("Verificando Conexion");
        start_intent.putExtra(V2RAY_SERVICE_CONFIG_EXTRA, currentConfig);
        siNoInternet(context);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            context.startForegroundService(start_intent);
        } else {
            context.startService(start_intent);
        }
    }


    public static void siNoInternet(final Context context) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (verificarInternet()) {
                    Log.d("V2Tunnel", "Túnel operativo – Internet a través de VPN");
                    SkStatus.logInfo("Conectando servidor");
                    SkStatus.updateStateString(SkStatus.SSH_CONECTADO, "SSH Connection Established");
                    SkStatus.logInfo("<font color='#4169E1'><strong>V2Ray Conectado</strong></font>");
                    Settings config = new Settings(context);
                        startAutoPing(context);
                    return;
                }
                Log.d("V2Tunnel", "No tienes conexión a internet a través del túnel");
                SkStatus.logInfo("No hay conexión a internet");
                stopApp();
                SkStatus.updateStateString(SkStatus.SSH_DESCONECTADO, context.getString(R.string.state_disconnected));
            }
        }, 2000);
    }

    private static Handler pingHandler = new Handler(Looper.getMainLooper());
    private static Runnable pingRunnable;

    private static void startAutoPing(Context context) {
        Settings config = new Settings(context);
        int pingInterval = config.getSSHPinger();

        pingRunnable = new Runnable() {
            @Override
            public void run() {
                getConnectedV2rayServerDelay(context, delayMs -> {
                    if (delayMs > 0) {
                        updatePingColor((int) delayMs);
                    }
                });
                pingHandler.postDelayed(this, pingInterval);
            }
        };
        pingHandler.post(pingRunnable);
    }

    private static void stopAutoPing() {
        if (pingRunnable != null) {
            pingHandler.removeCallbacks(pingRunnable);
        }
    }

    public static boolean verificarInternet() {
        HttpURLConnection connection = null;
        try {
            int port = currentConfig != null ? currentConfig.localSocksPort : 10808;
            URL url = new URL("http://connectivitycheck.gstatic.com/generate_204");
            Proxy proxy = new Proxy(
                    Proxy.Type.SOCKS,
                    new InetSocketAddress("127.0.0.1", port)
            );
            connection = (HttpURLConnection) url.openConnection(proxy);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2500);
            connection.setReadTimeout(2500);
            connection.setInstanceFollowRedirects(false);
            int responseCode = connection.getResponseCode();
            return (responseCode == 204 || responseCode == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    @Deprecated
    public static boolean IsPreparedForConnection(final Context context) {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS) != PermissionChecker.PERMISSION_GRANTED) {
                return false;
            }
        }
        Intent vpnServicePrepareIntent = VpnService.prepare(context);
        return vpnServicePrepareIntent == null;
    }

    @Deprecated
    public static void StartV2ray(final Context context, final String remark, final String config, final ArrayList<String> blocked_apps) {
        if (!Utilities.parseV2rayJsonFile(remark, config, blocked_apps)) {
            return;
        }
        startTunnel(context);
    }

    @Deprecated
    public static void StopV2ray(final Context context) {
        stopV2ray(context);
    }

    public static String detectProtocol(String v2rayJsonConfig) {
        try {
            JSONObject jsonConfig = new JSONObject(v2rayJsonConfig);
            JSONArray outbounds = jsonConfig.optJSONArray("outbounds");
            if (outbounds != null && outbounds.length() > 0) {
                JSONObject firstOutbound = outbounds.getJSONObject(0);
                return buildProtocolString(firstOutbound);
            }
            JSONObject outbound = jsonConfig.optJSONObject("outbound");
            if (outbound != null) {
                return buildProtocolString(outbound);
            }

            return "Unknown";

        } catch (Exception e) {
            Log.e("V2RayProtocol", "Error detecting protocol", e);
            return "Error";
        }
    }

    private static String buildProtocolString(JSONObject outbound) {
        String protocol = outbound.optString("protocol", "").toLowerCase();
        String protocolType = detectMainProtocol(protocol);
        if (protocol.equals("hysteria") || protocol.equals("hysteria2")) {
            return protocolType;
        }
        String transport = detectTransport(outbound);
        if (!transport.isEmpty()) {
            return protocolType + " | " + transport;
        }
        return protocolType;
    }

    private static String detectMainProtocol(String protocol) {
        switch (protocol) {
            case "vmess":
                return "VMess";
            case "vless":
                return "VLess";
            case "trojan":
                return "Trojan";
            case "shadowsocks":
                return "Shadowsocks";
            case "socks":
                return "SOCKS";
            case "http":
                return "HTTP";
            case "freedom":
                return "Freedom";
            case "blackhole":
                return "Blackhole";
            case "dns":
                return "DNS";
            case "hysteria":
            case "hysteria2":
                return "Hysteria 2";
            default:
                return protocol.isEmpty() ? "Unknown" : protocol;
        }
    }

    private static String detectTransport(JSONObject outbound) {
        try {
            JSONObject streamSettings = outbound.optJSONObject("streamSettings");
            if (streamSettings == null) {
                return "";
            }
            String network = streamSettings.optString("network", "").toLowerCase();
            switch (network) {
                case "ws":
                    return "WebSocket";
                case "http":
                    return "HTTP";
                case "grpc":
                    return "gRPC";
                case "quic":
                    return "QUIC";
                case "tcp":
                    return "TCP";
                case "kcp":
                    return "KCP";
                case "domainsocket":
                    return "Domain Socket";
                case "xhttp":
                    return "xHTTP";
                default:
                    return network.isEmpty() ? "" : network;
            }
        } catch (Exception e) {
            return "";
        }
    }
    }
