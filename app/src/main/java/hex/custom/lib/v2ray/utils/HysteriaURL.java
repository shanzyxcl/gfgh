package hex.custom.lib.v2ray.utils;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HysteriaURL {
    private URI uri;
    private int version;
    private String auth;
    private String obfsPassword;
    private String alpn;
    private String sni;
    private boolean allowInsecure;
    private Map<String, Object> finalmask;

    public HysteriaURL(String url) throws IllegalArgumentException {
        String scheme = url.split("://")[0].toLowerCase();
        if (!Arrays.asList("hysteria", "hysteria2", "hy", "hy2").contains(scheme)) {
            throw new IllegalArgumentException("url is invalid");
        }

        try {
            uri = new URI(url);
        } catch (Exception e) {
            throw new IllegalArgumentException("url is invalid");
        }

        version = (scheme.equals("hysteria2") || scheme.equals("hy2")) ? 2 : 1;
        auth = uri.getUserInfo() != null ? uri.getUserInfo() : "";

        obfsPassword = getQueryParam("obfs-password");
        if (obfsPassword == null) {
            obfsPassword = getQueryParam("obfs_password");
        }

        String fm = getQueryParam("fm");
        if (fm != null && !fm.isEmpty() && obfsPassword == null) {
            try {
                finalmask = new Gson().fromJson(fm, Map.class);
            } catch (Exception e) {
                finalmask = null;
            }
        }

        alpn = getQueryParam("alpn");
        sni = getQueryParam("sni");

        String insecureStr = getQueryParam("insecure");
        if (insecureStr == null) {
            insecureStr = getQueryParam("allowInsecure");
        }
        allowInsecure = "1".equals(insecureStr) || "true".equalsIgnoreCase(insecureStr);
    }

    private String getQueryParam(String key) {
        String query = uri.getQuery();
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] kv = param.split("=", 2);
            if (kv[0].equals(key)) {
                try {
                    return URLDecoder.decode(kv.length > 1 ? kv[1] : "", "UTF-8");
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    public String getAddress() {
        return uri.getHost() != null ? uri.getHost() : "";
    }

    public int getPort() {
        return uri.getPort() > 0 ? uri.getPort() : 0;
    }

    public String getRemark() {
        String fragment = uri.getFragment();
        if (fragment != null && !fragment.isEmpty()) {
            try {
                return URLDecoder.decode(fragment.replace("+", "%20"), "UTF-8");
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }

    public Map<String, Object> getOutboundMap() {
        return buildOutbound();
    }

    public List<Map<String, Object>> generateOutbounds() {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(buildOutbound());
        return list;
    }

    private Map<String, Object> buildOutbound() {
        Map<String, Object> outbound = new LinkedHashMap<>();

        Map<String, Object> mux = new LinkedHashMap<>();
        mux.put("enabled", false);
        mux.put("concurrency", -1);
        outbound.put("mux", mux);

        outbound.put("protocol", "hysteria");

        Map<String, Object> settings = new LinkedHashMap<>();
        settings.put("address", getAddress());
        settings.put("port", getPort());
        settings.put("version", version);
        outbound.put("settings", settings);

        Map<String, Object> streamSettings = new LinkedHashMap<>();
        streamSettings.put("network", "hysteria");
        streamSettings.put("security", "tls");
        if (obfsPassword != null && !obfsPassword.isEmpty()) {
            Map<String, Object> salamanderSettings = new LinkedHashMap<>();
            salamanderSettings.put("password", obfsPassword);

            Map<String, Object> salamander = new LinkedHashMap<>();
            salamander.put("type", "salamander");
            salamander.put("settings", salamanderSettings);

            List<Map<String, Object>> udpList = new ArrayList<>();
            udpList.add(salamander);

            Map<String, Object> udpWrapper = new LinkedHashMap<>();
            udpWrapper.put("udp", udpList);

            streamSettings.put("finalmask", udpWrapper);
        } else if (finalmask != null) {
            streamSettings.put("finalmask", finalmask);
        }

        Map<String, Object> hysteriaSettings = new LinkedHashMap<>();
        hysteriaSettings.put("auth", auth);
        hysteriaSettings.put("version", version);
        streamSettings.put("hysteriaSettings", hysteriaSettings);

        Map<String, Object> happyEyeballs = new LinkedHashMap<>();
        happyEyeballs.put("interleave", 2);
        happyEyeballs.put("maxConcurrentTry", 4);
        happyEyeballs.put("prioritizeIPv6", false);
        happyEyeballs.put("tryDelayMs", 250);

        Map<String, Object> sockopt = new LinkedHashMap<>();
        sockopt.put("domainStrategy", "UseIP");
        sockopt.put("happyEyeballs", happyEyeballs);
        streamSettings.put("sockopt", sockopt);

        Map<String, Object> tlsSettings = new LinkedHashMap<>();
        tlsSettings.put("allowInsecure", allowInsecure);
        if (alpn != null && !alpn.isEmpty()) {
            String[] parts = alpn.split(",");
            List<String> alpnList = new ArrayList<>();
            for (String p : parts) {
                alpnList.add(p.trim());
            }
            tlsSettings.put("alpn", alpnList);
        } else {
            tlsSettings.put("alpn", Collections.singletonList("h3"));
        }
        String serverName = (sni != null && !sni.isEmpty()) ? sni : uri.getHost();
        tlsSettings.put("serverName", serverName);
        streamSettings.put("tlsSettings", tlsSettings);

        outbound.put("streamSettings", streamSettings);
        outbound.put("tag", "proxy");

        return outbound;
    }


}