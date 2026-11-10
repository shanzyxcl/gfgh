package com.fenix.sshservice;

import com.fenix.sshservice.config.V2Config;

public class V2Configs {

    public static final long BYTE = 1;
    public static final long KILO_BYTE = BYTE * 1024;
    public static final long MEGA_BYTE = KILO_BYTE * 1024;
    public static final long GIGA_BYTE = MEGA_BYTE * 1024;
    public static V2RAY_CONNECTION_MODES V2RAY_CONNECTION_MODE = V2RAY_CONNECTION_MODES.VPN_TUN;
    public static String APPLICATION_NAME = null;
    public static int APPLICATION_ICON = 0;
    public static V2Config V2RAY_CONFIG = null;
    public static V2RAY_STATES V2RAY_STATE = V2RAY_STATES.V2RAY_DISCONNECTED;
    public static boolean ENABLE_TRAFFIC_AND_SPEED_STATICS = true;

    public enum V2RAY_SERVICE_COMMANDS {
        START_SERVICE,
        STOP_SERVICE,
        MEASURE_DELAY
    }

    public enum V2RAY_STATES {
        V2RAY_CONNECTED,
        V2RAY_DISCONNECTED,
        V2RAY_CONNECTING
    }

    public enum V2RAY_CONNECTION_MODES {
        VPN_TUN,
        PROXY_ONLY
    }

    public static final String DEFAULT_OUT_BOUND_PLACE_IN_FULL_JSON_CONFIG = "CONFIG_PROXY_OUTBOUND_PLACE";
    public static final String DEFAULT_FULL_JSON_CONFIG = "{\n" +
            "  \"dns\": {\n" +
            "    \"hosts\": {\n" +
            "      \"domain:googleapis.cn\": \"googleapis.com\"\n" +
            "    },\n" +
            "    \"servers\": [\n" +
            "      \"1.1.1.1\"\n" +
            "    ]\n" +
            "  },\n" +
            "  \"inbounds\": [\n" +
            "    {\n" +
            "      \"listen\": \"127.0.0.1\",\n" +
            "      \"port\": 10808,\n" +
            "      \"protocol\": \"socks\",\n" +
            "      \"settings\": {\n" +
            "        \"auth\": \"noauth\",\n" +
            "        \"udp\": true,\n" +
            "        \"userLevel\": 8\n" +
            "      },\n" +
            "      \"sniffing\": {\n" +
            "        \"destOverride\": [],\n" +
            "        \"enabled\": false\n" +
            "      },\n" +
            "      \"tag\": \"socks\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"listen\": \"127.0.0.1\",\n" +
            "      \"port\": 10809,\n" +
            "      \"protocol\": \"http\",\n" +
            "      \"settings\": {\n" +
            "        \"userLevel\": 8\n" +
            "      },\n" +
            "      \"tag\": \"http\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"log\": {\n" +
            "    \"loglevel\": \"error\"\n" +
            "  },\n" +
            "  \"outbounds\": [\n" +
            "    "+DEFAULT_OUT_BOUND_PLACE_IN_FULL_JSON_CONFIG+",\n" +
            "    {\n" +
            "      \"protocol\": \"freedom\",\n" +
            "      \"settings\": {},\n" +
            "      \"tag\": \"direct\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"protocol\": \"blackhole\",\n" +
            "      \"settings\": {\n" +
            "        \"response\": {\n" +
            "          \"type\": \"http\"\n" +
            "        }\n" +
            "      },\n" +
            "      \"tag\": \"block\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"remarks\": \"test\",\n" +
            "  \"routing\": {\n" +
            "    \"domainStrategy\": \"IPIfNonMatch\",\n" +
            "    \"rules\": [\n" +
            "      {\n" +
            "        \"ip\": [\n" +
            "          \"1.1.1.1\"\n" +
            "        ],\n" +
            "        \"outboundTag\": \"proxy\",\n" +
            "        \"port\": \"53\",\n" +
            "        \"type\": \"field\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";
}