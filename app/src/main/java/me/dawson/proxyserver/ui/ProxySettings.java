package me.dawson.proxyserver.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.fenix.sshservice.tunnel.TunnelUtils;
import com.fenix.vpn.FenixMainActivity;
import com.fenix.vpn.R;
import com.google.android.gms.ads.AdView;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

public class ProxySettings extends Activity implements ServiceConnection,
		OnCheckedChangeListener {




	public static final String TAG = "ProxySettings";

	protected static final String KEY_PREFS = "proxy_pref";
	protected static final String KEY_ENABALE = "proxy_enable";

	private static final int NOTIFICATION_ID = 20140701;

	private IProxyControl proxyControl = null;
	private AdView adsBannerView;
	private TextView tvInfo;
	private CheckBox cbEnable;
	private ToggleButton cbEnable1;
	private TextView ipproxy;
	private ImageView mButtonSet;
	private ImageView sinwifi, conwifi;
	@Override


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_shared);
		mButtonSet = (ImageView) findViewById(R.id.atras);
		mButtonSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent hostshare2 = new Intent(me.dawson.proxyserver.ui.ProxySettings.this, FenixMainActivity.class);
				startActivity(hostshare2);



			}
		});

		conwifi = (ImageView) findViewById(R.id.conwifi);
		sinwifi = (ImageView) findViewById(R.id.sinwifi);
		/*adsBannerView = (AdView) findViewById(R.id.adBannerWifi);

		// carrega anúncio
		if (TunnelUtils.isNetworkOnline(this)) {

			adsBannerView.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					if (adsBannerView != null) {
						adsBannerView.setVisibility(View.VISIBLE);
					}
				}
			});

			adsBannerView.loadAd(new AdRequest.Builder()
					.build());
		}*/

		Button wifiTetherButton = (Button) findViewById(R.id.WiFiTetherButton);
		wifiTetherButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchHotspotSettings();
				//wifisettiengs();
			}
		});

		Button restart = (Button) findViewById(R.id.restart);
		restart.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				restartapp();
				restartwifi();
			}
		});


		tvInfo = (TextView) findViewById(R.id.tv_info);
		TextView ipproxy = (TextView)findViewById ( R.id.ipproxy); // ipproxy
		ipproxy.setText(TunnelUtils.getLocalIpAddress()); //version
		cbEnable1 = (ToggleButton) findViewById(R.id.cb_enable1);
		cbEnable1.setOnCheckedChangeListener(this);

		Intent intent = new Intent(this, ProxyService.class);
		bindService(intent, this, Context.BIND_AUTO_CREATE);
	}

	public void restartapp() {
		Intent intent = new Intent(this, ProxySettings.class);
		int i = 123456;
		PendingIntent pendingIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_IMMUTABLE);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + ((long) 2000), pendingIntent);
		System.runFinalizersOnExit(true);
		System.exit(0);
		//Process.killProcess(Process.myPid());
	}

	private void restartwifi() {
		new AestheticDialog.Builder(this, DialogStyle.EMOJI, DialogType.SUCCESS)
				.setTitle("SocketClay")
				.setMessage("Se restablecio la conexion Wi-Fi..")
				.setDarkMode(true)
				.show();
	}

	private void launchHotspotSettings() {
		Intent tetherSettings = new Intent();
		tetherSettings.setClassName("com.android.settings", "com.android.settings.TetherSettings");
		startActivity(tetherSettings);
	}

	@Override
	public void onServiceConnected(ComponentName cn, IBinder binder) {
		proxyControl = (IProxyControl) binder;
		if (proxyControl != null) {
			updateProxy();
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName cn) {
		proxyControl = null;
	}

	@Override
	protected void onDestroy() {
		unbindService(this);
		super.onDestroy();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		SharedPreferences sp = getSharedPreferences(KEY_PREFS, MODE_PRIVATE);
		sp.edit().putBoolean(KEY_ENABALE, isChecked).apply();
		updateProxy();
	}


	private void updateProxy() {


		if (proxyControl == null) {
			return;
		}

		boolean isRunning = false;
		try {
			isRunning = proxyControl.isRunning();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		boolean shouldRun = getSharedPreferences(KEY_PREFS, MODE_PRIVATE)
				.getBoolean(KEY_ENABALE, false);
		if (shouldRun && !isRunning) {
			startProxy();
		} else if (!shouldRun && isRunning) {
			stopProxy();
		}

		try {
			isRunning = proxyControl.isRunning();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		if (isRunning) {

			tvInfo.setText(R.string.proxy_on);
			cbEnable1.setChecked(true);
			sinwifi.setVisibility(View.GONE);
			conwifi.setVisibility(View.VISIBLE);
		} else {
			tvInfo.setText(R.string.proxy_off);
			cbEnable1.setChecked(false);
			sinwifi.setVisibility(View.VISIBLE);
			conwifi.setVisibility(View.GONE);
		}
	}

	private void startProxy() {
		boolean started = false;
		try {
			started = proxyControl.start();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		if (!started) {
			return;
		}

		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Context context = getApplicationContext();

		Notification notification = new Notification();
		notification.icon = R.drawable.ic_wifi_tethering_white_24dp;
		notification.tickerText = getResources().getString(R.string.proxy_on);
		notification.when = System.currentTimeMillis();

		CharSequence contentTitle = getResources().getString(R.string.app_name);

		CharSequence contentText = getResources().getString(
				R.string.service_text);
		Intent intent = new Intent(this, ProxySettings.class);
		@SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, PendingIntent.FLAG_IMMUTABLE,
				intent, PendingIntent.FLAG_IMMUTABLE);

		notificationManager (contentTitle, contentText, pendingIntent);
		notification.flags |= Notification.FLAG_ONGOING_EVENT;

		manager.notify(NOTIFICATION_ID, notification);

		Toast.makeText(this, getResources().getString(R.string.proxy_started),
				Toast.LENGTH_SHORT).show();
	}

	private void notificationManager(CharSequence contentTitle, CharSequence contentText, PendingIntent pendingIntent) {
	}

	private void stopProxy() {
		boolean stopped = false;

		try {
			stopped = proxyControl.stop();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		if (!stopped) {
			return;
		}

		tvInfo.setText(R.string.proxy_off);
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(NOTIFICATION_ID);
		Toast.makeText(this, getResources().getString(R.string.proxy_stopped),
				Toast.LENGTH_SHORT).show();
	}

 /*  public void btnUpdate(View view)
    {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProxySettings.this);

        builder1.setTitle(Html.fromHtml("<b>Check IP <b>"));
        builder1.setMessage(Html.fromHtml("<b><font color='#cd0030'>NOTA IMPORTANTE ASEGURESE DE HABER HABILITADO SU ZONA WIFI ANTES DE ACTIVAR LA FUNCION:</font></b>   ") + "\n\n" + Html.fromHtml("<b><font color='red'>IP Proxy:</font></b>   ") + TunnelUtils.getLocalIpAddress() + "\n\n" + Html.fromHtml("<b><font color='red'>Puerto:    </font></b>") + "8080");

        builder1.setCancelable(false);
        builder1.setPositiveButton("ok", (dialog, which) -> {
        });
        AlertDialog Alert1 = builder1.create();
        Alert1 .show();
        ((TextView) Objects.requireNonNull(Alert1.findViewById(android.R.id.message))).setMovementMethod(LinkMovementMethod.getInstance());
    }*/
}

