package com.fenix.vpn;

import static com.fenix.sshservice.tunnel.TunnelManagerThread.filePublic;
import static com.fenix.sshservice.tunnel.TunnelManagerThread.oooOOO;
import static com.fenix.sshservice.util.SkProtect.DEX_MARKER;
import static com.fenix.sshservice.util.SkProtect.cache;
import static com.fenix.sshservice.util.SkProtect.dop;
import static com.fenix.sshservice.util.SkProtect.fb;
import static com.fenix.sshservice.util.SkProtect.getDataServers;
import static com.fenix.vpn.FenixApp.TOKEN_PASS;
import static com.fenix.vpn.FenixApp.oOOoOOo;
import static com.google.webview.Configs.MINITO;

import static go.errorProxy.ooOooOo;
import static ooOoO.o0oo0o00.o0oooo00;
import static ooOoO.oOoOoOO.oOoOOOOo;
import static ooOoO.oOoOoOO.oOoOOOo;
import static ooOoO.oOoOoOO.oOoOOOoo;
import static ooOoO.oOoOoOO.oOoOOo;
import static ooOoO.oOoOoOO.oOoOOo0;
import static ooOoO.oOoOoOO.oOoOOoO;
import static ooOoO.oOoOoOO.oOoOoOOOOo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fenix.sshservice.FenixVpnService;
import com.fenix.sshservice.StatisticGraphData;
import com.fenix.sshservice.StatisticGraphData.DataTransferStats;
import com.fenix.sshservice.V2Core;
import com.fenix.sshservice.config.ConfigParser;
import com.fenix.sshservice.config.Settings;
import com.fenix.sshservice.logger.ConnectionStatus;
import com.fenix.sshservice.logger.SkStatus;
import com.fenix.sshservice.tunnel.TunnelManagerHelper;
import com.fenix.sshservice.tunnel.TunnelManagerThread;
import com.fenix.sshservice.tunnel.TunnelUtils;
import com.fenix.sshservice.tunnel.vpn.TunnelVpnService;
import com.fenix.sshservice.util.SkProtect;
import com.fenix.sshservice.util.cdt;
import com.fenix.vpn.activities.AboutActivity;
import com.fenix.vpn.activities.BaseActivity;
import com.fenix.vpn.activities.ConfigGeralActivity;
import com.fenix.vpn.activities.SMSuPdater;
import com.fenix.vpn.adapter.CustomAdapter;
import com.fenix.vpn.adapter.LogsAdapter;
import com.fenix.vpn.adapter.SpinnerAdapter;
import com.fenix.vpn.servers.ServersActivity;
import ooOoO.AESCrypt;
import com.fenix.vpn.util.ConfigUpdate;
import ooOoO.ConfigUtil;
import com.fenix.vpn.util.RetrieveData;
import com.fenix.vpn.util.StoredData;
import com.fenix.vpn.util.Utils;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;

import org.conscrypt.Conscrypt;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.widget.SweetAlertDialog;
import dalvik.system.DexClassLoader;
import me.dawson.proxyserver.ui.ProxySettings;
import ooOoO.ooOOOOoo;

import com.google.webview.Configs;
import com.google.webview.AES;
import com.google.webview.Servers;

/**
 * Activity Principal
 * @author SlipkHunter
 */

public class FenixMainActivity extends BaseActivity
	implements View.OnClickListener, RadioGroup.OnCheckedChangeListener,
CompoundButton.OnCheckedChangeListener, SkStatus.StateListener, PayloadGenerator.OnDismissListener
{
	private static final String TAG = FenixMainActivity.class.getSimpleName();
	private static final String UPDATE_VIEWS = "MainUpdate";
	public static final String OPEN_LOGS = "com.teamvpn.http:openLogs";
	private static boolean regresoDelChat = false;


	private static final String AD_UNIT_ID = "ca-app-pub-8193985851272478/3857616506"; //REDWARD CARO
	private static final String AD_UNIT_ID2 = "ca-app-pub-8193985851272478/4447494665";  // REDWARD MEDIO
	private static final String AD_UNIT_ID3 = "ca-app-pub-8193985851272478/4447494665";  // REDWARD BARATO
	private static final String AD_UNIT_ID4 = "ca-app-pub-8193985851272478/4447494665";  // REDWARD BARATO

	public static int UPDATE_CODE = 22;
	AppUpdateManager appUpdateManager;
	private LinearLayout serverlayout;
	private ImageView serverimage;
	private TextView servername, serverinfo, serverping;
	public static FenixMainActivity instance;
    private LogsAdapter mLogAdapter;
    private RecyclerView logList;
    private ViewPager vp;
    private TabLayout tabs;
	private AlertDialog alertDialog, dialogLoading;
	private ConsentInformation consentInformation;
	private ConsentForm consentForm;

	private DrawerPanelMain mDrawerPanel;
	private Settings mConfig;
	private Toolbar toolbar_main;
	private Handler mHandler;

	private ImageView prendido, apagado, medio;
	private Button starterButton;

	private TextView status;
	private boolean mShown, mShown2;

	private AdView adsBannerView;

	private ConfigUtil configs;
	private Servers loadServerData;
	private Configs config;

	private TextView NombreOperador;

	//private Spinner serverSpinner;

    private static final String[] tabTitle = {"INICIO","REGISTRO"};
	private SpinnerAdapter serverAdapter;
	private SpinnerAdapter payloadAdapter;
	//private FloatingActionButton deleteLogs;

	private ArrayList<JSONObject> serverList;
	private ArrayList<JSONObject> payloadList;

	String[] countryNames={"Custom Payload","Custom SNI"};
    int flags[] = {R.drawable.tweaks, R.drawable.tweaks};

	private CustomAdapter customAdapter;
	private TextView bytes_in, bytes_out;

	///borrar
	private SweetAlertDialog pDialog;
	private LinearLayout mButtonSet, buttonPremium;
    private ImageView Token, modeFree;
	private CountDownTimer mCountDownTimer;
	private boolean mTimerRunning;
	private long google_mix_http;
	private long conectadogif;
	private TextInputEditText inputPwUser;
	private TextInputEditText inputPwPass;
	private long mEndTime, nEndTime;
	private long mTimeLeftBtn;
	private TextView mTextViewCountDown;
	private boolean mTimerEnabled;
	private CountDownTimer mBtnCountDown;
	//private AlertDialog dialog;
	private LinearLayout timer_layout, timer;
	private boolean isLoading, failedRewarded, loadRewarded;
	private RewardedAd rewardedAd;
	private InterstitialAd interstitialAd;
	private boolean adIsLoading;
	private SweetAlertDialog mDialog, mDialog1, mDialog2;

	private AlertDialog alertAds, alert, alert2, alert3, alert4, alert5, alert6;
	final int PERMISSION_REQUEST_CODE =112;
	private SharedPreferences myData;
	private static final String Inters = "ca-app-pub-8193985851272478/7601420395";
	private static final String Banner = "ca-app-pub-8193985851272478/7682107604";
	private TextView mButtonSet1;
	private ImageView relojoff ,relojon, relojanim, modePremium;
	public static final String TODAY_DATA = "todaydata";
	private static final int S_ONSTART_CALLED = 2;
	private static final int S_BIND_CALLED = 1;
	private FrameLayout adContainerView;
	private cdt cdt;
	private CountDownTimer startTime;
	private boolean stimeRunning;
	private boolean loadModeSSL;
	private long inicial, reward;
	private long zonas, simon, uppeli, matrix, exacto, binario, ugly;
	private SharedPreferences prefeTime, preferencias;
	//private String banner, intersticial, recompensado;
	private SharedPreferences.Editor /*editTime,*/ editSave, editServer, editIniciando, editFirm;
    public static final String ooOOOoo = new Object() {
        int wkkdev;
        byte[] s = new byte[]{-32, 81, -77, -84};
        public String toString() {
            byte[] buf = new byte[31];
            wkkdev = 537831618;
            buf[0] = (byte) ((((wkkdev ^ 537192642) >>> 14) ^ 0xAA) ^ s[0]);
            wkkdev = 513197220;
            buf[1] = (byte) ((((wkkdev ^ 439796900) >>> 19) ^ 0xAA) ^ s[1]);
            wkkdev = 1414478577;
            buf[2] = (byte) ((~((wkkdev ^ -1418839311) >>> 16)) ^ s[2]);
            wkkdev = -2035197954;
            buf[3] = (byte) ((((wkkdev ^ -2035156994) >>> 10) ^ 0xAA) ^ s[3]);
            wkkdev = -847854416;
            buf[4] = (byte) ((((wkkdev ^ -870923088) >>> 19) ^ 0xAA) ^ s[0]);
            wkkdev = 2087054057;
            buf[5] = (byte) ((((wkkdev ^ 2023090921) >>> 19) ^ 0xAA) ^ s[1]);
            wkkdev = -77582175;
            buf[6] = (byte) ((~((wkkdev ^ 40906913) >>> 19)) ^ s[2]);
            wkkdev = 1992508267;
            buf[7] = (byte) ((~((wkkdev ^ -1993284757) >>> 12)) ^ s[3]);
            wkkdev = -1759542428;
            buf[8] = (byte) ((~((wkkdev ^ 1759649636) >>> 10)) ^ s[0]);
            wkkdev = 864564952;
            buf[9] = (byte) ((((wkkdev ^ 891303640) >>> 19) ^ 0xAA) ^ s[1]);
            wkkdev = -463970500;
            buf[10] = (byte) ((~((wkkdev ^ 464256828) >>> 12)) ^ s[2]);
            wkkdev = -472992899;
            buf[11] = (byte) ((((wkkdev ^ -472697987) >>> 14) ^ 0xAA) ^ s[3]);
            wkkdev = 854124957;
            buf[12] = (byte) ((((wkkdev ^ 852289949) >>> 16) ^ 0xAA) ^ s[0]);
            wkkdev = -168780798;
            buf[13] = (byte) ((~((wkkdev ^ 166763522) >>> 19)) ^ s[1]);
            wkkdev = 1883029332;
            buf[14] = (byte) ((((wkkdev ^ 1880309588) >>> 15) ^ 0xAA) ^ s[2]);
            wkkdev = 1762991783;
            buf[15] = (byte) ((((wkkdev ^ 1762864807) >>> 12) ^ 0xAA) ^ s[3]);
            wkkdev = 139507278;
            buf[16] = (byte) ((((wkkdev ^ 135837262) >>> 17) ^ 0xAA) ^ s[0]);
            wkkdev = -981360214;
            buf[17] = (byte) ((((wkkdev ^ -995778134) >>> 17) ^ 0xAA) ^ s[1]);
            wkkdev = 485830707;
            buf[18] = (byte) ((((wkkdev ^ 485798963) >>> 10) ^ 0xAA) ^ s[2]);
            wkkdev = 479564660;
            buf[19] = (byte) ((((wkkdev ^ 480047988) >>> 13) ^ 0xAA) ^ s[3]);
            wkkdev = 839079745;
            buf[20] = (byte) ((~((wkkdev ^ -839600319) >>> 12)) ^ s[0]);
            wkkdev = 1566619745;
            buf[21] = (byte) ((((wkkdev ^ 1548007521) >>> 17) ^ 0xAA) ^ s[1]);
            wkkdev = 279431983;
            buf[22] = (byte) ((~((wkkdev ^ -279231697) >>> 10)) ^ s[2]);
            wkkdev = -1211764413;
            buf[23] = (byte) ((((wkkdev ^ -1212055229) >>> 12) ^ 0xAA) ^ s[3]);
            wkkdev = 1267259910;
            buf[24] = (byte) ((((wkkdev ^ 1272633862) >>> 17) ^ 0xAA) ^ s[0]);
            wkkdev = 1308908774;
            buf[25] = (byte) ((~((wkkdev ^ -1318298394) >>> 18)) ^ s[1]);
            wkkdev = 1915291951;
            buf[26] = (byte) ((((wkkdev ^ 1925777711) >>> 17) ^ 0xAA) ^ s[2]);
            wkkdev = -2106317753;
            buf[27] = (byte) ((((wkkdev ^ -2085346233) >>> 18) ^ 0xAA) ^ s[3]);
            wkkdev = 942805282;
            buf[28] = (byte) ((~((wkkdev ^ -1014886110) >>> 19)) ^ s[0]);
            wkkdev = 732496957;
            buf[29] = (byte) ((~((wkkdev ^ -731937731) >>> 14)) ^ s[1]);
            wkkdev = 455719550;
            buf[30] = (byte) ((((wkkdev ^ 455621246) >>> 10) ^ 0xAA) ^ s[2]);
            return new String(buf);
        }
    }.toString();//com,fenix.vpn.FenixAppActivity
    private ImageView backgroundView;
    private ImageView logoView;

    private boolean adWasStarted;
    private static final long MIN_SHOW_MS = 3000;
    private boolean userEarnedReward = false;
    private boolean adWasDismissed = false;
    private boolean rewardGiven = false;
    private long adStartTime = 0L;
    private DexClassLoader dexLoader;
    private Object adsManager;
    private Method setBannerIdMethod;
    private Method loadBannerMethod;
    private Method loadRewardedMethod;
    private Method loadRewardedMethod2;
    private Method showRewardedMethod;
    private Method loadInterstitialMethod;
    private Method showInterstitialMethod;
    private Method setRewardedIdMethod;
    private Method setInterstitialIdMethod;
    private Method hasRewardedMethod;
    private Method isLoadingMethod;
    private Method clearRewardedMethod;



    @SuppressLint({"WrongViewCast", "SuspiciousIndentation"})
	@Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		/*
		new android.app.AlertDialog.Builder(this)
				.setTitle("⚠️ Aviso Importante")
				.setMessage("Bienvenido a FenixVPN. Por favor, recuerda usar la aplicación oficial, si usas una *No Oficial corres el riesgo de ser hackeado* ¡Gracias por cumplir las normas!")
				.setPositiveButton("Aceptar", null)
				.setCancelable(false)
				.show();
				*/
		getConsentStaus();
		instance = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			requestPermissions();
		}

		ConsentRequestParameters params = new ConsentRequestParameters
				.Builder()
				.setTagForUnderAgeOfConsent(false)
				.build();

		Security.insertProviderAt(Conscrypt.newProvider(), 1);

		mHandler = new Handler();
		mConfig = new Settings(this);
		loadServerData = new Servers(this);
		config = new Configs(this);
		configs = new ConfigUtil(this);
		mDrawerPanel = new DrawerPanelMain(this);
		new SMSuPdater(this);
		new TorrentDetection(this, torrentList).init();
		myData = getSharedPreferences(TODAY_DATA, Context.MODE_PRIVATE);
		nEndTime = System.currentTimeMillis();
		loadModeSSL = true;
		preferencias = getSharedPreferences(FenixApp.PREFS_GERAL, Context.MODE_PRIVATE);
		prefeTime = mConfig.getPrefsPrivate();
		editFirm = preferencias.edit();
