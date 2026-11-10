package com.fenix.sshservice;

import android.app.Service;
import android.content.Intent;
import android.net.VpnService;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.fenix.sshservice.config.V2Config;
import com.fenix.sshservice.tunnel.vpn.V2Listener;
import com.fenix.sshservice.logger.SkStatus;

import hev.htproxy.TProxyService;

public class V2Service extends VpnService implements V2Listener {
    private static final String TAG = "HevSocks5";
    private ParcelFileDescriptor mInterface;
    private TProxyService tproxyService;
    private V2Config v2Config;
    private long startTime = 0;
    private static final long STOP_PROTECTION_MS = 5000; // Ignorar STOP_SERVICE en primeros 5 segundos

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "═══════════════════════════════════════");
        Log.i(TAG, "🔄 V2Service.onCreate()");
        Log.i(TAG, "═══════════════════════════════════════");
        V2Core.getInstance().setUpListener(this);
        Log.i(TAG, "✅ V2Core.setUpListener() done");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            Log.e(TAG, "❌ onStartCommand: intent is null");
            stopSelf();
            return START_NOT_STICKY;
        }
        
        V2Configs.V2RAY_SERVICE_COMMANDS startCommand = 
            (V2Configs.V2RAY_SERVICE_COMMANDS) intent.getSerializableExtra("COMMAND");
        
        if (startCommand == null) {
            Log.e(TAG, "❌ onStartCommand: COMMAND extra is null");
            stopSelf();
            return START_NOT_STICKY;
        }
        
        Log.i(TAG, "═══════════════════════════════════════");
        Log.i(TAG, "🔄 onStartCommand: " + startCommand.name());
        Log.i(TAG, "═══════════════════════════════════════");
        
        // Protección: ignorar STOP_SERVICE en los primeros segundos (evita race conditions)
        if (startCommand == V2Configs.V2RAY_SERVICE_COMMANDS.STOP_SERVICE && startTime > 0) {
            long elapsed = System.currentTimeMillis() - startTime;
            if (elapsed < STOP_PROTECTION_MS) {
                Log.w(TAG, "⚠ STOP_SERVICE recibido a los " + elapsed + "ms, IGNORADO (protección " + STOP_PROTECTION_MS + "ms)");
                return START_STICKY;
            }
        }
        
        switch (startCommand) {
            case START_SERVICE:
                startTime = System.currentTimeMillis();
                v2Config = (V2Config) intent.getSerializableExtra("V2RAY_CONFIG");
                if (v2Config == null) {
                    Log.e(TAG, "❌ V2RAY_CONFIG extra is null");
                    stopSelf();
                    return START_NOT_STICKY;
                }
                Log.i(TAG, "📋 Config loaded: " + v2Config.REMARK);
                Log.i(TAG, "📋 SOCKS5 port from config: " + v2Config.LOCAL_SOCKS5_PORT);
                Log.i(TAG, "📋 HTTP port from config: " + v2Config.LOCAL_HTTP_PORT);
                
                if (V2Core.getInstance().isV2rayCoreRunning()) {
                    Log.w(TAG, "⚠ V2Ray already running, stopping first...");
                    V2Core.getInstance().stopCore();
                }
                
                Log.i(TAG, "🚀 Starting V2Ray core...");
                boolean started = V2Core.getInstance().startCore(v2Config);
                if (!started) {
                    Log.e(TAG, "❌ V2Core.startCore() returned FALSE");
                    stopSelf();
                } else {
                    Log.i(TAG, "✅ V2Core.startCore() returned TRUE (V2Ray should start soon)");
                }
                break;
            case STOP_SERVICE:
                Log.i(TAG, "🛑 STOP_SERVICE received (stop completo)");
                // Llamar stopAllProcess que incluye stopSelf para finalizar el servicio
                stopAllProcess();
                break;
            case MEASURE_DELAY:
                Log.i(TAG, "⏱ MEASURE_DELAY requested");
                new Thread(() -> {
                    long delay = V2Core.getInstance().getConnectedV2rayServerDelay();
                    Log.i(TAG, "⏱ Server delay: " + delay + "ms");
                    Intent sendB = new Intent("CONNECTED_V2RAY_SERVER_DELAY");
                    sendB.putExtra("DELAY", String.valueOf(delay));
                    sendBroadcast(sendB);
                }, "MEASURE_CONNECTED_V2RAY_SERVER_DELAY").start();
                break;
            default:
                Log.w(TAG, "⚠ Unknown command: " + startCommand);
                stopSelf();
                break;
        }
        return START_STICKY;
    }

    // Limpia VPN y TProxyService pero NO llama stopSelf (para reinicios)
    private void cleanupVpn() {
        Log.i(TAG, "═══════════════════════════════════════");
        Log.i(TAG, "🧹 cleanupVpn()");
        Log.i(TAG, "═══════════════════════════════════════");
        
        if (tproxyService != null) {
            Log.i(TAG, "  Stopping TProxyService...");
            tproxyService.stopTun2Socks();
            tproxyService = null;
        }
        try {
            if (mInterface != null) {
                mInterface.close();
                Log.i(TAG, "  TUN interface closed");
            }
        } catch (Exception e) {
            Log.e(TAG, "  TUN close error", e);
        }
        Log.i(TAG, "✅ cleanupVpn completed");
    }
    
    private void stopAllProcess() {
        Log.i(TAG, "═══════════════════════════════════════");
        Log.i(TAG, "🛑 stopAllProcess()");
        Log.i(TAG, "═══════════════════════════════════════");
        
        stopForeground(true);
        cleanupVpn();
        
        Log.i(TAG, "  Stopping V2Ray core...");
        V2Core.getInstance().stopCore();
        
        try {
            stopSelf();
        } catch (Exception e) {
            Log.e(TAG, "  stopSelf failed", e);
        }
        Log.i(TAG, "✅ stopAllProcess completed");
    }

    public static final String ACTION_V2RAY_CONNECTED = "com.fenix.vpn.V2RAY_CONNECTED";
    
    private void setup() {
        Log.i(TAG, "═══════════════════════════════════════");
        Log.i(TAG, "🔄 V2Service.setup() - Building VPN");
        Log.i(TAG, "═══════════════════════════════════════");
        
        Intent prepare_intent = prepare(this);
        if (prepare_intent != null) {
            Log.e(TAG, "❌ VPN permission NOT granted (prepare returned non-null)");
            SkStatus.logInfo("Permiso VPN no otorgado para V2Ray");
            return;
        }
        
        Log.i(TAG, "✅ VPN permission granted");
        
        Builder builder = new Builder();
        builder.setSession(v2Config.REMARK);
        builder.setMtu(1500);
        builder.addAddress("26.26.26.1", 30);
        builder.addRoute("0.0.0.0", 0);
        // DNS like XRAY 26.6.1
        builder.addDnsServer("1.1.1.1");
        builder.addDnsServer("8.8.8.8");
        
        if (v2Config.BLOCKED_APPS != null) {
            for (String app : v2Config.BLOCKED_APPS) {
                try {
                    builder.addDisallowedApplication(app);
                } catch (Exception e) {
                    Log.w(TAG, "  addDisallowedApplication failed for: " + app);
                }
            }
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            builder.setMetered(false);
        }
        
        try {
            if (mInterface != null) {
                mInterface.close();
            }
            mInterface = builder.establish();
            
            if (mInterface == null) {
                Log.e(TAG, "❌ VPN establish returned null!");
                SkStatus.logInfo("Error: VPN establish retornó null");
                return;
            }
            
            int fd = mInterface.getFd();
            Log.i(TAG, "✅ VPN established successfully");
            Log.i(TAG, "   TUN FD:     " + fd);
            Log.i(TAG, "   SOCKS port: " + v2Config.LOCAL_SOCKS5_PORT);
            
            // Iniciar hev-socks5-tunnel JNI
            int socksPort = v2Config.LOCAL_SOCKS5_PORT;
            Log.i(TAG, "🚀 Creating TProxyService(port=" + socksPort + ", fd=" + fd + ")");
            
            tproxyService = new TProxyService(this, mInterface, socksPort, "26.26.26.2");
            
            if (TProxyService.isLoaded()) {
                Log.i(TAG, "✅ hev-socks5 library loaded, starting tunnel...");
                tproxyService.startTun2Socks();
                Log.i(TAG, "✅ TProxyService started!");
                SkStatus.logInfo("HevSocks5 tunnel iniciado");
            } else {
                Log.e(TAG, "❌ hev-socks5 library NOT loaded!");
                SkStatus.logInfo("Error: hev-socks5 library no cargada");
            }
            
            // ✅ Notificar al proceso principal que V2Ray está conectado
            Log.i(TAG, "📡 Enviando broadcast V2RAY_CONNECTED al proceso principal...");
            Intent connectedIntent = new Intent(ACTION_V2RAY_CONNECTED);
            sendBroadcast(connectedIntent);
            Log.i(TAG, "✅ Broadcast V2RAY_CONNECTED enviado");
            
            // --- DEBUG: medir delay del servidor remoto ---
            new Thread(() -> {
                try {
                    // Esperar un poco a que V2Ray estabilice
                    Thread.sleep(2000);
                    
                    long delay = V2Core.getInstance().getConnectedV2rayServerDelay();
                    Log.i(TAG, "⏱ Server delay (measureDelay): " + delay + "ms");
                    
                    if (delay < 0) {
                        Log.w(TAG, "⚠ El servidor remoto NO responde! " + v2Config.CONNECTED_V2RAY_SERVER_ADDRESS + ":" + v2Config.CONNECTED_V2RAY_SERVER_PORT);
                    } else {
                        Log.i(TAG, "✅ Servidor remoto responde OK en " + delay + "ms");
                    }
                    
                    // Verificar qué apps están en BLOCKED_APPS
                    if (v2Config.BLOCKED_APPS != null && !v2Config.BLOCKED_APPS.isEmpty()) {
                        Log.i(TAG, "📋 BLOCKED_APPS (apps que BYPASSEAN el VPN):");
                        for (String app : v2Config.BLOCKED_APPS) {
                            Log.i(TAG, "   → " + app);
                        }
                    } else {
                        Log.i(TAG, "📋 BLOCKED_APPS: vacío (todas las apps pasan por VPN)");
                    }
                    
                    // Verificar que 0.0.0.0/0 está en las rutas
                    Log.i(TAG, "🔍 VPN routes: addRoute 0.0.0.0/0 => todo el tráfico debe pasar por VPN");
                    
                } catch (Exception e) {
                    Log.w(TAG, "⏱ measureDelay exception: " + e.getMessage());
                }
            }, "V2Ray-Debug-Check").start();
            
        } catch (Exception e) {
            Log.e(TAG, "❌ Setup failed: " + e.getMessage(), e);
            SkStatus.logInfo("Error al configurar VPN: " + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "═══════════════════════════════════════");
        Log.i(TAG, "🔄 V2Service.onDestroy()");
        Log.i(TAG, "═══════════════════════════════════════");
        super.onDestroy();
    }

    @Override
    public void onRevoke() {
        Log.w(TAG, "⚠ VPN revoked by user");
        stopAllProcess();
    }

    @Override
    public boolean onProtect(int socket) {
        boolean result = protect(socket);
        Log.d(TAG, "🔒 onProtect(" + socket + ") => " + result);
        return result;
    }

    @Override
    public Service getService() {
        return this;
    }

    @Override
    public void startService() {
        Log.i(TAG, "═══════════════════════════════════════");
        Log.i(TAG, "🔄 V2Service.startService() called by V2Core callback");
        Log.i(TAG, "═══════════════════════════════════════");
        setup();
    }

    @Override
    public void stopService() {
        Log.i(TAG, "═══════════════════════════════════════");
        Log.i(TAG, "🔄 V2Service.stopService() called (desde V2Core callback)");
        Log.i(TAG, "   NOTA: cleanupVpn() sin stopSelf() para evitar matar el proceso");
        Log.i(TAG, "═══════════════════════════════════════");
        // SOLO limpia VPN/TProxy, NO llama stopSelf (la vida del servicio la maneja onStartCommand)
        cleanupVpn();
    }

    @Override
    public void onConnected() {
        Log.i(TAG, "✅ V2Service.onConnected() - V2Ray connected!");
    }

    @Override
    public void onError() {
        Log.e(TAG, "❌ V2Service.onError() - V2Ray error!");
    }

    @Override
    public void restartService() {
        Log.i(TAG, "🔄 V2Service.restartService() called");
    }
}
