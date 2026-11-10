package com.fenix.sshservice;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.fenix.sshservice.aidl.IUltraSSHServiceInternal;
import com.fenix.sshservice.config.Settings;
import com.fenix.sshservice.logger.ConnectionStatus;
import com.fenix.sshservice.logger.SkStatus;
import com.fenix.sshservice.tunnel.DNSTunnelThread;
import com.fenix.sshservice.tunnel.TunnelManagerThread;
import com.fenix.sshservice.tunnel.TunnelUtils;
import com.fenix.sshservice.tunnel.vpn.TunnelVpnService;
import com.fenix.sshservice.util.DummyActivity;
import com.fenix.vpn.FenixMainActivity;
import com.fenix.vpn.R;
import com.google.webview.Configs;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import hex.custom.lib.v2ray.V2rayController;

public class FenixVpnService extends Service
		implements SkStatus.StateListener
{
	private static final String TAG = FenixVpnService.class.getSimpleName();
	public static final String START_SERVICE = "com.fenix.vpn:startTunnel";

	private boolean v2rayrunning = false;
	private static final int PRIORITY_MIN = -2;
	private static final int PRIORITY_DEFAULT = 0;
	private static final int PRIORITY_MAX = 2;

	private Configs config;
	private NotificationManager mNotificationManager;
	public static boolean isRunning = false;
	private Handler mHandler;
	private Settings mPrefs;
	private Thread mTunnelThread;
	private TunnelManagerThread mTunnelManager;
	private ConnectivityManager connMgr;
	private PowerManager.WakeLock wakeLock;
	private Context mContext;



	private final IBinder mBinder = new IUltraSSHServiceInternal.Stub() {

		@Override
		public void stopVPN() {
			FenixVpnService.this.stopTunnel();
		}

	};

	private DNSTunnelThread mDnsThread;
	private ConnectivityManager connectivityManager;
	private ConnectivityManager.NetworkCallback callback;
	SharedPreferences prefs;
	private FenixVpnService mApp;

	@Override
	public void onCreate()
	{
		Log.i(TAG, "onCreate");

		super.onCreate();
		mApp = this;
		wakeLock = ((PowerManager)getSystemService(Context.POWER_SERVICE)).newWakeLock(1, "RTeamUser::WakelockService");

		mPrefs = new Settings(this);
		prefs = mPrefs.getPrefsPrivate();
		mHandler = new Handler();
		setUdpRedCallback();

		connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.i(TAG, "onStartCommand");

		startTunnelBroadcast();

		SkStatus.addStateListener(this);

		if (intent != null && START_SERVICE.equals(intent.getAction()))
			return START_NOT_STICKY;

		String stateMsg = getString(SkStatus.getLocalizedState(SkStatus.getLastState()));
		showNotification(stateMsg, NOTIFICATION_CHANNEL_NEWSTATUS_ID, 0, ConnectionStatus.LEVEL_START, null);

		new Thread(new Runnable() {
			@Override
			public void run() {
				startTunnel();
			}
		}).start();

		//return Service.START_STICKY;
		return Service.START_NOT_STICKY;
	}

	private void setUdpRedCallback() {
		connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkRequest request = new NetworkRequest.Builder().build();
		callback = new ConnectivityManager.NetworkCallback() {
			@Override
			public void onAvailable(Network network) {


			}

			@Override
			public void onLost(Network network) {

			}
		};
		connectivityManager.registerNetworkCallback(request, callback);
	}

	/**
	 * Tunnel
	 */

	public synchronized void startTunnel() {

		SkStatus.updateStateString(SkStatus.SSH_INICIANDO, getString(R.string.starting_service_ssh));

		networkStateChange(this, true);

		SkStatus.logInfo(String.format("Ip Local: %s", getIpPublic()));

		if(mPrefs.getWakelock()){
			wakeLock.acquire();
			wakeLock.release();
			SkStatus.logInfo("CPU Wakelock Activated");

		}
		config = new Configs(this);

////////// simcard//////
		TelephonyManager telephonyManager = ((TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE));
		String simOperatorName = telephonyManager.getSimOperatorName();


		SkStatus.logInfo(String.format("Operador : ➣ " + simOperatorName));
		String nombre;
		try {
			JSONObject object = config.getServersArray().getJSONObject(FenixMainActivity.instance.mainposition());
			nombre = object.getString(getString(R.string.sName));
			try {
				if (isv2raymode()) {
					V2rayController.startV2ray(mApp.getApplicationContext(), nombre, prefs.getString(Settings.V2RAY_CONFIG_KEY, ""), null);
					v2rayrunning = true;
				} else {
					SharedPreferences prefs = mPrefs.getPrefsPrivate();
					int tunnelType = prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);

					if (tunnelType == Settings.bTUNNEL_TYPE_SLOWDNS) {
						mPrefs.setBypass(true);
						mDnsThread = new DNSTunnelThread(this);
						mDnsThread.start();
					}
					mTunnelManager = new TunnelManagerThread(mHandler, this);
					mTunnelManager.setOnStopClienteListener(new TunnelManagerThread.OnStopCliente() {
						@Override
						public void onStop() {
							endTunnelService();
						}
					});

					mTunnelThread = new Thread(mTunnelManager);
					mTunnelThread.start();
				}


			} catch (Exception e) {
				SkStatus.logException(e);
				endTunnelService();
			}
		} catch (JSONException e) {
        }
    }

	public boolean isv2raymode() {
		return prefs.getInt(Settings.TUNNELTYPE_KEY, 0) == Settings.bTUNNEL_TYPE_V2RAY;
	}

	public synchronized void stopTunnel() {

		SharedPreferences prefs = mPrefs.getPrefsPrivate();
		int tunnelType = prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
		if (isv2raymode()) {
			V2rayController.stopV2ray(this);
			v2rayrunning = false;
			connectivityManager.unregisterNetworkCallback(callback);
		} else {
			if (tunnelType == Settings.bTUNNEL_TYPE_SLOWDNS) {
				mPrefs.setBypass(false);
				if (mDnsThread != null) {
					mDnsThread.interrupt();
				}
				mDnsThread = null;
			}
			if (mTunnelManager != null) {
				mTunnelManager.stopAll();

				networkStateChange(this, true);

				if (mTunnelThread != null) {
					mTunnelThread.interrupt();
					TunnelVpnService tunnelVpnService = new TunnelVpnService();
					tunnelVpnService.broadcastVpnDisconnect();
					Intent stopVPN = new Intent(FenixVpnService.TUNNEL_SSH_STOP_SERVICE);
					LocalBroadcastManager.getInstance(this).sendBroadcast(stopVPN);
					V2Core.getInstance().stopCore();
					TunnelManagerThread managerThread = new TunnelManagerThread(mHandler, this);
					managerThread.stopAll();

				}

				mTunnelManager = null;
			}
		}
	}

	protected String getIpPublic() {

		final android.net.NetworkInfo network = connMgr
				.getActiveNetworkInfo();

		if (network != null && network.isConnectedOrConnecting()) {
			return TunnelUtils.getLocalIpAddress();
		}
		else {
			return "Indisponivel";
		}
	}



	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onDestroy()
	{
		Log.i(TAG, "onDestroy");

		super.onDestroy();

		stopTunnel();

		stopTunnelBroadcast();

		SkStatus.removeStateListener(this);
	}

    public static void finishTunnel(Context c){
        Intent stopVPN = new Intent(TUNNEL_SSH_STOP_SERVICE);
        LocalBroadcastManager.getInstance(c).sendBroadcast(stopVPN);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

	@Override
	public void onTaskRemoved(Intent rootIntent){
		Log.d(TAG,"task removed");
		Intent intent = new Intent(this, DummyActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onLowMemory()
	 */
	@Override
	public void onLowMemory() {
		super.onLowMemory();

		SkStatus.logWarning("Low Memory Warning!");
	}

	public void endTunnelService() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				stopForeground(true);

				stopSelf();
				SkStatus.removeStateListener(FenixVpnService.this);
			}
		});
	}


	/**
	 * Notificação
	 */

	public static final String NOTIFICATION_CHANNEL_BG_ID = "openvpn_bg";
	public static final String NOTIFICATION_CHANNEL_NEWSTATUS_ID = "openvpn_newstat";
	public static final String NOTIFICATION_CHANNEL_USERREQ_ID = "openvpn_userreq";

	private Notification.Builder mNotifyBuilder = null;
	private String lastChannel;

	public synchronized void showNotification(final String msg, final String channel,
											  long when, ConnectionStatus status, Intent intent) {
		int icon = getIconByConnectionStatus(status);

		if (mNotifyBuilder == null) {
			//mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			mNotifyBuilder = new Notification.Builder(this)
					.setContentTitle(getString(R.string.app_name))
					.setOnlyAlertOnce(true)
					.setOngoing(true);

			if (!mPrefs.network_meter()){
				// Try to set the priority available since API 16 (Jellybean)
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					addVpnActionsToNotification(mNotifyBuilder);
				}

			}

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
				lpNotificationExtras(mNotifyBuilder, Notification.CATEGORY_SERVICE);
		}

		int priority = PRIORITY_DEFAULT;
		if (channel.equals(NOTIFICATION_CHANNEL_BG_ID))
			priority = PRIORITY_MIN;
		else if (channel.equals(NOTIFICATION_CHANNEL_USERREQ_ID))
			priority = PRIORITY_MAX;

		mNotifyBuilder.setSmallIcon(icon);
		mNotifyBuilder.setContentText(msg);
		mNotifyBuilder.setColor(ContextCompat.getColor(getApplicationContext(), socketclay.compatstyle.R.color.blue4));

		if (status == ConnectionStatus.LEVEL_WAITING_FOR_USER_INPUT) {
			PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_CANCEL_CURRENT);
			mNotifyBuilder.setContentIntent(pIntent);
		}
		else {
			mNotifyBuilder.setContentIntent(getGraphPendingIntent(this));
		}

		if (when != 0)
			mNotifyBuilder.setWhen(when);


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			jbNotificationExtras(priority, mNotifyBuilder);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			//noinspection NewApi
			mNotifyBuilder.setChannelId(channel);
		}

		String tickerText = msg;
		if (tickerText != null && !tickerText.equals(""))
			mNotifyBuilder.setTicker(tickerText);

		Notification notification = mNotifyBuilder.build();

		int notificationId = channel.hashCode();

		startForeground(notificationId, notification);

		mNotificationManager.notify(notificationId, notification);

		if (lastChannel != null && !channel.equals(lastChannel)) {
			// Cancel old notification
			mNotificationManager.cancel(lastChannel.hashCode());
		}

		lastChannel = channel;
		//mNotificationShowing = true;
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void lpNotificationExtras(Notification.Builder nbuilder, String category) {
		nbuilder.setCategory(category);
		nbuilder.setLocalOnly(true);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void jbNotificationExtras(int priority,
									  Notification.Builder nbuilder) {
		try {
			if (priority != 0) {
				Method setpriority = nbuilder.getClass().getMethod("setPriority", int.class);
				setpriority.invoke(nbuilder, priority);

				if (!mPrefs.network_meter()){
					Method setUsesChronometer = nbuilder.getClass().getMethod("setUsesChronometer", boolean.class);
					setUsesChronometer.invoke(nbuilder, true);
				}
			}

			//ignore exception
		} catch (NoSuchMethodException | IllegalArgumentException |
				 InvocationTargetException | IllegalAccessException e) {
			SkStatus.logException(e);
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	private void addVpnActionsToNotification(Notification.Builder nbuilder) {


	}

	private int getIconByConnectionStatus(ConnectionStatus level) {
		switch (level) {
			case LEVEL_CONNECTED:
				return R.drawable.ic_wkk;
			case LEVEL_AUTH_FAILED:
			case LEVEL_NONETWORK:
			case LEVEL_NOTCONNECTED:
			case LEVEL_CONNECTING_NO_SERVER_REPLY_YET:
			case LEVEL_CONNECTING_SERVER_REPLIED:
			case UNKNOWN_LEVEL:
			default:
				return R.drawable.ic_connecting;
		}
	}

	// Usado também pelo tunnel VPN
	public static PendingIntent getGraphPendingIntent(Context context) {
		// Let the configure Button show the Log

		Intent intent = new Intent();
		intent.setComponent(new ComponentName(context, context.getPackageName() + ".FenixMainActivity"));
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent startLW = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

		return startLW;
	}


	/**
	 * SkStatus.StateListener
	 */

	@Override
	public void updateState(String state, String msg, int resid, ConnectionStatus level, Intent intent) {

		// If the process is not running, ignore any state,
		// Notification should be invisible in this state

		if (mTunnelThread == null && !v2rayrunning)
			return;

		String channel = NOTIFICATION_CHANNEL_BG_ID;
		if (level.equals(ConnectionStatus.LEVEL_CONNECTED)) {
			channel = NOTIFICATION_CHANNEL_USERREQ_ID;
		}

		String stateMsg = getString(SkStatus.getLocalizedState(SkStatus.getLastState()));
		showNotification(stateMsg, channel, 0, level, null);
	}



	/**
	 * Tunnel Broadcast
	 */

	private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
		@Override
		public void onAvailable(Network net) {
			SkStatus.logDebug("Red disponivel");
		}

		@Override
		public void onLost(Network net) {
			SkStatus.logDebug("Red perdida");
		}

		@Override
		public void onUnavailable() {
			SkStatus.logDebug("Red indisponivel");
		}
	};

	public static final String TUNNEL_SSH_RESTART_SERVICE = FenixVpnService.class.getName() + "::restartservicebroadcast",
			TUNNEL_SSH_STOP_SERVICE = FenixVpnService.class.getName() + "::stopservicebroadcast";

	private void startTunnelBroadcast() {
		if (Build.VERSION.SDK_INT >= 24) {
			connMgr.registerDefaultNetworkCallback(networkCallback);
		}

		IntentFilter broadcastFilter = new IntentFilter();
		broadcastFilter.addAction(TUNNEL_SSH_STOP_SERVICE);
		broadcastFilter.addAction(TUNNEL_SSH_RESTART_SERVICE);

		LocalBroadcastManager.getInstance(this)
				.registerReceiver(mTunnelSSHBroadcastReceiver, broadcastFilter);
	}

	private void stopTunnelBroadcast() {
		LocalBroadcastManager.getInstance(this)
				.unregisterReceiver(mTunnelSSHBroadcastReceiver);

		if (Build.VERSION.SDK_INT >= 24)
			connMgr.unregisterNetworkCallback(networkCallback);
	}

	private BroadcastReceiver mTunnelSSHBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			if (action == null) {
				return;
			}

			if (action.equals(TUNNEL_SSH_RESTART_SERVICE)) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						if (mTunnelManager != null) {
							mTunnelManager.reconnectSSH();
						}
					}
				}).start();
			}

			else if (action.equals(TUNNEL_SSH_STOP_SERVICE)) {
				endTunnelService();
			}
		}
	};

	private static String lastStateMsg;

	protected void networkStateChange(Context context, boolean showStatusRepetido) {
		String netstatestring;

		try {
			// deprecated in 29
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

			if (networkInfo == null) {
				netstatestring = "not connected";
			} else {
				String subtype = networkInfo.getSubtypeName();
				if (subtype == null)
					subtype = "";
				String extrainfo = networkInfo.getExtraInfo();
				if (extrainfo == null)
					extrainfo = "";

				/*
				 if(networkInfo.getType()==android.net.ConnectivityManager.TYPE_WIFI) {
				 WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				 WifiInfo wifiinfo = wifiMgr.getConnectionInfo();
				 extrainfo+=wifiinfo.getBSSID();

				 subtype += wifiinfo.getNetworkId();
				 }*/


				netstatestring = String.format("%2$s %4$s to %1$s %3$s", networkInfo.getTypeName(),
						networkInfo.getDetailedState(), extrainfo, subtype);
			}

		} catch (Exception e) {
			netstatestring = e.getMessage();
		}

		if (showStatusRepetido || !netstatestring.equals(lastStateMsg))
			SkStatus.logInfo(netstatestring);

		lastStateMsg = netstatestring;
	}
}
