package com.fenix.sshservice.tunnel;

import static com.fenix.sshservice.util.SkProtect.getLib;
import static com.fenix.vpn.FenixApp.getTolito;
import static com.fenix.vpn.activities.BaseActivity.v7;
import static com.fenix.vpn.activities.BaseActivity.v8;

import static ooOoO.oOoOoOO.oOoOOooOO;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.widget.*;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.fenix.sshservice.*;
import com.fenix.sshservice.config.*;
import com.fenix.sshservice.logger.*;
import com.fenix.sshservice.tunnel.vpn.*;
import com.fenix.sshservice.util.AESDecryptor;
import com.fenix.vpn.R;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.ConnectionMonitor;
import com.trilead.ssh2.DebugLogger;
import com.trilead.ssh2.DynamicPortForwarder;
import com.trilead.ssh2.InteractiveCallback;
import com.trilead.ssh2.KnownHosts;
import com.trilead.ssh2.ProxyData;
import java.util.TimerTask;
import com.trilead.ssh2.ServerHostKeyVerifier;
import com.trilead.ssh2.transport.TransportManager;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.*;
import java.util.zip.CRC32;

public class TunnelManagerThread
implements Runnable, ConnectionMonitor, InteractiveCallback,
		ServerHostKeyVerifier, DebugLogger {
	private static final String TAG = TunnelManagerThread.class.getSimpleName();

	private OnStopCliente mListener;
	private Context mContext;
	private Handler mHandler;
	private Settings mConfig;
	private boolean mRunning = false, mStopping = false, mStarting = false;
	private Pinger pinger;
    private ConnectivityManager connectivityManager;
	private ConnectivityManager.NetworkCallback callback;
	private CountDownLatch mTunnelThreadStopSignal;
    private static UDPListener udpListener;
    public static SharedPreferences preferencias;
	//private ConnectivityManager mCmgr;
	private UDPHysteria UDPHysteria = null;
    private boolean udprunning = false;
	private ServerSocket serverSocket;
    private V2Tunnel v2Tunnel;
    private static V2Listener v2Listener;
    private boolean v2rayrunning = false;
    private ArrayList<String> alist = new ArrayList<>();


    public static V2Listener getV2rayServicesListener() {
        return v2Listener;
    }


    public interface OnStopCliente {
		void onStop();
	}

	public TunnelManagerThread(Handler handler, Context context) {
		mContext = context;
		mHandler = handler;
		mConfig = new Settings(context);
        preferencias = mConfig.getPrefsPrivate();
        alist.add(context.getPackageName());
		setUdpRedCallback();

		udpListener = new UDPListener() {
			@Override
			public void onConnecting() {
				SkStatus.updateStateString(SkStatus.SSH_CONECTANDO, "Conectando");
			}
			@Override
			public void onConnected() {
				try {
					SkStatus.updateStateString(SkStatus.SSH_CONECTADO, "SSH Connection Established");
					SkStatus.logInfo("<font color='#1AFF4C'><strong>UDP Connected Successfully</strong></font>");

					mConnected = true;
					startTunnelVpnService();
				} catch (Exception e)  {
					e.printStackTrace();

				}
			}
			@Override
			public void onNetworkLost() {
				UDPHysteria = new UDPHysteria(mContext);
				UDPHysteria.start();

			}
			@Override
			public void onAuthFailed() {
				Intent stopTunnel = new Intent(FenixVpnService.TUNNEL_SSH_STOP_SERVICE);
				LocalBroadcastManager.getInstance(context)
						.sendBroadcast(stopTunnel);
			}
			@Override
			public void onReconnecting() {
				SkStatus.updateStateString(SkStatus.SSH_CONECTANDO, "Conectando");
				if (udprunning) {
					Thread thread = UDPHysteria;
					if (thread != null && thread.isAlive()) {
						UDPHysteria.interrupt();
					}
					udprunning = false;
					//connectivityManager.unregisterNetworkCallback(callback);
				}
				try {
					Thread.sleep(1000);
					if (!udprunning) {
						UDPHysteria = new UDPHysteria(mContext);
						UDPHysteria.start();
						udprunning = true;
					} else {
						UDPHysteria.interrupt();
						stopAll();
						//SkStatus.logInfo("Servicio UDP es NULO");
					}
				} catch (InterruptedException e) {
					//SkStatus.logInfo("No se puedo iniciar Servicio UDP");
					UDPHysteria.interrupt();
					stopAll();
				}
			}
			@Override
			public void onConnectionLost() {
				UDPHysteria = new UDPHysteria(mContext);
				UDPHysteria.start();
			}
			@Override
			public void onError() {
				//stopAll();
			}
			@Override
			public void onDisconnected() {
				if (udprunning) {
					Thread thread = UDPHysteria;
					if (thread != null && thread.isAlive()) {
						UDPHysteria.interrupt();
					}
					udprunning = false;
					//connectivityManager.unregisterNetworkCallback(callback);
				}
			}
		};

        //V2RAY WakkoDEV
        v2Listener = new V2Listener() {
            @Override
            public boolean onProtect(int socket) {
                return false;
            }

            @Override
            public Service getService() {
                return null;
            }

            @Override
            public void startService() {
                SkStatus.updateStateString(SkStatus.SSH_CONECTANDO, "Conectando");
            }

            @Override
            public void stopService() {
            }
            @Override
            public void onConnected() {
                try {
                    SkStatus.updateStateString(SkStatus.SSH_CONECTADO, "SSH Connection Established");
                    SkStatus.logInfo("<font color='#00adbe'><strong>V2ray Conectado</strong></font>");
                    mConnected = false;
                    // NO llamar startTunnelVpnService() aquí!
                    // V2Service (proceso separado) YA creó su propia VPN + hev-socks5-tunnel.
                    // startTunnelVpnService() iniciaría TunnelVpnService (otra VPN)
                    // y Android solo permite 1 VPN → revocaría la de V2Service → no fluye tráfico.
                    if (mConfig.getPremium()) {
                        V2Tunnel.checkUser(mContext);
                    }
                } catch (Exception e)  {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
                V2Tunnel.stopApp();
            }

            @Override
            public void restartService() {
                if (v2rayrunning) {
                    V2Tunnel.StopV2ray(mContext.getApplicationContext());
                    v2Tunnel = null;
                    v2rayrunning = false;
                    connectivityManager.unregisterNetworkCallback(callback);
                    SkStatus.logInfo("Reconectando V2ray");
                }
                try {
                    Thread.sleep(3000);
                    if (v2Tunnel == null) {
                        v2Tunnel = new V2Tunnel(mContext);
                        String jsonV2ray = mConfig.getPrefsPrivate().getString(Settings.V2RAY_CONFIG_KEY, "");
                        V2Tunnel.StartV2ray(mContext.getApplicationContext(), "Default", jsonV2ray, alist);
                        v2rayrunning = true;
                    } else {
                        V2Tunnel.stopAllServices(mContext);
                        SkStatus.logInfo("Servicio V2ray es NULO");
                    }
                } catch (InterruptedException e) {
                    SkStatus.logInfo("No se puedo iniciar Servicio V2ray");
                }

            }
        };
        
        // Receiver para V2RAY_CONNECTED broadcast desde V2Service
        IntentFilter v2rayFilter = new IntentFilter(V2Service.ACTION_V2RAY_CONNECTED);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(v2rayConnectedReceiver, v2rayFilter, Context.RECEIVER_EXPORTED);
        } else {
            context.registerReceiver(v2rayConnectedReceiver, v2rayFilter);
        }
	}

    private final BroadcastReceiver v2rayConnectedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("HevSocks5", "📡 Recibido V2RAY_CONNECTED broadcast!");
            if (v2Listener != null) {
                v2Listener.onConnected();
            }
        }
    };

	public void setOnStopClienteListener(OnStopCliente listener) {
		mListener = listener;
	}

	@Override
	public void run() {
		mStarting = true;
		mTunnelThreadStopSignal = new CountDownLatch(1);

		SkStatus.logInfo("<strong>" + mContext.getString(R.string.starting_service_ssh) + "</strong>");

		int tries = 0;
		while (!mStopping) {
			try {
				if (!TunnelUtils.isNetworkOnline(mContext)) {
					SkStatus.updateStateString(SkStatus.SSH_AGUARDANDO_REDE, mContext.getString(R.string.state_nonetwork));

					SkStatus.logInfo(R.string.state_nonetwork);

					try {
						Thread.sleep(5000);
					} catch (InterruptedException e2) {
						stopAll();
						break;
					}
				} else {
					if (tries > 0)
						SkStatus.logInfo(mContext.getString(R.string.state_reconnecting));

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e2) {
						stopAll();
						break;
					}

					if(isudpmode()){
						udpListener.onConnecting();
						UDPHysteria = new UDPHysteria(mContext);
						UDPHysteria.start();
						udprunning = true;

						break;

					} if(isv2raymode()){
                        if (v2Tunnel == null) {
                            v2Tunnel = new V2Tunnel(mContext);
                            String jsonV2ray = mConfig.getPrefsPrivate().getString(Settings.V2RAY_CONFIG_KEY, "");
                            V2Tunnel.StartV2ray(mContext.getApplicationContext(), "Default", jsonV2ray, alist);
                            v2rayrunning = true;
                        }
                    } else{
						startClienteSSH();
					}
					break;
				}
			} catch (Exception e) {

				SkStatus.logError("<strong>" + mContext.getString(R.string.state_disconnected) + "</strong>");
				closeSSH();

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e2) {
					stopAll();
					break;
				}
			}

			tries++;
		}

		mStarting = false;

		if (!mStopping) {
			try {
				mTunnelThreadStopSignal.await();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		if (mListener != null) {
			mListener.onStop();
		}
	}

	public void stopAll() {
		if (mStopping) return;

		SkStatus.updateStateString(SkStatus.SSH_PARANDO, mContext.getString(R.string.stopping_service_ssh));
		SkStatus.logInfo("<strong>" + mContext.getString(R.string.stopping_service_ssh) + "</strong>");

        new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					mStopping = true;
					if (mTunnelThreadStopSignal != null)
						mTunnelThreadStopSignal.countDown();

					if (isudpmode()) {
						if (udprunning) {
							Thread thread = UDPHysteria;
							if (thread != null && thread.isAlive()) {
								UDPHysteria.interrupt();
							}
							udprunning = false;
							connectivityManager.unregisterNetworkCallback(callback);
						}

						if (mConnected) {
							stopForwarder();
						}
						mRunning = false;
						mStarting = false;
						mReconnecting = false;

					} else if (isv2raymode()) {
                        if (v2rayrunning) {
                            V2Tunnel.StopV2ray(mContext.getApplicationContext());
                            v2Tunnel = null;
                            v2rayrunning= false;
                            connectivityManager.unregisterNetworkCallback(callback);
                        }

                        mRunning = false;
                        mStarting = false;
                        mReconnecting = false;

                    } else {
                        closeSSH();
                        mRunning = false;
                        mStarting = false;
                        mReconnecting = false;
                    }

                    SkStatus.updateStateString(SkStatus.SSH_DESCONECTADO, mContext.getString(R.string.state_disconnected));
                    if (mConfig.network_meter()){
                        mContext.stopService(new Intent(mContext, NetSpeedNotification.class));
                    }
				}
        }, 1000);
	}

	@SuppressLint("SuspiciousIndentation")
    public void stopAllVPN() {
		if (mStopping) return;

        new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				mStopping = true;
				if (mTunnelThreadStopSignal != null)
					mTunnelThreadStopSignal.countDown();

				if (isudpmode()) {
					if (udprunning) {
						Thread thread = UDPHysteria;
						if (thread != null && thread.isAlive()) {
							UDPHysteria.interrupt();
						}
						udprunning = false;
						connectivityManager.unregisterNetworkCallback(callback);
					}

					if (mConnected) {
						stopForwarder();
					}
					mRunning = false;
					mStarting = false;
					mReconnecting = false;

				} else if (isv2raymode()) {
                    if (v2rayrunning) {
                        V2Tunnel.StopV2ray(mContext.getApplicationContext());
                        v2Tunnel = null;
                        v2rayrunning= false;
                        connectivityManager.unregisterNetworkCallback(callback);
                    }

                    mRunning = false;
                    mStarting = false;
                    mReconnecting = false;

                }
				else {
					closeSSH();
					mRunning = false;
					mStarting = false;
					mReconnecting = false;
				}

                SkStatus.updateStateString(SkStatus.SSH_DESCONECTADO, mContext.getString(R.string.state_disconnected));
                if (mConfig.network_meter()){
                    mContext.stopService(new Intent(mContext, NetSpeedNotification.class));
                }
			}
        }, 1000);
	}

	/**
	 * Forwarder
	 */

	protected void startForwarder(int portaLocal) throws Exception {
		if (!mConnected) {
			throw new Exception();
		}

		startForwarderSocks(portaLocal);

		startTunnelVpnService();
        
        if (mConfig.network_meter()){
            mContext.startService(new Intent(mContext, NetSpeedNotification.class));

        }
		
		/* If connected, 1s delay before pinger started */
		
		String PING = mConfig.setPinger();
		
		if (mConfig.setAutoPing()){
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
			
		   if (!PING.equals(""))
			{
			  pinger = new Pinger(mConnection, PING);
			  pinger.start();
			}
   
		 }
		
		new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						if (!mConnected) break;

						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							break;
						}
						
					    if (lastPingLatency > 0) {
							SkStatus.logInfo(String.format("Ping Latencia: <font color='green'>%d ms</font>", lastPingLatency));
							break;
						}
					}
				}
			}).start();
			
	}
	
	/**private synchronized void interruptPinger() {
		if (pinger != null && pinger.isAlive()) {
			SkStatus.logInfo("stopping pinger");

		    pinger.interrupt();
		}
	}*/
	
	protected void stopForwarder() {
		stopTunnelVpnService();
		
		stopForwarderSocks();
	}

	public static void actualizarClaveSiCambio(String urlStr, File fileDestino) {
		new Thread(() -> {
			try {
				URL url = new URL(urlStr);
				HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
				conexion.setRequestMethod("GET");

				if (conexion.getResponseCode() == 200) {
					InputStream inputStream = conexion.getInputStream();
					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
					byte[] data = new byte[4096];
					int n;

					while ((n = inputStream.read(data)) != -1) {
						buffer.write(data, 0, n);
					}

					byte[] nuevaClave = buffer.toByteArray();

					boolean debeGuardar = true;

					if (fileDestino.exists()) {
						byte[] claveActual = null;
						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
							claveActual = Files.readAllBytes(fileDestino.toPath());
						}
						debeGuardar = !Arrays.equals(claveActual, nuevaClave);
					}

					if (debeGuardar) {
						try (FileOutputStream fos = new FileOutputStream(fileDestino)) {
							fos.write(nuevaClave);
							Log.i("MSM", "Archivo descargado y guardado.");
						}
					} else {
						Log.i("MSM", "Archivo no cambió. No se guardó.");
					}

				} else {
					Log.e("MSM", "Error HTTP: " + conexion.getResponseCode());
				}

			} catch (Exception e) {
				Log.e("MSM", "Error al descargar el archivo: " + e.getMessage(), e);
			}
		}).start();
	}


	/**
	 * Cliente SSH
	 */

	private final static int AUTH_TRIES = 1;
	private final static int RECONNECT_TRIES = 5;

	private Connection mConnection;

	private boolean mConnected = false;

	protected void startClienteSSH() throws Exception {
		mStopping = false;
		mRunning = true;

		String servidor = mConfig.getPrivString(Settings.SERVIDOR_KEY);
		int porta = Integer.parseInt(mConfig.getPrivString(Settings.SERVIDOR_PORTA_KEY));
		String usuario = mConfig.getPrivString(Settings.USUARIO_KEY);

		String _senha = mConfig.getPrivString(Settings.SENHA_KEY);
		String senha = _senha.isEmpty() ? PasswordCache.getAuthPassword(null, false) : _senha;

		String keyPath = mConfig.getSSHKeypath();
		int portaLocal = Integer.parseInt(mConfig.getPrivString(Settings.PORTA_LOCAL_KEY));

		try {

			conectar(servidor, porta);

			for (int i = 0; i < AUTH_TRIES; i++) {
				if (mStopping) {
					return;
				}

				try {
					autenticar(usuario, senha);

					break;
				} catch (IOException e) {
					if (i + 1 >= AUTH_TRIES) {
						throw new IOException("Autenticación fallida");
					} else {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e2) {
							return;
						}
					}
				}
			}

			SkStatus.updateStateString(SkStatus.SSH_CONECTADO, "SSH Connection Establecida");
			SkStatus.logInfo("<strong><html><font color='#00adbe'>" + mContext.getString(R.string.state_connected) + "</font></html></strong>");

		 if (mConfig.getSSHPinger() > 0) {
			startPinger(mConfig.getSSHPinger());
			}
			 

			startForwarder(portaLocal);

		} catch (Exception e) {
			mConnected = false;

			throw e;
		}
	}

	public synchronized void closeSSH() {
		stopForwarder();
		stopPinger();

		if (serverSocket != null && !serverSocket.isClosed()) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (mConnection != null) {
			SkStatus.logDebug("Stopping SSH");
			mConnection.close();
		}
	}

	protected void conectar(String servidor, int porta) throws Exception {
		if (!mStarting) {
			throw new Exception();
		}

		SharedPreferences prefs = mConfig.getPrefsPrivate();

		// aqui deve conectar
		try {

			mConnection = new Connection(servidor, porta);

			if (mConfig.getModoDebug() && !prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
				// Desativado, pois estava enchendo o Logger
				//mConnection.enableDebugging(true, this);
				mHandler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(mContext, "Debug mode enabled",
										   Toast.LENGTH_SHORT).show();
						}
					});
			}



			// delay sleep
			if (mConfig.getIsDisabledDelaySSH()) {
				mConnection.setTCPNoDelay(true);
				SkStatus.logWarning("<strong><font color='#2795F5'>DelaySSH Activado</font></strong>");
			}

			//Data Compression
			if (mConfig.ssh_compression()){
				mConnection.setCompression(true);
				SkStatus.logInfo("<strong>SSH Compression Enabled</strong>");
			}

			// proxy
			addProxy(prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false), prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT),
					 (!prefs.getBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true) ? mConfig.getPrivString(Settings.CUSTOM_PAYLOAD_KEY) : null), mConfig.getPrivString(Settings.CUSTOM_SNI),
					 mConnection);
			// monitora a conexão
			mConnection.addConnectionMonitor(this);

			if (Build.VERSION.SDK_INT >= 23) {
				ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
				ProxyInfo proxy = cm.getDefaultProxy();
				if (proxy != null) {
					//SkStatus.logInfo("<strong>Welcome to the world of freenet</strong>");
				}
			}

			SkStatus.updateStateString(SkStatus.SSH_CONECTANDO, mContext.getString(R.string.state_connecting));
			SkStatus.logInfo(R.string.state_connecting);

			mConnection.connect(this, 10 * 1000, 20 * 1000);

			mConnected = true;

		} catch (Exception e) {

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));

			String cause = (e.getCause() != null) ? e.getCause().toString() : e.getMessage();
			if (useProxy && cause != null && cause.contains("Key exchange was not finished")) {
				SkStatus.logError("Proxy: Connection Lost");
			} else {
				SkStatus.logError("SSH: " + (cause != null ? cause : "Unknown error"));
			}

			throw new Exception(e);
		}
	}

	public static void filePublic(Context context){
		File keyFile = new File(context.getFilesDir(), "fnxdev");
		actualizarClaveSiCambio("https://raw.githubusercontent.com/Fenix1998x/Llave-SERVIDORES/refs/heads/main/Llave", keyFile);
	}

	@SuppressLint("SetWorldReadable")
	public static File decryptAndSavePrivateKey(Context context, byte[] encryptedData, String password) throws Exception {
		byte[] decrypted = AESDecryptor.decryptOpenSSL(encryptedData, password);

		File tempKey = File.createTempFile("fnxdev", "tmp", context.getFilesDir());
		try (FileOutputStream fos = new FileOutputStream(tempKey)) {
			fos.write(decrypted);
		}

		tempKey.setReadable(true, false);
		tempKey.setExecutable(false);
		tempKey.setWritable(false);
		tempKey.deleteOnExit();
		return tempKey;
	}


	/**
	 * Autenticação
	 */

	private static final String AUTH_PUBLICKEY = "publickey",
	AUTH_PASSWORD = "password", AUTH_KEYBOARDINTERACTIVE = "keyboard-interactive";

	protected void autenticar(String usuario, String senha) throws IOException {
		if (!mConnected) {
			throw new IOException();
		}

		SkStatus.updateStateString(SkStatus.SSH_AUTENTICANDO, mContext.getString(R.string.state_auth));

        try {
            if (mConnection.isAuthMethodAvailable(usuario, AUTH_PASSWORD)) {
                SkStatus.logInfo("Verificando la contraseña");
                String signedPass = oOoOOooOO(senha, getTolito(mContext, "UDP_OBFS"));

                boolean authOk = false;
                for (int i = 0; i < 3; i++) {
                    if (mConnection.authenticateWithPassword(usuario, signedPass)) {
                        authOk = true;
                        break;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {}
                }
                if (authOk) {
                    SkStatus.logInfo("<strong>" + mContext.getString(R.string.state_auth_success) + "</strong>");
                }
            }

        } catch (IllegalStateException e) {
			Log.e(TAG,
				  "La conexión desapareció mientras intentábamos autenticar",
				  e);
		} catch (Exception e) {
			Log.e(TAG, "Problem during handleAuthentication()", e);
		}

		try {
			if (mConnection.isAuthMethodAvailable(usuario, AUTH_PUBLICKEY)) {
				SkStatus.logInfo("Verificando la contraseña");

				File encryptedFile = new File(mContext.getFilesDir(), "fnxdev");
				byte[] encryptedBytes = null;
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
					encryptedBytes = Files.readAllBytes(encryptedFile.toPath());
				}

				File decryptedKeyFile = decryptAndSavePrivateKey(mContext, encryptedBytes, PASSWORD);

				if (decryptedKeyFile.exists()) {
					if (mConnection.authenticateWithPublicKey(usuario, decryptedKeyFile, PHRASE)) {
						SkStatus.logInfo("<strong>" + mContext.getString(R.string.state_auth_success) + "</strong>");
					}

					decryptedKeyFile.delete();
				} else {
					SkStatus.logInfo("El usuario o la contraseña son incorrectos o caducaron");
				}

			}
		} catch (Exception e) {
			Log.d(TAG, "Host does not support 'Public key' authentication.");
		}

		if (!mConnection.isAuthenticationComplete()) {
			SkStatus.logInfo("<font color='red'>El usuario o la contraseña son incorrectos o caducaron</font>");

			throw new IOException("No fue posible autenticarse con los datos proporcionados");

		}
	}

	// XXX: Is it right?
	@Override
	public String[] replyToChallenge(String name, String instruction,
									 int numPrompts, String[] prompt, boolean[] echo) throws Exception {
		String[] responses = new String[numPrompts];
		for (int i = 0; i < numPrompts; i++) {
			// request response from user for each prompt
			if (prompt[i].toLowerCase().contains("password"))
				responses[i] = mConfig.getPrivString(Settings.SENHA_KEY);
		}
		return responses;
	}


	/**
	 * ServerHostKeyVerifier
	 * Fingerprint
	 */

	@Override
	public boolean verifyServerHostKey(String hostname, int port,
									   String serverHostKeyAlgorithm, byte[] serverHostKey)
	throws Exception {

		String fingerPrint = KnownHosts.createHexFingerprint(
			serverHostKeyAlgorithm, serverHostKey);
		//int fingerPrintStatus = SSHConstants.FINGER_PRINT_CHANGED;

		SkStatus.logInfo("Finger Print: " + fingerPrint);

		//Log.d(TAG, "Finger Print Type: " + "");

		return true;
	}


	/**
	 * Proxy
	 */

	private boolean useProxy = false;

	protected void addProxy(boolean isProteger, int mTunnelType, String mCustomPayload, String mCustomSNI, Connection conn) throws Exception {

		if (mTunnelType != 0) {
			useProxy = true;

			switch (mTunnelType) {
				case Settings.bTUNNEL_TYPE_SSH_DIRECT:
					if (mCustomPayload != null) {
						try {
							ProxyData proxyData = new HttpProxyCustom(mConfig.getPrivString(Settings.SERVIDOR_KEY), Integer.parseInt(mConfig.getPrivString(Settings.SERVIDOR_PORTA_KEY)),
									null, null, mCustomPayload, true, mContext);

							conn.setProxyData(proxyData);

							if (!mCustomPayload.isEmpty() && !isProteger)
								SkStatus.logInfo("Payload: *******");

						} catch(Exception e) {
							throw new Exception(mContext.getString(R.string.error_proxy_invalid));
						}
					}
					else {
						useProxy = false;
					}
					break;

				case Settings.bTUNNEL_TYPE_SSH_PROXY:
					int puertoLocal = 54777;

					try {

						if (mCustomPayload != null && mCustomPayload.isEmpty()) {
							mCustomPayload = null;
						}

						new Thread(
								() -> {
									try {
										serverSocket = new ServerSocket(puertoLocal);
										while (!Thread.interrupted()) {
											Socket input = serverSocket.accept();
											Socket output =
													new HTTPSupport3(input).socket2();

											if (input != null) input.setKeepAlive(true);
											if (output != null) output.setKeepAlive(true);

											new HTTPThread(input, output, true).start();
											new HTTPThread(output, input, false).start();
										}
									} catch (IOException e) {
										// SkStatus.logError("HTTP: " + e.getMessage());
									}
								})
								.start();

						// Redirigimos el HttpProxyCustom hacia nuestro proxy local
						ProxyData proxyData = new HttpProxyCustom("127.0.0.1", puertoLocal,
								null, null, mCustomPayload, false, mContext);

						if (!isProteger)
							conn.setProxyData(proxyData);

					} catch(Exception e) {
						SkStatus.logError(R.string.error_proxy_invalid);
						throw new Exception(mContext.getString(R.string.error_proxy_invalid));
					}
					break;


				case Settings.bTUNNEL_TYPE_SSH_SSL:
					String customSNI = mCustomSNI;
					if (customSNI != null && customSNI.isEmpty()) {
						mCustomPayload = null;
					}

					String sshServer = mConfig.getPrivString(Settings.SERVIDOR_KEY);
					int sshPort = Integer.parseInt(mConfig.getPrivString(Settings.SERVIDOR_PORTA_KEY));

					try {

						ProxyData sslTypeData = new SSLTunnelProxy(sshServer, sshPort, customSNI);
						conn.setProxyData(sslTypeData);

					}catch(Exception e) {
						SkStatus.logInfo(e.getMessage());
					}
					break;

				case Settings.bTUNNEL_TYPE_PAY_SSL:

					String customSNI2 = mCustomSNI;
					if (customSNI2 != null && customSNI2.isEmpty()) {
						customSNI2 = null;
					}
					String customPayload2 = mCustomPayload;


					if (customPayload2 != null && customPayload2.isEmpty()) {
						customPayload2= null;
					}


					String sshServer2 = mConfig.getPrivString(Settings.SERVIDOR_KEY);
					int sshPort2 = Integer.parseInt(mConfig.getPrivString(Settings.SERVIDOR_PORTA_KEY));


					try{


						SSLProxy sslTun = new SSLProxy(sshServer2, sshPort2, customSNI2,customPayload2);
						conn.setProxyData(sslTun);


					} catch(Exception e) {
						SkStatus.logInfo(e.getMessage());
					}

					break;
				case Settings.bTUNNEL_TYPE_SLOWDNS:

					useProxy = false;

					break;

				default: useProxy = false;
			}
		}
	}


	/**
	 * Socks5 Forwarder
	 */

	private DynamicPortForwarder dpf;

	private synchronized void startForwarderSocks(int portaLocal) throws Exception {
		if (!mConnected) {
			throw new Exception();
		}

		//SkStatus.logInfo("starting socks local");
		//SkStatus.logDebug(String.format("socks local listen: %d", portaLocal));
		
		try {

			int nThreads = mConfig.getMaximoThreadsSocks();
			
			

			if (nThreads > 0) {
				dpf = mConnection.createDynamicPortForwarder(portaLocal, nThreads);

				SkStatus.logDebug("socks local number threads: " + Integer.toString(nThreads));
			}
			else {
				dpf = mConnection.createDynamicPortForwarder(portaLocal);
			}

		} catch (Exception e) {
			SkStatus.logError("Socks Local: " + (e.getCause() != null ? e.getCause().toString() : e.getMessage()));

			throw new Exception();
		}
	}
	

	private synchronized void stopForwarderSocks() {
		if (dpf != null) {
			try {
				dpf.close(); 
			} catch (IOException e) {}
			dpf = null;
		}
	}
	
	
	/**
	 * Pinger
	 */

	private Thread thPing;
	private long lastPingLatency = -1;

	private void startPinger(final int timePing) throws Exception {
		if (!mConnected) {
			throw new Exception();
		}
		
		SkStatus.logInfo("starting pinger");

		thPing = new Thread() {
			@Override
			public void run() {
				while (mConnected) {
					try {
						makePinger();
					} catch(InterruptedException e) {
						break;
					}
				}
				SkStatus.logDebug("pinger stopped");
			}

			private synchronized void makePinger() throws InterruptedException {
				try {
					if (mConnection != null) {
						long ping = mConnection.ping();
						if (lastPingLatency < 0) {
							lastPingLatency = ping;
						}
					}
					else throw new InterruptedException();
				} catch(Exception e) {
					Log.e(TAG, "ping error", e);
				}

				if (timePing == 0)
					return;

				if (timePing > 0)
					sleep(timePing*1000);
				else {
					SkStatus.logError("ping invalid");
					throw new InterruptedException();
				}
			}
		};

		// inicia
		thPing.start();
	}
	

	private synchronized void stopPinger() {
		if (thPing != null && thPing.isAlive()) {
			SkStatus.logInfo("stopping pinger");

			thPing.interrupt();
			thPing = null;
		}
	}
	
	/**
	 * Connection Monitor
	 */

	@Override
	public void connectionLost(Throwable reason) {
		if (mStarting || mStopping || mReconnecting) {
			return;
		}

		SkStatus.logError("<strong>" + mContext.getString(R.string.log_conection_lost) + "</strong>");
        
        SkStatus.logError("SSH: " + (reason.getMessage().toString()));

		reconnectSSH();
	}

	public boolean mReconnecting = false;

	public void reconnectSSH() {
		if (mStarting || mStopping || mReconnecting) {
			return;
		}

		mReconnecting = true;

		closeSSH();

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
			if (!TunnelUtils.isNetworkOnline(mContext)) {
				SkStatus.updateStateString(SkStatus.SSH_AGUARDANDO_REDE, "Esperando red ..");

				SkStatus.logInfo(R.string.state_nonetwork);
			} else {
				sleepTime = 3;
				mStarting = true;
				SkStatus.updateStateString(SkStatus.SSH_RECONECTANDO, "Reconectando..");

				SkStatus.logInfo("<strong>" + mContext.getString(R.string.state_reconnecting) + "</strong>");

                try {
                    if (isudpmode()){
                        udpListener.onReconnecting();

                        mStarting = false;
                        mReconnecting = false;

                        return;

                    }
                    else if (isv2raymode()){
                        v2Listener.restartService();

                        mStarting = false;
                        mReconnecting = false;

                        return;

                    } else {

                        startClienteSSH();
                        mStarting = false;
                        mReconnecting = false;
                        return;
                    }
				} catch (Exception e) {
					SkStatus.logInfo("<strong><html><font color=\"red\">" + mContext.getString(R.string.state_disconnected) + "</font></html></strong>");
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

		stopAll();
		
	}

	@Override
	public void onReceiveInfo(int id, String msg) {
		if (id == SERVER_BANNER) {
			SkStatus.logInfo("<strong>" + mContext.getString(R.string.log_server_banner) + msg + "</strong> " + "<br>Propietario: <strong><font color=#0062AF>" + mContext.getString(R.string.app_name) + "</font></strong>");
		}
	}


	/**
	 * Debug Logger
	 */

	@Override
	public void log(int level, String className, String message) {
		SkStatus.logDebug(String.format("%s: %s", className, message));
	}


	/**
	 * Vpn Tunnel
	 */

	String serverAddr;
    
    public static UDPListener getUDPListener() {
		return udpListener;
	}

	protected void startTunnelVpnService() throws IOException {
		if (!mConnected) {
			throw new IOException();
		}

		SkStatus.logInfo("starting tunnel service");
		SharedPreferences prefs = mConfig.getPrefsPrivate();

		// Broadcast
		IntentFilter broadcastFilter =
			new IntentFilter(TunnelVpnService.TUNNEL_VPN_DISCONNECT_BROADCAST);
		broadcastFilter.addAction(TunnelVpnService.TUNNEL_VPN_START_BROADCAST);
		// Inicia Broadcast
		LocalBroadcastManager.getInstance(mContext)
			.registerReceiver(m_vpnTunnelBroadcastReceiver, broadcastFilter);

		String m_socksServerAddress = String.format("127.0.0.1:%s", mConfig.getPrivString(Settings.PORTA_LOCAL_KEY));
		boolean m_dnsForward = mConfig.getVpnDnsForward();
		String m_udpResolver = mConfig.getVpnUdpForward() ? mConfig.getVpnUdpResolver() : null;
		SkStatus.logInfo("<font color=\"#87CEFA\">Udp Resolver:</font> " + mConfig.getVpnUdpResolver());
		String servidorIP = mConfig.getPrivString(Settings.SERVIDOR_KEY);
		try {
			if (prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT) == Settings.bTUNNEL_TYPE_SSH_PROXY) {
				servidorIP = mConfig.getPrivString(Settings.PROXY_IP_KEY);
			} else if (prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT) == Settings.bTUNNEL_TYPE_SSH_DIRECT) {
				servidorIP = mConfig.getPrivString(Settings.SERVIDOR_KEY);
			} else if (prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT) == Settings.bTUNNEL_TYPE_UDP) {
				String setting_ip = mConfig.getPrivString(Settings.UDP_SERVER);
				InetAddress inetAddress = InetAddress.getByName(setting_ip);
				servidorIP = inetAddress.getHostAddress();
			}
		} catch(Exception e) {
			SkStatus.logError(R.string.error_proxy_invalid);
			throw new IOException(mContext.getString(R.string.error_proxy_invalid));
		}

		try {
			InetAddress servidorAddr = TransportManager.createInetAddress(servidorIP);
			serverAddr = servidorIP = servidorAddr.getHostAddress();

		} catch(UnknownHostException e) {
			SkStatus.logInfo("error");
			throw new IOException(mContext.getString(R.string.error_server_ip_invalid));
		}


		String[] m_excludeIps = {servidorIP};

		String[] m_dnsResolvers = null;
		if (m_dnsForward) {
			m_dnsResolvers = new String[]{mConfig.getVpnDnsResolver()};
		} else {
			List<String> lista = VpnUtils.getNetworkDnsServer(mContext);
			m_dnsResolvers = new String[]{lista.get(0)};
		}

		if (isServiceVpnRunning()) {
			Log.d(TAG, "already running service");

			TunnelVpnManager tunnelManager = TunnelState.getTunnelState()
				.getTunnelManager();

			if (tunnelManager != null) {
				tunnelManager.restartTunnel(m_socksServerAddress);
			}

			return;
		}

		Intent startTunnelVpn = new Intent(mContext, TunnelVpnService.class);
		startTunnelVpn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		TunnelVpnSettings settings = new TunnelVpnSettings(m_socksServerAddress, m_dnsForward, m_dnsResolvers,
				(m_dnsForward && m_udpResolver == null || !m_dnsForward && m_udpResolver != null), m_udpResolver, m_excludeIps,
				mConfig.getIsFilterApps(), mConfig.getIsFilterBypassMode(), mConfig.getFilterApps(), mConfig.getIsTetheringSubnet(), mConfig.getBypass());
		startTunnelVpn.putExtra(TunnelVpnManager.VPN_SETTINGS, settings);

		if (mContext.startService(startTunnelVpn) == null) {
			SkStatus.logInfo("failed to start tunnel vpn service");

			throw new IOException("Vpn Service failed to start");
		}

		TunnelState.getTunnelState().setStartingTunnelManager();
	}
    
    public static Inet4Address getIPv4Addresses(InetAddress[] inetAddressArr) {
		for (InetAddress inetAddress : inetAddressArr) {
			if (inetAddress instanceof Inet4Address) {
				return (Inet4Address) inetAddress;
			}
		}
		return null;
	}

	public static boolean isServiceVpnRunning() {
		TunnelState tunnelState = TunnelState.getTunnelState();
		return tunnelState.getStartingTunnelManager() || tunnelState.getTunnelManager() != null;
	}

	protected synchronized void stopTunnelVpnService() {
		if (!isServiceVpnRunning()) {
			return;
		}

		// Use signalStopService to asynchronously stop the service.
		// 1. VpnService doesn't respond to stopService calls
		// 2. The UI will not block while waiting for stopService to return
		// This scheme assumes that the UI will monitor that the service is
		// running while the Activity is not bound to it. This is the state
		// while the tunnel is shutting down.
		SkStatus.logInfo("stopping tunnel service");

		TunnelVpnManager currentTunnelManager = TunnelState.getTunnelState()
			.getTunnelManager();

		if (currentTunnelManager != null) {
			currentTunnelManager.signalStopService();
		}

		/*if (mThreadLocation != null && mThreadLocation.isAlive()) {
		 mThreadLocation.interrupt();
		 }
		 mThreadLocation = null;*/

		// Parando Broadcast
		LocalBroadcastManager.getInstance(mContext)
			.unregisterReceiver(m_vpnTunnelBroadcastReceiver);
	}

	//private Thread mThreadLocation;

	// Local BroadcastReceiver
	private BroadcastReceiver m_vpnTunnelBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public synchronized void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			if (TunnelVpnService.TUNNEL_VPN_START_BROADCAST.equals(action)) {
				boolean startSuccess = intent.getBooleanExtra(TunnelVpnService.TUNNEL_VPN_START_SUCCESS_EXTRA, true);

				if (!startSuccess) {
					stopAll();
				}

			} else if (TunnelVpnService.TUNNEL_VPN_DISCONNECT_BROADCAST.equals(action)) {
				stopAll();
			}
		}
	};
    
    private boolean isudpmode() {
		return preferencias.getInt(Settings.TUNNELTYPE_KEY, 0) == Settings.bTUNNEL_TYPE_UDP;
	}

	public static boolean isv2raymode() {
		return preferencias.getInt(Settings.TUNNELTYPE_KEY, 0) == Settings.bTUNNEL_TYPE_V2RAY;
	}

	private void setUdpRedCallback() {
		connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkRequest request = new NetworkRequest.Builder().build();
		callback = new ConnectivityManager.NetworkCallback() {
			@Override
			public void onAvailable(Network network) {
				//SkStatus.logInfo("<font color='white'><strong>La red esta Activa</strong></font>");

			}
			@Override
			public void onLost(Network network) {
				if (udprunning) {
					//SkStatus.logInfo("<font color='white'>Sin red disponible :( </strong></font>");
					//stopAll();
				}
			}
		};
		connectivityManager.registerNetworkCallback(request, callback);

	}

	private final String PHRASE = (new Object() {
		int MDevz;
		public String toString() {
			byte[] buf = new byte[13];
			MDevz = -1067015094;
			buf[0] = (byte) (MDevz >>> 16);
			MDevz = 424090970;
			buf[1] = (byte) (MDevz >>> 22);
			MDevz = -877195980;
			buf[2] = (byte) (MDevz >>> 15);
			MDevz = 56331684;
			buf[3] = (byte) (MDevz >>> 2);
			MDevz = -1640785516;
			buf[4] = (byte) (MDevz >>> 22);
			MDevz = -1618728052;
			buf[5] = (byte) (MDevz >>> 13);
			MDevz = 240564697;
			buf[6] = (byte) (MDevz >>> 2);
			MDevz = -1345732268;
			buf[7] = (byte) (MDevz >>> 10);
			MDevz = 1695079810;
			buf[8] = (byte) (MDevz >>> 9);
			MDevz = -243245439;
			buf[9] = (byte) (MDevz >>> 1);
			MDevz = 1529328475;
			buf[10] = (byte) (MDevz >>> 19);
			MDevz = -162377542;
			buf[11] = (byte) (MDevz >>> 20);
			MDevz = 941923747;
			buf[12] = (byte) (MDevz >>> 6);
			return new String(buf);
		}
	}.toString());
	private final String PASSWORD = (new Object() {
		int MDevz;
		public String toString() {
			byte[] buf = new byte[38];
			MDevz = -255567281;
			buf[0] = (byte) (MDevz >>> 24);
			MDevz = -12386;
			buf[1] = (byte) (MDevz >>> 7);
			MDevz = -219888;
			buf[2] = (byte) (MDevz >>> 11);
			MDevz = -181;
			buf[3] = (byte) (MDevz >>> 1);
			MDevz = -246372;
			buf[4] = (byte) (MDevz >>> 14);
			MDevz = -1549;
			buf[5] = (byte) (MDevz >>> 4);
			MDevz = -13822;
			buf[6] = (byte) (MDevz >>> 7);
			MDevz = -2977877;
			buf[7] = (byte) (MDevz >>> 15);
			MDevz = -32;
			buf[8] = (byte) (MDevz >>> 1);
			MDevz = -1585846;
			buf[9] = (byte) (MDevz >>> 14);
			MDevz = -56188104;
			buf[10] = (byte) (MDevz >>> 19);
			MDevz = -364;
			buf[11] = (byte) (MDevz >>> 2);
			MDevz = 1844842195;
			buf[12] = (byte) (MDevz >>> 5);
			MDevz = 85506076;
			buf[13] = (byte) (MDevz >>> 7);
			MDevz = 462838055;
			buf[14] = (byte) (MDevz >>> 22);
			MDevz = 993269952;
			buf[15] = (byte) (MDevz >>> 19);
			MDevz = 810964295;
			buf[16] = (byte) (MDevz >>> 12);
			MDevz = 619256709;
			buf[17] = (byte) (MDevz >>> 6);
			MDevz = 533910473;
			buf[18] = (byte) (MDevz >>> 9);
			MDevz = -1161436997;
			buf[19] = (byte) (MDevz >>> 10);
			MDevz = -1916;
			buf[20] = (byte) (MDevz >>> 6);
			MDevz = -1388472524;
			buf[21] = (byte) (MDevz >>> 24);
			MDevz = -14310;
			buf[22] = (byte) (MDevz >>> 7);
			MDevz = -16942110;
			buf[23] = (byte) (MDevz >>> 20);
			MDevz = -73160;
			buf[24] = (byte) (MDevz >>> 10);
			MDevz = -941018433;
			buf[25] = (byte) (MDevz >>> 23);
			MDevz = -238692;
			buf[26] = (byte) (MDevz >>> 13);
			MDevz = -10516;
			buf[27] = (byte) (MDevz >>> 7);
			MDevz = -457577;
			buf[28] = (byte) (MDevz >>> 12);
			MDevz = -134115;
			buf[29] = (byte) (MDevz >>> 13);
			MDevz = -1149;
			buf[30] = (byte) (MDevz >>> 4);
			MDevz = -29481165;
			buf[31] = (byte) (MDevz >>> 18);
			MDevz = -245626344;
			buf[32] = (byte) (MDevz >>> 23);
			MDevz = -10582;
			buf[33] = (byte) (MDevz >>> 7);
			MDevz = -116441142;
			buf[34] = (byte) (MDevz >>> 20);
			MDevz = -271832780;
			buf[35] = (byte) (MDevz >>> 24);
			MDevz = -73034;
			buf[36] = (byte) (MDevz >>> 10);
			MDevz = -898;
			buf[37] = (byte) (MDevz >>> 3);
			return new String(buf);
		}
	}.toString()); //🔥🔥🔥vpnfenix⭐️⭐️⭐️

    public static String ooOOoo(String path) {
        try {
            CRC32 crc = new CRC32();
            FileInputStream fis = new FileInputStream(path);
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                crc.update(buffer, 0, read);
            }
            fis.close();
            return Long.toHexString(crc.getValue());

        } catch (Exception e) {
            return "";
        }
    }

    public static boolean oooOOO(String nativeLibDir) {
        String realCRC = ooOOoo(nativeLibDir + getLib);
        String expectedCRC = "0";
        String abi = Build.SUPPORTED_ABIS[0];
        if (abi.equals(v8)) {
            expectedCRC = oOoOoOo;
        } else if (abi.equals(v7)) {
            expectedCRC = oOoOoO;
        }

        return !realCRC.equalsIgnoreCase(expectedCRC);
    }

    public static final String oOoOoOo = new Object() {
        int wkkdev;
        byte[] s = new byte[]{-108, 84, -101, -65, 84, -66};
        public String toString() {
            byte[] buf = new byte[8];
            wkkdev = 1329374591;
            buf[0] = (byte) ((~((wkkdev ^ -1328777857) >>> 12)) ^ s[0]);
            wkkdev = 1425261994;
            buf[1] = (byte) ((~((wkkdev ^ -1424570966) >>> 15)) ^ s[1]);
            wkkdev = 178057219;
            buf[2] = (byte) ((((wkkdev ^ 177926147) >>> 17) ^ 0xAA) ^ s[2]);
            wkkdev = 2093096333;
            buf[3] = (byte) ((((wkkdev ^ 2111446413) >>> 19) ^ 0xAA) ^ s[3]);
            wkkdev = -850827250;
            buf[4] = (byte) ((((wkkdev ^ -849680370) >>> 13) ^ 0xAA) ^ s[4]);
            wkkdev = -380188803;
            buf[5] = (byte) ((((wkkdev ^ -380516483) >>> 12) ^ 0xAA) ^ s[5]);
            wkkdev = 1892401044;
            buf[6] = (byte) ((((wkkdev ^ 1890828180) >>> 19) ^ 0xAA) ^ s[0]);
            wkkdev = 617954166;
            buf[7] = (byte) ((((wkkdev ^ 617405302) >>> 13) ^ 0xAA) ^ s[1]);
            return new String(buf);
        }
    }.toString();//arm64-v8a
    public static final String oOoOoO = new Object() {
        int wkkdev;
        byte[] s = new byte[]{81, 37, -117, 87};
        public String toString() {
            byte[] buf = new byte[8];
            wkkdev = 30297394;
            buf[0] = (byte) ((((wkkdev ^ 16796978) >>> 16) ^ 0xAA) ^ s[0]);
            wkkdev = 1185839011;
            buf[1] = (byte) ((((wkkdev ^ 1131313059) >>> 19) ^ 0xAA) ^ s[1]);
            wkkdev = 951265949;
            buf[2] = (byte) ((((wkkdev ^ 983771805) >>> 19) ^ 0xAA) ^ s[2]);
            wkkdev = -970802525;
            buf[3] = (byte) ((((wkkdev ^ -970044765) >>> 12) ^ 0xAA) ^ s[3]);
            wkkdev = -737533314;
            buf[4] = (byte) ((((wkkdev ^ -725147010) >>> 16) ^ 0xAA) ^ s[0]);
            wkkdev = 2089802989;
            buf[5] = (byte) ((((wkkdev ^ 2089321709) >>> 11) ^ 0xAA) ^ s[1]);
            wkkdev = -980792254;
            buf[6] = (byte) ((((wkkdev ^ -976401342) >>> 16) ^ 0xAA) ^ s[2]);
            wkkdev = 672758101;
            buf[7] = (byte) ((((wkkdev ^ 673970517) >>> 14) ^ 0xAA) ^ s[3]);
            return new String(buf);
        }
    }.toString();//armeabi-v7a

}
