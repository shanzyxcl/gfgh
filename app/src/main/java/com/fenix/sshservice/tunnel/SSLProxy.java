package com.fenix.sshservice.tunnel;

import android.content.Context;

import com.trilead.ssh2.HTTPProxyException;
import com.trilead.ssh2.ProxyData;
import com.trilead.ssh2.crypto.Base64;
import com.trilead.ssh2.sftp.Packet;
import com.trilead.ssh2.transport.ClientServerHello;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.security.SecureRandom;
import java.util.Arrays;

import android.util.Log;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import com.fenix.sshservice.logger.SkStatus;

class SSLProxy implements ProxyData
{
    private Context mContext;

    @Override
    public void close()
    {
        if (mSocket == null) return;

        try {
            mSocket.close();
        } catch (IOException e) { /* failed */ }
    }



    class HandshakeTunnelCompletedListener implements HandshakeCompletedListener {
        private final String val$host;
        private final int val$port;
        private final SSLSocket val$sslSocket;

        HandshakeTunnelCompletedListener( String str, int i, SSLSocket sSLSocket) {
            this.val$host = str;
            this.val$port = i;
            this.val$sslSocket = sSLSocket;


        }


        public void handshakeCompleted(HandshakeCompletedEvent handshakeCompletedEvent) {
            // addLog(new StringBuffer().append("<b>Established ").append(handshakeCompletedEvent.getSession().getProtocol()).append(" connection with ").append(val$host).append(":").append(this.val$port).append(" using ").append(handshakeCompletedEvent.getCipherSuite()).append("</b>").toString());
            //addLog(new StringBuffer().append("<b>Established ").append(handshakeCompletedEvent.getSession().getProtocol()).append(" connection ").append("using ").append(handshakeCompletedEvent.getCipherSuite()).append("</b>").toString());
            //addLog(new StringBuffer().append("Supported cipher suites: ").append(Arrays.toString(this.val$sslSocket.getSupportedCipherSuites())).toString());
            //addLog(new StringBuffer().append("Enabled cipher suites: ").append(Arrays.toString(this.val$sslSocket.getEnabledCipherSuites())).toString());
            SkStatus.logInfo(new StringBuffer().append("SSL: Supported protocols: <br>").append(Arrays.toString(val$sslSocket.getSupportedProtocols())).toString().replace("[", "").replace("]", "").replace(",", "<br>"));
            SkStatus.logInfo(new StringBuffer().append("SSL: Enabled protocols: <br>").append(Arrays.toString(val$sslSocket.getEnabledProtocols())).toString().replace("[", "").replace("]", "").replace(",", "<br>"));
            SkStatus.logInfo("SSL: Using cipher " + handshakeCompletedEvent.getSession().getCipherSuite());
            SkStatus.logInfo("SSL: Using protocol " + handshakeCompletedEvent.getSession().getProtocol());
            SkStatus.logInfo("SSL: Handshake finished");
        }
    }


    private  String proxyHost;
    private  String proxyPass;
    private int proxyPort;
    private  String proxyUser;
    private String requestPayload;




    private String stunnelServer;
    private int stunnelPort = 443;
    private String stunnelHostSNI;;
    private Socket mSocket;



    public SSLProxy(String server, int port, String hostSni, String requestPayload) {
        this.stunnelServer = server;
        this.stunnelPort = port;
        this.stunnelHostSNI = hostSni;
        this.proxyUser = null;
        this.proxyPass = null;
        this.requestPayload = requestPayload;
    }

