package com.v2ray.ang.service;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.util.Log;


import com.fenix.sshservice.config.Settings;
import com.fenix.sshservice.logger.SkStatus;

import hex.custom.lib.v2ray.core.Tun2SocksControl;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import hex.custom.lib.v2ray.services.V2rayVPNService;
import hex.custom.lib.v2ray.utils.VpnInterfaceAddressConfig;

public class TProxyService implements Tun2SocksControl {

    static {
        System.loadLibrary("hev-socks5-tunnel");
    }


    private static native void TProxyStartService(String configPath, int fd);

    private static native void TProxyStopService();

    private static native long[] TProxyGetStats();

    private final Context context;
    private final ParcelFileDescriptor vpnInterface;
    private final BooleanSupplier isRunningProvider;
    private final Runnable restartCallback;
    private String LOOPBACK = "127.0.0.1";
    int porta;

    public TProxyService(Context context, ParcelFileDescriptor vpnInterface, BooleanSupplier isRunningProvider, Runnable restartCallback, int port) {
        this.context = context;
        this.vpnInterface = vpnInterface;
        this.isRunningProvider = isRunningProvider;
        this.restartCallback = restartCallback;
        porta = port;
    }

    @Override
    public void startTun2Socks() {
        String configContent = buildConfig();
        File configFile = new File(context.getFilesDir(), "hev-socks5-tunnel.yaml");
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(configContent);
        } catch (IOException e) {
            Log.d(V2rayVPNService.class.getSimpleName(), "Failed to write hev-socks5-tunnel config: " + e.getMessage());
        }

        SkStatus.logInfo("Hev-Socks5 Iniciado");
        Log.d(V2rayVPNService.class.getSimpleName(), "HevSocks5Tunnel Config content:\n" + configContent);

        try {
            TProxyStartService(configFile.getAbsolutePath(), vpnInterface.getFd());
        } catch (Exception e) {
            Log.d(V2rayVPNService.class.getSimpleName(), "HevSocks5Tunnel exception: " + e.getMessage());
        }
    }

    public static VpnInterfaceAddressConfig getCurrentVpnInterfaceAddressConfig() {
        int selectedIndex = 0;
        return VpnInterfaceAddressConfig.getConfigByIndex(selectedIndex);
    }

    private String buildConfig() {
        Settings mConfig = new Settings(context);
        Object vpnConfig =  getCurrentVpnInterfaceAddressConfig();

        StringBuilder sb = new StringBuilder();
        sb.append("tunnel:\n");
        sb.append("  mtu: ").append("1500").append("\n");
        sb.append("  ipv4: ").append(getIpv4Client(vpnConfig)).append("\n");

        sb.append("socks5:\n");
        sb.append("  port: ").append(porta).append("\n");
        sb.append("  address: ").append(LOOPBACK).append("\n");
        sb.append("  udp: 'udp'\n");

        String timeoutSetting = "300,600";
        if (timeoutSetting == null) {
            timeoutSetting = "300,60";
        }

        List<String> parts = new ArrayList<>();
        for (String part : timeoutSetting.split(",")) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                parts.add(trimmed);
            }
        }
        int tcpTimeout = 300;
        int udpTimeout = 60;
        if (parts.size() > 0) {
            try {
                tcpTimeout = Integer.parseInt(parts.get(0));
            } catch (NumberFormatException ignored) {
            }
        }
        if (parts.size() > 1) {
            try {
                udpTimeout = Integer.parseInt(parts.get(1));
            } catch (NumberFormatException ignored) {
            }
        }

        sb.append("misc:\n");
        sb.append("  tcp-read-write-timeout: ").append(tcpTimeout * 1000).append("\n");
        sb.append("  udp-read-write-timeout: ").append(udpTimeout * 1000).append("\n");
        sb.append("  log-level: ").append("warn").append("\n");

        return sb.toString();
    }

    private String getIpv4Client(Object vpnConfig) {
        try {
            return (String) vpnConfig.getClass().getField("ipv4Client").get(vpnConfig);
        } catch (Exception e) {
            return "";
        }
    }

    private String getIpv6Client(Object vpnConfig) {
        try {
            return (String) vpnConfig.getClass().getField("ipv6Client").get(vpnConfig);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void stopTun2Socks() {
        try {
            Log.d(V2rayVPNService.class.getSimpleName(), "TProxyStopService...");
            TProxyStopService();
            SkStatus.logInfo("Hev-Socks5- Desconectado");
        } catch (Exception e) {
            Log.d(V2rayVPNService.class.getSimpleName(), "Failed to stop hev-socks5-tunnel" + e);
        }
    }
}