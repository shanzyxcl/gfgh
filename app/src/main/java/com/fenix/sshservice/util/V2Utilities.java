package com.fenix.sshservice.util;

import android.content.Context;
import android.provider.Settings;
import android.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import android.util.Log;

import com.fenix.sshservice.V2Configs;
import com.fenix.sshservice.config.V2Config;
import com.fenix.sshservice.tunnel.V2rayConfigConverter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class V2Utilities {

    public static String getDeviceIdForXUDPBaseKey() {
        String androidId = Settings.Secure.ANDROID_ID;
        byte[] androidIdBytes = androidId.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(Arrays.copyOf(androidIdBytes, 32), Base64.NO_PADDING | Base64.URL_SAFE);
    }

    public static void CopyFiles(InputStream src, File dst) throws IOException {
        try (OutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = src.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        }
    }

    public static String getUserAssetsPath(Context context) {
        File extDir = context.getExternalFilesDir("assets");
        if (extDir == null) {
            return "";
        }
        if (!extDir.exists()) {
            return context.getDir("assets", 0).getAbsolutePath();
        } else {
            return extDir.getAbsolutePath();
        }
    }

    public static void copyAssets(final Context context) {
        String extFolder = getUserAssetsPath(context);
        try {
            String geo = "geosite.dat,geoip.dat";
            for (String assets_obj : context.getAssets().list("")) {
                if (geo.contains(assets_obj)) {
                    CopyFiles(context.getAssets().open(assets_obj), new File(extFolder, assets_obj));
                }
            }
        } catch (Exception e) {
            Log.e("V2Utilities", "copyAssets failed=>", e);
        }
    }

    public static String normalizeV2rayFullConfig(String config) {
        // Usar V2rayConfigConverter para convertir URIs a JSON
        String converted = V2rayConfigConverter.convert(config, 10808, null);
        if (converted.startsWith("{")) {
            return converted;
        }
        if (config.startsWith("{")) {
            return config;
        }
        return V2Configs.DEFAULT_FULL_JSON_CONFIG.replace(
            V2Configs.DEFAULT_OUT_BOUND_PLACE_IN_FULL_JSON_CONFIG,
            config
        );
    }

    public static String convertIntToTwoDigit(int value) {
        if (value < 10) return "0" + value;
        else return value + "";
    }

    public static String parseTraffic(final double bytes, final boolean inBits, final boolean isMomentary) {
        double value = inBits ? bytes * 8 : bytes;
        if (value < V2Configs.KILO_BYTE) {
            return String.format(Locale.getDefault(), "%.1f " + (inBits ? "b" : "B") + (isMomentary ? "/s" : ""), value);
        } else if (value < V2Configs.MEGA_BYTE) {
            return String.format(Locale.getDefault(), "%.1f K" + (inBits ? "b" : "B") + (isMomentary ? "/s" : ""), value / V2Configs.KILO_BYTE);
        } else if (value < V2Configs.GIGA_BYTE) {
            return String.format(Locale.getDefault(), "%.1f M" + (inBits ? "b" : "B") + (isMomentary ? "/s" : ""), value / V2Configs.MEGA_BYTE);
        } else {
            return String.format(Locale.getDefault(), "%.2f G" + (inBits ? "b" : "B") + (isMomentary ? "/s" : ""), value / V2Configs.GIGA_BYTE);
        }
    }

    public static V2Config parseV2rayJsonFile(final String remark, String config, final ArrayList<String> blockedApplication) {
        final V2Config v2Config = new V2Config();
        v2Config.REMARK = remark;
        v2Config.BLOCKED_APPS = blockedApplication;
        v2Config.APPLICATION_ICON = V2Configs.APPLICATION_ICON;
        v2Config.APPLICATION_NAME = V2Configs.APPLICATION_NAME;
        try {
            JSONObject config_json = new JSONObject(normalizeV2rayFullConfig(config));
            try {
                JSONArray inbounds = config_json.getJSONArray("inbounds");
                for (int i = 0; i < inbounds.length(); i++) {
                    try {
                        if (inbounds.getJSONObject(i).getString("protocol").equals("socks")) {
                            v2Config.LOCAL_SOCKS5_PORT = inbounds.getJSONObject(i).getInt("port");
                            // --- Remove userLevel & ensure udp=true (como HZV2ray) ---
                            JSONObject sockInbound = inbounds.getJSONObject(i);
                            if (sockInbound.has("settings")) {
                                sockInbound.getJSONObject("settings").remove("userLevel");
                                sockInbound.getJSONObject("settings").put("udp", true);
                            }
                            // NO forzamos sniffing (HZV2ray no lo usa: "Sin sniffing para evitar interferencias con UDP")
                            // Incluso removemos sniffing si existe en la config almacenada
                            sockInbound.remove("sniffing");
                        }
                    } catch (Exception e) {
                        //ignore
                    }
                    try {
                        if (inbounds.getJSONObject(i).getString("protocol").equals("http")) {
                            v2Config.LOCAL_HTTP_PORT = inbounds.getJSONObject(i).getInt("port");
                            // Remove userLevel from HTTP inbound too
                            if (inbounds.getJSONObject(i).has("settings")) {
                                inbounds.getJSONObject(i).getJSONObject("settings").remove("userLevel");
                            }
                        }
                    } catch (Exception e) {
                        //ignore
                    }
                }
            } catch (Exception e) {
                // Log.w(V2Core.class.getSimpleName(), "startCore warn => can`t find inbound port of socks5 or http.");
                return null;
            }
            try {
                v2Config.CONNECTED_V2RAY_SERVER_ADDRESS = config_json.getJSONArray("outbounds")
                        .getJSONObject(0).getJSONObject("settings")
                        .getJSONArray("vnext").getJSONObject(0)
                        .getString("address");
                v2Config.CONNECTED_V2RAY_SERVER_PORT = config_json.getJSONArray("outbounds")
                        .getJSONObject(0).getJSONObject("settings")
                        .getJSONArray("vnext").getJSONObject(0)
                        .getString("port");
            } catch (Exception e) {
                v2Config.CONNECTED_V2RAY_SERVER_ADDRESS = config_json.getJSONArray("outbounds")
                        .getJSONObject(0).getJSONObject("settings")
                        .getJSONArray("servers").getJSONObject(0)
                        .getString("address");
                v2Config.CONNECTED_V2RAY_SERVER_PORT = config_json.getJSONArray("outbounds")
                        .getJSONObject(0).getJSONObject("settings")
                        .getJSONArray("servers").getJSONObject(0)
                        .getString("port");
            }
            
            // --- Force cleanup of outbound VMess user fields (remove non-VMess fields) ---
            try {
                JSONObject mainOutbound = config_json.getJSONArray("outbounds").getJSONObject(0);
                // Remove mux if disabled (harmless but clean)
                if (mainOutbound.has("mux")) {
                    JSONObject muxObj = mainOutbound.getJSONObject("mux");
                    if (!muxObj.optBoolean("enabled", false)) {
                        mainOutbound.remove("mux");
                    }
                }
                // Clean streamSettings: remove empty security
                if (mainOutbound.has("streamSettings")) {
                    JSONObject ss = mainOutbound.getJSONObject("streamSettings");
                    String sec = ss.optString("security", "");
                    if (sec.isEmpty()) {
                        ss.remove("security");
                    }
                }
                // Clean VNext users: remove level, encryption, flow (for VMess/VLess)
                try {
                    JSONArray vnext = mainOutbound.getJSONObject("settings").optJSONArray("vnext");
                    if (vnext != null) {
                        for (int vi = 0; vi < vnext.length(); vi++) {
                            JSONArray users = vnext.getJSONObject(vi).optJSONArray("users");
                            if (users != null) {
                                for (int ui = 0; ui < users.length(); ui++) {
                                    JSONObject u = users.getJSONObject(ui);
                                    // Remove fields not relevant for VMess
                                    u.remove("encryption");
                                    u.remove("flow");
                                    // Remove level entirely (HZV2ray no usa level en users)
                                    u.remove("level");
                                }
                            }
                        }
                    }
                } catch (Exception e) { /* ignore */ }
                // Clean servers for trojan/ss: remove level
                try {
                    JSONArray servers = mainOutbound.getJSONObject("settings").optJSONArray("servers");
                    if (servers != null) {
                        for (int si = 0; si < servers.length(); si++) {
                            servers.getJSONObject(si).remove("level");
                        }
                    }
                } catch (Exception e) { /* ignore */ }
                Log.d("HevSocks5", "  Cleaned outbound fields (security, mux, user level/encryption/flow)");
            } catch (Exception e) {
                Log.w("HevSocks5", "  Outbound field cleanup failed (non-fatal): " + e.getMessage());
            }

            // NOTA: No añadimos dns-out. HZV2ray usa solo catch-all y funciona.
            // Pero el stored config PUEDE tener dns-out de sesiones anteriores. Lo eliminamos:
            try {
                JSONArray outbounds = config_json.getJSONArray("outbounds");
                for (int i = outbounds.length() - 1; i >= 0; i--) {
                    String tag = outbounds.getJSONObject(i).optString("tag", "");
                    if ("dns-out".equals(tag)) {
                        outbounds.remove(i);
                        Log.d("HevSocks5", "  Removed stale dns-out outbound");
                    }
                }
            } catch (Exception e) { /* ignore */ }
            // Remove DNS routing rules (dns-out) if present
            try {
                if (config_json.has("routing")) {
                    JSONObject routing = config_json.getJSONObject("routing");
                    JSONArray rules = routing.optJSONArray("rules");
                    if (rules != null) {
                        for (int i = rules.length() - 1; i >= 0; i--) {
                            String ot = rules.getJSONObject(i).optString("outboundTag", "");
                            if ("dns-out".equals(ot)) {
                                rules.remove(i);
                                Log.d("HevSocks5", "  Removed stale DNS routing rule (dns-out)");
                            }
                        }
                        // If only 1 rule left and it's a catch-all, we're good
                    }
                }
            } catch (Exception e) { /* ignore */ }
            // Also restore DNS to single server if we added extra ones before
            try {
                if (config_json.has("dns")) {
                    JSONObject dns = config_json.getJSONObject("dns");
                    JSONArray servers = dns.optJSONArray("servers");
                    if (servers != null && servers.length() > 1) {
                        // Keep only the first DNS server (like HZV2ray)
                        while (servers.length() > 1) {
                            servers.remove(servers.length() - 1);
                        }
                        Log.d("HevSocks5", "  Reduced DNS to single server (like HZV2ray)");
                    }
                }
            } catch (Exception e) { /* ignore */ }

            // --- Replace policy/stats with level 0 (not 8) to avoid UDP ASSOCIATE issues ---
            try {
                if (config_json.has("policy")) {
                    config_json.remove("policy");
                }
                if (config_json.has("stats")) {
                    config_json.remove("stats");
                }
            } catch (Exception ignore_error) {
                //ignore
            }
            // --- Force loglevel to "warning" so we can see V2Ray errors ---
            try {
                if (config_json.has("log")) {
                    config_json.getJSONObject("log").put("loglevel", "warning");
                    Log.d("HevSocks5", "  loglevel forced to warning");
                }
            } catch (Exception e) { /* ignore */ }
            
            if (V2Configs.ENABLE_TRAFFIC_AND_SPEED_STATICS) {
                try {
                    JSONObject policy = new JSONObject();
                    JSONObject levels = new JSONObject();
                    // Use level 0 (default) instead of 8 to avoid UDP ASSOCIATE rejection
                    levels.put("0", new JSONObject()
                            .put("connIdle", 300)
                            .put("handshake", 4));
                    JSONObject system = new JSONObject()
                            .put("statsOutboundUplink", true)
                            .put("statsOutboundDownlink", true);
                    policy.put("levels", levels);
                    policy.put("system", system);
                    config_json.put("policy", policy);
                    config_json.put("stats", new JSONObject());
                    v2Config.ENABLE_TRAFFIC_STATICS = true;
                    Log.d("HevSocks5", "  Policy set with level 0 (stats enabled)");
                } catch (Exception e) {
                    //ignore
                }
            }
            v2Config.V2RAY_FULL_JSON_CONFIG = config_json.toString();
            Log.i("HevSocks5", "📋 FINAL CONFIG (after normalization):\n" + v2Config.V2RAY_FULL_JSON_CONFIG);
            return v2Config;
        } catch (Exception e) {
            // Log.e(V2Utilities.class.getName(), "parseV2rayJsonFile failed => ", e);
            
            //ignore
        }
        return null;
    }
}