//		editFirm.putBoolean(SkProtect.firm, true);
//		editFirm.apply();

		/*banner = prefeTime.getString("banner", "");
		intersticial = prefeTime.getString("intersticial", "");
		recompensado = prefeTime.getString("recompensado", "");*/

		MobileAds.initialize(this, initializationStatus -> {});

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER); // portrait only

		SharedPreferences prefs = getSharedPreferences(FenixApp.PREFS_GERAL, Context.MODE_PRIVATE);


		boolean showFirstTime = prefs.getBoolean("connect_first_time", true);
		int lastVersion = prefs.getInt("last_version", 0);

		// se primeira vez
		if (showFirstTime) {
            SharedPreferences.Editor pEdit = prefs.edit();
            pEdit.putBoolean("connect_first_time", false);
            pEdit.apply();

			Settings.setDefaultConfig(this);
        }

		try {
			int idAtual = ConfigParser.getBuildId(this);

			if (lastVersion < idAtual) {
				SharedPreferences.Editor pEdit = prefs.edit();
				pEdit.putInt("last_version", idAtual);
				pEdit.apply();

				// se estiver atualizando
				if (!showFirstTime) {
					if (lastVersion > 1) {
						Settings.setDefaultConfig(this);
						Settings.clearSettings(this);

						//Toast.makeText(this, "Las configuraciones fueron borradas para evitar errores", Toast.LENGTH_LONG).show();
						//new Handler().postDelayed(() -> restart_app(), 1000);
					}
				}

			}
		} catch(IOException e) {}


		// operador
        setAdsManager();
        inAppUp();

		SkProtect.init(this);
		editSave = preferencias.edit();

		TelephonyManager telephonyManager = ((TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE));
		String simOperatorName = telephonyManager.getSimOperatorName();
		String RegionSim = telephonyManager.getSimCountryIso();
		editServer = preferencias.edit();
		///NombreOperador = (TextView) findViewById(R.id.tutextview);
		//NombreOperador.setText(simOperatorName + "(" + RegionSim.toUpperCase() + ")");

		// verifica se existe algum problema
	//	SkProtect.CharlieProtect();

		// recebe local dados
		IntentFilter filter = new IntentFilter();
		filter.addAction(UPDATE_VIEWS);
		filter.addAction(OPEN_LOGS);

        if (!oOOoOOo()) {
            if (!mConfig.getPremium()) {
                oOoOOOOo();
            }
        }

		LocalBroadcastManager.getInstance(this)
			.registerReceiver(mActivityReceiver, filter);

		//Version Config
		TextView version = (TextView)findViewById ( R.id.config_version_info);
		version.setText("" + config.getVersion());

        //status
		status = (TextView) findViewById(R.id.status);

		//IP MRX
		TextView ipmrx = (TextView)findViewById (R.id.ipmrx);
		ipmrx.setText(""+TunnelUtils.getLocalIpAddress());

		mTextViewCountDown = (TextView) findViewById(R.id.tvTimeRemaining);
		mTextViewCountDown.setVisibility(View.VISIBLE);

        timer_layout = (LinearLayout) findViewById(R.id.timerLayout);
		timer = (LinearLayout) findViewById(R.id.timer);
		timer.setVisibility(View.VISIBLE);

		serverlayout = findViewById(R.id.layout_mainservers);
		serverlayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(FenixMainActivity.this, ServersActivity.class));
				showInterstitial();
			}
		});
		serverimage = findViewById(R.id.imagemainlayout);
		servername = findViewById(R.id.nombremainlayout);
		serverinfo = findViewById(R.id.infomainlayout);
        serverping = findViewById(R.id.server_ping);
        serverping.setVisibility(View.GONE);
        if (!isSSL(ooOOOoo)) {
            oOoOOoO();
            throw new RuntimeException("");
        }

        adsBannerView = findViewById(R.id.adBannerMainView);
		timer_layout = findViewById(R.id.timerLayout);
		mButtonSet1 = findViewById(R.id.btnAddTime2);
		relojoff = findViewById(R.id.relojoff);
        Token = findViewById(R.id.token);
		relojanim = findViewById(R.id.relojanim);
        modePremium = findViewById(R.id.mode_premium);
        modeFree = findViewById(R.id.mode_free);
		mButtonSet = findViewById(R.id.btnAddTime);
		mButtonSet.setOnClickListener(v -> {
            if (mConfig.getPremium()){
                token();
            } else {
                mButtonSet1.setText("OK");
                relojanim.setVisibility(View.GONE);
                relojon.setVisibility(View.VISIBLE);
                loadRewarded = true;
                failedRewarded = true;
                try {
                    boolean hasRewarded = (Boolean) hasRewardedMethod.invoke(adsManager);
                    boolean loading = (Boolean) isLoadingMethod.invoke(adsManager);
                    if (hasRewarded) {
                        verAds();
                    } else {
                        if (loading) {
                            loadingAds();
                        } else {
                            loadRewardedAd2();
                            loadingAds();
                        }
                        btnTimer();
                    }
                } catch (Exception e) {
                    if (rewardedAd != null) {
                        verAds();
                    } else {
                        if (FenixMainActivity.this.isLoading) {
                            loadingAds();
                        } else {
                            loadRewardedAd02();
                            loadingAds();
                        }
                        btnTimer();
                    }
                }
            }

        });

        buttonPremium = findViewById(R.id.btnPremium);
        buttonPremium.setOnClickListener(v -> {
            if (mConfig.getPremium()) {
                modeFree();
            } else {
                modeVIP();
            }
        });


        if (mConfig.getPremium()){
            mButtonSet1.setVisibility(View.GONE);
            relojon.setVisibility(View.GONE);
            Token.setVisibility(View.VISIBLE);
            modeFree.setVisibility(View.VISIBLE);
            modePremium.setVisibility(View.GONE);
            mTextViewCountDown.setText("Cargando información...");
            setBanner();
            adsBannerView.setVisibility(View.VISIBLE);

        } else{
            mButtonSet1.setVisibility(View.VISIBLE);
            relojon.setVisibility(View.VISIBLE);
            Token.setVisibility(View.GONE);
            modePremium.setVisibility(View.VISIBLE);
            modeFree.setVisibility(View.GONE);
            adsBannerView.setVisibility(View.GONE);
            loadRewardedAd();
            bannerAdaptable();
            timerTask();
        }

        loadAd();

		welcomeNotif();
		setMainView();
		liveData();
		checkAppSignature();
        updateBackground();

        go.errorProxy.init(getApplicationContext());

	}

    private void setAdsManager (){
        File dexFile = new File(getFilesDir(), cache);
        try {
            boolean copyDex;
            if (!dexFile.exists()) {
                copyDex = true;
            } else {
                String assetHash = sha256(getAssets().open(fb));
                String fileHash = sha256(new FileInputStream(dexFile));
                copyDex = !assetHash.equals(fileHash);
            }
            if (copyDex) {
                if (dexFile.exists()) {
                    dexFile.setWritable(true, false);
                    dexFile.delete();
                }

                InputStream in = getAssets().open(fb);
                byte[] encryptedBytes = getDataServers(in, DEX_MARKER);
                byte[] originalBytes = o0oooo00(encryptedBytes);
                FileOutputStream out = new FileOutputStream(dexFile);

                out.write(originalBytes);

                out.flush();
                out.close();
                in.close();
            }

            dexFile.setWritable(false, false);
            dexFile.setReadOnly();

            File optDir = new File(getFilesDir(), dop);
            if (!optDir.exists()) {
                optDir.mkdirs();
            }

            dexLoader = new DexClassLoader(dexFile.getAbsolutePath(), optDir.getAbsolutePath(), null, getClassLoader());
            Class<?> cls = dexLoader.loadClass("ooOoO.OOooOoOO");
            Constructor<?> constructor = cls.getConstructor(Activity.class, ooOOOOoo.class);

            ooOOOOoo callback = new ooOOOOoo() {
                @Override
                public void onRewardLoaded() {
                    runOnUiThread(() -> {
                        if (loadRewarded) {
                            verAds();
                            alertAds.dismiss();
                        }
                        mButtonSet1.setText("");
                        relojon.setVisibility(View.GONE);
                        relojanim.setVisibility(View.VISIBLE);
                    });
                }

                @Override
                public void onRewardFailed() {
                    runOnUiThread(() -> {
                        if (failedRewarded) {
                            showError();
                            alertAds.dismiss();
                        }
                    });
                }

                @Override
                public void onRewardLoaded2() {
                    verAds();
                    mButtonSet1.setText("");
                    relojon.setVisibility(View.GONE);
                    relojanim.setVisibility(View.VISIBLE);
                    alertAds.dismiss();
                }

                @Override
                public void onRewardFailed2() {
                    showError();
                    alertAds.dismiss();
                }

                @Override
                public void onRewardEarned() {
                    runOnUiThread(() -> {
                        sucess_text();
                    });
                }

                @Override
                public void onRewardClosed() {
                    runOnUiThread(() -> {
                        btnTimer();
                        relojanim.setVisibility(View.GONE);
                        loadRewardedAd();
                    });
                }

                @Override
                public void onInterstitialClosed() {}
            };

            adsManager = constructor.newInstance(this, callback);
            setBannerIdMethod = cls.getMethod("setBannerId", String.class);
            loadBannerMethod = cls.getMethod("loadBanner", ViewGroup.class);
            setRewardedIdMethod = cls.getMethod("setRewardedId", String.class);
            setInterstitialIdMethod = cls.getMethod("setInterstitialId", String.class);
            loadRewardedMethod = cls.getMethod("loadRewardedAd");
            loadRewardedMethod2 = cls.getMethod("loadRewardedAd2");
            showRewardedMethod = cls.getMethod( "showRewardedVideo");
            loadInterstitialMethod = cls.getMethod("loadInterstitial");
            showInterstitialMethod = cls.getMethod("showInterstitial");
            setBannerIdMethod.invoke(adsManager, "banner");
            setRewardedIdMethod.invoke(adsManager, "rewarded");
            setInterstitialIdMethod.invoke(adsManager, "");
            hasRewardedMethod = cls.getMethod("hasRewardedAd");
            isLoadingMethod = cls.getMethod("isRewardedLoading");
            clearRewardedMethod = cls.getMethod("clearRewarded");
        } catch (Exception e) {
            SkStatus.logInfo("Err: " + e);

        }
    }

    private String sha256(InputStream input) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int len;
        while ((len = input.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        byte[] data = baos.toByteArray();
        byte[] marker = DEX_MARKER.getBytes("UTF-8");
        for (int i = 0; i <= data.length - marker.length; i++) {
            boolean found = true;
            for (int j = 0; j < marker.length; j++) {
                if (data[i + j] != marker[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {data = Arrays.copyOfRange(data, i + marker.length, data.length);
                break;
            }
        }
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data);
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private void loadRewardedAd() {
        loadRewarded = false;
        failedRewarded = false;
        try {
            loadRewardedMethod.invoke(adsManager);
        } catch (Exception e) {
            loadRewardedAd0();
        }
    }

    private void loadRewardedAd2() {
        try {
            loadRewardedMethod2.invoke(adsManager);
        } catch (Exception e) {
            loadRewardedAd02();
        }
    }

    private void loadAd() {
        try {
            loadInterstitialMethod.invoke(adsManager);
        } catch (Exception e) {
            loadAd0();
        }
    }

    private void showRewardedVideo() {
        try {
            showRewardedMethod.invoke(adsManager);
        } catch (Exception e) {
            showRewardedVideo0();
        }
    }

    private void showInterstitial() {
        try {
            showInterstitialMethod.invoke(adsManager);
        } catch (Exception e) {
            showInterstitial0();
        }
    }

    private void restartSoft(){
		this.startActivity(this.getIntent());
        this.finish();
        this.overridePendingTransition(0, 0);

    }

    private void modeVIP(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.mode_vip, null);
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setView(inflate);
        TextView mode = inflate.findViewById(R.id.mode_vip);
        mode.setText("Modo VIP");
        ImageView image = inflate.findViewById(R.id.logoVip);
        image.setImageResource(R.drawable.premium);
        TextView text1 = inflate.findViewById(R.id.txtVip1);
        text1.setText("Si tienes una cuenta VIP dale en aceptar o adquierela con https://t.me/FeniiX1 o +573238087004");
        TextView text2 = inflate.findViewById(R.id.txtVip2);
        text2.setText("Podras tener acceso ilimitado sin ver anuncios");
        LinearLayout ok = inflate.findViewById(R.id.ok_vip);
        LinearLayout no = inflate.findViewById(R.id.no_vip);
        final AlertDialog alert = builer.create();
        alert.setCanceledOnTouchOutside(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.show();
        no.setOnClickListener(v -> alert.dismiss());
        ok.setOnClickListener(v -> {
            mConfig.setPremium(true);
            if (FenixApp.oOOoOOo()) {
                TunnelManagerHelper.stopSocketClay(FenixMainActivity.this);
            }
            new Handler().postDelayed(() -> restartSoft(), 1000);
        });
        alert.show();

    }

    private void modeFree(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.mode_vip, null);
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setView(inflate);
        TextView mode = inflate.findViewById(R.id.mode_vip);
        mode.setText("Modo Anuncios");
        ImageView image = inflate.findViewById(R.id.logoVip);
        image.setImageResource(R.drawable.free);
        TextView text1 = inflate.findViewById(R.id.txtVip1);
        text1.setText("Tendras que ver anuncios para usar la aplicación");
        TextView text2 = inflate.findViewById(R.id.txtVip2);
        text2.setText("Contribuyes al optimo funcionamiento de la aplicación");
        LinearLayout ok = inflate.findViewById(R.id.ok_vip);
        LinearLayout no = inflate.findViewById(R.id.no_vip);
        final AlertDialog alert = builer.create();
        alert.setCanceledOnTouchOutside(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.show();
        no.setOnClickListener(v -> alert.dismiss());
        ok.setOnClickListener(v -> {
            mConfig.setPremium(false);
            if (FenixApp.oOOoOOo()) {
                TunnelManagerHelper.stopSocketClay(FenixMainActivity.this);
            }
            new Handler().postDelayed(() -> restartSoft(), 1000);
        });
        alert.show();

    }


    private void timerTask() {
        new Timer().schedule(new TimerTask(){
            @Override
            public void run(){
                runOnUiThread(() -> {
                    oOoOOOo();
                    if (oOoOOOoo() <= 0) {
                        mTextViewCountDown.setText(fenixvpn);
                    } else {
                        mTextViewCountDown.setText(oOoOOo0());
                    }
                });
            }
        }, 0,1000);
    }

    private void updateBackground() {
        backgroundView = findViewById(R.id.backgroundView);
        logoView = findViewById(R.id.logoView);
        updateImages(IMAGE_URL, LOGO_URL);
    }

    private void updateImages(String bgUrl, String logoUrl) {
        File bgFinal = new File(getFilesDir(), "background.jpg");
        File logoFinal = new File(getFilesDir(), "logo.png");

        if (bgFinal.exists()) {
            Bitmap bgBmp = BitmapFactory.decodeFile(bgFinal.getAbsolutePath());
            backgroundView.setImageBitmap(bgBmp);
        }
        if (logoFinal.exists()) {
            Bitmap logoBmp = BitmapFactory.decodeFile(logoFinal.getAbsolutePath());
            logoView.setImageBitmap(logoBmp);
        }

        new Thread(() -> {
            boolean bgNeedUpdate = shouldDownload(bgUrl, bgFinal);
            boolean logoNeedUpdate = shouldDownload(logoUrl, logoFinal);

            if (!bgNeedUpdate && !logoNeedUpdate) return;

            File bgTemp = new File(getFilesDir(), "background_temp.jpg");
            File logoTemp = new File(getFilesDir(), "logo_temp.png");
            final boolean[] bgDone = {false};
            final boolean[] logoDone = {false};

            Runnable checkAndApply = () -> {
                if (bgDone[0] && logoDone[0]) {
                    try {
                        if (bgTemp.exists()) copyFile(bgTemp, bgFinal);
                        if (logoTemp.exists()) copyFile(logoTemp, logoFinal);
                        bgTemp.delete();
                        logoTemp.delete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(() -> {
                        fadeToNewImage(backgroundView, bgFinal);
                        fadeToNewImage(logoView, logoFinal);
                    });
                }
            };

            if (bgNeedUpdate) {
                new Thread(() -> {
                    boolean ok = downloadFile(bgUrl, bgTemp);
                    bgDone[0] = ok;
                    runOnUiThread(checkAndApply);
                }).start();
            } else {
                bgDone[0] = true;
            }

            if (logoNeedUpdate) {
                new Thread(() -> {
                    boolean ok = downloadFile(logoUrl, logoTemp);
                    logoDone[0] = ok;
                    runOnUiThread(checkAndApply);
                }).start();
            } else {
                logoDone[0] = true;
            }
        }).start();
    }

    private boolean shouldDownload(String urlStr, File localFile) {
        if (!localFile.exists()) return true;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.setRequestProperty("User-Agent", "AndroidDownloader/1.0");
            conn.setRequestProperty("Connection", "close");
            conn.setConnectTimeout(4000);
            conn.setReadTimeout(4000);
            conn.connect();
            int remoteLength = conn.getContentLength();
            conn.disconnect();

            if (remoteLength <= 0) return false;
            return localFile.length() != remoteLength;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void fadeToNewImage(ImageView view, File imageFile) {
        Drawable drawable = Drawable.createFromPath(imageFile.getAbsolutePath());
        if (drawable == null) return;

        AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f);
        fadeOut.setDuration(300);
        fadeOut.setFillAfter(true);

        fadeOut.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override public void onAnimationStart(android.view.animation.Animation animation) {}
            @Override public void onAnimationEnd(android.view.animation.Animation animation) {
                view.setImageDrawable(drawable);
                AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
                fadeIn.setDuration(300);
                view.startAnimation(fadeIn);
            }
            @Override public void onAnimationRepeat(android.view.animation.Animation animation) {}
        });

        view.startAnimation(fadeOut);
    }

    private boolean downloadFile(String urlStr, File outputFile) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "AndroidDownloader/1.0");
            conn.setRequestProperty("Connection", "close");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(30000);
            conn.connect();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return false;

            InputStream input = conn.getInputStream();
            FileOutputStream output = new FileOutputStream(outputFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            output.close();
            input.close();
            conn.disconnect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void copyFile(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0)
                out.write(buf, 0, len);
        }
    }

	/***Mensaje de admob**/
	public void getConsentStaus(){
		consentInformation = UserMessagingPlatform.getConsentInformation(this);

		ConsentDebugSettings debugSettings= new ConsentDebugSettings.Builder(this)
				.setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
				.build();

		ConsentRequestParameters parameters= new ConsentRequestParameters.Builder()
				.setConsentDebugSettings(debugSettings)
				.setTagForUnderAgeOfConsent(false)
				.build();

		consentInformation.requestConsentInfoUpdate(this, parameters,
				new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
					@Override
					public void onConsentInfoUpdateSuccess() {


						if(consentInformation.isConsentFormAvailable()){
							loadform();
						}
					}
				},
				new ConsentInformation.OnConsentInfoUpdateFailureListener() {
					@Override
					public void onConsentInfoUpdateFailure( FormError formError) {

					}
				}
		);
	}

	public void loadform(){
		UserMessagingPlatform.loadConsentForm(this, new UserMessagingPlatform.OnConsentFormLoadSuccessListener() {
					@Override
					public void onConsentFormLoadSuccess(ConsentForm consentForm) {
						FenixMainActivity.this.consentForm= consentForm;

						if(consentInformation.getConsentStatus()==ConsentInformation.ConsentStatus.UNKNOWN){
							consentForm.show(FenixMainActivity.this, new ConsentForm.OnConsentFormDismissedListener() {
								@Override
								public void onConsentFormDismissed( FormError formError) {
									loadform();
								}
							});
						}
						if(consentInformation.getConsentStatus()==ConsentInformation.ConsentStatus.REQUIRED){
							consentForm.show(FenixMainActivity.this, new ConsentForm.OnConsentFormDismissedListener() {
								@Override
								public void onConsentFormDismissed( FormError formError) {
									loadform();
								}
							});
						}

					}
				},
				new UserMessagingPlatform.OnConsentFormLoadFailureListener() {
					@Override
					public void onConsentFormLoadFailure(FormError formError) {
						loadform();
					}
				});
	}


	/**fin del Mensaje de admob**/

	@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
	private void requestPermissions() {
		int permissionCheckStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int permissionCheckNotifications = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS);

		List<String> permissionsList = new ArrayList<>();

		if (permissionCheckStorage != PackageManager.PERMISSION_GRANTED) {
			permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
			permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		}

		if (Build.VERSION.SDK_INT > 32 && permissionCheckNotifications != PackageManager.PERMISSION_GRANTED) {
			permissionsList.add(Manifest.permission.POST_NOTIFICATIONS);
		}

		if (!permissionsList.isEmpty()) {
			String[] permissions = permissionsList.toArray(new String[0]);
			ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == PERMISSION_REQUEST_CODE) {
			if (grantResults.length > 0) {
				for (int i = 0; i < grantResults.length; i++) {
					if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
						// Permiso concedido
					} else {
						// Permiso denegado
					}
				}
			}
		}
	}

	private void inAppUp() {

		appUpdateManager = AppUpdateManagerFactory.create(this);
		Task<AppUpdateInfo> task = appUpdateManager.getAppUpdateInfo();
		task.addOnSuccessListener(appUpdateInfo -> {

			if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
					&& appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
			) {

				try {
					appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE,
							FenixMainActivity.this,
							UPDATE_CODE
					);
				} catch (IntentSender.SendIntentException e) {
					e.printStackTrace();
					Log.d("updateerror", "onSuccess: " + e.toString());
				}
			}
		});

        try {
            Class<?> cls = dexLoader.loadClass("ooOoO.OOooOo");
            Method method = cls.getMethod("ooO", Context.class);
            boolean ok = (Boolean) method.invoke(null, this);
            if (ok) {
                doLayout();
            }

        } catch (Exception e) {
            SkStatus.logInfo("cls: " + e.getMessage());
            e.printStackTrace();
        }

		appUpdateManager.registerListener(listener);
	}
	InstallStateUpdatedListener listener = installState -> {
		if (installState.installStatus() == InstallStatus.DOWNLOADED){
			popUp();
		}
	};

	private void popUp() {

		Snackbar snackbar = Snackbar.make(
				findViewById(android.R.id.content),
				"Actualización de la Aplicación lista",
				Snackbar.LENGTH_INDEFINITE
		);

		snackbar.setAction("Recargar App", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				appUpdateManager.completeUpdate();

			}
		});
		snackbar.setActionTextColor(Color.parseColor("#FFFFFF"));
		snackbar.show();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == UPDATE_CODE){
			if (resultCode != RESULT_OK){
			}
		}
	}


	private void Network() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		TextView iplocal = (TextView)findViewById (R.id.iplocal);

		if (mWifi.isConnected())
		{
			iplocal.setText("WIFI");


		} else if (mMobile.isConnected()) {

			iplocal.setText("DATOS");


		} else {
			iplocal.setText("NO HAY CONEXION DE INTERNET");

		}
	}

	////////baner tiempo/////////
	private void verAds(){

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflate = inflater.inflate(R.layout.renovar_acceso, null);
		AlertDialog.Builder builer = new AlertDialog.Builder(this);
		builer.setView(inflate);
		/*AdView adView = inflate.findViewById(R.id.adBannerReward);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);*/
		LinearLayout ok = inflate.findViewById(R.id.hadsButton);
		ImageView no = inflate.findViewById(R.id.hadsButton2);
		//ok.setText("Ver Anuncio");
		final AlertDialog alert = builer.create();
		alert.setCanceledOnTouchOutside(false);
		alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		alert.getWindow().setGravity(Gravity.CENTER);
		alert.show();
		no.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				//showInterstitial();  //aca iva quitado
			}
		});
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				{
					showRewardedVideo();
					alert.dismiss();
					//aca va el de pausar tiempo una vez se vea los anuncios

				}

			}});

		alert.show();

	}



	/** Interstitial Ads **/

	public void loadAd0() {
        if (!mConfig.getPremium()) {
            if (adIsLoading || interstitialAd != null) {
                return;
            }
            adIsLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(this, configs.Intersticial(), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    FenixMainActivity.this.interstitialAd = interstitialAd;
                    adIsLoading = false;
                    interstitialAd.setFullScreenContentCallback(
                            new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    FenixMainActivity.this.interstitialAd = null;
                                    loadAd();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    FenixMainActivity.this.interstitialAd = null;
                                    loadAd();
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                }
                            });
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    FenixMainActivity.this.interstitialAd = null;
                    adIsLoading = false;
                    //loadAd();
                }
            });
        } else {
            adIsLoading = false;
            FenixMainActivity.this.interstitialAd = null;
        }
	}

	public void showInterstitial0() {
		if (interstitialAd != null) {
			interstitialAd.show(this);
		} else {
			loadAd0();
		}
	}
    // end

	private void welcomeNotif(){

		NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder notification = new Notification.Builder(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			notification.setChannelId(this.getPackageName() + ".TEAM");
			createNotification(notificationManager, this.getPackageName() + ".TEAM");
		}

		notification.setContentTitle("HTTP Fenix VPN")
			.setContentText("Conexiones HTTP Fenix VPN Full Calidad")
			.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
			.setDefaults(Notification.DEFAULT_ALL)
			.setPriority(Notification.PRIORITY_HIGH)
			.setShowWhen(true)
			.setSmallIcon(R.drawable.ic_wkk)
			.setColor(ContextCompat.getColor(getApplicationContext(), socketclay.compatstyle.R.color.blue4));
		notificationManager.notify(4130,notification.getNotification());
	}

	private void createNotification(NotificationManager notificationManager, String id)
	{
		NotificationChannel mNotif = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			mNotif = new NotificationChannel(id, "Developer", NotificationManager.IMPORTANCE_HIGH);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			mNotif.setShowBadge(true);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			notificationManager.createNotificationChannel(mNotif);
		}
		// TODO: Implement this method
	}
	
	
	private String[] torrentList = new String[] {
		"com.tdo.showbox",
		"com.nitroxehgfnon.terrarium",
		"com.pklbhfhfhox.translatorspro",
		"com.xunghglei.downloadprovider",
	    "com.migjhhghjnhui.networkcapture",
		"com.minhghjghghui.networkcapture.pro",
		"com.packahjhjhjgesniffer.frtparlak",
		"com.minhhjgghgjhui.wifianalyzer",
		"com.tdo.shohghhgwbox",
		"com.nitroxejghhghgnon.terrarium",
		"co.we.torghhjhgrent",
		"com.spacehghgjhgship.netprotect",
		"com.utorgjhgghrent.client.pro",
		"com.utorjhhgjhhgrent.client",
		"com.xunghhgjghlei.downloadprovider",
		"com.epighgghgjhc.app.iTorrent",
		"hu.bute.daghghjgai.amorg.drtorrent",
		"com.mojhgjhghbilityflow.torrent.prof",
		"com.brhghghgute.torrentolite",
		"com.nghghhghgebula.swift",
		"tv.bghhghgitx.media",
		"com.DroiDhgghjhjownloader",
		"bitking.torrent.downloader",
		"org.transdghhgjroid.lite",
		"com.mobilitjghhgyflow.tvp",
		"com.gabordegjghmko.torrnado",
		"com.frostwijghgre.android",
		"com.vuze.andhgjghroid.remote",
		"com.akingi.togjhghjrrent",
		"com.utorghghghrent.web",
		"com.paoljghghghod.torrentsearch2",
		"com.teeonjhghhsoft.ztorrent",
		"megabytehggh.tdm",
		"com.bittghjghjgjhorrent.client.pro",
		"com.mobiljghjityflow.torrent",
		"com.utohgjgjrrent.client.pro",
		"com.bittogjhjrrent.client",
		"com.AndrjhfgjoidA.DroiDownloader",
		"com.indris.yifgfgfytorrents",
		"com.delphihggffhcoder.flud",
		"com.oidappghfghfgs.bittorrent",
		"dwleee.torfhgfgfrentsearch",
		"com.vufggfhfze.torrent.downloader",
		"megabgfgfghyte.dm",
		"com.fgrouptech.kickasstorrents",
		"com.utorgfghgfhgrent.client",
		"com.jrumghgfgfhmyapps.rootbrowser.classic",
		"hu.tagshfdfhgoft.ttorrent.lite",
		"idm.interfghhfnet.download.manager.plus",
		"idm.interhgfghhnet.download.manager",
		"idm.intehgghfdfhrnet.download.manager.adm.lite",
		"com.adgughdhard.android.contentblocker",
		"com.minhgfghfgui.wifianalayzer",
		"appisniffghdfer.online",
		"com.aconngfhgo.blesniffer",
		"com.packagesniffer.frtparlak",
		"com.guoshhggdi.httpcanary",
		"app.greyshhhghirts.sslcapture",
		"jp.co.taosoftghfghgfware.android.packetcapture",
		"com.minhui.nehfdtwohghfdrkcapture",
		"in.gopalakrghfgdhishnareddy.torrent",
		"com.delphicghfghdoder.flud",
		"hu.tagsoft.hgdfhttorrent.lite",
		"co.wghhe.torrent",
		"com.mobigfhfhlityflow.torrent",
		"com.hghghgh.torrentsave",
		"com.marughgfhdgtigroup.downtor",
		"com.akinhfghgfhgi.torrent",
		"com.gamemalt.strgdfghfgeamtorrentvideos",
		"com.hffhhgghgh.move.films",
		"com.nest.lohghghhgfgix.metorrent.hd.movies.torrent",
		"torrentvillaghhggflite.romreviewer.com",
		"com.easegggggggggggggggggghggglifeapps.torrz",
		"com.saghhhgfmp.money.carinsurance",
		"com.megjghghghghdiaget.android",
		"com.vuzfjgghjge.torrent.downloader",
		"com.dv.ajhghjjgdm",
		"com.bigljhghhybt.android.client",
		"com.foosghjgsi.bitcloud",
		"downloajghfgjd.torrent.magnet",
		"bin.mgjghjhjht.plus.canary",
		"com.tgjjghjermux",
		"app.grehgjghjyshirts.sslcapture",
		"jp.co.taosohgjfjftware.android.packetcapture",
		"com.minjhjkhui.networkcapture",
		"hu.tagsfhgjhoft.ttorrent.lite",
		"co.we.torhfhfghgrent",
	};

	private void btnTimer() {

		mBtnCountDown = new CountDownTimer(15000, 1000){

			@Override
			public void onTick(long millisUntilFinished) {
				mTimeLeftBtn = millisUntilFinished;
				mButtonSet.setEnabled(false);
				updateBtnText();
				relojon.setVisibility(View.VISIBLE);
				relojanim.setVisibility(View.GONE);
			}
			@Override
			public void onFinish() {
				mButtonSet.setEnabled(true);
				mButtonSet1.setText("OK");
				relojon.setVisibility(View.VISIBLE);
                try {
                    boolean hasRewarded = (Boolean) hasRewardedMethod.invoke(adsManager);
                    if (hasRewarded) {
                        relojanim.setVisibility(View.VISIBLE);
                        relojon.setVisibility(View.GONE);
                        mButtonSet1.setText("");
                    }
                } catch (Exception e) {
                    if (rewardedAd != null) {
                        relojanim.setVisibility(View.VISIBLE);
                        relojon.setVisibility(View.GONE);
                        mButtonSet1.setText("");
                    }
                }
			}

		}.start();

	}




	private void updateBtnText() {
		int seconds = (int) (mTimeLeftBtn / 1000) % 60;
		String timeLeftFormatted;
		if (seconds > 0) {
			timeLeftFormatted = String.format(Locale.getDefault(),
					"%02d", seconds);

			mButtonSet1.setText(timeLeftFormatted);

		}
	}


	/*code comienzo*/
	private void loadingAds() {
		String msjContext = "Cargando Recompensa por favor Espere..";
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflate = inflater.inflate(R.layout.anunciorecompensa, null);
		AlertDialog.Builder builer = new AlertDialog.Builder(this);
		builer.setView(inflate);
		TextView mContext = inflate.findViewById(R.id.textContent);
		mContext.setText(msjContext);
		alertAds = builer.create();
		alertAds.setCanceledOnTouchOutside(false);
		alertAds.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		alertAds.getWindow().setGravity(Gravity.CENTER);
        /*loadRewarded = false;
        failedRewarded = false;
        try {
            clearRewardedMethod.invoke(adsManager);

        } catch (Exception ignored) {}*/
		alertAds.show();

	}

	private void loadingAds2() {
		String msjContext = "Cargando Recompensa ( 2 ) Por favor Espere..";
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflate = inflater.inflate(R.layout.anunciorecompensa, null);
		AlertDialog.Builder builer = new AlertDialog.Builder(this);
		builer.setView(inflate);
		TextView mContext = inflate.findViewById(R.id.textContent);
		mContext.setText(msjContext);
		alert2 = builer.create();
		alert2.setCanceledOnTouchOutside(false);
		alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		alert2.getWindow().setGravity(Gravity.CENTER);
		alert2.show();
		//btnTimer();
	}



	private void loadingAds3() {
		String msjContext = "Cargando Recompensa ( 3 ) Por favor Espere..";
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflate = inflater.inflate(R.layout.anunciorecompensa, null);
		AlertDialog.Builder builer = new AlertDialog.Builder(this);
		builer.setView(inflate);
		TextView mContext = inflate.findViewById(R.id.textContent);
		mContext.setText(msjContext);
		alert3 = builer.create();
		alert3.setCanceledOnTouchOutside(false);
		alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		alert3.getWindow().setGravity(Gravity.CENTER);
		alert3.show();
		//btnTimer();
	}

	private void loadingAds4() {
		String msjContext = "Cargando Recompensa ( 4 ) Por favor Espere..";
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflate = inflater.inflate(R.layout.anunciorecompensa, null);
		AlertDialog.Builder builer = new AlertDialog.Builder(this);
		builer.setView(inflate);
		TextView mContext = inflate.findViewById(R.id.textContent);
		mContext.setText(msjContext);
		alert4 = builer.create();
		alert4.setCanceledOnTouchOutside(false);
		alert4.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		alert4.getWindow().setGravity(Gravity.CENTER);
		alert4.show();
		//btnTimer();
	}


	private void loadingAds5() {
		String msjContext = "Forsando Anuncio ( 1 :)  ) Por favor Espere..";
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflate = inflater.inflate(R.layout.anunciorecompensa, null);
		AlertDialog.Builder builer = new AlertDialog.Builder(this);
		builer.setView(inflate);
		TextView mContext = inflate.findViewById(R.id.textContent);
		mContext.setText(msjContext);
		alert5 = builer.create();
		alert5.setCanceledOnTouchOutside(false);
		alert5.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		alert5.getWindow().setGravity(Gravity.CENTER);
		alert5.show();
		//btnTimer();
	}


	private void loadingAds6() {
		String msjContext = "Forsando Anuncio ( 2 :) ) Por favor Espere..";
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflate = inflater.inflate(R.layout.anunciorecompensa, null);
		AlertDialog.Builder builer = new AlertDialog.Builder(this);
		builer.setView(inflate);
		TextView mContext = inflate.findViewById(R.id.textContent);
		mContext.setText(msjContext);
		alert6 = builer.create();
		alert6.setCanceledOnTouchOutside(false);
		alert6.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		alert6.getWindow().setGravity(Gravity.CENTER);
		alert6.show();
		//btnTimer();
	}


	private void showError() {
		// Crear un Dialog personalizado
		final Dialog errorDialog = new Dialog(this);

		// Establecer el diseño personalizado y la transparencia en las esquinas
		errorDialog.setContentView(R.layout.activity_noapps);
		errorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		// Obtener referencias a las vistas del layout dentro del Dialog
		TextView titleTextView = errorDialog.findViewById(R.id.titleTextVieww);
		TextView messageTextView = errorDialog.findViewById(R.id.messageTextView);
		Button exitButton = errorDialog.findViewById(R.id.exitButton);
		Button retryButton = errorDialog.findViewById(R.id.retryButton);

		// Establecer el título y el mensaje
		titleTextView.setText("Error al Cargar el Anuncio");
		messageTextView.setText("¡Error al cargar el anuncio, verifique su conexión de Internet!\n\nNota: si este error continúa, comuníquese con el desarrollador para obtener más ayuda.");

		// Manejar el clic del botón de salida (exitButton)
		exitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Cerrar el Dialog
				errorDialog.dismiss();
			}
		});

		// Manejar el clic del botón de reintentar (retryButton)
		retryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Llamar a la función para reintentar (por ejemplo, timehours())
				loadRewardedAd2(); // Asegúrate de implementar esta función según tus necesidades
				loadingAds();
				// Cerrar el Dialog
				errorDialog.dismiss();
			}
		});

		// Mostrar el Dialog personalizado
		errorDialog.show();
	}



	private void loadRewardedAd0() {
		//Toast.makeText(FenixMainActivity.this, "Rewarded 1", Toast.LENGTH_SHORT).show();
		loadRewarded = false;
		failedRewarded = false;
		if (rewardedAd == null) {
			isLoading = true;
			AdRequest adRequest = new AdRequest.Builder().build();
			RewardedAd.load(this, configs.Recompensado(), adRequest, new RewardedAdLoadCallback() {
						@Override
						public void onAdFailedToLoad(LoadAdError loadAdError) {
							rewardedAd = null;
							FenixMainActivity.this.isLoading = false;
							if (failedRewarded) {
								showError();
							}
							alertAds.dismiss();
							//Toast.makeText(FenixMainActivity.this, "Error 1", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onAdLoaded(RewardedAd rewardedAd) {
							FenixMainActivity.this.rewardedAd = rewardedAd;
							FenixMainActivity.this.isLoading = false;
							if (loadRewarded){
								verAds();
							}
							mButtonSet1.setText("");
							relojon.setVisibility(View.GONE);
							relojanim.setVisibility(View.VISIBLE);
							alertAds.dismiss();
							//Toast.makeText(FenixMainActivity.this, "Load 1", Toast.LENGTH_SHORT).show();
						}
					});
		}
	}




	private void loadRewardedAd02() {
		//Toast.makeText(FenixMainActivity.this, "Rewarded 2", Toast.LENGTH_SHORT).show();
		if (rewardedAd == null) {
			isLoading = true;
			AdRequest adRequest = new AdRequest.Builder().build();
			RewardedAd.load(this, configs.Recompensado(), adRequest, new RewardedAdLoadCallback() {
						@Override
						public void onAdFailedToLoad(LoadAdError loadAdError) {
							//Toast.makeText(FenixMainActivity.this, "Error 2", Toast.LENGTH_SHORT).show();
							rewardedAd = null;
							FenixMainActivity.this.isLoading = false;
							showError();
							alertAds.dismiss();
						}

						@Override
						public void onAdLoaded(RewardedAd rewardedAd) {
							//Toast.makeText(FenixMainActivity.this, "Load 2", Toast.LENGTH_SHORT).show();
							FenixMainActivity.this.rewardedAd = rewardedAd;
							FenixMainActivity.this.isLoading = false;
							verAds();
							mButtonSet1.setText("");
							relojon.setVisibility(View.GONE);
							relojanim.setVisibility(View.VISIBLE);
							alertAds.dismiss();
							//showRewardedVideo();
						}
					});
		}
	}
