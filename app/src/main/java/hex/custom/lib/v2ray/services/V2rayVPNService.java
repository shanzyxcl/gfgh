package hex.custom.lib.v2ray.services;

import static hex.custom.lib.v2ray.utils.V2rayConstants.V2RAY_SERVICE_COMMAND_EXTRA;
import static hex.custom.lib.v2ray.utils.V2rayConstants.V2RAY_SERVICE_COMMAND_INTENT;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.VpnService;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fenix.sshservice.config.Settings;
import com.fenix.sshservice.logger.SkStatus;
import com.v2ray.ang.service.TProxyService;

import hex.custom.lib.v2ray.core.Tun2SocksControl;

import java.io.File;
import java.io.FileDescriptor;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import hex.custom.lib.v2ray.core.V2rayCoreExecutor;
import hex.custom.lib.v2ray.interfaces.StateListener;
import hex.custom.lib.v2ray.interfaces.Tun2SocksListener;
import hex.custom.lib.v2ray.model.V2rayConfigModel;
import hex.custom.lib.v2ray.utils.V2rayConstants;

import hex.custom.lib.v2ray.interfaces.V2rayServicesListener;

public class V2rayVPNService extends VpnService implements V2rayServicesListener, Tun2SocksListener {
    private ParcelFileDescriptor tunnelInterface;
    private V2rayCoreExecutor v2rayCoreExecutor;
    private NotificationService notificationService;
    boolean isRunning = true;
    private StaticsBroadCastService staticsBroadCastService;
    private V2rayConstants.CONNECTION_STATES connectionState = V2rayConstants.CONNECTION_STATES.DISCONNECTED;
    private V2rayConfigModel currentConfig = new V2rayConfigModel();
    private boolean isServiceCreated = false;
    private boolean isServiceSetupStarted = false;
    private final AtomicBoolean hevStarting = new AtomicBoolean(false);

