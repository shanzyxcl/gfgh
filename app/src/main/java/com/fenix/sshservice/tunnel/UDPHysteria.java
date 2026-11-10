package com.fenix.sshservice.tunnel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fenix.sshservice.config.Settings;
import com.fenix.sshservice.config.SettingsConstants;
import com.fenix.sshservice.logger.SkStatus;
import com.fenix.vpn.R;

import java.io.File;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UDPHysteria extends Thread {
    private String address;
    private String auth;
    private String ca;
    Context context;
    private String dir;
    private String dwon;
    private UDPSetting err;
    private File fileConf;
    private File fileDns;
    private File fileca;
    private String listen;
    private String obfs;
    private UDPSetting out;
    private String rc_conn;
    private String rc_w;
    private String retry;
    private String serv;
    private String server, port;
    private final SharedPreferences sp;
    private Process udpProcess;
    private String up;
    private String estado = "";
    private UDPListener udpListener;
    private boolean mReconnecting = false, mStopping = false, mStarting = false;
    private final static int RECONNECT_TRIES = 5;



    public UDPHysteria(Context context) {
        this.context = context;
        this.dir = context.getFilesDir().getPath();
        this.sp = new Settings(context).getPrefsPrivate();
        udpListener = TunnelManagerThread.getUDPListener();

    }

    public static Inet4Address getIPv4Addresses(InetAddress[] inetAddressArr) {
        for (InetAddress inetAddress : inetAddressArr) {
            if (inetAddress instanceof Inet4Address) {
                return (Inet4Address) inetAddress;
            }
        }
        return null;
    }


    public void run2(String str) {
        //SkStatus.logInfo("run: "+str);
        //String str2;
        //String str3 = null;
        if (!str.contains("ZIVPN UDP running")) {
            if (str.contains("no recent network activity")) {
                if (sp.getBoolean("reconnectup", false)) {
                    //SkStatus.logInfo("<font color='red'><strong>Connection Lost retrying </strong></font>");
                    stopVudp2();
                    return;
                }

                try {
                    //reconectarUDP();
                } catch (Exception e) {
                    stopVudp2();
                }


            } else if (str.contains("auth error")) {
                SkStatus.logInfo("<font color='red'><strong>Error al auntenticar</strong></font>");
                udpListener.onAuthFailed();
            }
            try {
                reconnectUDP();
            } catch (Exception e) {
                    stopVudp2();
            }

            return;
        } else {

            SkStatus.logInfo("Verificando conexión");
            udpListener.onConnected();


        }
    }

    public void stopVudp() {
        //SkStatus.logInfo("Stopping UDP....");
        interrupt();
        while (true) {
            try {
                Thread.sleep(2000);
                mStopping = true;
                TunnelManagerHelper.stopSocketClay(this.context);
                return;
            } catch (InterruptedException unused) {
                unused.printStackTrace();
            }
        }
    }

    private boolean printToFile(File file, String str) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println(str);
            printWriter.flush();
            printWriter.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void interrupt() {
        UDPSetting UDPSetting = this.out;
        if (UDPSetting != null) {
            UDPSetting.setInterrupted(true);
            out.interrupt();
            out = null;
        }
        UDPSetting UDPSetting2 = err;
        if (UDPSetting2 != null) {
            UDPSetting2.setInterrupted(true);
            err.interrupt();
            err = null;
        }
        Process process = udpProcess;
        if (process != null) {
            process.destroy();
            udpProcess = null;
        }
        File file = fileConf;
        if (file != null && file.exists()) {
            fileConf.delete();
        }
        super.interrupt();
    }


    @Override
    public void run() {
        this.fileca = new File(dir, "fenixudp.ca.crt");
        String string = this.sp.getString(SettingsConstants.UDP_SERVER, "udp.wakkodev.online");
        this.serv = string;
        if (string.isEmpty()) {
            this. serv = "udp.wakkodev.online";
            try {
                this.sp.edit().putString(SettingsConstants.UDP_SERVER, serv).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        InetAddress[] inetAddressArr = new InetAddress[0];
        try {
            inetAddressArr = InetAddress.getAllByName(this.serv);
        } catch (UnknownHostException e2) {
            e2.printStackTrace();
        }
        try {
            this.address = getIPv4Addresses(inetAddressArr).getHostAddress();
        } catch (Exception e3) {
            e3.printStackTrace();
            if (String.valueOf(e3).contains("on a null object reference")) {
                SkStatus.logInfo("<font color='red'><strong>Invalid UDP Server</strong></font>");
                stopVudp2();
            }
        }
        port = sp.getString(Settings.UDP_PORT, "1");
        server = address + ":" + port;
        String string2 = sp.getString(SettingsConstants.UDP_OBFS, "wakkovpn");
        obfs = string2;
        if (string2.isEmpty()) {
            obfs = "wakkovpn";
            try {
                sp.edit().putString(SettingsConstants.UDP_OBFS, obfs).apply();
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
        String string3 = sp.getString(SettingsConstants.UDP_AUTH, "naju");
        auth = string3;
        if (string3.isEmpty()) {
            auth = "naju";
            try {
                sp.edit().putString(SettingsConstants.UDP_AUTH, auth).apply();
            } catch (Exception e5) {
                e5.printStackTrace();
            }
        }

        String string4 = sp.getString(SettingsConstants.UDP_UP, "100");
        up = string4;
        if (string4.isEmpty()) {
            up = "1";
            try {
                sp.edit().putString(SettingsConstants.UDP_UP, up).apply();
            } catch (Exception e5) {
                e5.printStackTrace();
            }
        }
        String string5 = sp.getString(SettingsConstants.UDP_DOWN, "100");
        dwon = string5;
        if (string5.isEmpty()) {
            dwon = "2";
            try {
                sp.edit().putString(SettingsConstants.UDP_DOWN, dwon).apply();
            } catch (Exception e5) {
                e5.printStackTrace();
            }
        }
        this.retry = "3";

        String string6 = sp.getString(SettingsConstants.UDP_BUFFER, "196608");
        rc_conn = string6;
        if (string6.isEmpty()) {
            rc_conn = "196608";
            try {
                sp.edit().putString(SettingsConstants.UDP_BUFFER, rc_conn).apply();
            } catch (Exception e5) {
                e5.printStackTrace();
            }
        }
        try {
            this.rc_w = String.valueOf((Integer.parseInt(this.rc_conn) * 5) / 2);
        } catch (NumberFormatException e6) {
            e6.printStackTrace();
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("127.0.0.1");
        stringBuilder2.append(":1080");
        this.listen = stringBuilder2.toString();
        String str = dir + "/fenixudp.ca.crt";
        ca = str;
        String format = String.format("{\n  \"server\": \"%s\",\n  \"obfs\": \"%s\",\n  \"auth_str\": \"%s\",\n  \"up_mbps\": %s,\n  \"down_mbps\": %s,\n  \"retry\": %s,\n  \"retry_interval\": 1,\n  \"socks5\": {\n    \"listen\": \"%s\"\n  },\n  \"insecure\": true,\n  \"ca\": \"%s\",\n  \"recv_window_conn\": %s,\n  \"recv_window\": %s\n}", server, obfs, auth, up, dwon, retry, listen, str, rc_conn, rc_w);
        fileConf = new File(dir, "configudp.json");
        if (!printToFile(fileca, "-----BEGIN CERTIFICATE-----\nMIIDizCCAnOgAwIBAgIUGxLl5Ou4dR1h3c9lUcaM5bp4ZBswDQYJKoZIhvcNAQEL\nBQAwVTELMAkGA1UEBhMCQ04xCzAJBgNVBAgMAkdEMQswCQYDVQQHDAJTWjEUMBIG\nA1UECgwLWklWUE4sIEluYy4xFjAUBgNVBAMMDVpJVlBOIFJvb3QgQ0EwHhcNMjMw\nMjExMDkwMjM1WhcNMzMwMjA4MDkwMjM1WjBVMQswCQYDVQQGEwJDTjELMAkGA1UE\nCAwCR0QxCzAJBgNVBAcMAlNaMRQwEgYDVQQKDAtaSVZQTiwgSW5jLjEWMBQGA1UE\nAwwNWklWUE4gUm9vdCBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB\nAMQsHTq2UD4WDOvNUFGQuKd0PEitgQzSh12qH9aJ5jnCtbWjqVNDRQSW0ietg4Po\nqOfKLOBvGOJcGkrYlAAynnwsufdkZd2Jj2+FAXloAbMBK5cjqRANfPJ7ns3S5zL2\nt2+Xv/O6H58NL5QksyIHb2Vcosfelwuvj5Lq+MvyqGZikce5IaykgjjV0OsrBnsC\neK4yAeoxsqVixGwmcJDLGOIJDGYcDdaElqJqFCyOjOhLLDymx9JbeOb3DpiRNFNN\nlwXi2rfvpnmpGNwNt9sclWAQTL3cfV4GsCovT02r1qxcAqqRE4U1nqMRqk0KfyQn\nUebOat/0jNJI9YxJByuVBK0CAwEAAaNTMFEwHQYDVR0OBBYEFGk91bjhFZfcKkpm\n5SxVkqnSGhXBMB8GA1UdIwQYMBaAFGk91bjhFZfcKkpm5SxVkqnSGhXBMA8GA1Ud\nEwEB/wQFMAMBAf8wDQYJKoZIhvcNAQELBQADggEBAEr4aeE0ib5/7neEcRWCE1pg\nw0j/958bdaSdQJJvYEpc7brCHhp5lmNJA+MjVcCXCL4/8KfuEcyGNPPSPo7wbuYJ\nO9jsJmQOklfyvlKGJschvc8AZ0E0AGdrgGam1KApjrb6Xly5bqgV4KPBQ7KttBVw\nwFfTm0yjD3nAjaSXi3I/MG+gMGnUXoTMZa3iS2pomBMHLdTksiujbbH7RP9mzPT3\n7UvyVmtw7eQFEjEYceVWHlhXCjL9gpcJiX/wu9XzREDpNCqY2R3zb+ZGYuQD0L5h\nzv0u1CF+Cfkkg8luxol+aWc+1ac/8TGLV1WOGj4FuEMfxQPXWFqhc8VEyxZ/r/w=\n-----END CERTIFICATE-----") || !printToFile(fileConf, format)) {
            //  interrupt();
            stopVudp2();
            return;
        }
        Log.d("Config", format);
        File file = new File(this.context.getApplicationInfo().nativeLibraryDir, "libfarikudp.so");
        fileDns = file;
        String[] strArr = {file.getAbsolutePath(), "client", "--config", fileConf.getAbsolutePath()};
        UDPSetting.OnResultListener onResultListener = new UDPSetting.OnResultListener() {
            @Override
            public final void onResult(String str2) {
                run2(str2);
            }
        };
        try {
            mStarting = true;
            SkStatus.logInfo("Iniciando UDP");
            Process start = new ProcessBuilder(new String[0]).command(strArr).redirectErrorStream(true).start();
            udpProcess = start;
            out = new UDPSetting(start.getInputStream(), onResultListener);
            err = new UDPSetting(udpProcess.getErrorStream(), onResultListener);
            out.setInterrupted(false);
            out.start();
            err.setInterrupted(false);
            err.start();
            udpProcess.waitFor();
        } catch (Exception e7) {
            //SkStatus.logInfo("Process: "+ e7);
            e7.printStackTrace();
        }
    }

    public void stopVudp2() {
        new Thread(new Runnable() {
            @Override
            public final void run() {
                stopVudp();
            }
        }).start();
    }

    public void reconnectUDP() {
        if (mStarting || mStopping || mReconnecting) {
            return;
        }

        mReconnecting = true;
        stopVudp2();
        SkStatus.updateStateString(SkStatus.SSH_RECONECTANDO, "Reconectando..");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            mReconnecting = false;
            return;
        }

        SkStatus.clearLog();

        for (int i = 0; i < RECONNECT_TRIES; i++) {
            if (mStopping) {
                mReconnecting = false;
                return;
            }

            int sleepTime = 5;
            if (!TunnelUtils.isNetworkOnline(context)) {
                SkStatus.updateStateString(SkStatus.SSH_AGUARDANDO_REDE, "Esperando red ..");

                SkStatus.logInfo(R.string.state_nonetwork);
            } else {
                sleepTime = 3;
                mStarting = true;
                SkStatus.updateStateString(SkStatus.SSH_RECONECTANDO, "Reconectando..");

                SkStatus.logInfo("<strong>" + context.getString(R.string.state_reconnecting) + "</strong>");

                try {

                    if (udpProcess == null){
                        udpListener.onReconnecting();
                        mStarting = false;
                        mReconnecting = false;

                        return;

                    }

                } catch (Exception e) {
                    SkStatus.logInfo("<strong><html><font color=\"red\">" + context.getString(R.string.state_disconnected) + "</font></html></strong>");
                }

                mStarting = false;
            }

            try {
                Thread.sleep(sleepTime * 1000);
                i--;
            } catch (InterruptedException e2) {
                mReconnecting = false;
                return;
            }
        }

        mReconnecting = false;
        stopVudp2();

    }

    // seguridad MRX AES AES CESAR
    public static String Choco(String texto, int desastre) { //codificarCesar
        String resultado = "";
        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);
            if (Character.isLetter(caracter)) {
                if (Character.isUpperCase(caracter)) {
                    resultado += (char) ((caracter + desastre - 65) % 26 + 65);
                } else {
                    resultado += (char) ((caracter + desastre - 97) % 26 + 97);
                }
            } else {
                resultado += caracter;
            }
        }
        return resultado;
    }

    public static String Vainilla(String texto, int desastre) { //decodificarCesar
        return Choco(texto, 26 - desastre);
    }

}