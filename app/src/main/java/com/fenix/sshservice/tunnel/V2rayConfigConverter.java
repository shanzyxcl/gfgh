package com.fenix.sshservice.tunnel;

import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class V2rayConfigConverter {
    private static final String TAG = "V2rayConfigConverter";

    public static String convert(String input, int socksPort, String assetsPath) {
        if (input == null || input.trim().isEmpty()) return input;

        String trimmed = input.trim();

        // --- DETECCIÓN AUTOMÁTICA DE ENCRIPTACIÓN ---
        boolean isKnownProtocol = trimmed.startsWith("vmess://") || trimmed.startsWith("vless://") ||
                trimmed.startsWith("trojan://") || trimmed.startsWith("ss://") ||
                trimmed.startsWith("hysteria2://") || trimmed.startsWith("hy2://") ||
                trimmed.startsWith("tuic://") || trimmed.startsWith("xhttp://");

        if (!trimmed.startsWith("{") && !isKnownProtocol) {
            // If not JSON and not known protocol, try to use as-is (maybe plain text config)
            return trimmed;
        }

        // Si es un JSON, lo devolvemos
        if (trimmed.startsWith("{")) {
            return trimmed;
        }

        try {
            if (trimmed.startsWith("vmess://")) {
                return convertVmess(trimmed, socksPort, assetsPath);
            } else if (trimmed.startsWith("vless://")) {
                return convertVless(trimmed, socksPort, assetsPath);
            } else if (trimmed.startsWith("trojan://")) {
                return convertTrojan(trimmed, socksPort, assetsPath);
            } else if (trimmed.startsWith("ss://")) {
                return convertSS(trimmed, socksPort, assetsPath);
            } else if (trimmed.startsWith("hysteria2://") || trimmed.startsWith("hy2://")) {
                return convertHysteria2(trimmed, socksPort, assetsPath);
            } else if (trimmed.startsWith("tuic://")) {
                return convertTuic(trimmed, socksPort, assetsPath);
            } else if (trimmed.startsWith("xhttp://")) {
                return convertXHttp(trimmed, socksPort, assetsPath);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error convirtiendo URI a JSON: " + e.getMessage());
        }

        return trimmed;
    }

    private static String convertVmess(String uri, int socksPort, String assetsPath) throws Exception {
        String base64 = uri.substring(8);
        int hashIdx = base64.indexOf("#");
        if (hashIdx > 0) base64 = base64.substring(0, hashIdx);

        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        JSONObject vmess = new JSONObject(new String(data));

        JSONObject config = createBaseConfig(socksPort, assetsPath);
        JSONObject outbound = config.getJSONArray("outbounds").getJSONObject(0);
        outbound.put("protocol", "vmess");

        JSONObject settings = new JSONObject();
        JSONArray vnext = new JSONArray();
        JSONObject server = new JSONObject();
        server.put("address", vmess.optString("add"));
        server.put("port", vmess.optInt("port"));

        JSONArray users = new JSONArray();
        JSONObject user = new JSONObject();
        user.put("id", vmess.optString("id"));
        user.put("alterId", vmess.optInt("aid", 0));
        user.put("security", vmess.optString("scy", "auto"));
        users.put(user);

        server.put("users", users);
        vnext.put(server);
        settings.put("vnext", vnext);
        outbound.put("settings", settings);

        Map<String, String> query = new HashMap<>();
        query.put("type", vmess.optString("net"));
        query.put("security", vmess.optString("tls"));
        query.put("host", vmess.optString("host"));
        query.put("path", vmess.optString("path"));
        query.put("sni", vmess.optString("sni"));
        query.put("headerType", vmess.optString("type"));

        JSONObject streamSettings = createStreamSettings(
                vmess.optString("net"),
                vmess.optString("tls"),
                vmess.optString("host"),
                vmess.optString("path"),
                vmess.optString("sni"),
                vmess.optString("type"),
                query
        );
        if (streamSettings != null) {
            outbound.put("streamSettings", streamSettings);
        }

        return config.toString();
    }

    private static String convertVless(String uriStr, int socksPort, String assetsPath) throws Exception {
        String cleanUri = uriStr.split("#")[0];
        URI uri = new URI(cleanUri);
        Map<String, String> query = parseQuery(uri.getRawQuery());

        JSONObject config = createBaseConfig(socksPort, assetsPath);
        JSONObject outbound = config.getJSONArray("outbounds").getJSONObject(0);
        outbound.put("protocol", "vless");

        JSONObject settings = new JSONObject();
        JSONArray vnext = new JSONArray();
        JSONObject server = new JSONObject();
        server.put("address", uri.getHost());
        server.put("port", uri.getPort());

        JSONArray users = new JSONArray();
        JSONObject user = new JSONObject();
        user.put("id", uri.getUserInfo());
        user.put("encryption", query.containsKey("encryption") ? query.get("encryption") : "none");
        user.put("flow", query.containsKey("flow") ? query.get("flow") : "");
        users.put(user);

        server.put("users", users);
        vnext.put(server);
        settings.put("vnext", vnext);
        outbound.put("settings", settings);

        JSONObject streamSettings = createStreamSettings(
                query.get("type"),
                query.get("security"),
                query.get("host"),
                query.get("path"),
                query.get("sni"),
                query.get("headerType"),
                query
        );
        if (streamSettings != null) {
            outbound.put("streamSettings", streamSettings);
        }

        return config.toString();
    }

    private static String convertTrojan(String uriStr, int socksPort, String assetsPath) throws Exception {
        String cleanUri = uriStr.split("#")[0];
        URI uri = new URI(cleanUri);
        Map<String, String> query = parseQuery(uri.getRawQuery());

        JSONObject config = createBaseConfig(socksPort, assetsPath);
        JSONObject outbound = config.getJSONArray("outbounds").getJSONObject(0);
        outbound.put("protocol", "trojan");

        JSONObject settings = new JSONObject();
        JSONArray servers = new JSONArray();
        JSONObject server = new JSONObject();
        server.put("address", uri.getHost());
        server.put("port", uri.getPort());
        server.put("password", uri.getUserInfo());
        servers.put(server);
        settings.put("servers", servers);
        outbound.put("settings", settings);

        JSONObject streamSettings = createStreamSettings(
                query.get("type"),
                query.get("security"),
                query.get("host"),
                query.get("path"),
                query.get("sni"),
                query.get("headerType"),
                query
        );
        if (streamSettings != null) {
            outbound.put("streamSettings", streamSettings);
        }

        return config.toString();
    }

    private static String convertSS(String uriStr, int socksPort, String assetsPath) throws Exception {
        String cleanUri = uriStr.split("#")[0];
        URI uri = new URI(cleanUri);
        JSONObject config = createBaseConfig(socksPort, assetsPath);
        JSONObject outbound = config.getJSONArray("outbounds").getJSONObject(0);
        outbound.put("protocol", "shadowsocks");

        JSONObject settings = new JSONObject();
        JSONArray servers = new JSONArray();
        JSONObject server = new JSONObject();
        server.put("address", uri.getHost());
        server.put("port", uri.getPort());

        String userInfo = uri.getUserInfo();
        if (userInfo != null) {
            if (!userInfo.contains(":")) {
                try {
                    byte[] decoded = Base64.decode(userInfo, Base64.DEFAULT);
                    userInfo = new String(decoded);
                } catch (Exception ignored) {}
            }
            String[] parts = userInfo.split(":", 2);
            if (parts.length == 2) {
                server.put("method", parts[0]);
                server.put("password", parts[1]);
            }
        }

        servers.put(server);
        settings.put("servers", servers);
        outbound.put("settings", settings);

        return config.toString();
    }

    private static String convertHysteria2(String uriStr, int socksPort, String assetsPath) throws Exception {
        String cleanUri = uriStr.split("#")[0];
        URI uri = new URI(cleanUri);
        Map<String, String> query = parseQuery(uri.getRawQuery());

        JSONObject config = createBaseConfig(socksPort, assetsPath);
        JSONObject outbound = config.getJSONArray("outbounds").getJSONObject(0);
        outbound.put("protocol", "hysteria2");

        JSONObject settings = new JSONObject();
        JSONArray servers = new JSONArray();
        JSONObject server = new JSONObject();
        server.put("address", uri.getHost());
        server.put("port", uri.getPort());
        server.put("password", uri.getUserInfo());
        servers.put(server);
        settings.put("servers", servers);
        outbound.put("settings", settings);

        JSONObject streamSettings = new JSONObject();
        streamSettings.put("network", "udp");

        JSONObject hysteria2Settings = new JSONObject();
        if (query.containsKey("sni")) hysteria2Settings.put("serverName", query.get("sni"));
        if (query.containsKey("insecure")) hysteria2Settings.put("allowInsecure", query.get("insecure").equals("1"));
        streamSettings.put("hysteria2Settings", hysteria2Settings);

        outbound.put("streamSettings", streamSettings);

        return config.toString();
    }

    private static String convertTuic(String uriStr, int socksPort, String assetsPath) throws Exception {
        String cleanUri = uriStr.split("#")[0];
        URI uri = new URI(cleanUri);
        Map<String, String> query = parseQuery(uri.getRawQuery());

        JSONObject config = createBaseConfig(socksPort, assetsPath);
        JSONObject outbound = config.getJSONArray("outbounds").getJSONObject(0);
        outbound.put("protocol", "tuic");

        JSONObject settings = new JSONObject();
        JSONArray servers = new JSONArray();
        JSONObject server = new JSONObject();
        server.put("address", uri.getHost());
        server.put("port", uri.getPort());

        String uInfo = uri.getUserInfo();
        if (uInfo != null) {
            String[] userInfo = uInfo.split(":", 2);
            if (userInfo.length == 2) {
                server.put("uuid", userInfo[0]);
                server.put("password", userInfo[1]);
            } else {
                server.put("uuid", userInfo[0]);
            }
        }

        servers.put(server);
        settings.put("servers", servers);
        outbound.put("settings", settings);

        JSONObject streamSettings = new JSONObject();
        streamSettings.put("network", "udp");

        JSONObject tuicSettings = new JSONObject();
        if (query.containsKey("sni")) tuicSettings.put("serverName", query.get("sni"));
        if (query.containsKey("alpn")) tuicSettings.put("alpn", new JSONArray(query.get("alpn").split(",")));
        if (query.containsKey("congestion_control")) tuicSettings.put("congestionControl", query.get("congestion_control"));
        streamSettings.put("tuicSettings", tuicSettings);

        outbound.put("streamSettings", streamSettings);

        return config.toString();
    }

    private static String convertXHttp(String uriStr, int socksPort, String assetsPath) throws Exception {
        String cleanUri = uriStr.split("#")[0];
        URI uri = new URI(cleanUri);
        Map<String, String> query = parseQuery(uri.getRawQuery());

        JSONObject config = createBaseConfig(socksPort, assetsPath);
        JSONObject outbound = config.getJSONArray("outbounds").getJSONObject(0);
        outbound.put("protocol", "vless");

        JSONObject settings = new JSONObject();
        JSONArray vnext = new JSONArray();
        JSONObject server = new JSONObject();
        server.put("address", uri.getHost());
        server.put("port", uri.getPort());

        JSONArray users = new JSONArray();
        JSONObject user = new JSONObject();
        user.put("id", uri.getUserInfo());
        user.put("encryption", "none");
        users.put(user);

        server.put("users", users);
        vnext.put(server);
        settings.put("vnext", vnext);
        outbound.put("settings", settings);

        JSONObject streamSettings = createStreamSettings(
                "xhttp",
                query.getOrDefault("security", "tls"),
                query.get("host"),
                query.get("path"),
                query.get("sni"),
                query.get("headerType"),
                query
        );
        if (streamSettings != null) {
            outbound.put("streamSettings", streamSettings);
        }

        return config.toString();
    }

    private static JSONObject createBaseConfig(int socksPort, String assetsPath) throws Exception {
        JSONObject config = new JSONObject();

        // Log level warning como v2.txt
        JSONObject log = new JSONObject();
        log.put("loglevel", "warning");
        config.put("log", log);

        // DNS con un solo servidor
        JSONObject dns = new JSONObject();
        JSONArray dnsServers = new JSONArray();
        dnsServers.put("1.1.1.1");
        dns.put("servers", dnsServers);
        config.put("dns", dns);

        // Inbound SOCKS5 (igual que v2.txt pero sin sniffing ni userLevel)
        JSONArray inbounds = new JSONArray();
        JSONObject inbound = new JSONObject();
        inbound.put("listen", "127.0.0.1");
        inbound.put("port", socksPort);
        inbound.put("protocol", "socks");
        inbound.put("tag", "socks");
        JSONObject inSettings = new JSONObject();
        inSettings.put("auth", "noauth");
        inSettings.put("udp", true);
        inbound.put("settings", inSettings);
        // Sin sniffing (se elimina en parseV2rayJsonFile)
        inbounds.put(inbound);
        config.put("inbounds", inbounds);

        // Outbounds como v2.txt: proxy, freedom(con domainStrategy UseIP), blackhole
        JSONArray outbounds = new JSONArray();
        
        // 1. Proxy outbound (se completa en convertVmess/convertVless/...)
        JSONObject mainOutbound = new JSONObject();
        mainOutbound.put("tag", "proxy");
        outbounds.put(mainOutbound);

        // 2. Freedom con domainStrategy UseIP como v2.txt
        JSONObject freedom = new JSONObject();
        freedom.put("protocol", "freedom");
        freedom.put("tag", "direct");
        JSONObject freedomSettings = new JSONObject();
        freedomSettings.put("domainStrategy", "UseIP");
        freedom.put("settings", freedomSettings);
        outbounds.put(freedom);

        // 3. Blackhole como v2.txt
        JSONObject blackhole = new JSONObject();
        blackhole.put("protocol", "blackhole");
        blackhole.put("tag", "block");
        JSONObject blackholeSettings = new JSONObject();
        JSONObject blackholeResponse = new JSONObject();
        blackholeResponse.put("type", "http");
        blackholeSettings.put("response", blackholeResponse);
        blackhole.put("settings", blackholeSettings);
        outbounds.put(blackhole);

        config.put("outbounds", outbounds);

        // Routing como v2.txt: domainStrategy AsIs + reglas DNS
        JSONObject routing = new JSONObject();
        routing.put("domainStrategy", "AsIs");
        JSONArray rules = new JSONArray();
        
        // DNS routing rules como v2.txt
        JSONObject dnsRule1 = new JSONObject();
        dnsRule1.put("type", "field");
        dnsRule1.put("port", "53");
        dnsRule1.put("ip", new JSONArray().put("8.8.8.8"));
        dnsRule1.put("outboundTag", "direct");
        rules.put(dnsRule1);

        JSONObject dnsRule2 = new JSONObject();
        dnsRule2.put("type", "field");
        dnsRule2.put("port", "53");
        dnsRule2.put("ip", new JSONArray().put("1.1.1.1"));
        dnsRule2.put("outboundTag", "proxy");
        rules.put(dnsRule2);

        JSONObject dnsRule3 = new JSONObject();
        dnsRule3.put("type", "field");
        dnsRule3.put("port", "53");
        dnsRule3.put("ip", new JSONArray().put("223.5.5.5"));
        dnsRule3.put("outboundTag", "direct");
        rules.put(dnsRule3);

        // geoip:private rule si hay assets (opcional)
        if (assetsPath != null) {
            JSONObject privateRule = new JSONObject();
            privateRule.put("type", "field");
            privateRule.put("ip", new JSONArray().put("geoip:private"));
            privateRule.put("outboundTag", "direct");
            rules.put(privateRule);
        }

        routing.put("rules", rules);
        config.put("routing", routing);

        // Policy y stats como v2.txt (luego normalizados por parseV2rayJsonFile)
        JSONObject policy = new JSONObject();
        JSONObject levels = new JSONObject();
        JSONObject level8 = new JSONObject();
        level8.put("connIdle", 300);
        level8.put("handshake", 4);
        levels.put("8", level8);
        policy.put("levels", levels);
        JSONObject pSettings = new JSONObject();
        pSettings.put("statsOutboundUplink", true);
        pSettings.put("statsOutboundDownlink", true);
        policy.put("system", pSettings);
        config.put("policy", policy);

        config.put("stats", new JSONObject());

        return config;
    }

    private static JSONObject createStreamSettings(String net, String security, String host, String path, String sni, String headerType, Map<String, String> query) throws Exception {
        boolean noNet = (net == null || net.isEmpty() || net.equalsIgnoreCase("tcp"));
        boolean noSec = (security == null || security.isEmpty() || security.equalsIgnoreCase("none"));

        if (noNet && noSec) return null;

        JSONObject stream = new JSONObject();
        stream.put("network", (net != null && !net.isEmpty()) ? net : "tcp");

        if (!noSec) {
            stream.put("security", security);
        }

        if ("tls".equalsIgnoreCase(security) || "xtls".equalsIgnoreCase(security)) {
            JSONObject tls = new JSONObject();
            if (sni != null && !sni.isEmpty()) tls.put("serverName", sni);
            else if (host != null && !host.isEmpty()) tls.put("serverName", host);
            // Como v2.txt: allowInsecure true para evitar problemas de TLS
            tls.put("allowInsecure", true);
            tls.put("show", false);

            if ("xtls".equalsIgnoreCase(security)) stream.put("xtlsSettings", tls);
            else stream.put("tlsSettings", tls);
        } else if ("reality".equalsIgnoreCase(security) && query != null) {
            JSONObject reality = new JSONObject();
            reality.put("show", false);
            reality.put("fingerprint", query.getOrDefault("fp", "chrome"));
            reality.put("serverName", query.getOrDefault("sni", ""));
            reality.put("publicKey", query.getOrDefault("pbk", ""));
            reality.put("shortId", query.getOrDefault("sid", ""));
            reality.put("spiderX", query.getOrDefault("spx", ""));
            stream.put("realitySettings", reality);
        }

        if ("ws".equalsIgnoreCase(net)) {
            JSONObject ws = new JSONObject();
            if (path != null) ws.put("path", URLDecoder.decode(path, "UTF-8"));
            if (host != null && !host.isEmpty()) {
                JSONObject headers = new JSONObject();
                headers.put("Host", host);
                ws.put("headers", headers);
            }
            stream.put("wsSettings", ws);
        } else if ("grpc".equalsIgnoreCase(net)) {
            JSONObject grpc = new JSONObject();
            if (path != null) grpc.put("serviceName", URLDecoder.decode(path, "UTF-8"));
            if (query != null && query.containsKey("mode")) grpc.put("multiMode", query.get("mode").equalsIgnoreCase("multi"));
            stream.put("grpcSettings", grpc);
        } else if ("http".equalsIgnoreCase(net) || "h2".equalsIgnoreCase(net)) {
            JSONObject http = new JSONObject();
            if (path != null) http.put("path", URLDecoder.decode(path, "UTF-8"));
            if (host != null && !host.isEmpty()) {
                JSONArray hosts = new JSONArray();
                hosts.put(host);
                http.put("host", hosts);
            }
            stream.put("httpSettings", http);
        } else if ("quic".equalsIgnoreCase(net)) {
            JSONObject quic = new JSONObject();
            quic.put("security", host);
            quic.put("key", path);
            JSONObject header = new JSONObject();
            header.put("type", headerType != null ? headerType : "none");
            quic.put("header", header);
            stream.put("quicSettings", quic);
        } else if ("kcp".equalsIgnoreCase(net) || "mkcp".equalsIgnoreCase(net)) {
            JSONObject kcp = new JSONObject();
            JSONObject header = new JSONObject();
            header.put("type", headerType != null ? headerType : "none");
            kcp.put("header", header);
            stream.put("kcpSettings", kcp);
        } else if ("xhttp".equalsIgnoreCase(net)) {
            JSONObject xhttp = new JSONObject();
            if (path != null) xhttp.put("path", URLDecoder.decode(path, "UTF-8"));
            if (host != null && !host.isEmpty()) xhttp.put("host", host);
            if (query != null) {
                if (query.containsKey("mode")) xhttp.put("mode", query.get("mode"));
                if (query.containsKey("extra")) {
                    try {
                        xhttp.put("extra", new JSONObject(URLDecoder.decode(query.get("extra"), "UTF-8")));
                    } catch (Exception ignored) {}
                }
            }
            stream.put("xhttpSettings", xhttp);
        }

        return stream;
    }

    private static Map<String, String> parseQuery(String query) throws Exception {
        Map<String, String> map = new HashMap<>();
        if (query == null) return map;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0) {
                map.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                        URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            }
        }
        return map;
    }
}