    @Override
    public Socket openConnection(String hostname, int port, int connectTimeout, int readTimeout) throws IOException
    {


        mSocket = SocketChannel.open().socket();
        mSocket.setKeepAlive(true);
        mSocket.connect(new InetSocketAddress(stunnelServer, stunnelPort));

        if (mSocket.isConnected())
        {
            mSocket = doSSLHandshake(hostname, stunnelHostSNI, port);
            //SkStatus.logInfo(R.string.state_proxy_running);

            String requestPayload = getRequestPayload(hostname, port);

            OutputStream out = mSocket.getOutputStream();
            // suporte a [split] na payload
            if (!TunnelUtils.injectSplitPayload(requestPayload, out)) {
                try {
                    out.write(requestPayload.getBytes("ISO-8859-1"));
                } catch (UnsupportedEncodingException e2) {
                    out.write(requestPayload.getBytes());
                }
                out.flush();
            }

            byte[] buffer = new byte[1024];
            InputStream in = mSocket.getInputStream();

            // lê primeira linha de resposta HTTP
            int httpStatus = 0;
            String httpReponseFirstLine = "";
            try {
                int lineLen = ClientServerHello.readLineRN(in, buffer);
                if (lineLen > 0) {
                    try {
                        httpReponseFirstLine = new String(buffer, 0, lineLen, "ISO-8859-1");
                    } catch (UnsupportedEncodingException e3) {
                        httpReponseFirstLine = new String(buffer, 0, lineLen);
                    }
                    // Extraer código de estado HTTP de forma segura
                    if (httpReponseFirstLine.startsWith("HTTP/") && httpReponseFirstLine.length() >= 12) {
                        try {
                            httpStatus = Integer.parseInt(httpReponseFirstLine.substring(9, 12));
                        } catch (NumberFormatException e) {
                            Log.w("SSLProxy", "Could not parse status code");
                            String[] parts = httpReponseFirstLine.split(" ");
                            if (parts.length >= 2) {
                                try {
                                    httpStatus = Integer.parseInt(parts[1]);
                                } catch (NumberFormatException e2) {
                                    httpStatus = 200;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new IOException("Proxy did not send any response");
            }

            SkStatus.logInfo("<strong>" + httpReponseFirstLine + "</strong>");

            // Leer el resto de headers HTTP
            String httpReponseAll = httpReponseFirstLine;
            while (true) {
                int lineLen = ClientServerHello.readLineRN(in, buffer);
                if (lineLen == 0) break;
                httpReponseAll += "\n";
                try {
                    httpReponseAll += new String(buffer, 0, lineLen, "ISO-8859-1");
                } catch (UnsupportedEncodingException e3) {
                    httpReponseAll += new String(buffer, 0, lineLen);
                }
            }

            if (!httpReponseAll.isEmpty())
                SkStatus.logDebug(httpReponseAll);

            if (httpStatus == 200 || httpStatus == 101) {
                return this.mSocket;
            } else {
                SkStatus.logInfo("Proxy: Forzando respuesta 200");
                String forcedResponse = "HTTP/1.0 200 Connection established\r\n\r\n";
                out.write(forcedResponse.getBytes());
                out.flush();
                SkStatus.logInfo(forcedResponse);
                return this.mSocket;
            }


        }



        return mSocket;
    }


    private String getRequestPayload(String hostname, int port) {
        String payload = this.requestPayload;

        if (payload != null) {
            payload = TunnelUtils.formatCustomPayload(hostname, port, payload);
        }
        else {
            StringBuffer sb = new StringBuffer();

            sb.append("CONNECT ");
            sb.append(hostname);
            sb.append(':');
            sb.append(port);
            sb.append(" HTTP/1.0\r\n");
            if (!(this.proxyUser == null || this.proxyPass == null)) {
                char[] encoded;
                String credentials = this.proxyUser + ":" + this.proxyPass;
                try {
                    encoded = Base64.encode(credentials.getBytes("ISO-8859-1"));
                } catch (UnsupportedEncodingException e) {
                    encoded = Base64.encode(credentials.getBytes());
                }
                sb.append("Proxy-Authorization: Basic ");
                sb.append(encoded);
                sb.append("\r\n");
            }
            sb.append("\r\n");

            payload = sb.toString();
        }

        return payload;
    }




    private Socket doSSLHandshake(Socket socket, String host, String sni, int port) throws IOException {
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };
        try {
            SSLContext sSLContext = SSLContext.getInstance("TLS");
            KeyManager[] keyManagerArr = null;
            sSLContext.init(keyManagerArr, trustAllCerts, new SecureRandom());
            SSLSocket socket3 = (SSLSocket) sSLContext.getSocketFactory().createSocket(socket, host, port, true);
            try {
                socket3.setEnabledProtocols(socket3.getSupportedProtocols());
            } catch (Exception e) {
                SkStatus.logWarning("Could not set enabled protocols: " + e.getMessage());
            }
            if (sSLContext.getSocketFactory() instanceof android.net.SSLCertificateSocketFactory && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                try {
                    ((android.net.SSLCertificateSocketFactory)sSLContext.getSocketFactory()).setHostname(socket, sni);
                } catch (Exception e) {
                    SkStatus.logWarning("SNI setHostname failed: " + e.getMessage());
                }
            } else {
                try {
                    socket.getClass().getMethod("setHostname", String.class).invoke(socket, sni);
                    SkStatus.logInfo("Setting up SNI...");
                } catch (Throwable e) {
                    // ignore any error, we just can't set the hostname...
                }
            }
            socket3.addHandshakeCompletedListener(new HandshakeTunnelCompletedListener(host, port, socket3));
            SkStatus.logInfo("Starting SSL Handshake...");
            socket3.startHandshake();
            return socket3;
        } catch (Exception e) {
            IOException iOException = new IOException(new StringBuffer().append("Could not do SSL handshake: ").append(e).toString());
            throw iOException;
        }
    }


    private SSLSocket doSSLHandshake(String host, String sni, int port) throws IOException {
        try {
            TLSSocketFactory tsf = new TLSSocketFactory();
            SSLSocket socket = (SSLSocket) tsf.createSocket(host, port);
            try {
                socket.getClass().getMethod("setHostname", String.class).invoke(socket, sni);
                SkStatus.logInfo("Setting up SNI...");
            } catch (Throwable e) {
                // ignore any error, we just can't set the hostname...
            }

            try {
                socket.setEnabledProtocols(socket.getSupportedProtocols());
            } catch (Exception e) {
                SkStatus.logWarning("Could not set enabled protocols: " + e.getMessage());
            }
            socket.addHandshakeCompletedListener(new HandshakeTunnelCompletedListener(host, port, socket));
            SkStatus.logInfo("Starting SSL Handshake ws...");
            socket.startHandshake();
            return socket;
        } catch (Exception e) {
            IOException iOException = new IOException(new StringBuffer().append("Could not do SSL handshake: ").append(e).toString());
            throw iOException;
        }
    }

}
