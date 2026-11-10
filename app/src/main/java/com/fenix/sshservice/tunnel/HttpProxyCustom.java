package com.fenix.sshservice.tunnel;

import com.fenix.vpn.R;
import com.trilead.ssh2.crypto.Base64;
import com.trilead.ssh2.transport.ClientServerHello;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import com.trilead.ssh2.ProxyData;
import com.fenix.sshservice.logger.SkStatus;
import com.trilead.ssh2.transport.TransportManager;

import android.content.Context;
import android.util.Log;

/**
 * By Skank3r
 */
public class HttpProxyCustom
implements ProxyData
{

	private final String proxyHost;
    private final String proxyPass;
    private final int proxyPort;
    private final String proxyUser;
    private final String requestPayload;
	private boolean modoDropbear = false;

	private Socket sock;
	private Context mContext;

	public HttpProxyCustom(String proxyHost, int proxyPort, Context context) {
        this(proxyHost, proxyPort, null, null, context);
    }

    public HttpProxyCustom(String proxyHost, int proxyPort, String proxyUser, String proxyPass, Context context) {
        this(proxyHost, proxyPort, proxyUser, proxyPass, null, false, context);
    }

    public HttpProxyCustom(String proxyHost, int proxyPort, String proxyUser, String proxyPass, String requestPayload, boolean modoDropbear, Context context) {
        if (proxyHost == null) {
            throw new IllegalArgumentException("proxyHost must be non-null");
        } else if (proxyPort < 0) {
            throw new IllegalArgumentException("proxyPort must be non-negative");
        } else {
            this.proxyHost = proxyHost;
            this.proxyPort = proxyPort;
            this.proxyUser = proxyUser;
            this.proxyPass = proxyPass;
            this.requestPayload = requestPayload;
			this.modoDropbear = modoDropbear;
			this.mContext = context;
        }
    }

	@Override
    public Socket openConnection(String hostname, int port, int connectTimeout, int readTimeout) throws IOException {
		sock = new Socket();

		InetAddress addr = TransportManager.createInetAddress(this.proxyHost);
		sock.connect(new InetSocketAddress(addr, this.proxyPort), connectTimeout);
        sock.setSoTimeout(readTimeout);
        try { sock.setKeepAlive(true); } catch (Exception ignored) { }

		SkStatus.logInfo(R.string.state_proxy_running);

		String requestPayload = getRequestPayload(hostname, port);

		// anti vpn sniffer
		if (TunnelUtils.isActiveVpn(mContext)) {
			SkStatus.logInfo("<strong>" + mContext.getString(R.string.error_vpn_sniffer_detected) + "</strong>");

			throw new IOException("error detected");
		}

		SkStatus.logInfo(R.string.state_proxy_inject);

		OutputStream out = sock.getOutputStream();

		// suporte a [split] na payload
		if (!TunnelUtils.injectSplitPayload(requestPayload, out)) {
			try {
				out.write(requestPayload.getBytes("ISO-8859-1"));
			} catch (UnsupportedEncodingException e2) {
				out.write(requestPayload.getBytes());
			}
			out.flush();
		}

		// suporta Dropbear (SSH + PAYLOAD)
		if (modoDropbear) {
			return sock;
		}

        byte[] buffer = new byte[1024];
        InputStream in = sock.getInputStream();

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
						Log.w("HttpProxy", "Could not parse status code, trying alternative");
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

		// Validar respuesta HTTP
		if (!httpReponseFirstLine.startsWith("HTTP/"))
			throw new IOException("The proxy did not send back a valid HTTP response.");

		if (httpStatus == 200 || httpStatus == 101) {
			// 200 OK o 101 Switching Protocols son exitosos
			return sock;
		} else {
			// Para otros códigos, forzamos 200
			SkStatus.logInfo("Proxy: Forzando respuesta 200");
			String forcedResponse = "HTTP/1.0 200 Connection established\r\n\r\n";
			out.write(forcedResponse.getBytes());
			out.flush();
			SkStatus.logInfo(forcedResponse);
			return sock;
		}
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

	@Override
	public void close()
	{
		if (sock == null) return;

		try {
			sock.close();
		} catch (IOException e) { /* failed */ }
	}

}