/*
	private void loadRewardedAd3() {
		if (rewardedAd == null) {
			isLoading = true;
			AdRequest adRequest = new AdRequest.Builder().build();
			RewardedAd.load(
					this,
					AD_UNIT_ID3,
					adRequest,
					new RewardedAdLoadCallback() {
						@Override
						public void onAdFailedToLoad(LoadAdError loadAdError) {
							rewardedAd = null;
							//showError(););
							//alert.dismiss();
							loadRewardedAd4();

						}

						@Override
						public void onAdLoaded(RewardedAd rewardedAd) {
							FenixMainActivity.this.rewardedAd = rewardedAd;
							FenixMainActivity.this.isLoading = false;
							mButtonSet1.setText("");
							relojon.setVisibility(View.GONE);
							relojanim.setVisibility(View.VISIBLE);
							alertAds.dismiss();
							//showRewardedVideo();
						}
					});
		}
	}

	private void loadRewardedAd4() {
		if (rewardedAd == null) {
			isLoading = true;
			AdRequest adRequest = new AdRequest.Builder().build();
			RewardedAd.load(
					this,
					AD_UNIT_ID4,
					adRequest,
					new RewardedAdLoadCallback() {
						@Override
						public void onAdFailedToLoad(LoadAdError loadAdError) {
							rewardedAd = null;
							showError();
							//loadRewardedAd5();
							//alert4.dismiss();
						}

						@Override
						public void onAdLoaded(RewardedAd rewardedAd) {
							FenixMainActivity.this.rewardedAd = rewardedAd;
							FenixMainActivity.this.isLoading = false;
							mButtonSet1.setText("");
							relojon.setVisibility(View.GONE);
							relojanim.setVisibility(View.VISIBLE);
							alertAds.dismiss();
							//showRewardedVideo();
						}
					});
		}
	}


	private void loadRewardedAd5() {
		if (rewardedAd == null) {
			isLoading = true;
			AdRequest adRequest = new AdRequest.Builder().build();
			RewardedAd.load(
					this,
					AD_UNIT_ID4,
					adRequest,
					new RewardedAdLoadCallback() {
						@Override
						public void onAdFailedToLoad(LoadAdError loadAdError) {
							rewardedAd = null;
							// showError();
							loadRewardedAd6();
							//alert5.dismiss();
						}

						@Override
						public void onAdLoaded(RewardedAd rewardedAd) {
							FenixMainActivity.this.rewardedAd = rewardedAd;
							FenixMainActivity.this.isLoading = false;
							mButtonSet1.setText("");
							relojon.setVisibility(View.GONE);
							relojanim.setVisibility(View.VISIBLE);
							alertAds.dismiss();
							//showRewardedVideo();
						}
					});
		}
	}


	private void loadRewardedAd6() {
		if (rewardedAd == null) {
			isLoading = true;
			AdRequest adRequest = new AdRequest.Builder().build();
			RewardedAd.load(
					this,
					AD_UNIT_ID4,
					adRequest,
					new RewardedAdLoadCallback() {
						@Override
						public void onAdFailedToLoad(LoadAdError loadAdError) {
							rewardedAd = null;
							showError();
							alertAds.dismiss();
						}

						@Override
						public void onAdLoaded(RewardedAd rewardedAd) {
							FenixMainActivity.this.rewardedAd = rewardedAd;
							FenixMainActivity.this.isLoading = false;
							mButtonSet1.setText("");
							relojon.setVisibility(View.GONE);
							relojanim.setVisibility(View.VISIBLE);
							alertAds.dismiss();
							//showRewardedVideo();
						}
					});
		}
	}

	private void loadRewardedAdStart() {
		if (rewardedAd == null) {
			isLoading = true;
			AdRequest adRequest = new AdRequest.Builder().build();
			RewardedAd.load(
					this,
					AD_UNIT_ID4,
					adRequest,
					new RewardedAdLoadCallback() {
						@Override
						public void onAdFailedToLoad(LoadAdError loadAdError) {
							rewardedAd = null;

						}

						@Override
						public void onAdLoaded(RewardedAd rewardedAd) {
							FenixMainActivity.this.rewardedAd = rewardedAd;
							FenixMainActivity.this.isLoading = false;
							mButtonSet1.setText("");
							relojon.setVisibility(View.GONE);
							relojanim.setVisibility(View.VISIBLE);
							alertAds.dismiss();
							//showRewardedVideo();
						}
					});
		}
	}*/

    private synchronized void resetAdFlags() {
        adWasStarted = false;
        userEarnedReward = false;
        adWasDismissed = false;
        rewardGiven = false;
        adStartTime = 0L;
    }

    private synchronized void attemptGrant() {
        if (rewardGiven) return;
        if (adWasStarted && userEarnedReward && adWasDismissed) {
            long shown = System.currentTimeMillis() - adStartTime;
            if (shown < MIN_SHOW_MS) {
                return;
            }
            rewardGiven = true;
            sucess_text();
        }
    }

	private void showRewardedVideo0() {
        if (rewardedAd == null) {
            return;
        }

        resetAdFlags();

        rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        adWasStarted = true;
                        adStartTime = System.currentTimeMillis();

                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        rewardedAd = null;
                        resetAdFlags();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        //// Toast.makeText(SocksHttpMainActivity.this,"⚡ Gracias por apoyar a HTTP Elver VPN ⚡", Toast.LENGTH_LONG).show();
                        rewardedAd = null;
                        adWasDismissed = true;
                        btnTimer();
                        attemptGrant();
                        relojanim.setVisibility(View.GONE);
                        loadRewardedAd0();
                    }
                });

        Activity activityContext = FenixMainActivity.this;

        rewardedAd.show(activityContext, rewardItem -> {
            userEarnedReward = true;
            attemptGrant();
        });
    }


	private void sucess_text() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflate = inflater.inflate(R.layout.succes, null);
		AlertDialog.Builder builer = new AlertDialog.Builder(this);
		builer.setView(inflate);
		ImageView iv = inflate.findViewById(R.id.succes_Image);
		TextView title = inflate.findViewById(R.id.success_text1);
		TextView ms = inflate.findViewById(R.id.success_text2);
		Button ok = inflate.findViewById(R.id.success_ok);
		iv.setImageResource(R.drawable.ic_timert);
		title.setText("¡Felicidades!");
		ms.setText("Has añadido 2 horas con exito");
		ok.setText("Guardar");
		final AlertDialog alert = builer.create();
		alert.setCanceledOnTouchOutside(false);
		alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		alert.getWindow().setGravity(Gravity.CENTER);
		alert.show();
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					alert.dismiss();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		alert.show();
	}

	/*code fin */


	/*code comienzo de code new*/
	/*
	private void loadingAds() {
		mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
		mDialog.getProgressHelper().setBarColor(Color.parseColor("#0062AF"));
		mDialog.setTitleText("Anuncio de Recompensa");
		mDialog.setContentText("Por favor espere mientras se carga... \n\nNota: \nNecesitas terminar el video para reclamar tu recompensa de tiempo");
		mDialog.setCancelable(true);
		mDialog.show();
	}

	private void loadingAds2() {
		mDialog1 = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
		mDialog1.getProgressHelper().setBarColor(Color.parseColor("#0062AF"));
		mDialog1.setTitleText("Anuncio de Recompensa Supletorio");
		mDialog1.setContentText("Por favor espere mientras se carga... \n\nNota: \nNecesitas terminar el video para reclamar tu recompensa de tiempo");
		mDialog1.setCancelable(true);
		mDialog1.show();
	}

	private void loadingAds3() {
		mDialog2 = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
		mDialog2.getProgressHelper().setBarColor(Color.parseColor("#0062AF"));
		mDialog2.setTitleText("Anuncio de Recompensa Supletorio");
		mDialog2.setContentText("Por favor espere mientras se carga... \n\nNota: \nNecesitas terminar el video para reclamar tu recompensa de tiempo");
		mDialog2.setCancelable(true);
		mDialog2.show();
	}

	private void showError() {
		new SweetAlertDialog(FenixMainActivity.this, SweetAlertDialog.ERROR_TYPE)
				.setTitleText("Error al Cargar el Anuncio")
				.setContentText("¡Error al cargar el anuncio, verifique su conexión de Internet!  \n\nNota: si este error continúa, comuníquese con el desarrollador para obtener más ayuda.")
				.show();
	}*/

