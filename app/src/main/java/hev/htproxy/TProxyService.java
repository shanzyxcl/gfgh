package hev.htproxy;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.fenix.sshservice.tunnel.vpn.Tun2SocksControl;

import java.io.File;

/**
 * hev-socks5-tunnel vía JNI nativa.
 * El .so "libhev-socks5-tunnel.so" registra natives para hev.htproxy.TProxyService
 * 
 * Clase Java pura para evitar que R8 elimine métodos nativos.
 * TAG para logcat: "HevSocks5"
 */
public class TProxyService implements Tun2SocksControl {
    
    private static final String TAG = "HevSocks5";
    private static boolean loaded = false;
    
    private Context context;
    private ParcelFileDescriptor vpnInterface;
    private int socksPort;
    private String tunnelIp;
    
    // Métodos nativos (registrados por JNI_OnLoad del .so)
    public static native void TProxyStartService(String configPath, int fd);
    public static native void TProxyStopService();
    public static native long[] TProxyGetStats();
    
    static {
        try {
            System.loadLibrary("hev-socks5-tunnel");
            loaded = true;
            Log.i(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            Log.i(TAG, "✅ hev-socks5-tunnel JNI LOADED OK");
            Log.i(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        } catch (Throwable t) {
            loaded = false;
            Log.e(TAG, "❌ CRITICAL: Failed to load hev-socks5-tunnel", t);
            throw new RuntimeException("Failed to load hev-socks5-tunnel", t);
        }
    }
    
    public TProxyService(Context context, ParcelFileDescriptor vpnInterface, int socksPort, String tunnelIp) {
        this.context = context;
        this.vpnInterface = vpnInterface;
        this.socksPort = socksPort;
        this.tunnelIp = tunnelIp;
    }
    
    @Override
    public void startTun2Socks() {
        int fd = vpnInterface.getFd();
        Log.i(TAG, "========================================");
        Log.i(TAG, "🚀 startTun2Socks()");
        Log.i(TAG, "   SOCKS port: " + socksPort);
        Log.i(TAG, "   TUN FD:     " + fd);
        Log.i(TAG, "   Tunnel IP:  " + tunnelIp);
        Log.i(TAG, "========================================");
        
        String configContent = buildYamlConfig();
        File configFile = new File(context.getFilesDir(), "hev-socks5.yaml");
        File logFile = new File(context.getFilesDir(), "hev-socks5-tunnel.log");
        
        try {
            java.io.FileWriter writer = new java.io.FileWriter(configFile);
            writer.write(configContent);
            writer.close();
            
            Log.i(TAG, "   Config file: " + configFile.getAbsolutePath());
            Log.i(TAG, "   YAML config:\n" + configContent);
            
            TProxyStartService(configFile.getAbsolutePath(), fd);
            Log.i(TAG, "✅ HevSocks5Tunnel started SUCCESS (fd=" + fd + ")");
            
            // Leer log de hev-socks5 para debug
            if (logFile.exists()) {
                try {
                    java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(logFile));
                    String line;
                    Log.i(TAG, "━━━ hev-socks5 log output ━━━");
                    while ((line = br.readLine()) != null) {
                        Log.i(TAG, "  " + line);
                    }
                    br.close();
                    Log.i(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━");
                } catch (Exception ignored) {}
            } else {
                Log.w(TAG, "⚠ hev-socks5 log file NOT FOUND: " + logFile.getAbsolutePath());
            }
            
        } catch (Throwable t) {
            Log.e(TAG, "❌ HevSocks5Tunnel start FAILED: " + t.getMessage(), t);
            if (t instanceof RuntimeException) throw (RuntimeException) t;
            throw new RuntimeException("hev-socks5-tunnel start failed", t);
        }
    }
    
    private String buildYamlConfig() {
        StringBuilder sb = new StringBuilder();
        sb.append("tunnel:\n");
        sb.append("  mtu: 1500\n");
        sb.append("  ipv4: ").append(tunnelIp).append("\n");
        sb.append("socks5:\n");
        sb.append("  port: ").append(socksPort).append("\n");
        sb.append("  address: 127.0.0.1\n");
        sb.append("  udp: 'udp'\n");
        sb.append("misc:\n");
        sb.append("  tcp-read-write-timeout: 300000\n");
        sb.append("  udp-read-write-timeout: 60000\n");
        sb.append("  log-level: debug\n");
        sb.append("  log-file: ").append(context.getFilesDir()).append("/hev-socks5-tunnel.log\n");
        return sb.toString();
    }
    
    @Override
    public void stopTun2Socks() {
        if (!loaded) return;
        Log.i(TAG, "========================================");
        Log.i(TAG, "🛑 stopTun2Socks()");
        Log.i(TAG, "========================================");
        try {
            TProxyStopService();
            Log.i(TAG, "✅ HevSocks5Tunnel stopped OK");
        } catch (Throwable t) {
            Log.e(TAG, "❌ HevSocks5Tunnel stop FAILED: " + t.getMessage(), t);
        }
    }
    
    public static boolean isLoaded() {
        return loaded;
    }
}