    private final BroadcastReceiver serviceCommandBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                V2rayConstants.SERVICE_COMMANDS serviceCommand = (V2rayConstants.SERVICE_COMMANDS) intent.getSerializableExtra(V2rayConstants.V2RAY_SERVICE_COMMAND_EXTRA);
                if (serviceCommand == null) {
                    return;
                }
                switch (serviceCommand) {
                    case STOP_SERVICE:
                        if (v2rayCoreExecutor != null) {
                            v2rayCoreExecutor.stopCore(true);
                        }
                        stopService();
                        break;
                    case MEASURE_DELAY:
                        if (v2rayCoreExecutor != null) {
                            v2rayCoreExecutor.broadCastCurrentServerDelay();
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception ignore) {
            }
        }
    };

    Process process;
    private Settings mConfig;
    private Tun2SocksControl tun2SocksService;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!isServiceCreated) {
            mConfig = new Settings(this);
            connectionState = V2rayConstants.CONNECTION_STATES.CONNECTING;
            StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(threadPolicy);
            v2rayCoreExecutor = new V2rayCoreExecutor(this);
            notificationService = new NotificationService(this);
            staticsBroadCastService = new StaticsBroadCastService(this, new StateListener() {
                @Override
                public V2rayConstants.CONNECTION_STATES getConnectionState() {
                    return connectionState;
                }

                @Override
                public V2rayConstants.CORE_STATES getCoreState() {
                    if (v2rayCoreExecutor == null) {
                        return V2rayConstants.CORE_STATES.IDLE;
                    }
                    return v2rayCoreExecutor.getCoreState();
                }

                @Override
                public long getDownloadSpeed() {
                    if (v2rayCoreExecutor == null) {
                        return -1;
                    }
                    return v2rayCoreExecutor.getDownloadSpeed();
                }

                @Override
                public long getUploadSpeed() {
                    if (v2rayCoreExecutor == null) {
                        return -1;
                    }
                    return v2rayCoreExecutor.getUploadSpeed();
                }


            });
            isServiceCreated = true;
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            V2rayConstants.SERVICE_COMMANDS serviceCommand = (V2rayConstants.SERVICE_COMMANDS) intent.getSerializableExtra(V2RAY_SERVICE_COMMAND_EXTRA);
            if (serviceCommand == null) {
                return super.onStartCommand(intent, flags, startId);
            }
            switch (serviceCommand) {
                case STOP_SERVICE:
                    v2rayCoreExecutor.stopCore(true);
                    isRunning = false;
                    if (process != null) {
                        process.destroy();
                    }
                    break;
                case START_SERVICE:
                    currentConfig = (V2rayConfigModel) intent.getSerializableExtra(V2rayConstants.V2RAY_SERVICE_CONFIG_EXTRA);
                    if (currentConfig == null) {
                        stopService();
                        break;
                    }
                    staticsBroadCastService.isTrafficStaticsEnabled = currentConfig.enableTrafficStatics;
                    if (currentConfig.enableTrafficStatics && currentConfig.enableTrafficStaticsOnNotification) {
                        staticsBroadCastService.trafficListener = notificationService.trafficListener;
                    }
                   if (v2rayCoreExecutor.startCore(currentConfig)) {
                       Log.e(V2rayVPNService.class.getSimpleName(), "onStartCommand success => com.v2ray core started.");
                   } else {
                       this.onDestroy();
                   }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        registerReceiver(serviceCommandBroadcastReceiver, new IntentFilter(V2RAY_SERVICE_COMMAND_INTENT), RECEIVER_EXPORTED);
                    } else {
                        registerReceiver(serviceCommandBroadcastReceiver, new IntentFilter(V2RAY_SERVICE_COMMAND_INTENT));
                    }
                    return START_STICKY;
                default:
                    onDestroy();
                    break;
            }
        } catch (Exception ignore) {
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onRevoke() {
        stopService();
    }

    private void setupService() {
        if (isServiceSetupStarted) {
            return;
        } else {
            isServiceSetupStarted = true;
        }
        try {
            if (tunnelInterface != null) {
                tunnelInterface.close();
            }
        } catch (Exception ignore) {
        }
        Intent prepare_intent = prepare(this);
        if (prepare_intent != null) {
            return;
        }
        Builder builder = getBuilder();
        try {
            try {
                builder.addDisallowedApplication(getPackageName());
            } catch (Exception e) {
            }
            tunnelInterface = builder.establish();
            isRunning = true;
                runHevsocks();
            if (isRunning) {
                connectionState = V2rayConstants.CONNECTION_STATES.CONNECTED;
                Notification notification = notificationService.setConnectedNotification(currentConfig.remark, currentConfig.applicationIcon);
                if (notification != null) {
                    startForeground(1, notification);
                }
                staticsBroadCastService.start();
            }
        } catch (Exception e) {
            Log.e(V2rayVPNService.class.getSimpleName(), "setupFailed = ", e);
            stopService();
        }
    }

    private void runHevsocks() {
        if (!hevStarting.compareAndSet(false, true)) {
            return;
        }
        new Thread(() -> {

            int socksPort = currentConfig != null ? currentConfig.localSocksPort : 10808;
            try {
                    tun2SocksService = new TProxyService(
                            this,
                            tunnelInterface,
                            () -> isRunning,
                            this::runHevsocks,
                            socksPort
                    );

                if (tun2SocksService != null) {
                    tun2SocksService.startTun2Socks();
                }
            } finally {
                hevStarting.set(false);
            }
        }, "Hevsocks_Init_Thread").start();
    }

    private void runTun2socks() {
        ArrayList<String> cmd = new ArrayList<>(Arrays.asList(new File(getApplicationInfo().nativeLibraryDir, "libv2ray.so").getAbsolutePath(),
                "--netif-ipaddr", "26.26.26.2",
                "--netif-netmask", "255.255.255.252",
                "--socks-server-addr", "127.0.0.1:" + currentConfig.localSocksPort,
                "--tunmtu", "1500",
                "--sock-path", "sock_path",
                "--enable-udprelay",
                "--loglevel", "error"));
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            processBuilder.redirectErrorStream(true);
            process = processBuilder.directory(getApplicationContext().getFilesDir()).start();
            new Thread(() -> {
                try {
                    process.waitFor();
                    if (isRunning) {
                        runTun2socks();
                    }
                } catch (InterruptedException e) {
                    //ignore
                }
            }, "Tun2socks_Thread").start();
            sendFileDescriptor();
        } catch (Exception e) {
            Log.e("VPN_SERVICE", "FAILED=>", e);
            this.onDestroy();
        }
    }

    @NonNull
    private Builder getBuilder() {
        Builder builder = new Builder();
        builder.setSession(currentConfig.remark);
        builder.setMtu(1500);
        builder.addAddress("26.26.26.1", 30);
        builder.addRoute("0.0.0.0", 0);
        if (currentConfig.enableLocalTunneledDNS) {
            builder.addDnsServer("26.26.26.2");
        } else {
            builder.addDnsServer("1.1.1.1");
            builder.addDnsServer("8.8.8.8");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            builder.setMetered(false);
        }
        if (currentConfig.blockedApplications != null) {
            for (int i = 0; i < currentConfig.blockedApplications.size(); i++) {
                try {
                    builder.addDisallowedApplication(currentConfig.blockedApplications.get(i));
                } catch (Exception ignore) {
                }
            }
        }
        return builder;
    }

    private void sendFileDescriptor() {
        String localSocksFile = new File(getApplicationContext().getFilesDir(), "sock_path").getAbsolutePath();
        FileDescriptor tunFd = tunnelInterface.getFileDescriptor();
        new Thread(() -> {
            int sendFDTries = 0;
            boolean isSendFDSuccess = false;
            while (true){
                try {
                    Thread.sleep(50L * sendFDTries);
                    LocalSocket clientLocalSocket = new LocalSocket();
                    clientLocalSocket.connect(new LocalSocketAddress(localSocksFile, LocalSocketAddress.Namespace.FILESYSTEM));
                    if (!clientLocalSocket.isConnected()) {
                        Log.i("SOCK_FILE", "Unable to connect to localSocksFile [" + localSocksFile + "]");
                    } else {
                        Log.i("SOCK_FILE", "connected to sock file [" + localSocksFile + "]");
                    }
                    OutputStream clientOutStream = clientLocalSocket.getOutputStream();
                    clientLocalSocket.setFileDescriptorsForSend(new FileDescriptor[]{tunFd});
                    clientOutStream.write(32);
                    clientLocalSocket.shutdownOutput();
                    clientLocalSocket.close();
                    isSendFDSuccess = true;
                    break;
                } catch (Exception ignore) {
                }
            }
            if (!isSendFDSuccess) {
                Log.w("SendFDFailed", "Could`nt send file descriptor !");
            }
        }, "sendFd_Thread").start();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(serviceCommandBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onProtect(int socket) {
        return protect(socket);
    }

    @Override
    public Service getService() {
        return this;
    }

    @Override
    public void startService() {
        setupService();
    }

    @Override
    public void stopService() {
        try {
            staticsBroadCastService.sendDisconnectedBroadCast(this);
            if (process != null) {
                process.destroy();
            }
            if (tun2SocksService != null) {
                tun2SocksService.stopTun2Socks();
            }
            staticsBroadCastService.stop();
            stopForeground(true);
            notificationService.dismissNotification();
            stopSelf();
            try {
                tunnelInterface.close();
            } catch (Exception ignore) {

            }
        } catch (Exception e) {
            Log.d(V2rayVPNService.class.getSimpleName(), "stopService => ", e);
        }
    }

    @Override
    public void OnTun2SocksHasMassage(V2rayConstants.CORE_STATES tun2SocksState, String newMessage) {
        SkStatus.logInfo("TUN2SOCKS " + newMessage);
    }

}