/*	private void loadRewardedAd() {
		if (rewardedAd == null) {
			isLoading = true;
			AdRequest adRequest = new AdRequest.Builder().build();
			RewardedAd.load(
					this,
					AD_UNIT_ID,
					adRequest,
					new RewardedAdLoadCallback() {
						@Override
						public void onAdFailedToLoad(LoadAdError loadAdError) {
							rewardedAd = null;
							loadingAds2();
							loadRewardedAd1();
							mDialog.dismiss();
						}

						@Override
						public void onAdLoaded(RewardedAd rewardedAd) {
							FenixMainActivity.this.rewardedAd = rewardedAd;
							FenixMainActivity.this.isLoading = false;
							mDialog.dismiss();
							showRewardedVideo();
						}


					});
		}
	}*/

	/*private void loadRewardedAd1() {
		if (rewardedAd == null) {
			isLoading = true;
			AdRequest adRequest = new AdRequest.Builder().build();
			RewardedAd.load(
					this,
					AD_UNIT_ID2,
					adRequest,
					new RewardedAdLoadCallback() {
						@Override
						public void onAdFailedToLoad(LoadAdError loadAdError) {
							rewardedAd = null;
							//showError();
							loadingAds3();
							loadRewardedAd2();
							mDialog1.dismiss();
						}

						@Override
						public void onAdLoaded(RewardedAd rewardedAd) {
							FenixMainActivity.this.rewardedAd = rewardedAd;
							FenixMainActivity.this.isLoading = false;
							mDialog1.dismiss();
							showRewardedVideo();
						}
					});
		}
	}*/

