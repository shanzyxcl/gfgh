package hex.custom.lib.v2ray.utils;

public enum VpnInterfaceAddressConfig {

    OPTION_1("10.10.14.x", "10.10.14.1", "10.10.14.2", "fc00::10:10:14:1", "fc00::10:10:14:2"),
    OPTION_2("10.1.0.x", "10.1.0.1", "10.1.0.2", "fc00::10:1:0:1", "fc00::10:1:0:2"),
    OPTION_3("10.0.0.x", "10.0.0.1", "10.0.0.2", "fc00::10:0:0:1", "fc00::10:0:0:2"),
    OPTION_4("172.31.0.x", "172.31.0.1", "172.31.0.2", "fc00::172:31:0:1", "fc00::172:31:0:2"),
    OPTION_5("172.20.0.x", "172.20.0.1", "172.20.0.2", "fc00::172:20:0:1", "fc00::172:20:0:2"),
    OPTION_6("172.16.0.x", "172.16.0.1", "172.16.0.2", "fc00::172:16:0:1", "fc00::172:16:0:2"),
    OPTION_7("192.168.100.x", "192.168.100.1", "192.168.100.2", "fc00::192:168:100:1", "fc00::192:168:100:2");

    private final String displayName;
    private final String ipv4Client;
    private final String ipv4Router;
    private final String ipv6Client;
    private final String ipv6Router;

    VpnInterfaceAddressConfig(String displayName, String ipv4Client, String ipv4Router, String ipv6Client, String ipv6Router) {
        this.displayName = displayName;
        this.ipv4Client = ipv4Client;
        this.ipv4Router = ipv4Router;
        this.ipv6Client = ipv6Client;
        this.ipv6Router = ipv6Router;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIpv4Client() {
        return ipv4Client;
    }

    public String getIpv4Router() {
        return ipv4Router;
    }

    public String getIpv6Client() {
        return ipv6Client;
    }

    public String getIpv6Router() {
        return ipv6Router;
    }

    public static VpnInterfaceAddressConfig getConfigByIndex(int index) {
        VpnInterfaceAddressConfig[] values = values();
        if (index >= 0 && index < values.length) {
            return values[index];
        }
        return OPTION_1;
    }
}