package hex.custom.lib.v2ray.utils;

import static hex.custom.lib.v2ray.utils.V2rayConfigs.currentConfig;
import static hex.custom.lib.v2ray.utils.V2rayConstants.DEFAULT_OUT_BOUND_PLACE_IN_FULL_JSON_CONFIG;

import android.content.Context;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fenix.sshservice.tunnel.V2rayConfigConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import hex.custom.lib.v2ray.core.V2rayCoreExecutor;
import libv2ray.Libv2ray;

public class Utilities {

    public static String getDeviceIdForXUDPBaseKey(Context context) {
        String androidId = null;
        try {
            androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception ignored) {
        }
        if (androidId == null || androidId.isEmpty()) {
            androidId = "pitogod";
        }
        byte[] androidIdBytes = androidId.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(Arrays.copyOf(androidIdBytes, 32),
                Base64.NO_PADDING | Base64.NO_WRAP | Base64.URL_SAFE);
    }

    public static void CopyFiles(InputStream src, File dst) throws IOException {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try (OutputStream out = Files.newOutputStream(dst.toPath())) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = src.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        } else {
            try (OutputStream out = new FileOutputStream(dst)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = src.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
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
            for (String assets_obj : Objects.requireNonNull(context.getAssets().list(""))) {
                if (geo.contains(assets_obj)) {
                    CopyFiles(context.getAssets().open(assets_obj), new File(extFolder, assets_obj));
                }
            }
        } catch (Exception e) {
            Log.e(Utilities.class.getSimpleName(), "copyAssets failed=>", e);
        }
    }

    public static String convertIntToTwoDigit(int value) {
        if (value < 10) return "0" + value;
        else return String.valueOf(value);
    }

    public static String parseTraffic(final double bytes, final boolean inBits, final boolean isMomentary) {
        double value = inBits ? bytes * 8 : bytes;
        if (value < V2rayConstants.KILO_BYTE) {
            return String.format(Locale.getDefault(), "%.1f " + (inBits ? "b" : "B") + (isMomentary ? "/s" : ""), value);
        } else if (value < V2rayConstants.MEGA_BYTE) {
            return String.format(Locale.getDefault(), "%.1f K" + (inBits ? "b" : "B") + (isMomentary ? "/s" : ""), value / V2rayConstants.KILO_BYTE);
        } else if (value < V2rayConstants.GIGA_BYTE) {
            return String.format(Locale.getDefault(), "%.1f M" + (inBits ? "b" : "B") + (isMomentary ? "/s" : ""), value / V2rayConstants.MEGA_BYTE);
        } else {
            return String.format(Locale.getDefault(), "%.2f G" + (inBits ? "b" : "B") + (isMomentary ? "/s" : ""), value / V2rayConstants.GIGA_BYTE);
        }
    }

    public static String normalizeV2rayFullConfig(String config){
        if (config.contains("hysteria2://") || config.contains("hy2://")){
            HysteriaURL hy = new HysteriaURL(config);
            Map<String, Object> outbounds = hy.getOutboundMap();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(outbounds);
            return V2rayConstants.DEFAULT_FULL_JSON_CONFIG.replace(DEFAULT_OUT_BOUND_PLACE_IN_FULL_JSON_CONFIG, json);
        } else {
            String converted = V2rayConfigConverter.convert(config, 10808, null);
            if (converted.startsWith("{")) {
                return converted;
            }
            if (config.startsWith("{")) {
                return config;
            }
            return V2rayConstants.DEFAULT_FULL_JSON_CONFIG.replace(
                    V2rayConstants.DEFAULT_OUT_BOUND_PLACE_IN_FULL_JSON_CONFIG,
                    config
            );
        }
    }