/*	private void loadRewardedAd2() {
		if (rewardedAd == null) {
			isLoading = true;
			AdRequest adRequest = new AdRequest.Builder().build();
			RewardedAd.load(
					this,
					AD_UNIT_ID3,
					adRequest,
					new RewardedAdLoadCallback() {
						@Override
						public void onAdFailedToLoad(LoadAdError loadAdError) {
							rewardedAd = null;
							showError();
							mDialog2.dismiss();
						}

						@Override
						public void onAdLoaded(RewardedAd rewardedAd) {
							FenixMainActivity.this.rewardedAd = rewardedAd;
							FenixMainActivity.this.isLoading = false;
							mDialog2.dismiss();
							showRewardedVideo();
						}
					});
		}
	}
	*/
	/*code fin */


	/*private void showRewardedVideo() {
		if (rewardedAd == null) {
			return;
		}
		rewardedAd.setFullScreenContentCallback(
				new FullScreenContentCallback() {
					@Override
					public void onAdShowedFullScreenContent() {


					}

					@Override
					public void onAdFailedToShowFullScreenContent(AdError adError) {
						rewardedAd = null;

					}

					@Override
					public void onAdDismissedFullScreenContent() {
						Toast.makeText(FenixMainActivity.this, "⚡ Gracias por apoyar a HTTP Tunnel VPN ⚡", Toast.LENGTH_LONG).show();
						rewardedAd = null;
						btnTimer();
					}
				});

		Activity activityContext = FenixMainActivity.this;

		rewardedAd.show(
				activityContext,
				new OnUserEarnedRewardListener() {
					@Override
					public void onUserEarnedReward(RewardItem rewardItem) {
						long ads_time = 2 * rewardItem.getAmount() * 3600 * 1000;
						addTime(ads_time);
						Toast.makeText(FenixMainActivity.this, "2 horas añadidas con éxito!!", Toast.LENGTH_LONG).show();
					}

				});
	}
*/

	private AdSize getAdSize() {
		// Determine the screen width (less decorations) to use for the ad width.
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);

		float density = outMetrics.density;

		float adWidthPixels = adContainerView.getWidth();

		// If the ad hasn't been laid out, default to the full screen width.
		if (adWidthPixels == 0) {
			adWidthPixels = outMetrics.widthPixels;
		}

		int adWidth = (int) (adWidthPixels / density);
		return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);

	}

	/**
	 * Layout
	 */

    private void setBanner(){
        // set ADS

        if (TunnelUtils.isNetworkOnline(FenixMainActivity.this)) {
            adsBannerView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    if (adsBannerView != null) {
                        adsBannerView.setVisibility(View.VISIBLE);
                    }
                }
            });
            adsBannerView.loadAd(new AdRequest.Builder().build());
        }
    }

    private void bannerAdaptable() {
        adContainerView = findViewById(R.id.ad_view_container);
        try {
            loadBannerMethod.invoke(adsManager, adContainerView);
        } catch (Exception e) {
            bannerAdaptable0();
        }
    }

    private void bannerAdaptable0() {
        //BANNER ADAPTABLE WAKKODEV
        adContainerView = findViewById(R.id.ad_view_container);
        AdView adView = new AdView(this);
        adContainerView.addView(adView);
        adView.setAdUnitId(configs.Banner());

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        Bundle extras = new Bundle();
        extras.putString("collapsible", "top");

        AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build();
        adView.loadAd(adRequest);
    }
	
	private void doLayout() {
		setContentView(R.layout.activity_main_drawer);

		toolbar_main = (Toolbar) findViewById(R.id.toolbar_main);
		
		mDrawerPanel.setDrawer(toolbar_main);
		setSupportActionBar(toolbar_main);


		starterButton = (Button) findViewById(R.id.ConnectSocket);
		apagado = findViewById(R.id.btnapagado);
		prendido = findViewById(R.id.btnprendido);
		medio = findViewById(R.id.btnmedio);

      
        final SharedPreferences prefs = mConfig.getPrefsPrivate();
        SharedPreferences.Editor edit = prefs.edit();
		final SharedPreferences sPrefs = mConfig.getPrefsPrivate();
        sPrefs.edit().putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false).apply();
		sPrefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT).apply();

		//serverSpinner = (Spinner) findViewById(R.id.serverSpinner);
      
		serverList = new ArrayList<>();
		payloadList = new ArrayList<>();

		//serverAdapter = new SpinnerAdapter(this, R.id.serverSpinner, serverList);
		
		///serverSpinner.setAdapter(serverAdapter);
		updateConfig(true);
		updateUsuarios(true);
		updateAnuncios(true);
		starterButton.setOnClickListener(this);

		bytes_in = (TextView) findViewById (R.id.bytes_in);
		bytes_out = (TextView) findViewById (R.id.bytes_out);
		relojoff = (ImageView) findViewById(R.id.relojoff);
		relojon = (ImageView) findViewById(R.id.relojon);



		List<String> Listportauto = new ArrayList<String>();
		Listportauto.add("AUTO");
		ArrayAdapter<String> Adptorportaut = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Listportauto);


		Adptorportaut.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



		List<String> Listport = new ArrayList<String>();
		Listport.add("80");
		Listport.add("443");
		Listport.add("3128");
		Listport.add("8080");
		Listport.add("8081");
		Listport.add("8789");
		Listport.add("8799");
		Listport.add("8888");
		Listport.add("8000");

		ArrayAdapter<String> Adptorport = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Listport);

		Adptorport.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);





		customAdapter = new CustomAdapter(this,flags,countryNames);







	
		// fix bugs
		if (mConfig.getPrefsPrivate().getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
			if (mConfig.getPrefsPrivate().getBoolean(Settings.CONFIG_INPUT_PASSWORD_KEY, false)) {

			}
		}
        
        edit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, !true);

		// customPayloadSwitch.setOnCheckedChangeListener(this);

        
        doTabs();
	}

    //CLASS No Exist [Obfs]
    private boolean isSSL(String className) {
        try  {
            Class.forName(className);
            return true;
        }  catch (ClassNotFoundException e) {
            return false;
        }
    }



	private void loading() {
		// Asegúrate de que el diálogo anterior se haya cerrado antes de mostrar uno nuevo
		dismissCurrentDialog();

		// Crear un objeto AlertDialog
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

		// Inflar el diseño personalizado en el AlertDialog
		View dialogView = getLayoutInflater().inflate(R.layout.abuscando_update, null);
		dialogBuilder.setView(dialogView);

		// Configurar el AlertDialog
		dialogBuilder.setCancelable(false);

		// Crear el AlertDialog
		dialogLoading = dialogBuilder.create();

		// Obtener una referencia al botón "OK" en el diseño personalizado
		Button btnOK = dialogView.findViewById(R.id.btnOK);

		// Configurar un OnClickListener para cerrar el diálogo cuando se hace clic en el botón "OK"
		btnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogLoading.dismiss();
			}
		});

		// Establecer el fondo transparente
		dialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		dialogLoading.show();
	}

	// Método para cerrar el diálogo actual
	private void dismissCurrentDialog() {
		if (alertDialog != null && alertDialog.isShowing()) {
			alertDialog.dismiss();
		}
	}


	private void setupSSH(){
		
		try
		{
			SharedPreferences prefs = mConfig.getPrefsPrivate();
			prefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT).apply();

			SharedPreferences.Editor edit = prefs.edit();
		
			//int pos1 = serverSpinner.getSelectedItemPosition();
			//String ssh_port = config.getServersArray().getJSONObject(pos1).getString("ServerPort");
			///edit.putString(Settings.SERVIDOR_PORTA_KEY, ssh_port).apply();

			if (prefs.getString(Settings.PROXY_PORTA_KEY, "").isEmpty()){
				edit.putString(Settings.PROXY_PORTA_KEY, "80").apply();

			}
			
			
		}
		catch (Exception e)
		{}
	}
	
	private void setupSSL(){
		
		setupSSH(); // fix bug
			
		SharedPreferences prefs = mConfig.getPrefsPrivate();
		prefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_SSL).apply();

		SharedPreferences.Editor edit = prefs.edit();
		edit.putString(Settings.SERVIDOR_PORTA_KEY, "443").apply();
			
	}

	

    @Override
    public void onCheckedChanged(CompoundButton p1, boolean p2) {
    }

	public SweetAlertDialog getmDialog() {
		return mDialog;
	}

	public class DrawerPanelMain
	implements NavigationView.OnNavigationItemSelectedListener
	{
		private AppCompatActivity mActivity;

		public DrawerPanelMain(AppCompatActivity activity) {
			mActivity = activity;
		}


		private DrawerLayout drawerLayout;
		private ActionBarDrawerToggle toggle;

		public void setDrawer(Toolbar toolbar) {
			NavigationView drawerNavigationView = (NavigationView) mActivity.findViewById(R.id.drawerNavigationView);
			drawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawerLayoutMain);

			// set drawer
			toggle = new ActionBarDrawerToggle(mActivity,
											   drawerLayout, toolbar, R.string.open, R.string.cancel);

			drawerLayout.setDrawerListener(toggle);

			toggle.syncState();

			// set app info
			PackageInfo pinfo = Utils.getAppInfo(mActivity);
			if (pinfo != null) {
				String version_nome = pinfo.versionName;
				int version_code = pinfo.versionCode;
				String header_text = String.format("v. %s (%d)", version_nome, version_code);

				View view = drawerNavigationView.getHeaderView(0);

				TextView app_info_text = view.findViewById(R.id.nav_headerAppVersion);
				app_info_text.setText(header_text);
			}


			// set navigation view
			drawerNavigationView.setNavigationItemSelectedListener(this);
			drawerNavigationView.setItemIconTintList(null);
		}

		public ActionBarDrawerToggle getToogle() {
			return toggle;
		}

		public DrawerLayout getDrawerLayout() {
			return drawerLayout;
		}

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			int id = item.getItemId();

			if (id == R.id.update) {
				loading();
				updateConfig(false);
				updateUsuarios(false);
				updateAnuncios(false);
				drawerLayout.closeDrawers();
			}

			else if (id == R.id.miSettings) {
				Intent intent = new Intent(mActivity, ConfigGeralActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivity.startActivity(intent);
				showInterstitial();
			}

			if (id == R.id.telegramm) {
				String url23 = "https://t.me/fenix1998X";
				Intent intent43 = new Intent(Intent.ACTION_VIEW, Uri.parse(url23));
				intent43.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivity.startActivity(Intent.createChooser(intent43, "Open With"));

			} else if (id == R.id.pruebavelocidad) {
				String url22 = "https://www.speedtest.net/";
				Intent intent42 = new Intent(Intent.ACTION_VIEW, Uri.parse(url22));
				intent42.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivity.startActivity(Intent.createChooser(intent42, mActivity.getText(R.string.open_with)));

			} else if (id == R.id.grupotelegram) {
				String url21 = "https://t.me/Fenix1998X";
				Intent intent41 = new Intent(Intent.ACTION_VIEW, Uri.parse(url21));
				intent41.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivity.startActivity(Intent.createChooser(intent41, mActivity.getText(R.string.open_with)));

			} else if (id == R.id.hostcris) {
				Intent hostshare2 = new Intent(FenixMainActivity.this, ProxySettings.class);
				startActivity(hostshare2);
				showInterstitial();

			} else if (id == R.id.miAbout) {
				Intent aboutIntent = new Intent(mActivity, AboutActivity.class);
				mActivity.startActivity(aboutIntent);
				showInterstitial();

			} else if (id == R.id.btn_abrir_juego) {
				FenixMainActivity.this.abrirJuegoMayorMenor();
				return true;

			} else if (id == R.id.btn_juego_frutas) {
				FenixMainActivity.this.abrirJuegoFrutas();
				return true;

			} else if (id == R.id.btn_abrir_chat) {
				FenixMainActivity.this.abrirChatComunidad();
				return true;

			} else if (id == R.id.btn_abrir_ruleta) {
				FenixMainActivity.this.abrirJuegoRuleta();
				return true;
			}

			return true;
		}
		}

	private void checkAppSignature() {
		try {
			@SuppressLint("PackageManagerGetSignatures")
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
			Signature[] signatures = packageInfo.signatures;
			String currentSignatureSHA1 = getSignatureSHA1(signatures[0]);

			if (!currentSignatureSHA1.equals(SkProtect.storedSignatureSHA1)) {
				/*Intent stopVPN = new Intent(FenixVpnService.TUNNEL_SSH_STOP_SERVICE);
				LocalBroadcastManager.getInstance(FenixMainActivity.this).sendBroadcast(stopVPN);
				VCoreManager.getInstance().stopCore();
				TunnelManagerThread managerThread = new TunnelManagerThread(mHandler, FenixMainActivity.this);
				managerThread.stopAllVPN();
				oficialVersion();*/
			}

			editFirm.putBoolean(SkProtect.firm, false);
			editFirm.apply();

		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String getSignatureSHA1(Signature signature) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] signatureBytes = signature.toByteArray();
			byte[] digest = md.digest(signatureBytes);
			StringBuilder sb = new StringBuilder();
			for (byte b : digest) {
				sb.append(String.format("%02X", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
    
    public void doTabs() {
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		//deleteLogs = (FloatingActionButton)findViewById(R.id.delete_log);
		mLogAdapter = new LogsAdapter(layoutManager,this);
		logList = findViewById(R.id.recyclerLog);
		logList.setAdapter(mLogAdapter);
		logList.setLayoutManager(layoutManager);
		mLogAdapter.scrollToLastPosition();
		vp = findViewById(R.id.viewpager);
		tabs = findViewById(R.id.tablayout);
		vp.setAdapter(new MyAdapter(Arrays.asList(tabTitle)));
		vp.setOffscreenPageLimit(2);
		tabs.setTabMode(TabLayout.MODE_FIXED);
		tabs.setTabGravity(TabLayout.GRAVITY_FILL);
		tabs.setupWithViewPager(vp);

        if (oooOOO(getApplicationInfo().nativeLibraryDir)) {
            oOoOOoO();
            throw new RuntimeException(err);
        }
	} 
        
    public class MyAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            // TODO: Implement this method
            return 2;
        }

        @Override
        public boolean isViewFromObject(View p1, Object p2)
        {
            // TODO: Implement this method
            return p1 == p2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            int[] ids = new int[]{R.id.tab1, R.id.tab2};
            int id = 0;
            id = ids[position];
            // TODO: Implement this method
            return findViewById(id);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            // TODO: Implement this method
            return titles.get(position);
        }

        private List<String> titles;
        public MyAdapter(List<String> str)
        {
            titles = str;
        }
	}




	private void clearz() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflate = inflater.inflate(R.layout.addtime, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(inflate);
		ImageView iv = inflate.findViewById(R.id.images_dialog1);
		TextView title = inflate.findViewById(R.id.text_view_dialog1);
		TextView ms = inflate.findViewById(R.id.text_view_dialog2);
		Button ok = inflate.findViewById(R.id.add_time_dialog);
		iv.setImageResource(R.drawable.deleee);
		title.setText("¿Está seguro de borrar los datos de la aplicación?");
		ms.setText("El sistema borrará todos los datos de actualizaciones \n y no se pueden recuperar.");
		ok.setText("  Clear  ");

		final AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.show();

		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					dialog.dismiss();
					String packageName = getApplicationContext().getPackageName();
					Runtime runtime = Runtime.getRuntime();
					runtime.exec("pm clear " + packageName);

					// Mostrar mensaje de éxito
					Toast.makeText(getApplicationContext(), "Los datos se han borrado exitosamente✔\uFE0F", Toast.LENGTH_LONG).show();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}




	private void doUpdateLayout() {
		SharedPreferences prefs = mConfig.getPrefsPrivate();

		boolean isRunning = oOOoOOo();
		int tunnelType = prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
		
		setStarterButton(starterButton, this);

		String proxyStr = getText(R.string.no_value).toString();

		if (prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
			proxyStr = "*******";
		}
		else {
			String proxy = mConfig.getPrivString(Settings.PROXY_IP_KEY);

			if (proxy != null && !proxy.isEmpty())
				proxyStr = String.format("%s:%s", proxy, mConfig.getPrivString(Settings.PROXY_PORTA_KEY));

		} 









		int msgVisibility = View.GONE;
		int loginVisibility = View.GONE;
		String msgText = "";
		boolean enabled_radio = !isRunning;

		if (prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {

			if (prefs.getBoolean(Settings.CONFIG_INPUT_PASSWORD_KEY, false)) {
				loginVisibility = View.VISIBLE;



				//inputPwPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
			String msg = mConfig.getPrivString(Settings.CONFIG_MENSAGEM_KEY);
			if (!msg.isEmpty()) {
				msgText = msg.replace("\n", "<br/>");
				msgVisibility = View.VISIBLE;
			}

			if (mConfig.getPrivString(Settings.PROXY_IP_KEY).isEmpty() ||
					mConfig.getPrivString(Settings.PROXY_PORTA_KEY).isEmpty()) {
				enabled_radio = false;
			}
		}




		// desativa/ativa radio group

	}


	private synchronized void doSaveData() {
		try {
            if (mConfig.getPremium()){
                ooOooOo(this);
            } else {
                oOoOoOOOOo(this);
            }

			SharedPreferences prefs = mConfig.getPrefsPrivate();
			SharedPreferences.Editor edit = prefs.edit();
			edit.apply();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/*	private void setSpinner(){
		SharedPreferences prefs = mConfig.getPrefsPrivate();
		int server = prefs.getInt("LastSelectedServer", 0);
		int payload = prefs.getInt("LastSelectedPayload", 0);
		serverSpinner.setSelection(server);
	}

	private void saveSpinner(){
		SharedPreferences prefs = mConfig.getPrefsPrivate();
		SharedPreferences.Editor edit = prefs.edit();
		int server = serverSpinner.getSelectedItemPosition();
		edit.putInt("LastSelectedServer", server);
		edit.apply();
	}
*/


	public int mainposition() {
		SharedPreferences prefs = mConfig.getPrefsPrivate();
		return prefs.getInt("LastSelectedServer", 0);
	}


	private void setMainView() {
		try {
			JSONObject server = config.getServersArray().getJSONObject(mainposition());
			String nombre = decodeDecrypted(server.getString(getString(R.string.sName)));
			String info = decodeDecrypted(server.getString(getString(R.string.sInfo)));
			String imagen = decodeDecrypted(server.getString(getString(R.string.sFlag)));
			if (info.isEmpty() || info == null) {
				serverinfo.setText(getString(R.string.app_name));
			} else {
				serverinfo.setText(info);
			}
			servername.setText(nombre);
			InputStream stream = getAssets().open("flags/" + imagen + ".png");
			serverimage.setImageDrawable(Drawable.createFromStream(stream, imagen + ".png"));
			stream.close();
		} catch (Exception e)  {
			Toast.makeText(getApplicationContext(), "Error al cargar los servers, intenta actualizar los servidores", Toast.LENGTH_SHORT).show();
		}
	}

    public static long getPing(String host, int port) {
        Socket socket = new Socket();
        try {
            long start = System.currentTimeMillis();
            InetAddress address = InetAddress.getByName(host);
            socket.setTcpNoDelay(true);
            socket.connect(new InetSocketAddress(address.getHostAddress(), port), 2000);

            return System.currentTimeMillis() - start;

        } catch (Exception e) {
            return -1;
        } finally {
            try {
                socket.close();
            } catch (Exception ignored) {}
        }
    }

    private void actualizarPingServidor() {
        new Thread(() -> {
            try {
                JSONObject server = config.getServersArray().getJSONObject(mainposition());
                int type = server.getInt("serverType");
                String host = null;
                int port = 443;

                if (type == 1 || type == 2) {
                    host = decodeDecrypted(server.getString(getString(R.string.ServerIP)));
                    port = Integer.parseInt(decodeDecrypted(server.getString(getString(R.string.ServerPort))));
                } else if (type == 3) {
                    host = decodeDecrypted(server.getString(getString(R.string.udpserver)));
                    port = 22;
                } else if (type == 4) {
                    host = decodeDecrypted(server.getString(getString(R.string.V2rayAddress)));
                    port = 80;
                }
                long ping = getPing(host, port);

                runOnUiThread(() -> {
                    if (ping >= 0) {
                        serverping.setText(ping + " ms");
                        if (ping < 100) {
                            serverping.setTextColor(Color.parseColor("#04de72"));
                        } else if (ping < 200) {
                            serverping.setTextColor(Color.parseColor("#FFB300"));
                        } else {
                            serverping.setTextColor(Color.parseColor("#F44336"));
                        }
                    } else {
                        serverping.setVisibility(View.GONE);
                        serverping.setText("-- ms");
                        serverping.setTextColor(Color.parseColor("#F44336"));
                    }
                });

            } catch (Exception ignored) {
            }

        }).start();
    }

    private final Handler pingHandler =
            new Handler(Looper.getMainLooper());

    private final Runnable pingRunnable =
            new Runnable() {
                @Override
                public void run() {
                    if (SkStatus.isTunnelActive()) {
                        actualizarPingServidor();
                        pingHandler.postDelayed(this, 5000);
                    }
                }
            };

	private void updateConfig(final boolean isOnCreate) {
		new ConfigUpdate(this, new ConfigUpdate.OnUpdateListener() {
			@Override
			public void onUpdateListener(String result) {
				try {
					if (!result.contains("Error al obtener datos")) {
						String json_data = AES.decrypt(MINITO, result);
						if (isNewVersion(json_data)) {
							changeList(result);
						} else {
							if (!isOnCreate) {
								noUpdateDialog();
							}
						}
					} else if(result.contains("Error al obtener datos") && !isOnCreate){
						errorUpdateDialog(result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start(isOnCreate, "servidores");
	}

	private boolean isNewVersion(String result) {
		try {
			String current = config.getVersion();
			String update = new JSONObject(result).getString("Version");
			return config.versionCompare(update, current);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void newUpdateDialog(final String result) {
		try {
			File file = new File(getFilesDir(), Configs.UPDATE_SERVERS);
			OutputStream out = new FileOutputStream(file);
			out.write(result.getBytes());
			out.flush();
			out.close();
			restart_app();
			//new Handler().postDelayed(() -> changeList(), 1000);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}


	private void changeList(final String result) {
		try {
			String json_data = AES.decrypt(MINITO, result);
			String notes = new JSONObject(json_data).getString("ReleaseNotes");

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			View dialogView = getLayoutInflater().inflate(R.layout.activity_update, null);

			builder.setView(dialogView);
			final AlertDialog dialog = builder.create();

			// Establecer el fondo transparente del AlertDialog
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);


			TextView titleTextView = dialogView.findViewById(R.id.titleTextVieww);
			TextView notesTextView = dialogView.findViewById(R.id.notesTextVieww);
			//Button updateButton = dialogView.findViewById(R.id.updateButtonn);
			TextView countdownTextView = dialogView.findViewById(R.id.countdownTextView);
			//Button cancelButton = dialogView.findViewById(R.id.cancelButtonn);

			titleTextView.setText("Servidores Actualizados");
			notesTextView.setText(""+ notes);

			/*updateButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//restart_app();
				}
			});*/

			dialog.show();

			final int countdownDuration = 5;

			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				int countdown = countdownDuration;

				@Override
				public void run() {
					countdown--;
					countdownTextView.setText(countdown + " segundos para reiniciar la aplicación");

					if (countdown > 0) {
						handler.postDelayed(this, 1000);
					} else {
						newUpdateDialog(result);
						dialog.dismiss(); // Cierra el diálogo cuando se reinicia la aplicación
					}
				}
			}, 1000);
		} catch (JSONException | GeneralSecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	private void noUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View dialogView = getLayoutInflater().inflate(R.layout.activity_update, null);

		builder.setView(dialogView);
		final AlertDialog dialog = builder.create();

		// Establecer el fondo transparente del AlertDialog
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);


		TextView titleTextView = dialogView.findViewById(R.id.titleTextVieww);
		TextView notesTextView = dialogView.findViewById(R.id.notesTextVieww);
		Button updateButton = dialogView.findViewById(R.id.updateButtonn);
		//Button cancelButton = dialogView.findViewById(R.id.cancelButtonn);

		titleTextView.setText("Servidores Actualizados");
		notesTextView.setText(config.getNote());

		updateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
		dialogLoading.dismiss();
	}

	/*private void noUpdateDialog() {
		// Crear un objeto AlertDialog
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

		// Inflar el diseño personalizado en el AlertDialog
		View dialogView = getLayoutInflater().inflate(R.layout.activity_secess, null);
		dialogBuilder.setView(dialogView);

		// Configurar el AlertDialog
		dialogBuilder.setCancelable(false);

		// Crear y mostrar el AlertDialog
		final AlertDialog alertDialog = dialogBuilder.create();

		// Obtener una referencia al botón "OK" en el diseño personalizado
		Button btnOK = dialogView.findViewById(R.id.btnOK);

		// Configurar un OnClickListener para cerrar el diálogo cuando se hace clic en el botón "OK"
		btnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});

		// Establecer el fondo transparente
		alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		alertDialog.show();
		dialogLoading.dismiss();

	}*/


	private void errorUpdateDialog(String error) {
		// Crear un objeto AlertDialog
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

		// Inflar el diseño personalizado en el AlertDialog
		View dialogView = getLayoutInflater().inflate(R.layout.activity_error, null);
		dialogBuilder.setView(dialogView);

		// Configurar el AlertDialog
		dialogBuilder.setCancelable(false);

		// Crear y mostrar el AlertDialog
		final AlertDialog alertDialog = dialogBuilder.create();

		// Obtener una referencia al botón "OK" en el diseño personalizado
		Button btnOK = dialogView.findViewById(R.id.btnOK);

		// Configurar un OnClickListener para cerrar el diálogo cuando se hace clic en el botón "OK"
		btnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});

		// Establecer el fondo transparente
		alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		alertDialog.show();
	}



	private void restart_app() {
		Context context = FenixApp.getApp();
		PackageManager packageManager = context.getPackageManager();
		Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
		ComponentName componentName = intent.getComponent();
		Intent mainIntent = Intent.makeRestartActivityTask(componentName);
		context.startActivity(mainIntent);
		Runtime.getRuntime().exit(0);

	}

	//ACTUALIZACION USUARIO
	private void updateUsuarios(final boolean isOnCreate) {
		new ConfigUpdate(this, new ConfigUpdate.OnUpdateListener() {
			@Override
			public void onUpdateListener(String result) {
				try {
					if (!result.contains("Error al obtener datos")) {
						String json_data = AES.decrypt(MINITO, result);
						if (nuevaVersion(json_data)) {
							usersUpdateDialog(result);
						} else {
							if (!isOnCreate) {
								//noUpdateDialog();
								//Toast.makeText(WakkoVPNMainActivity.this, "No hay actualizacion",Toast.LENGTH_SHORT).show();
							}
						}
					} else if(result.contains("Error al obtener datos") && !isOnCreate){
						//errorUpdateDialog(result);
						//Toast.makeText(WakkoVPNMainActivity.this, "Error al actualizar",Toast.LENGTH_SHORT).show();

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start(isOnCreate, "usuarios");
	}

	private void usersUpdateDialog(final String result) {
		//toastText(getString(R.string.txt_update_user));
		Toast.makeText(this, "Actualización de servidores completa",Toast.LENGTH_LONG).show();
		try {
			FileOutputStream files = openFileOutput(Configs.UPDATE_USERS, MODE_PRIVATE);
			files.write(result.getBytes());
			files.flush();
			files.close();

		}
		catch (Exception e){
			e.printStackTrace();
			Toast.makeText(this, "Servidores sin actualizar",Toast.LENGTH_LONG).show();

		}

	}

	private boolean nuevaVersion(String result) {
		try {
			String current = config.getVersionUser();
			String update = new JSONObject(result).getString("Version");
			return config.versionCompare(update, current);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * Tunnel SSH
	 */


	public void setStarterButton(Button starterButton, Activity activity) {
		String state = SkStatus.getLastState();
		boolean isRunning = oOOoOOo();

		if (starterButton != null) {
			int resId;
			
			SharedPreferences prefsPrivate = new Settings(activity).getPrefsPrivate();

			if (ConfigParser.isValidadeExpirou(prefsPrivate
					.getLong(Settings.CONFIG_VALIDADE_KEY, 0))) {
				resId = R.string.expired;
				starterButton.setEnabled(false);

				if (isRunning) {
					ooOooOo(activity);
				}
			}
			else if (prefsPrivate.getBoolean(Settings.BLOQUEAR_ROOT_KEY, false) &&
					ConfigParser.isDeviceRooted(activity)) {
			   resId = R.string.blocked;
			   starterButton.setEnabled(false);
			   
			   Toast.makeText(activity, R.string.error_root_detected, Toast.LENGTH_SHORT)
					.show();

			   if (isRunning) {
				   ooOooOo(activity);
			   }
			}
			else if (SkStatus.SSH_INICIANDO.equals(state)) {
				resId = R.string.stop;

				starterButton.setEnabled(false);
				serverlayout.setEnabled(false);
				prendido.setVisibility(View.GONE);
				medio.setVisibility(View.VISIBLE);
				apagado.setVisibility(View.GONE);
				FileInputStream fileInputStream = null;

			}
			else if (SkStatus.SSH_PARANDO.equals(state)) {
				resId = R.string.state_stopping;
				starterButton.setEnabled(false);
                prendido.setVisibility(View.GONE);
                medio.setVisibility(View.VISIBLE);
                apagado.setVisibility(View.GONE);

			}else if (SkStatus.SSH_DESCONECTADO.equals(state)){
				resId = R.string.start;
				starterButton.setEnabled(true);
				serverlayout.setEnabled(true);
				prendido.setVisibility(View.GONE);
				medio.setVisibility(View.GONE);
				apagado.setVisibility(View.VISIBLE);
                timer_layout.setVisibility(View.VISIBLE);
                if (!mConfig.getPremium()) {
                    oOoOOOOo();
                }
				}else {
				resId = isRunning ? R.string.stop : R.string.start;
				starterButton.setEnabled(true);
			}

			starterButton.setText(resId);
		}
	}

	@Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        if (mDrawerPanel.getToogle() != null)
			mDrawerPanel.getToogle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerPanel.getToogle() != null)
			mDrawerPanel.getToogle().onConfigurationChanged(newConfig);
    }
	
	private boolean isMostrarSenha = false;
	
	@Override
	public void onClick(View p1) {
		int id = p1.getId();
		if (id == R.id.ConnectSocket) {
			doSaveData();
			loadServerData.loadServer();
			if (!oOoOOo("oOoOoOOOOo")) {
				//oOoOOoO();
			}
            if (mConfig.getPremium()){
                SharedPreferences prefs = mConfig.getPrefsPrivate();
                SharedPreferences.Editor edit = prefs.edit();

                String idaqui = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                String udpauth = idaqui + ":" + TOKEN_PASS;
                edit.putString(Settings.USUARIO_KEY, idaqui)
                        .putString(Settings.SENHA_KEY, TOKEN_PASS)
                        .putString(Settings.UDP_AUTH, udpauth)
                        .apply();
            }
			mShown = false;
			mShown2 = false;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup p1, int p2)
	{
		SharedPreferences.Editor edit = mConfig.getPrefsPrivate().edit();



		edit.apply();

		//doSaveData();
		doUpdateLayout();
	}

	/*protected void showBoasVindas() {
		new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
			.setTitleText("Atención")
			.setContentText("Si tiene alguna duda o dificultad a la hora de conectar la aplicación, comunicate con el Administrador para más información.")
			.setConfirmText("OK")
			.show();
	}*/

	@Override
	public void updateState(final String state, String msg, int localizedResId, final ConnectionStatus level, Intent intent)
	{
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				doUpdateLayout();
				if (oOOoOOo()){
					if (level.equals(ConnectionStatus.LEVEL_CONNECTED)){
						if (!mShown){
							showInterstitial();
							Toast.makeText(FenixMainActivity.this,"Conectado",Toast.LENGTH_SHORT).show();
							mShown = true;
						}
                        serverping.setVisibility(View.VISIBLE);
                        pingHandler.post(pingRunnable);
						prendido.setVisibility(View.VISIBLE);
						medio.setVisibility(View.GONE);
						apagado.setVisibility(View.GONE);

						// --- ESTADO: CONECTADO ---
						status.setText("Conectado");
						status.setTextColor(android.graphics.Color.parseColor("#4CAF50")); // Verde

						serverlayout.setEnabled(false);
						showInterstitial();
						timer_layout.setVisibility(View.VISIBLE);
						//checkAppSignature();
						if (mConfig.getPremium()){
							CheckUser();
						}

					}

					if(level.equals(ConnectionStatus.LEVEL_CONNECTING_SERVER_REPLIED)){
						// --- ESTADO: VERIFICANDO ---
						status.setText(R.string.state_auth);
						status.setTextColor(android.graphics.Color.parseColor("#FF9800")); // Naranja

						prendido.setVisibility(View.GONE);
						medio.setVisibility(View.VISIBLE);
						apagado.setVisibility(View.GONE);

					}
					if(level.equals(ConnectionStatus.LEVEL_CONNECTING_NO_SERVER_REPLY_YET)){

					}
					//start();

				}
				if(level.equals(ConnectionStatus.UNKNOWN_LEVEL) || level.equals(ConnectionStatus.LEVEL_NOTCONNECTED)){
					if (!mShown2 && level.equals(ConnectionStatus.UNKNOWN_LEVEL)){
						showInterstitial();
						Toast.makeText(FenixMainActivity.this," Desconectado",Toast.LENGTH_SHORT).show();
						mShown2 = true;
					}
                    serverping.setVisibility(View.GONE);
                    pingHandler.removeCallbacks(pingRunnable);
					if (!mConfig.getPremium()) {
						oOoOOOOo();
					}
					prendido.setVisibility(View.GONE);

					// --- ESTADO: DESCONECTADO ---
					status.setText("Desconectado");
					status.setTextColor(android.graphics.Color.parseColor("#F44336")); // Rojo

					serverlayout.setEnabled(true);


				}

				if(level.equals(ConnectionStatus.LEVEL_NONETWORK)){


				}
				if(level.equals(ConnectionStatus.LEVEL_AUTH_FAILED)){
					status.setText(R.string.state_auth_failed);


				}
			}

		});
		if (SkStatus.SSH_CONECTADO.equals(state)) {
				// carrega ads banner
				if (adsBannerView != null && TunnelUtils.isNetworkOnline(FenixMainActivity.this)) {
					adsBannerView.setAdListener(new AdListener() {
						@Override
						public void onAdLoaded() {
							if (adsBannerView != null && !isFinishing()) {
								adsBannerView.setVisibility(View.VISIBLE);

							}
						}
					});

				}
				//start();

		}
	}

	public void oficialVersion() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialog);
		View dialogView = getLayoutInflater().inflate(R.layout.admob, null);
		dialogBuilder.setView(dialogView);
		TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
		TextView dialogMessage = dialogView.findViewById(R.id.dialogMessage);
		dialogTitle.setText("Atencion");
		dialogMessage.setText("Esta no es la versión oficial, por favor descarga la versión oficial desde Google Play Store");
		final AlertDialog dialog = dialogBuilder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Descargar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(SkProtect.link));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent, getText(R.string.open_with)));
				Utils.exitAll(FenixMainActivity.this);
			}
		});
		/*dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.minimize), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Minimiza la app
				Intent startMain = new Intent(Intent.ACTION_MAIN);
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(startMain);
				dialog.dismiss();
			}
		});*/
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Salir", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface p1, int p2) {
				p1.dismiss();
				Utils.exitAll(FenixMainActivity.this);
			}
		});
		dialog.show();
	}


	/**
	 * Recebe locais Broadcast
	 */


	public void showExitDialog() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialog);
		View dialogView = getLayoutInflater().inflate(R.layout.admob, null);
		dialogBuilder.setView(dialogView);
		TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
		TextView dialogMessage = dialogView.findViewById(R.id.dialogMessage);
		AdView adView = dialogView.findViewById(R.id.adBannerExit);
		dialogTitle.setText("Atencion");
		dialogMessage.setText("Desea salir o minimizar");
		final AlertDialog dialog = dialogBuilder.create();
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.exit), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
                if (!mConfig.getPremium()) {
                    oOoOOOOo();
                }
				Utils.exitAll(FenixMainActivity.this);
			}
		});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.minimize), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Minimiza la app
				Intent startMain = new Intent(Intent.ACTION_MAIN);
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(startMain);
				dialog.dismiss();
			}
		});
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Cancelar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface p1, int p2) {
				p1.dismiss();
			}
		});
		dialog.show();
	}

	private BroadcastReceiver mActivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null)
                return;

            if (action.equals(UPDATE_VIEWS) && !isFinishing()) {
				doUpdateLayout();
				setMainView();
			}else if (action.equals(OPEN_LOGS)) {
				vp.setCurrentItem(1, true);
			}
			
        }
    };


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerPanel.getToogle() != null && mDrawerPanel.getToogle().onOptionsItemSelected(item)) {
            return true;
        }

			int id = item.getItemId();
			
			if (id == R.id.update) {
				loading();
				updateConfig(false);
				updateUsuarios(false);
				updateAnuncios(false);
			}
			
			else if (id == R.id.miSettings) {
				Intent intentSettings = new Intent(this, ConfigGeralActivity.class);
				//intentSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentSettings);
				showInterstitial();
			}

			else if (id == R.id.miExit) {
				if (Build.VERSION.SDK_INT >= 16) {
					finishAffinity();
				}

				System.exit(0);
			}

			else if (id == R.id.clear){
				clearz();
				showInterstitial();
		}

		return super.onOptionsItemSelected(item);
	}



	@Override
	public void onDismiss(String payload)
	{
		Toast.makeText(this,"Payload generado satisfactoriamente!",Toast.LENGTH_SHORT).show();
		alertDialog.dismiss();
		// TODO: Implement this method
	}

	@Override
	public void onBackPressed() {
		showExitDialog();
	}
	//borrar
	/*public void onBackPressed()
	{
		new SweetAlertDialog(FenixMainActivity.this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(getString(R.string.attention))
            .setContentText(getString(R.string.alert_exit))
            .setConfirmText(getString(R.string.exit))
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog)
                {
                    // TODO: Implement this method

                    Utils.exitAll(FenixMainActivity.this);

                }})
            .setCancelText(getString(R.string.minimize))
			.showCancelButton(true)
            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener(){

                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog)
                {
                    // TODO: Implement this method
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                }


            })

            .show();
	}*/





	private void updateHeaderCallback() {
		DataTransferStats dataTransferStats = StatisticGraphData.getStatisticData().getDataTransferStats();
		bytes_in.setText(dataTransferStats.byteCountToDisplaySize(dataTransferStats.getTotalBytesReceived(), false));
		bytes_out.setText(dataTransferStats.byteCountToDisplaySize(dataTransferStats.getTotalBytesSent(), false));
	}

	@Override
	public void onResume() {
		super.onResume();

		filePublic(this);

		SharedPreferences prefs = mConfig.getPrefsPrivate();

		if (prefs.getInt("CustomSetup", 0) == 1) { // Custom Setup

			if (prefs.getInt("TunnelType", 0) == 1) { // SSH

				setupSSH();

			} else if (prefs.getInt("TunnelType", 0) == 2) { // SSL

				setupSSL();

			}
		} // <- Aquí cierra correctamente la configuración

		// El anuncio queda libre y limpio al final del ciclo de vida
		
		new Timer().schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								//updateHeaderCallback();
								Network();
								// TODO: Implement this method
							}
						});
					// TODO: Implement this method
				}
			}, 0,1000);
		
		
		SkStatus.addStateListener(this);
		
		if (adsBannerView != null) {
			//adsBannerView.resume();
			//showInterstitial();
			adsBannerView.setVisibility(View.VISIBLE);
		}
    }

	@Override
	protected void onPause()
	{
		super.onPause();
		
		//doSaveData();
		
		SkStatus.removeStateListener(this);
		
		if (adsBannerView != null) {
			adsBannerView.pause();
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		LocalBroadcastManager.getInstance(this)
			.unregisterReceiver(mActivityReceiver);
			
		if (adsBannerView != null) {
			adsBannerView.destroy();
		}
	}

	/**
	 * Utils
	 */

	public static void updateMainViews(Context context) {
		Intent updateView = new Intent(UPDATE_VIEWS);
		LocalBroadcastManager.getInstance(context)
			.sendBroadcast(updateView);
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

	public void getData() {
		boolean isRunning = oOOoOOo();
		long mUpload, mDownload, saved_Send, saved_Down/*,up, down*/;
		String saved_date, tDate;
		List<Long> allData;
		allData = RetrieveData.findData();
		mDownload = allData.get(0);
		mUpload = allData.get(1);
		StoredData.storedData(mDownload, mUpload);
		//down = mDownload;
		//up = mUpload;
		Calendar ca = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
		tDate = sdf.format(ca.getTime());
		saved_date = myData.getString("today_date", "empty");
		SharedPreferences.Editor editor = myData.edit();
		if (saved_date.equals(tDate)) {
			saved_Send = myData.getLong("UP_DATA", 0);
			saved_Down = myData.getLong("DOWN_DATA", 0);
			editor.putLong("UP_DATA", mUpload + saved_Send);
			editor.putLong("DOWN_DATA", mDownload + saved_Down);
			editor.apply();
		} else {
			editor.clear();
			editor.putString("today_date", tDate);
			editor.apply();
		}
		if (isRunning) {
			bytes_in.setText(render_bandwidth(myData.getLong("UP_DATA", 0)));
			bytes_out.setText(render_bandwidth(myData.getLong("DOWN_DATA", 0)));
		} else {
			myData.edit().putLong("UP_DATA", 0).apply();
			myData.edit().putLong("DOWN_DATA", 0).apply();
		}
		StatisticGraphData.DataTransferStats stats = StatisticGraphData.getStatisticData().getDataTransferStats();
		//String duration = stats.isConnected() ? stats.elapsedTimeToDisplay(stats.getElapsedTime()) : "00h:00m:00s";

	}

	private String render_bandwidth(double bw) {
		String postfix;
		float div;
		Object[] objArr;
		float bwf = (float) bw;
		if (bwf >= 1.0E12f) {
			postfix = "TB";
			div = 1.0995116E12f;
		} else if (bwf >= 1.0E9f) {
			postfix = "GB";
			div = 1.0737418E9f;
		} else if (bwf >= 1000000.0f) {
			postfix = "MB";
			div = 1048576.0f;
		} else if (bwf >= 1000.0f) {
			postfix = "KB";
			div = 1024.0f;
		} else {
			objArr = new Object[S_BIND_CALLED];
			objArr[0] = Float.valueOf(bwf);
			return String.format("%.0f", objArr);
		}
		objArr = new Object[S_ONSTART_CALLED];
		objArr[0] = Float.valueOf(bwf / div);
		objArr[S_BIND_CALLED] = postfix;
		return String.format("%.2f %s", objArr);
	}

	private void liveData() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						getData();
					}
				});
			}
		}, 0, 1000);
	}

	private void stopVPN() {
		Intent stopVPN = new Intent(FenixVpnService.TUNNEL_SSH_STOP_SERVICE);
		LocalBroadcastManager.getInstance(FenixMainActivity.this).sendBroadcast(stopVPN);
		V2Core.getInstance().stopCore();
		TunnelManagerThread managerThread = new TunnelManagerThread(mHandler, FenixMainActivity.this);
		managerThread.stopAllVPN();
        TunnelVpnService tunnelVpnService = new TunnelVpnService();
        tunnelVpnService.broadcastVpnDisconnect();
	}

	//ACTUALIZACION ANUNCIOS
	private void updateAnuncios(final boolean isOnCreate) {
		new ConfigUpdate(this, new ConfigUpdate.OnUpdateListener() {
			@Override
			public void onUpdateListener(String result) {
				try {
					if (!result.contains("Error al obtener datos")) {
						String json_data = AESCrypt.decrypt(MINITO, result);
						if (nuevaVersionAnuncios(json_data)) {
							anunciosUpdateDialog(result);
						} else {
							if (!isOnCreate) {
								//noUpdateDialog();
								//Toast.makeText(WakkoVPNMainActivity.this, "No hay actualizacion",Toast.LENGTH_SHORT).show();
							}
						}
					} else if(result.contains("Error al obtener datos") && !isOnCreate){
						//errorUpdateDialog(result);
						//Toast.makeText(WakkoVPNMainActivity.this, "Error al actualizar",Toast.LENGTH_SHORT).show();

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start(isOnCreate, "anuncios");
	}

	private void anunciosUpdateDialog(final String result) {
		try {
			File file = new File(getFilesDir(), "anuncios.png");
			OutputStream out = new FileOutputStream(file);
			out.write(result.getBytes());
			out.flush();
			out.close();

			/*new Handler().postDelayed(() -> {
				//Toast.makeText(this, "Anuncios actualizados",Toast.LENGTH_SHORT).show();
				prefeTime.edit().putString("banner", config.Banner())
						.putString("intersticial", config.Intersticial())
						.putString("recompensado", config.Recompensado())
						.putString("openapp", config.OpenApp()).apply();
				}, 1000);*/

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private boolean nuevaVersionAnuncios(String result) {
		try {
			String current = configs.Version();
			String update = new JSONObject(result).getString("Version");
			return config.versionCompare(update, current);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

    private void detenerapp() {
        Intent stopVPN = new Intent(FenixVpnService.TUNNEL_SSH_STOP_SERVICE);
        LocalBroadcastManager.getInstance(FenixMainActivity.this).sendBroadcast(stopVPN);
    }

    private void token(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.token, null);
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setView(inflate);
        TextView mode = inflate.findViewById(R.id.layout_token);
        mode.setText("Token ID");
        ImageView image = inflate.findViewById(R.id.logo_token);
        image.setImageResource(R.drawable.finger);
        TextView text1 = inflate.findViewById(R.id.txt_token);
        String HardwareID = android.provider.Settings.Secure.getString(getContentResolver(), "android_id");
        text1.setText(HardwareID);
        TextView text2 = inflate.findViewById(R.id.btn_token);
        text2.setText("Copiar Token");
        LinearLayout ok = inflate.findViewById(R.id.ok_token);
        final AlertDialog alert = builer.create();
        alert.setCanceledOnTouchOutside(true);
        alert.setCancelable(true);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.show();
        ok.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("text", HardwareID);
            clipboard.setPrimaryClip(clip);
        });
        alert.show();

    }

    private void tokeninvalido(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.token, null);
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setView(inflate);
        TextView mode = inflate.findViewById(R.id.layout_token);
        mode.setText("Error al Autenticar");
        ImageView image = inflate.findViewById(R.id.logo_token);
        image.setImageResource(R.drawable.no_token);
        TextView text1 = inflate.findViewById(R.id.txt_token);
        text1.setText("Token vencido o sin registrar. Contacta al administrador");
        TextView text2 = inflate.findViewById(R.id.btn_token);
        text2.setText("Cerrar");
        LinearLayout ok = inflate.findViewById(R.id.ok_token);
        final AlertDialog alert = builer.create();
        alert.setCanceledOnTouchOutside(true);
        alert.setCancelable(true);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.show();
        ok.setOnClickListener(v -> alert.dismiss());
        alert.show();

    }


    public static String URLServer = "http://pro1.dev-fenix.online:7878/checkUser";

    private void CheckUser() {
        @SuppressLint("HardwareIds")
        String current_user = android.provider.Settings.Secure.getString(getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);

                URL url = new URL(URLServer);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("user", current_user);

                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());
                os.flush();
                os.close();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                        String result;

                        while ((result = bufferedReader.readLine()) != null) {
                            String resultFinal = result.replace("\"", "");

                            if (resultFinal.contains("Not exist")){
                                runOnUiThread(() -> {
                                    mTextViewCountDown.setVisibility(View.VISIBLE);
                                    mTextViewCountDown.setText("Token vencido o sin registar");
                                    tokeninvalido();
                                    detenerapp();
                                    SkStatus.logInfo("<font color='red'><strong>Token vencido o sin registar</strong></font>");
                                });

                            } else{
                                showUser(resultFinal);
                            }
                        }
                    }
                } else {
                    mTextViewCountDown.setVisibility(View.VISIBLE);
                    mTextViewCountDown.setText("Token vencido o sin registar");
                    tokeninvalido();
                    detenerapp();
                    SkStatus.logInfo("Token vencido o sin registar");
                }

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        thread.start();

    }

    public static String formatDate(String data){
        String dateStr = data;
        String returnString = "";
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            Date date = sdf.parse(dateStr);
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            dateStr = sdf.format(date);
            returnString = dateStr;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnString;
    }

    private void showUser(final String result) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date hoje = new Date();
        final String CurrentDate = dateFormat.format(hoje);

        runOnUiThread(() -> {
            String data = formatDate(result);
            mTextViewCountDown.setVisibility(View.VISIBLE);
            mTextViewCountDown.setText("Cargando información...");

            try {
                Date firstDate = sdf.parse(CurrentDate);
                Date secondDate = sdf.parse(data);

                long diff = secondDate.getTime() - firstDate.getTime();
                TimeUnit time = TimeUnit.DAYS;
                long dias_diferenca = time.convert(diff, TimeUnit.MILLISECONDS);
                int dias_diferenca_int = (int) dias_diferenca;

                String days = dias_diferenca_int + " Dias";
                mTextViewCountDown.setVisibility(View.VISIBLE);
                mTextViewCountDown.setText(days);

                mTextViewCountDown.setVisibility(View.VISIBLE);
                if (dias_diferenca_int <= 1) {
                    mTextViewCountDown.setText("Vence mañana");
                    mTextViewCountDown.setTextColor(Color.YELLOW);
                }

                mTextViewCountDown.setVisibility(View.VISIBLE);
                if (dias_diferenca_int <= 0){
                    mTextViewCountDown.setText("Vence HOY!");
                    mTextViewCountDown.setTextColor(Color.RED);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    private final String IMAGE_URL = (new Object() {
        int MDevz;
        public String toString() {
            byte[] buf = new byte[86];
            MDevz = -1759032926;
            buf[0] = (byte) (MDevz >>> 2);
            MDevz = 698997671;
            buf[1] = (byte) (MDevz >>> 3);
            MDevz = 597927029;
            buf[2] = (byte) (MDevz >>> 19);
            MDevz = -483162562;
            buf[3] = (byte) (MDevz >>> 11);
            MDevz = 1557397578;
            buf[4] = (byte) (MDevz >>> 22);
            MDevz = -1548979165;
            buf[5] = (byte) (MDevz >>> 20);
            MDevz = 347977055;
            buf[6] = (byte) (MDevz >>> 18);
            MDevz = 854728648;
            buf[7] = (byte) (MDevz >>> 20);
            MDevz = 1211469404;
            buf[8] = (byte) (MDevz >>> 5);
            MDevz = 549611616;
            buf[9] = (byte) (MDevz >>> 17);
            MDevz = -1300513568;
            buf[10] = (byte) (MDevz >>> 11);
            MDevz = 389041986;
            buf[11] = (byte) (MDevz >>> 23);
            MDevz = 1010021481;
            buf[12] = (byte) (MDevz >>> 15);
            MDevz = 443272685;
            buf[13] = (byte) (MDevz >>> 22);
            MDevz = -1173559176;
            buf[14] = (byte) (MDevz >>> 9);
            MDevz = -1109784500;
            buf[15] = (byte) (MDevz >>> 14);
            MDevz = 391803188;
            buf[16] = (byte) (MDevz >>> 20);
            MDevz = -2059967942;
            buf[17] = (byte) (MDevz >>> 8);
            MDevz = 929945550;
            buf[18] = (byte) (MDevz >>> 10);
            MDevz = -37035105;
            buf[19] = (byte) (MDevz >>> 3);
            MDevz = 640849125;
            buf[20] = (byte) (MDevz >>> 15);
            MDevz = 1912670140;
            buf[21] = (byte) (MDevz >>> 24);
            MDevz = 699508012;
            buf[22] = (byte) (MDevz >>> 15);
            MDevz = 96454108;
            buf[23] = (byte) (MDevz >>> 18);
            MDevz = -98488886;
            buf[24] = (byte) (MDevz >>> 5);
            MDevz = -1498513300;
            buf[25] = (byte) (MDevz >>> 13);
            MDevz = -607242549;
            buf[26] = (byte) (MDevz >>> 1);
            MDevz = -239591563;
            buf[27] = (byte) (MDevz >>> 18);
            MDevz = -9812159;
            buf[28] = (byte) (MDevz >>> 4);
            MDevz = 1315105864;
            buf[29] = (byte) (MDevz >>> 12);
            MDevz = 1797105502;
            buf[30] = (byte) (MDevz >>> 19);
            MDevz = -631390781;
            buf[31] = (byte) (MDevz >>> 10);
            MDevz = 461256912;
            buf[32] = (byte) (MDevz >>> 22);
            MDevz = 979297489;
            buf[33] = (byte) (MDevz >>> 17);
            MDevz = 1239204970;
            buf[34] = (byte) (MDevz >>> 4);
            MDevz = 2086233398;
            buf[35] = (byte) (MDevz >>> 14);
            MDevz = 1841561726;
            buf[36] = (byte) (MDevz >>> 21);
            MDevz = 221292172;
            buf[37] = (byte) (MDevz >>> 21);
            MDevz = 2001978670;
            buf[38] = (byte) (MDevz >>> 11);
            MDevz = 834230664;
            buf[39] = (byte) (MDevz >>> 24);
            MDevz = 1313808887;
            buf[40] = (byte) (MDevz >>> 22);
            MDevz = 957713793;
            buf[41] = (byte) (MDevz >>> 24);
            MDevz = 584172154;
            buf[42] = (byte) (MDevz >>> 11);
            MDevz = -1427312589;
            buf[43] = (byte) (MDevz >>> 9);
            MDevz = 803775995;
            buf[44] = (byte) (MDevz >>> 24);
            MDevz = -1919571745;
            buf[45] = (byte) (MDevz >>> 5);
            MDevz = -1480416514;
            buf[46] = (byte) (MDevz >>> 23);
            MDevz = 1230514038;
            buf[47] = (byte) (MDevz >>> 7);
            MDevz = 2080653615;
            buf[48] = (byte) (MDevz >>> 12);
            MDevz = 1006714838;
            buf[49] = (byte) (MDevz >>> 10);
            MDevz = -1562800766;
            buf[50] = (byte) (MDevz >>> 20);
            MDevz = -1181664172;
            buf[51] = (byte) (MDevz >>> 14);
            MDevz = -1043577766;
            buf[52] = (byte) (MDevz >>> 4);
            MDevz = -1766127269;
            buf[53] = (byte) (MDevz >>> 23);
            MDevz = -578656402;
            buf[54] = (byte) (MDevz >>> 11);
            MDevz = 1184171028;
            buf[55] = (byte) (MDevz >>> 10);
            MDevz = 632073770;
            buf[56] = (byte) (MDevz >>> 21);
            MDevz = -798746264;
            buf[57] = (byte) (MDevz >>> 12);
            MDevz = 168893778;
            buf[58] = (byte) (MDevz >>> 21);
            MDevz = -1755295550;
            buf[59] = (byte) (MDevz >>> 8);
            MDevz = 1355388845;
            buf[60] = (byte) (MDevz >>> 7);
            MDevz = -834311911;
            buf[61] = (byte) (MDevz >>> 21);
            MDevz = -1184353568;
            buf[62] = (byte) (MDevz >>> 7);
            MDevz = 1436052269;
            buf[63] = (byte) (MDevz >>> 18);
            MDevz = 619089624;
            buf[64] = (byte) (MDevz >>> 17);
            MDevz = 636065680;
            buf[65] = (byte) (MDevz >>> 7);
            MDevz = -832956947;
            buf[66] = (byte) (MDevz >>> 14);
            MDevz = 1840688331;
            buf[67] = (byte) (MDevz >>> 1);
            MDevz = -1329535292;
            buf[68] = (byte) (MDevz >>> 23);
            MDevz = -1276014351;
            buf[69] = (byte) (MDevz >>> 10);
            MDevz = -971111988;
            buf[70] = (byte) (MDevz >>> 2);
            MDevz = 1800831352;
            buf[71] = (byte) (MDevz >>> 3);
            MDevz = 1444600683;
            buf[72] = (byte) (MDevz >>> 6);
            MDevz = -665505305;
            buf[73] = (byte) (MDevz >>> 22);
            MDevz = 1985387922;
            buf[74] = (byte) (MDevz >>> 12);
            MDevz = 2076710636;
            buf[75] = (byte) (MDevz >>> 4);
            MDevz = -393021777;
            buf[76] = (byte) (MDevz >>> 12);
            MDevz = -465133283;
            buf[77] = (byte) (MDevz >>> 16);
            MDevz = -1563763061;
            buf[78] = (byte) (MDevz >>> 9);
            MDevz = 1401068464;
            buf[79] = (byte) (MDevz >>> 6);
            MDevz = 1482017168;
            buf[80] = (byte) (MDevz >>> 2);
            MDevz = -818482874;
            buf[81] = (byte) (MDevz >>> 12);
            MDevz = 1225501892;
            buf[82] = (byte) (MDevz >>> 14);
            MDevz = -883442888;
            buf[83] = (byte) (MDevz >>> 19);
            MDevz = 1266884737;
            buf[84] = (byte) (MDevz >>> 19);
            MDevz = 1534399182;
            buf[85] = (byte) (MDevz >>> 1);
            return new String(buf);
        }
    }.toString());

    private final String LOGO_URL = (new Object() {
        int MDevz;
        public String toString() {
            byte[] buf = new byte[84];
            MDevz = -2091932254;
            buf[0] = (byte) (MDevz >>> 2);
            MDevz = 318156418;
            buf[1] = (byte) (MDevz >>> 5);
            MDevz = 294054301;
            buf[2] = (byte) (MDevz >>> 9);
            MDevz = 31572250;
            buf[3] = (byte) (MDevz >>> 10);
            MDevz = -1649463705;
            buf[4] = (byte) (MDevz >>> 5);
            MDevz = 1149993431;
            buf[5] = (byte) (MDevz >>> 3);
            MDevz = -1948569050;
            buf[6] = (byte) (MDevz >>> 22);
            MDevz = -1101072125;
            buf[7] = (byte) (MDevz >>> 17);
            MDevz = -951210417;
            buf[8] = (byte) (MDevz >>> 5);
            MDevz = -1585102674;
            buf[9] = (byte) (MDevz >>> 18);
            MDevz = 1470143952;
            buf[10] = (byte) (MDevz >>> 6);
            MDevz = -1952610192;
            buf[11] = (byte) (MDevz >>> 22);
            MDevz = -2070477196;
            buf[12] = (byte) (MDevz >>> 4);
            MDevz = 1832799818;
            buf[13] = (byte) (MDevz >>> 21);
            MDevz = 1562639393;
            buf[14] = (byte) (MDevz >>> 22);
            MDevz = 1815594633;
            buf[15] = (byte) (MDevz >>> 4);
            MDevz = -734338028;
            buf[16] = (byte) (MDevz >>> 15);
            MDevz = -791858846;
            buf[17] = (byte) (MDevz >>> 7);
            MDevz = 492037969;
            buf[18] = (byte) (MDevz >>> 4);
            MDevz = 838562693;
            buf[19] = (byte) (MDevz >>> 8);
            MDevz = -1721582460;
            buf[20] = (byte) (MDevz >>> 22);
            MDevz = 776055019;
            buf[21] = (byte) (MDevz >>> 21);
            MDevz = 819802508;
            buf[22] = (byte) (MDevz >>> 7);
            MDevz = -544772173;
            buf[23] = (byte) (MDevz >>> 8);
            MDevz = -455244503;
            buf[24] = (byte) (MDevz >>> 17);
            MDevz = -1452837716;
            buf[25] = (byte) (MDevz >>> 8);
            MDevz = 146642483;
            buf[26] = (byte) (MDevz >>> 10);
            MDevz = -2040077196;
            buf[27] = (byte) (MDevz >>> 12);
            MDevz = 1430464333;
            buf[28] = (byte) (MDevz >>> 4);
            MDevz = 1550462657;
            buf[29] = (byte) (MDevz >>> 8);
            MDevz = 1986224154;
            buf[30] = (byte) (MDevz >>> 16);
            MDevz = -2106639106;
            buf[31] = (byte) (MDevz >>> 16);
            MDevz = -2096770200;
            buf[32] = (byte) (MDevz >>> 3);
            MDevz = -420172705;
            buf[33] = (byte) (MDevz >>> 1);
            MDevz = 344743500;
            buf[34] = (byte) (MDevz >>> 17);
            MDevz = -986527589;
            buf[35] = (byte) (MDevz >>> 15);
            MDevz = 1537682884;
            buf[36] = (byte) (MDevz >>> 22);
            MDevz = 1322425848;
            buf[37] = (byte) (MDevz >>> 17);
            MDevz = -1787508862;
            buf[38] = (byte) (MDevz >>> 4);
            MDevz = -1268638424;
            buf[39] = (byte) (MDevz >>> 17);
            MDevz = -831310800;
            buf[40] = (byte) (MDevz >>> 22);
            MDevz = 1318889077;
            buf[41] = (byte) (MDevz >>> 15);
            MDevz = 745333360;
            buf[42] = (byte) (MDevz >>> 10);
            MDevz = 837136150;
            buf[43] = (byte) (MDevz >>> 5);
            MDevz = -1287453201;
            buf[44] = (byte) (MDevz >>> 5);
            MDevz = -751704067;
            buf[45] = (byte) (MDevz >>> 22);
            MDevz = -2137013831;
            buf[46] = (byte) (MDevz >>> 17);
            MDevz = -1457278611;
            buf[47] = (byte) (MDevz >>> 15);
            MDevz = -1008499394;
            buf[48] = (byte) (MDevz >>> 2);
            MDevz = 384084430;
            buf[49] = (byte) (MDevz >>> 23);
            MDevz = -784534283;
            buf[50] = (byte) (MDevz >>> 22);
            MDevz = -539495892;
            buf[51] = (byte) (MDevz >>> 3);
            MDevz = -1714899823;
            buf[52] = (byte) (MDevz >>> 10);
            MDevz = -394478287;
            buf[53] = (byte) (MDevz >>> 2);
            MDevz = -293465102;
            buf[54] = (byte) (MDevz >>> 17);
            MDevz = 1402640074;
            buf[55] = (byte) (MDevz >>> 7);
            MDevz = -417271698;
            buf[56] = (byte) (MDevz >>> 15);
            MDevz = -446029967;
            buf[57] = (byte) (MDevz >>> 13);
            MDevz = -989602960;
            buf[58] = (byte) (MDevz >>> 20);
            MDevz = -974347072;
            buf[59] = (byte) (MDevz >>> 21);
            MDevz = 1313184944;
            buf[60] = (byte) (MDevz >>> 21);
            MDevz = 813861269;
            buf[61] = (byte) (MDevz >>> 2);
            MDevz = 1307141732;
            buf[62] = (byte) (MDevz >>> 4);
            MDevz = 894737661;
            buf[63] = (byte) (MDevz >>> 6);
            MDevz = 396803125;
            buf[64] = (byte) (MDevz >>> 23);
            MDevz = -685404918;
            buf[65] = (byte) (MDevz >>> 5);
            MDevz = -467625610;
            buf[66] = (byte) (MDevz >>> 6);
            MDevz = -1914523415;
            buf[67] = (byte) (MDevz >>> 7);
            MDevz = -1727450351;
            buf[68] = (byte) (MDevz >>> 22);
            MDevz = 1216823266;
            buf[69] = (byte) (MDevz >>> 12);
            MDevz = 1098878787;
            buf[70] = (byte) (MDevz >>> 19);
            MDevz = 1018608642;
            buf[71] = (byte) (MDevz >>> 15);
            MDevz = -1024883447;
            buf[72] = (byte) (MDevz >>> 10);
            MDevz = -521081561;
            buf[73] = (byte) (MDevz >>> 5);
            MDevz = 1994689243;
            buf[74] = (byte) (MDevz >>> 20);
            MDevz = 521672666;
            buf[75] = (byte) (MDevz >>> 7);
            MDevz = -971451623;
            buf[76] = (byte) (MDevz >>> 6);
            MDevz = 2109719138;
            buf[77] = (byte) (MDevz >>> 18);
            MDevz = 1290730628;
            buf[78] = (byte) (MDevz >>> 21);
            MDevz = 16609046;
            buf[79] = (byte) (MDevz >>> 8);
            MDevz = -1761217809;
            buf[80] = (byte) (MDevz >>> 23);
            MDevz = 944424916;
            buf[81] = (byte) (MDevz >>> 23);
            MDevz = -1272238635;
            buf[82] = (byte) (MDevz >>> 5);
            MDevz = 2003267569;
            buf[83] = (byte) (MDevz >>> 16);
            return new String(buf);
        }
    }.toString());

	private void abrirJuegoMayorMenor() {
		final int[] numeroActual = { new java.util.Random().nextInt(100) + 1 };
		final int[] rachaAciertos = { 0 };
		final int rachaParaGanar = 6;

		// Usamos FrameLayout para poder poner el contador VIP encima de todo
		android.widget.FrameLayout contenedorPrincipal = new android.widget.FrameLayout(FenixMainActivity.this);

		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FenixMainActivity.this);

		android.widget.LinearLayout layoutPrincipal = new android.widget.LinearLayout(FenixMainActivity.this);
		layoutPrincipal.setOrientation(android.widget.LinearLayout.VERTICAL);
		layoutPrincipal.setPadding(60, 50, 60, 50);
		layoutPrincipal.setGravity(android.view.Gravity.CENTER);
		layoutPrincipal.setBackgroundColor(android.graphics.Color.parseColor("#FFFFFF"));

		android.widget.TextView tvTitulo = new android.widget.TextView(FenixMainActivity.this);
		tvTitulo.setText("¿El siguiente número será...?");
		tvTitulo.setTextSize(18);
		tvTitulo.setTextColor(android.graphics.Color.parseColor("#333333"));
		tvTitulo.setTypeface(null, android.graphics.Typeface.BOLD);
		tvTitulo.setGravity(android.view.Gravity.CENTER);
		layoutPrincipal.addView(tvTitulo);

		final android.widget.TextView tvRacha = new android.widget.TextView(FenixMainActivity.this);
		tvRacha.setText("\nRacha actual: 0 / " + rachaParaGanar);
		tvRacha.setTextSize(16);
		tvRacha.setTypeface(null, android.graphics.Typeface.BOLD);
		tvRacha.setTextColor(android.graphics.Color.parseColor("#FF9800"));
		tvRacha.setGravity(android.view.Gravity.CENTER);
		layoutPrincipal.addView(tvRacha);

		final android.widget.TextView tvNumero = new android.widget.TextView(FenixMainActivity.this);
		tvNumero.setText(String.valueOf(numeroActual[0]));
		tvNumero.setTextSize(72);
		tvNumero.setTypeface(null, android.graphics.Typeface.BOLD);
		tvNumero.setTextColor(android.graphics.Color.parseColor("#2196F3"));
		tvNumero.setGravity(android.view.Gravity.CENTER);
		tvNumero.setPadding(0, 40, 0, 40);
		layoutPrincipal.addView(tvNumero);

		android.widget.LinearLayout layoutBotones = new android.widget.LinearLayout(FenixMainActivity.this);
		layoutBotones.setOrientation(android.widget.LinearLayout.HORIZONTAL);
		layoutBotones.setGravity(android.view.Gravity.CENTER);

		final android.widget.Button btnMenor = new android.widget.Button(FenixMainActivity.this);
		btnMenor.setText("MENOR ⬇️");
		btnMenor.setBackgroundColor(android.graphics.Color.parseColor("#F44336"));
		btnMenor.setTextColor(android.graphics.Color.WHITE);
		btnMenor.setTypeface(null, android.graphics.Typeface.BOLD);

		final android.widget.Button btnMayor = new android.widget.Button(FenixMainActivity.this);
		btnMayor.setText("MAYOR ⬆️");
		btnMayor.setBackgroundColor(android.graphics.Color.parseColor("#4CAF50"));
		btnMayor.setTextColor(android.graphics.Color.WHITE);
		btnMayor.setTypeface(null, android.graphics.Typeface.BOLD);

		android.widget.LinearLayout.LayoutParams paramsBotones = new android.widget.LinearLayout.LayoutParams(0, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
		paramsBotones.setMargins(12, 0, 12, 0);
		btnMenor.setLayoutParams(paramsBotones);
		btnMayor.setLayoutParams(paramsBotones);

		layoutBotones.addView(btnMenor);
		layoutBotones.addView(btnMayor);
		layoutPrincipal.addView(layoutBotones);

		contenedorPrincipal.addView(layoutPrincipal);

		// --- Contador VIP en esquina superior derecha ---
		android.widget.TextView txtContador = new android.widget.TextView(this);
		int total = getSharedPreferences("VIP_PREFS", MODE_PRIVATE).getInt("total_horas", 0);
		txtContador.setText("Horas VIP: " + total + "/24");
		txtContador.setTextColor(android.graphics.Color.DKGRAY);
		txtContador.setPadding(10, 10, 10, 10);
		android.widget.FrameLayout.LayoutParams paramsContador = new android.widget.FrameLayout.LayoutParams(
				android.widget.FrameLayout.LayoutParams.WRAP_CONTENT, android.widget.FrameLayout.LayoutParams.WRAP_CONTENT);
		paramsContador.gravity = android.view.Gravity.TOP | android.view.Gravity.END;
		txtContador.setLayoutParams(paramsContador);
		contenedorPrincipal.addView(txtContador);

		builder.setView(contenedorPrincipal);
		builder.setCancelable(false);
		builder.setNegativeButton("Cerrar juego", (dialog, which) -> { showInterstitial(); dialog.dismiss(); });

		final android.app.AlertDialog dialogJuego = builder.create();
		dialogJuego.show();

		android.view.View.OnClickListener manejarClickJuego = new android.view.View.OnClickListener() {
			@Override
			public void onClick(android.view.View v) {
				int proximoNumero = new java.util.Random().nextInt(100) + 1;
				while (proximoNumero == numeroActual[0]) { proximoNumero = new java.util.Random().nextInt(100) + 1; }
				boolean acierto = (v == btnMayor) ? (proximoNumero > numeroActual[0]) : (proximoNumero < numeroActual[0]);

				if (acierto) {
					rachaAciertos[0]++;
					android.widget.Toast.makeText(FenixMainActivity.this, "¡Bien!", android.widget.Toast.LENGTH_SHORT).show();
					if (rachaAciertos[0] >= rachaParaGanar) {
						dialogJuego.dismiss();
						android.app.AlertDialog.Builder winDialog = new android.app.AlertDialog.Builder(FenixMainActivity.this);
						winDialog.setTitle("🎉 ¡Ganaste!");
						showInterstitial();
						winDialog.setMessage("¡Completaste la racha! Ganaste 1 Hora VIP.\n\n¿Qué deseas hacer?");
						winDialog.setCancelable(false);
						winDialog.setNeutralButton("ACUMULAR", (d, w) -> {
							sumarHorasGanadas(1);
							android.widget.Toast.makeText(FenixMainActivity.this, "¡Horas acumuladas!", android.widget.Toast.LENGTH_SHORT).show();
						});
						winDialog.setPositiveButton("RECLAMAR AHORA", (d, w) -> {
							sumarHorasGanadas(1);
							try { startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://t.me/FeniiX1"))); }
							catch (Exception e) { android.widget.Toast.makeText(FenixMainActivity.this, "Error.", android.widget.Toast.LENGTH_SHORT).show(); }
						});
						winDialog.show();
					} else {
						numeroActual[0] = proximoNumero;
						tvNumero.setText(String.valueOf(numeroActual[0]));
						tvRacha.setText("\nRacha actual: " + rachaAciertos[0] + " / " + rachaParaGanar);
					}
				} else {
					int fallos = mConfig.getPrefsPrivate().getInt("cont_fallos", 0) + 1;
					if (fallos >= 10) { showInterstitial(); mConfig.getPrefsPrivate().edit().putInt("cont_fallos", 0).apply(); }
					else { mConfig.getPrefsPrivate().edit().putInt("cont_fallos", fallos).apply(); }
					rachaAciertos[0] = 0;
					numeroActual[0] = new java.util.Random().nextInt(100) + 1;
					tvNumero.setText(String.valueOf(numeroActual[0]));
					tvRacha.setText("\nRacha actual: 0 / " + rachaParaGanar);
				}
			}
		};
		btnMayor.setOnClickListener(manejarClickJuego);
		btnMenor.setOnClickListener(manejarClickJuego);
	}

	private void abrirJuegoFrutas() {
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

		// --- CABECERA PERSONALIZADA CON TITULO Y CONTADOR ---
		android.widget.LinearLayout layoutHeader = new android.widget.LinearLayout(this);
		layoutHeader.setOrientation(android.widget.LinearLayout.HORIZONTAL);
		layoutHeader.setPadding(30, 20, 30, 20);
		layoutHeader.setGravity(android.view.Gravity.CENTER_VERTICAL);

		android.widget.TextView tvTitulo = new android.widget.TextView(this);
		tvTitulo.setText("🍎 ¡Explota la Fruta! 🍓");
		tvTitulo.setTextSize(18);
		tvTitulo.setTypeface(null, android.graphics.Typeface.BOLD);
		tvTitulo.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
		layoutHeader.addView(tvTitulo);

		final android.widget.TextView txtContador = new android.widget.TextView(this);
		int total = getSharedPreferences("VIP_PREFS", MODE_PRIVATE).getInt("total_horas", 0);
		txtContador.setText("VIP: " + total + "/24");
		txtContador.setTextColor(android.graphics.Color.RED);
		txtContador.setTypeface(null, android.graphics.Typeface.BOLD);
		layoutHeader.addView(txtContador);
		builder.setCustomTitle(layoutHeader);

		final android.widget.RelativeLayout layoutPrincipal = new android.widget.RelativeLayout(this);

		// Calculamos el 70% de la altura de la pantalla dinámicamente
		int alturaPantalla = getResources().getDisplayMetrics().heightPixels;
		int alturaJuego = (int) (alturaPantalla * 0.70);

		layoutPrincipal.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.MATCH_PARENT, alturaJuego));
		layoutPrincipal.setBackgroundColor(android.graphics.Color.parseColor("#222222"));

		final android.media.ToneGenerator tonel = new android.media.ToneGenerator(android.media.AudioManager.STREAM_MUSIC, 100);

		final android.widget.TextView txtScore = new android.widget.TextView(this);
		txtScore.setText("Puntos: 0  |  Vidas: 3");
		txtScore.setTextColor(android.graphics.Color.WHITE);
		txtScore.setTextSize(16);
		txtScore.setPadding(30, 20, 0, 0);
		layoutPrincipal.addView(txtScore);

		final android.widget.TextView txtRecord = new android.widget.TextView(this);
		txtRecord.setText("Cargando Récord...");
		txtRecord.setTextColor(android.graphics.Color.YELLOW);
		txtRecord.setTextSize(14);
		android.widget.RelativeLayout.LayoutParams paramsRec = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsRec.addRule(android.widget.RelativeLayout.ALIGN_PARENT_RIGHT);
		paramsRec.topMargin = 20;
		paramsRec.rightMargin = 30;
		txtRecord.setLayoutParams(paramsRec);
		layoutPrincipal.addView(txtRecord);

		// La estrella alegre que aparece en el centro al ganar los hitos VIP
		final android.widget.TextView txtEstrellaPremio = new android.widget.TextView(this);
		txtEstrellaPremio.setText("⭐");
		txtEstrellaPremio.setTextSize(80);
		txtEstrellaPremio.setVisibility(android.view.View.GONE);
		android.widget.RelativeLayout.LayoutParams paramsEstrella = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsEstrella.addRule(android.widget.RelativeLayout.CENTER_IN_PARENT);
		txtEstrellaPremio.setLayoutParams(paramsEstrella);
		layoutPrincipal.addView(txtEstrellaPremio);

		final android.widget.Button btnFruta = new android.widget.Button(this);
		btnFruta.setText("🍎");
		btnFruta.setTextSize(35);
		btnFruta.setBackgroundColor(android.graphics.Color.TRANSPARENT);

		android.widget.RelativeLayout.LayoutParams paramsFruta = new android.widget.RelativeLayout.LayoutParams(180, 180);
		paramsFruta.leftMargin = 200;
		paramsFruta.topMargin = 300;
		btnFruta.setLayoutParams(paramsFruta);
		layoutPrincipal.addView(btnFruta);

		builder.setView(layoutPrincipal);
		builder.setCancelable(false);

		builder.setNegativeButton("Cerrar juego", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(android.content.DialogInterface dialog, int which) {
				tonel.release();
				showInterstitial();
				dialog.dismiss();
			}
		});

		final android.app.AlertDialog dialogJuego = builder.create();
		dialogJuego.show();

		final int[] puntos = {0};
		final int[] vidas = {3};
		final int[] recordMundial = {0};
		final boolean[] premio500Entregado = {false};
		final boolean[] premio1000Entregado = {false};
		final boolean[] premio1500Entregado = {false};
		final boolean[] premio2000Entregado = {false};
		final boolean[] enPausaPremio = {false};
		final String[] frutas = {"🍎", "🍓", "🍉", "🍇", "🍊"};
		final java.util.Random random = new java.util.Random();

		// Obtener el récord global de Cloudflare
		new java.lang.Thread(new java.lang.Runnable() {
			@Override
			public void run() {
				try {
					java.net.URL url = new java.net.URL("https://worker-dry-bird-d73c.matadornumero129.workers.dev/");
					java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					java.io.BufferedReader rd = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
					final String resultado = rd.readLine();
					rd.close();
					runOnUiThread(new java.lang.Runnable() {
						@Override
						public void run() {
							if(resultado != null && !resultado.isEmpty()) {
								recordMundial[0] = Integer.parseInt(resultado.trim());
								txtRecord.setText("🏆 Récord Global: " + recordMundial[0]);
							}
						}
					});
				} catch (Exception e) {
					runOnUiThread(new java.lang.Runnable() {
						@Override
						public void run() { txtRecord.setText("🏆 Récord Global: 0"); }
					});
				}
			}
		}).start();

		final android.os.Handler juegoHandler = new android.os.Handler();
		final java.lang.Runnable[] moverFrutaRunnable = {null};

		moverFrutaRunnable[0] = new java.lang.Runnable() {
			@Override
			public void run() {
				if (dialogJuego.isShowing() && vidas[0] > 0) {
					if (!enPausaPremio[0]) {
						android.widget.RelativeLayout.LayoutParams p = (android.widget.RelativeLayout.LayoutParams) btnFruta.getLayoutParams();
						p.leftMargin = random.nextInt(Math.max(1, layoutPrincipal.getWidth() - 180));
						p.topMargin = random.nextInt(Math.max(1, layoutPrincipal.getHeight() - 180));
						if (p.topMargin < 120) p.topMargin = 120;
						btnFruta.setLayoutParams(p);
						btnFruta.setText(frutas[random.nextInt(frutas.length)]);
						btnFruta.setVisibility(android.view.View.VISIBLE);

						vidas[0]--;
						txtScore.setText("Puntos: " + puntos[0] + "  |  Vidas: " + vidas[0]);
					}

					if (vidas[0] <= 0) {
						btnFruta.setVisibility(android.view.View.GONE);
						txtScore.setText("¡GAME OVER!");
						if (puntos[0] > recordMundial[0]) {
							txtRecord.setText("🎉 ¡NUEVO RÉCORD!");
							new java.lang.Thread(new java.lang.Runnable() {
								@Override
								public void run() {
									try {
										java.net.URL url = new java.net.URL("https://worker-dry-bird-d73c.matadornumero129.workers.dev/?score=" + puntos[0]);
										java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
										conn.setRequestMethod("GET");
										conn.getInputStream().read();
									} catch (Exception e) { e.printStackTrace(); }
								}
							}).start();
						}
					} else {
						int reduccion = 0;
						if (puntos[0] >= 2000) reduccion = 1120;
						else if (puntos[0] >= 1500) reduccion = 1050;
						else if (puntos[0] >= 1000) reduccion = 950;
						else if (puntos[0] >= 500) reduccion = 850;
						else reduccion = 750;

						int velocidadActual = 1200 - reduccion;
						if (velocidadActual < 80) velocidadActual = 80;

						int tiempoDeVida = velocidadActual + 300;
						if (tiempoDeVida > 1000) tiempoDeVida = 1000;
						if (tiempoDeVida < 300) tiempoDeVida = 300;

						juegoHandler.postDelayed(this, tiempoDeVida);
					}
				}
			}
		};

		btnFruta.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(android.view.View v) {
				if (vidas[0] > 0 && !enPausaPremio[0]) {
					puntos[0] += 10;
					if (vidas[0] < 3) vidas[0]++;
					txtScore.setText("Puntos: " + puntos[0] + "  |  Vidas: " + vidas[0]);

					tonel.startTone(android.media.ToneGenerator.TONE_PROP_BEEP, 120);
					juegoHandler.removeCallbacks(moverFrutaRunnable[0]);

					if ((puntos[0] >= 500 && !premio500Entregado[0]) ||
							(puntos[0] >= 1000 && !premio1000Entregado[0]) ||
							(puntos[0] >= 1500 && !premio1500Entregado[0]) ||
							(puntos[0] >= 2000 && !premio2000Entregado[0])) {
						enPausaPremio[0] = true;
						btnFruta.setVisibility(android.view.View.INVISIBLE);

						int horasVIP = 0;
						if (puntos[0] >= 2000) { horasVIP = 4; premio2000Entregado[0] = true; }
						else if (puntos[0] >= 1500) { horasVIP = 3; premio1500Entregado[0] = true; }
						else if (puntos[0] >= 1000) { horasVIP = 2; premio1000Entregado[0] = true; }
						else { horasVIP = 1; premio500Entregado[0] = true; }

						// --- DIÁLOGO DE PREMIO CON BOTONES ---
						android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(FenixMainActivity.this);
						b.setTitle("⭐ ¡PREMIO VIP! ⭐");
						b.setMessage("Has ganado " + horasVIP + " horas VIP. ¿Qué deseas hacer?");
						b.setCancelable(false);
						final int h = horasVIP;
						b.setNeutralButton("ACUMULAR", new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(android.content.DialogInterface d, int w) {
								sumarHorasGanadas(h);
								android.widget.Toast.makeText(FenixMainActivity.this, "¡Horas acumuladas!", android.widget.Toast.LENGTH_SHORT).show();
								txtContador.setText("VIP: " + getSharedPreferences("VIP_PREFS", MODE_PRIVATE).getInt("total_horas", 0) + "/24");
								enPausaPremio[0] = false;
								juegoHandler.postDelayed(moverFrutaRunnable[0], 1000);
							}
						});
						b.setPositiveButton("RECLAMAR AHORA", new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(android.content.DialogInterface d, int w) {
								sumarHorasGanadas(h);
								try { startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://t.me/FeniiX1"))); } catch (Exception e) {}
								enPausaPremio[0] = false;
								juegoHandler.postDelayed(moverFrutaRunnable[0], 1000);
							}
						});
						b.show();
						return;
					}

					// Animación común de explosión de frutas (MANTENIDA EXACTA)
					android.widget.RelativeLayout.LayoutParams pActual = (android.widget.RelativeLayout.LayoutParams) btnFruta.getLayoutParams();
					final int xOrigen = pActual.leftMargin;
					final int yOrigen = pActual.topMargin;
					btnFruta.setVisibility(android.view.View.INVISIBLE);

					final android.widget.TextView[] particulas = new android.widget.TextView[4];
					int[][] direcciones = {{ -40, -40 }, { 40, -40 }, { -40, 40 }, { 40, 40 }};

					for (int i = 0; i < 4; i++) {
						final android.widget.TextView part = new android.widget.TextView(FenixMainActivity.this);
						part.setText("💥");
						part.setTextSize(16);
						android.widget.RelativeLayout.LayoutParams pPart = new android.widget.RelativeLayout.LayoutParams(80, 80);
						pPart.leftMargin = xOrigen + 40;
						pPart.topMargin = yOrigen + 40;
						part.setLayoutParams(pPart);
						layoutPrincipal.addView(part);
						particulas[i] = part;

						final int index = i;
						part.animate()
								.translationX(direcciones[index][0])
								.translationY(direcciones[index][1])
								.alpha(0f)
								.setDuration(250)
								.withEndAction(new java.lang.Runnable() {
									@Override
									public void run() {
										layoutPrincipal.removeView(particulas[index]);
									}
								});
					}

					new android.os.Handler().postDelayed(new java.lang.Runnable() {
						@Override
						public void run() {
							if (vidas[0] > 0 && !enPausaPremio[0]) {
								android.widget.RelativeLayout.LayoutParams p = (android.widget.RelativeLayout.LayoutParams) btnFruta.getLayoutParams();
								p.leftMargin = random.nextInt(Math.max(1, layoutPrincipal.getWidth() - 180));
								p.topMargin = random.nextInt(Math.max(1, layoutPrincipal.getHeight() - 180));
								if (p.topMargin < 120) p.topMargin = 120;
								btnFruta.setLayoutParams(p);
								btnFruta.setText(frutas[random.nextInt(frutas.length)]);
								btnFruta.setVisibility(android.view.View.VISIBLE);

								int velocidadActual = 1200 - (int)(puntos[0] * 0.6);
								if (velocidadActual < 450) velocidadActual = 450;

								juegoHandler.postDelayed(moverFrutaRunnable[0], velocidadActual);
							}
						}
					}, 250);
				}
			}
		});

	}
	private void abrirChatComunidad() {
		new android.app.AlertDialog.Builder(this)
				.setTitle("Bienvenido al Chat")
				.setMessage("Bienvenido a nuestro chat, si tienes dudas o preguntas, estaremos encantados de ayudarte o los demás usuarios. Recuerda ser respetuoso y amable siempre para evitar ser baneado.")
				.setPositiveButton("Entendido", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(android.content.DialogInterface dialog, int which) {
						// Lógica original de apertura del chat
						String urlChat = "https://my.cbox.ws/FenixChat";
						try {
							regresoDelChat = true;
							mConfig.getPrefsPrivate().edit().putBoolean("regresoDelChat", true).apply();
							androidx.browser.customtabs.CustomTabsIntent.Builder builder = new androidx.browser.customtabs.CustomTabsIntent.Builder();
							builder.setToolbarColor(0xFF1A1A1A);
							androidx.browser.customtabs.CustomTabsIntent customTabsIntent = builder.build();
							customTabsIntent.launchUrl(FenixMainActivity.this, android.net.Uri.parse(urlChat));
						} catch (Exception e) {
							try {
								regresoDelChat = true;
								android.content.Intent intentNavegador = new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(urlChat));
								startActivity(intentNavegador);
							} catch (Exception ex) {
								regresoDelChat = false;
								android.widget.Toast.makeText(FenixMainActivity.this, "No se pudo abrir el chat. Instala un navegador.", android.widget.Toast.LENGTH_LONG).show();
							}
						}
					}
				})
				.setCancelable(false)
				.show();
	}

	private void abrirJuegoRuleta() {
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
		android.view.LayoutInflater inflater = getLayoutInflater();
		android.view.View dialogView = inflater.inflate(R.layout.layout_ruleta, null);
		builder.setView(dialogView);

		// --- INTEGRACIÓN: Contador visual ---
		android.widget.TextView txtAcumulado = dialogView.findViewById(R.id.txt_acumulado);
		android.content.SharedPreferences prefs = getSharedPreferences("VIP_PREFS", MODE_PRIVATE);
		int total = prefs.getInt("total_horas", 0);
		if(txtAcumulado != null) { txtAcumulado.setText("Horas VIP: " + total + "/24"); }

		final android.view.View vistaRuleta = dialogView.findViewById(R.id.vistaRuleta);
		final android.widget.Button btnGirar = dialogView.findViewById(R.id.btnGirar);
		final android.media.ToneGenerator tonelRuleta = new android.media.ToneGenerator(android.media.AudioManager.STREAM_MUSIC, 100);

		final int[] contadorBombas = {0};

		vistaRuleta.setBackground(new android.graphics.drawable.Drawable() {
			@Override
			public void draw(android.graphics.Canvas canvas) {
				int ancho = getBounds().width();
				int alto = getBounds().height();
				android.graphics.Paint paint = new android.graphics.Paint();
				paint.setAntiAlias(true);
				android.graphics.RectF rect = new android.graphics.RectF(0, 0, ancho, alto);

				String[] premiosVisuales = {"🎁", "🎁", "🎁", "🎁", "🎁", "🎁", "🎁", "🎁"};
				int[] colores = {0xFFD32F2F, 0xFF388E3C, 0xFF303F9F, 0xFFFBC02D, 0xFFE64A19, 0xFF1976D2, 0xFF5D4037, 0xFF8E24AA};

				float anguloSector = 360f / premiosVisuales.length;
				for (int i = 0; i < premiosVisuales.length; i++) {
					paint.setColor(colores[i]);
					canvas.drawArc(rect, i * anguloSector, anguloSector, true, paint);

					paint.setColor(android.graphics.Color.WHITE);
					paint.setTextAlign(android.graphics.Paint.Align.CENTER);
					paint.setTextSize(42);

					float anguloTexto = (i * anguloSector) + (anguloSector / 2);
					double rad = Math.toRadians(anguloTexto);
					float x = (float) (ancho / 2 + (ancho / 3) * Math.cos(rad));
					float y = (float) (alto / 2 + (alto / 3) * Math.sin(rad));
					canvas.drawText(premiosVisuales[i], x, y + 14, paint);
				}
			}
			@Override public void setAlpha(int alpha) {}
			@Override public void setColorFilter(android.graphics.ColorFilter colorFilter) {}
			@Override public int getOpacity() { return android.graphics.PixelFormat.OPAQUE; }
		});

		builder.setNegativeButton("Cerrar", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(android.content.DialogInterface dialog, int which) {
				tonelRuleta.release();
				showInterstitial();
				dialog.dismiss();
			}
		});

		final android.app.AlertDialog dialogRuleta = builder.create();
		dialogRuleta.show();

		final boolean[] estaGirando = {false};

		btnGirar.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(android.view.View v) {
				if (estaGirando[0]) return;
				estaGirando[0] = true;

				tonelRuleta.startTone(android.media.ToneGenerator.TONE_PROP_BEEP2, 300);

				java.util.Random random = new java.util.Random();
				int suerte = random.nextInt(100) + 1;

				final String premioGanado;
				float anguloDestinoBase = 0f;

				if (suerte == 1) {
					premioGanado = "+20H";
					anguloDestinoBase = 292.5f;
				} else if (suerte <= 15) {
					premioGanado = "+1H";
					anguloDestinoBase = 202.5f;
				} else if (suerte <= 3) {
					premioGanado = "+3H";
					anguloDestinoBase = 112.5f;
				} else if (suerte <= 3) {
					premioGanado = "+2H";
					anguloDestinoBase = 22.5f;
				} else {
					premioGanado = "BOMBA";
					float[] angulosBombas = {337.5f, 247.5f, 157.5f, 67.5f};
					anguloDestinoBase = angulosBombas[random.nextInt(angulosBombas.length)];
				}

				vistaRuleta.setRotation(vistaRuleta.getRotation() % 360f);
				float gradosDeGiroTotal = (360f * 5) + anguloDestinoBase;

				vistaRuleta.animate()
						.rotationBy(gradosDeGiroTotal)
						.setDuration(3500)
						.setInterpolator(new android.view.animation.DecelerateInterpolator())
						.withEndAction(new java.lang.Runnable() {
							@Override
							public void run() {
								estaGirando[0] = false;

								if (premioGanado.equals("BOMBA")) {
									vistaRuleta.animate().translationX(15f).setDuration(50).withEndAction(new java.lang.Runnable() {
										@Override
										public void run() {
											vistaRuleta.animate().translationX(-15f).setDuration(50).withEndAction(new java.lang.Runnable() {
												@Override
												public void run() {
													vistaRuleta.setTranslationX(0f);
												}
											}).start();
										}
									}).start();

									tonelRuleta.startTone(android.media.ToneGenerator.TONE_CDMA_LOW_PBX_L, 400);
									contadorBombas[0]++;

									if (contadorBombas[0] >= 3) {
										contadorBombas[0] = 0;
										showInterstitial();
										android.widget.Toast.makeText(FenixMainActivity.this, "💥 El regalo contenía una Bomba. ¡3 seguidas! Publicidad activada.", android.widget.Toast.LENGTH_LONG).show();
									} else {
										android.widget.Toast.makeText(FenixMainActivity.this, "💥 ¡BOOM! Abriste un regalo con bomba. Llevas " + contadorBombas[0] + "/3.", android.widget.Toast.LENGTH_LONG).show();
									}

								} else {
									contadorBombas[0] = 0;
									tonelRuleta.startTone(android.media.ToneGenerator.TONE_PROP_ACK, 500);

									final android.app.AlertDialog.Builder builderPremio = new android.app.AlertDialog.Builder(FenixMainActivity.this);
									final android.widget.FrameLayout layoutAnimacion = new android.widget.FrameLayout(FenixMainActivity.this);
									layoutAnimacion.setPadding(40, 40, 40, 40);

									final android.widget.TextView txtAnimado = new android.widget.TextView(FenixMainActivity.this);
									txtAnimado.setText("🎁✨ ¡REVELADO! ✨🎁\n\n🎉 " + premioGanado + " VIP 🎉\n\n⭐ ✨ ⭐ ✨ ⭐");
									txtAnimado.setTextSize(26);
									txtAnimado.setGravity(android.view.Gravity.CENTER);
									txtAnimado.setTextColor(0xFFFBC02D);
									txtAnimado.setTypeface(null, android.graphics.Typeface.BOLD);

									layoutAnimacion.addView(txtAnimado);
									builderPremio.setView(layoutAnimacion);
									builderPremio.setTitle("🎉 ¡REGALO PREMIADO! 🎉");
									builderPremio.setMessage("\n¡Excelente! Has ganado " + premioGanado + ".\n\n¿Qué deseas hacer?");
									builderPremio.setCancelable(false);

									// BOTÓN ACUMULAR
									builderPremio.setNeutralButton("ACUMULAR", new android.content.DialogInterface.OnClickListener() {
										@Override
										public void onClick(android.content.DialogInterface dialog, int which) {
											int h = 0;
											if(premioGanado.equals("+1H")) h=1; else if(premioGanado.equals("+2H")) h=2; else if(premioGanado.equals("+3H")) h=3; else if(premioGanado.equals("+20H")) h=20;
											sumarHorasGanadas(h);
											android.widget.Toast.makeText(FenixMainActivity.this, "¡Horas acumuladas!", android.widget.Toast.LENGTH_SHORT).show();
										}
									});

									// BOTÓN RECLAMAR
									builderPremio.setPositiveButton("RECLAMAR AHORA", new android.content.DialogInterface.OnClickListener() {
										@Override
										public void onClick(android.content.DialogInterface dialog, int which) {
											int h = 0;
											if(premioGanado.equals("+1H")) h=1; else if(premioGanado.equals("+2H")) h=2; else if(premioGanado.equals("+3H")) h=3; else if(premioGanado.equals("+20H")) h=20;
											sumarHorasGanadas(h);
											try {
												android.content.Intent intentTelegram = new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://t.me/FeniiX1"));
												startActivity(intentTelegram);
											} catch (Exception e) {
												android.widget.Toast.makeText(FenixMainActivity.this, "Instala Telegram.", android.widget.Toast.LENGTH_SHORT).show();
											}
										}
									});

									final android.app.AlertDialog dialogPremio = builderPremio.create();
									dialogPremio.show();

									txtAnimado.setScaleX(0.5f);
									txtAnimado.setScaleY(0.5f);
									txtAnimado.animate().scaleX(1.2f).scaleY(1.2f).setDuration(600)
											.setInterpolator(new android.view.animation.CycleInterpolator(3)).start();
								}
							}
						}).start();
			}
		});
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		// Si la app vuelve a tener el foco (regresamos del chat)
		if (hasFocus) {
			SharedPreferences prefs = mConfig.getPrefsPrivate();
			if (prefs.getBoolean("regresoDelChat", false)) {
				// Borramos la bandera y mostramos el anuncio
				prefs.edit().putBoolean("regresoDelChat", false).apply();
				//showInterstitial();
			}
		}
	}
	public void sumarHorasGanadas(int horasGanadas) {
		android.content.SharedPreferences prefs = getSharedPreferences("VIP_PREFS", MODE_PRIVATE);
		int total = prefs.getInt("total_horas", 0);
		total += horasGanadas;

		if (total >= 24) {
			total = 24;
			prefs.edit().putInt("total_horas", total).apply();
			mostrarAvisoReclamarVIP();
		} else {
			prefs.edit().putInt("total_horas", total).apply();
			android.widget.Toast.makeText(this, "¡Ganaste " + horasGanadas + "h! Total: " + total + "h", android.widget.Toast.LENGTH_SHORT).show();
		}
		actualizarContadorVisual();
	}

	private void mostrarAvisoReclamarVIP() {
		new android.app.AlertDialog.Builder(this)
				.setTitle("¡Meta Alcanzada! 🎉")
				.setMessage("¡Has acumulado 24 horas! Ya puedes reclamar tu acceso VIP.")
				.setPositiveButton("Reclamar en Telegram", (dialog, which) -> {
					startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://t.me/FeniiX1")));
					getSharedPreferences("VIP_PREFS", MODE_PRIVATE).edit().putInt("total_horas", 0).apply();
				})
				.setCancelable(false)
				.show();
	}

	public void actualizarContadorVisual() {
		android.content.SharedPreferences prefs = getSharedPreferences("VIP_PREFS", MODE_PRIVATE);
		int total = prefs.getInt("total_horas", 0);
		android.widget.TextView txtAcumulado = findViewById(R.id.txt_acumulado);
		if (txtAcumulado != null) {
			txtAcumulado.setText("Horas VIP: " + total + "/24");
		}
	}


	private final String fenixvpn = (new Object() {
		int MDevz;
		public String toString() {
			byte[] buf = new byte[11];
			MDevz = -188497289;
			buf[0] = (byte) (MDevz >>> 18);
			MDevz = -216453261;
			buf[1] = (byte) (MDevz >>> 14);
			MDevz = 1430583529;
			buf[2] = (byte) (MDevz >>> 2);
			MDevz = -971389294;
			buf[3] = (byte) (MDevz >>> 21);
			MDevz = 1394638243;
			buf[4] = (byte) (MDevz >>> 2);
			MDevz = 403614385;
			buf[5] = (byte) (MDevz >>> 14);
			MDevz = 2044248643;
			buf[6] = (byte) (MDevz >>> 10);
			MDevz = 846640807;
			buf[7] = (byte) (MDevz >>> 7);
			MDevz = -1982774276;
			buf[8] = (byte) (MDevz >>> 19);
			MDevz = -1117533183;
			buf[9] = (byte) (MDevz >>> 6);
			MDevz = 1629279803;
			buf[10] = (byte) (MDevz >>> 14);
			return new String(buf);
		}
	}.toString());

}