    public static boolean parseV2rayJsonFile(final String remark, String config, final ArrayList<String> blockedApplication) {
        currentConfig.remark = remark;
        currentConfig.blockedApplications = blockedApplication;
        try {
            JSONObject configJson = new JSONObject(normalizeV2rayFullConfig(config));
            try {
                JSONArray inbounds = configJson.getJSONArray("inbounds");
                for (int i = 0; i < inbounds.length(); i++) {
                    try {
                        if (inbounds.getJSONObject(i).getString("protocol").equals("socks")) {
                            currentConfig.localSocksPort = inbounds.getJSONObject(i).getInt("port");
                        }
                    } catch (Exception e) {}
                    try {
                        if (inbounds.getJSONObject(i).getString("protocol").equals("http")) {
                            currentConfig.localHttpPort = inbounds.getJSONObject(i).getInt("port");
                        }
                    } catch (Exception e) {}
                }
            } catch (Exception e) {
                Log.w(V2rayCoreExecutor.class.getSimpleName(), "startCore warn => can`t find inbound port of socks5 or http.");
                return false;
            }
            JSONArray outbounds = configJson.getJSONArray("outbounds");
            if (outbounds.length() == 0) {
                return false;
            }
            JSONObject firstOutbound = outbounds.getJSONObject(0);
            currentConfig.currentServerAddress = findStringValue(firstOutbound, "address");
            Integer port = findIntValue(firstOutbound, "port");
            if (currentConfig.currentServerAddress == null || port == null) {
                return false;
            }
            currentConfig.currentServerPort = port;
            try {
                if (configJson.has("policy")) configJson.remove("policy");
                if (configJson.has("stats")) configJson.remove("stats");
            } catch (Exception ignoreError) {}
            ensureDirectDnsRoute(configJson);
            if (currentConfig.enableTrafficStatics) {
                try {
                    JSONObject policy = new JSONObject();
                    JSONObject levels = new JSONObject();
                    levels.put("8", new JSONObject()
                            .put("connIdle", 300)
                            .put("downlinkOnly", 1)
                            .put("handshake", 4)
                            .put("uplinkOnly", 1));
                    JSONObject system = new JSONObject()
                            .put("statsOutboundUplink", true)
                            .put("statsOutboundDownlink", true);
                    policy.put("levels", levels);
                    policy.put("system", system);
                    configJson.put("policy", policy);
                    configJson.put("stats", new JSONObject());
                    currentConfig.enableTrafficStatics = true;
                } catch (Exception e) {}
            }
            currentConfig.fullJsonConfig = configJson.toString();
            return true;
        } catch (Exception e) {
            Log.e(Utilities.class.getName(), "parseV2rayJsonFile failed => ", e);
        }
        return false;
    }

    @Nullable
    private static String findStringValue(@NonNull JSONObject obj, @NonNull String targetKey) {
        if (obj.has(targetKey)) return obj.optString(targetKey, null);
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            Object value = obj.opt(keys.next());
            if (value instanceof JSONObject) {
                String found = findStringValue((JSONObject) value, targetKey);
                if (found != null) return found;
            } else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                for (int i = 0; i < array.length(); i++) {
                    Object item = array.opt(i);
                    if (item instanceof JSONObject) {
                        String found = findStringValue((JSONObject) item, targetKey);
                        if (found != null) return found;
                    }
                }
            }
        }
        return null;
    }
    @Nullable
    private static Integer findIntValue(@NonNull JSONObject obj, @NonNull String targetKey) {
        if (obj.has(targetKey)) {
            Object value = obj.opt(targetKey);
            if (value instanceof Number) return ((Number) value).intValue();
            try { return Integer.parseInt(value.toString()); } catch (NumberFormatException ignored) { return null; }
        }
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            Object value = obj.opt(keys.next());
            if (value instanceof JSONObject) {
                Integer found = findIntValue((JSONObject) value, targetKey);
                if (found != null) return found;
            } else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                for (int i = 0; i < array.length(); i++) {
                    Object item = array.opt(i);
                    if (item instanceof JSONObject) {
                        Integer found = findIntValue((JSONObject) item, targetKey);
                        if (found != null) return found;
                    }
                }
            }
        }
        return null;
    }

    private static void ensureDirectDnsRoute(JSONObject configJson) {
        try {
            JSONArray outbounds = configJson.optJSONArray("outbounds");
            if (outbounds == null) {
                return;
            }

            boolean hasDirectOutbound = false;
            for (int i = 0; i < outbounds.length(); i++) {
                JSONObject outbound = outbounds.optJSONObject(i);
                if (outbound != null && "direct".equals(outbound.optString("tag"))) {
                    hasDirectOutbound = true;
                    break;
                }
            }

            if (!hasDirectOutbound) {
                outbounds.put(new JSONObject()
                        .put("tag", "direct")
                        .put("protocol", "freedom")
                        .put("settings", new JSONObject()));
            }

            JSONObject routing = configJson.optJSONObject("routing");
            if (routing == null) {
                routing = new JSONObject();
                routing.put("domainStrategy", "AsIs");
                configJson.put("routing", routing);
            }

            JSONArray rules = routing.optJSONArray("rules");
            if (rules == null) {
                rules = new JSONArray();
            }

            JSONObject dnsDirectRule = new JSONObject()
                    .put("type", "field")
                    .put("network", "udp")
                    .put("port", "53")
                    .put("outboundTag", "direct");

            JSONArray newRules = new JSONArray();
            newRules.put(dnsDirectRule);
            for (int i = 0; i < rules.length(); i++) {
                newRules.put(rules.get(i));
            }
            routing.put("rules", newRules);
        } catch (Exception e) {
            Log.w(Utilities.class.getSimpleName(), "ensureDirectDnsRoute failed", e);
        }
    }

    public static String normalizeIpv6(String address) {
        if (isIpv6Address(address) && !address.contains("[") && !address.contains("]")) {
            return String.format("[%s]", address);
        } else {
            return address;
        }
    }

    public static boolean isIpv6Address(String address) {
        String[] tmp = address.split(":");
        return tmp.length > 2;
    }
}
