package com.fenix.sshservice.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.util.Log;


import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.fenix.sshservice.FenixVpnService;
import com.fenix.vpn.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SkProtect {

	private static final String TAG = SkProtect.class.getSimpleName();

	private static final String oOoOoOo应用程序OoOo包名称 = new Object() {
		int MDevz;
		public String toString() {
			byte[] buf = new byte[13];
			MDevz = -1278062;
			buf[0] = (byte) (MDevz >>> 13);
			MDevz = -75497560;
			buf[1] = (byte) (MDevz >>> 19);
			MDevz = -18700;
			buf[2] = (byte) (MDevz >>> 7);
			MDevz = -1712147;
			buf[3] = (byte) (MDevz >>> 13);
			MDevz = 427819071;
			buf[4] = (byte) (MDevz >>> 22);
			MDevz = 813;
			buf[5] = (byte) (MDevz >>> 3);
			MDevz = 3604494;
			buf[6] = (byte) (MDevz >>> 15);
			MDevz = -614466;
			buf[7] = (byte) (MDevz >>> 12);
			MDevz = -2211956;
			buf[8] = (byte) (MDevz >>> 14);
			MDevz = -26763;
			buf[9] = (byte) (MDevz >>> 7);
			MDevz = -574619669;
			buf[10] = (byte) (MDevz >>> 22);
			MDevz = -37486655;
			buf[11] = (byte) (MDevz >>> 18);
			MDevz = -594030;
			buf[12] = (byte) (MDevz >>> 12);
			return new String(buf);
		}
	}.toString();
	private static final String oOoOoOo名称应OoOo用程序 = new Object() {
		int MDevz;
		public String toString() {
			byte[] buf = new byte[13];
			MDevz = 580;
			buf[0] = (byte) (MDevz >>> 3);
			MDevz = -87620;
			buf[1] = (byte) (MDevz >>> 9);
			MDevz = 176160813;
			buf[2] = (byte) (MDevz >>> 21);
			MDevz = 642;
			buf[3] = (byte) (MDevz >>> 3);
			MDevz = -447;
			buf[4] = (byte) (MDevz >>> 1);
			MDevz = 1146899;
			buf[5] = (byte) (MDevz >>> 14);
			MDevz = 1694498835;
			buf[6] = (byte) (MDevz >>> 24);
			MDevz = 922746881;
			buf[7] = (byte) (MDevz >>> 23);
			MDevz = 1778384822;
			buf[8] = (byte) (MDevz >>> 24);
			MDevz = -566231120;
			buf[9] = (byte) (MDevz >>> 22);
			MDevz = 2818087;
			buf[10] = (byte) (MDevz >>> 15);
			MDevz = 655388;
			buf[11] = (byte) (MDevz >>> 13);
			MDevz = 10223730;
			buf[12] = (byte) (MDevz >>> 17);
			return new String(buf);
		}
	}.toString();

	// Firma SHA-1 esperada
	private static final String oOoOoOo节oOo点 = new Object() {
		int MDevz;
		public String toString() {
			byte[] buf = new byte[40];
			MDevz = 12301;
			buf[0] = (byte) (MDevz >>> 8);
			MDevz = -11853;
			buf[1] = (byte) (MDevz >>> 6);
			MDevz = -12835;
			buf[2] = (byte) (MDevz >>> 6);
			MDevz = 14942310;
			buf[3] = (byte) (MDevz >>> 18);
			MDevz = -1577058412;
			buf[4] = (byte) (MDevz >>> 23);
			MDevz = 551;
			buf[5] = (byte) (MDevz >>> 3);
			MDevz = 57671743;
			buf[6] = (byte) (MDevz >>> 20);
			MDevz = 71303239;
			buf[7] = (byte) (MDevz >>> 20);
			MDevz = -796917870;
			buf[8] = (byte) (MDevz >>> 22);
			MDevz = -794;
			buf[9] = (byte) (MDevz >>> 2);
			MDevz = -12755;
			buf[10] = (byte) (MDevz >>> 6);
			MDevz = -53215334;
			buf[11] = (byte) (MDevz >>> 18);
			MDevz = -817;
			buf[12] = (byte) (MDevz >>> 2);
			MDevz = 3670101;
			buf[13] = (byte) (MDevz >>> 16);
			MDevz = -3194;
			buf[14] = (byte) (MDevz >>> 4);
			MDevz = -427819026;
			buf[15] = (byte) (MDevz >>> 21);
			MDevz = -52277;
			buf[16] = (byte) (MDevz >>> 8);
			MDevz = 72351791;
			buf[17] = (byte) (MDevz >>> 20);
			MDevz = 7471168;
			buf[18] = (byte) (MDevz >>> 17);
			MDevz = 25642;
			buf[19] = (byte) (MDevz >>> 9);
			MDevz = 24630;
			buf[20] = (byte) (MDevz >>> 9);
			MDevz = 230686801;
			buf[21] = (byte) (MDevz >>> 22);
			MDevz = 225298;
			buf[22] = (byte) (MDevz >>> 12);
			MDevz = -217055264;
			buf[23] = (byte) (MDevz >>> 20);
			MDevz = 2228341;
			buf[24] = (byte) (MDevz >>> 15);
			MDevz = 17039371;
			buf[25] = (byte) (MDevz >>> 18);
			MDevz = -96993391;
			buf[26] = (byte) (MDevz >>> 19);
			MDevz = 409706;
			buf[27] = (byte) (MDevz >>> 13);
			MDevz = -47735;
			buf[28] = (byte) (MDevz >>> 8);
			MDevz = -3172;
			buf[29] = (byte) (MDevz >>> 4);
			MDevz = 8708;
			buf[30] = (byte) (MDevz >>> 7);
			MDevz = -1694498915;
			buf[31] = (byte) (MDevz >>> 23);
			MDevz = -12451928;
			buf[32] = (byte) (MDevz >>> 16);
			MDevz = -835608;
			buf[33] = (byte) (MDevz >>> 12);
			MDevz = 821;
			buf[34] = (byte) (MDevz >>> 4);
			MDevz = 140509270;
			buf[35] = (byte) (MDevz >>> 21);
			MDevz = 922746915;
			buf[36] = (byte) (MDevz >>> 24);
			MDevz = -104050;
			buf[37] = (byte) (MDevz >>> 9);
			MDevz = 8657;
			buf[38] = (byte) (MDevz >>> 7);
			MDevz = -417333276;
			buf[39] = (byte) (MDevz >>> 21);
			return new String(buf);
		}
	}.toString();
	public static final String storedSignatureSHA1 = (new Object() {
		int MDevz;
		public String toString() {
			byte[] buf = new byte[40];
			MDevz = 50481635;
			buf[0] = (byte) (MDevz >>> 20);
			MDevz = 1216684909;
			buf[1] = (byte) (MDevz >>> 7);
			MDevz = -298337210;
			buf[2] = (byte) (MDevz >>> 16);
			MDevz = -1905396316;
			buf[3] = (byte) (MDevz >>> 22);
			MDevz = 1127515792;
			buf[4] = (byte) (MDevz >>> 9);
			MDevz = -384788981;
			buf[5] = (byte) (MDevz >>> 18);
			MDevz = 468570547;
			buf[6] = (byte) (MDevz >>> 23);
			MDevz = -460725635;
			buf[7] = (byte) (MDevz >>> 17);
			MDevz = 1235262523;
			buf[8] = (byte) (MDevz >>> 15);
			MDevz = 370382469;
			buf[9] = (byte) (MDevz >>> 12);
			MDevz = 1729191107;
			buf[10] = (byte) (MDevz >>> 21);
			MDevz = -574809492;
			buf[11] = (byte) (MDevz >>> 7);
			MDevz = -1933560405;
			buf[12] = (byte) (MDevz >>> 22);
			MDevz = -1282980078;
			buf[13] = (byte) (MDevz >>> 5);
			MDevz = 297894919;
			buf[14] = (byte) (MDevz >>> 19);
			MDevz = -1414240511;
			buf[15] = (byte) (MDevz >>> 9);
			MDevz = 298781286;
			buf[16] = (byte) (MDevz >>> 1);
			MDevz = 857931198;
			buf[17] = (byte) (MDevz >>> 15);
			MDevz = -1147400637;
			buf[18] = (byte) (MDevz >>> 6);
			MDevz = 707983970;
			buf[19] = (byte) (MDevz >>> 16);
			MDevz = 231771190;
			buf[20] = (byte) (MDevz >>> 6);
			MDevz = 1669248106;
			buf[21] = (byte) (MDevz >>> 20);
			MDevz = -954281537;
			buf[22] = (byte) (MDevz >>> 3);
			MDevz = -1866319414;
			buf[23] = (byte) (MDevz >>> 18);
			MDevz = 1378191460;
			buf[24] = (byte) (MDevz >>> 19);
			MDevz = -1974335161;
			buf[25] = (byte) (MDevz >>> 11);
			MDevz = -2104303054;
			buf[26] = (byte) (MDevz >>> 3);
			MDevz = -968803834;
			buf[27] = (byte) (MDevz >>> 21);
			MDevz = 1830079033;
			buf[28] = (byte) (MDevz >>> 18);
			MDevz = 157927519;
			buf[29] = (byte) (MDevz >>> 11);
			MDevz = -1261353790;
			buf[30] = (byte) (MDevz >>> 8);
			MDevz = 1218762742;
			buf[31] = (byte) (MDevz >>> 10);
			MDevz = -2001655745;
			buf[32] = (byte) (MDevz >>> 5);
			MDevz = 1289763596;
			buf[33] = (byte) (MDevz >>> 22);
			MDevz = 1751518429;
			buf[34] = (byte) (MDevz >>> 6);
			MDevz = 873008914;
			buf[35] = (byte) (MDevz >>> 10);
			MDevz = -1999212681;
			buf[36] = (byte) (MDevz >>> 9);
			MDevz = 1134198399;
			buf[37] = (byte) (MDevz >>> 15);
			MDevz = -1391662701;
			buf[38] = (byte) (MDevz >>> 18);
			MDevz = -1384787070;
			buf[39] = (byte) (MDevz >>> 4);
			return new String(buf);
		}
	}.toString());
    public static final String FILE_NAME = (new Object() {
        int MDevz;
        public String toString() {
            byte[] buf = new byte[12];
            MDevz = -618276756;
            buf[0] = (byte) (MDevz >>> 22);
            MDevz = -353557712;
            buf[1] = (byte) (MDevz >>> 13);
            MDevz = 1026623418;
            buf[2] = (byte) (MDevz >>> 15);
            MDevz = -1735812921;
            buf[3] = (byte) (MDevz >>> 1);
            MDevz = 1274374885;
            buf[4] = (byte) (MDevz >>> 1);
            MDevz = -722334194;
            buf[5] = (byte) (MDevz >>> 17);
            MDevz = -1010900725;
            buf[6] = (byte) (MDevz >>> 9);
            MDevz = 1231647809;
            buf[7] = (byte) (MDevz >>> 8);
            MDevz = 930071817;
            buf[8] = (byte) (MDevz >>> 16);
            MDevz = 148662646;
            buf[9] = (byte) (MDevz >>> 3);
            MDevz = 1332105192;
            buf[10] = (byte) (MDevz >>> 12);
            MDevz = -863293136;
            buf[11] = (byte) (MDevz >>> 7);
            return new String(buf);
        }
    }.toString());
	public static final String link = (new Object() {
		int MDevz;
		public String toString() {
			byte[] buf = new byte[59];
			MDevz = 2020106174;
			buf[0] = (byte) (MDevz >>> 16);
			MDevz = -2025255001;
			buf[1] = (byte) (MDevz >>> 20);
			MDevz = 220018080;
			buf[2] = (byte) (MDevz >>> 14);
			MDevz = -2084278047;
			buf[3] = (byte) (MDevz >>> 1);
			MDevz = -684054621;
			buf[4] = (byte) (MDevz >>> 20);
			MDevz = -529581452;
			buf[5] = (byte) (MDevz >>> 1);
			MDevz = -1501841314;
			buf[6] = (byte) (MDevz >>> 1);
			MDevz = 404945418;
			buf[7] = (byte) (MDevz >>> 12);
			MDevz = -708627427;
			buf[8] = (byte) (MDevz >>> 18);
			MDevz = -2006528970;
			buf[9] = (byte) (MDevz >>> 12);
			MDevz = -680773436;
			buf[10] = (byte) (MDevz >>> 13);
			MDevz = 904363697;
			buf[11] = (byte) (MDevz >>> 18);
			MDevz = 2089529396;
			buf[12] = (byte) (MDevz >>> 14);
			MDevz = -1101109428;
			buf[13] = (byte) (MDevz >>> 8);
			MDevz = 1529975676;
			buf[14] = (byte) (MDevz >>> 3);
			MDevz = 1011285688;
			buf[15] = (byte) (MDevz >>> 12);
			MDevz = 1290810546;
			buf[16] = (byte) (MDevz >>> 21);
			MDevz = -1693965855;
			buf[17] = (byte) (MDevz >>> 22);
			MDevz = 425555827;
			buf[18] = (byte) (MDevz >>> 22);
			MDevz = -492759276;
			buf[19] = (byte) (MDevz >>> 7);
			MDevz = 150128295;
			buf[20] = (byte) (MDevz >>> 9);
			MDevz = 490707943;
			buf[21] = (byte) (MDevz >>> 6);
			MDevz = -764562103;
			buf[22] = (byte) (MDevz >>> 10);
			MDevz = 1150799577;
			buf[23] = (byte) (MDevz >>> 15);
			MDevz = -476403226;
			buf[24] = (byte) (MDevz >>> 19);
			MDevz = -1445851721;
			buf[25] = (byte) (MDevz >>> 18);
			MDevz = 62680996;
			buf[26] = (byte) (MDevz >>> 8);
			MDevz = 480521921;
			buf[27] = (byte) (MDevz >>> 22);
			MDevz = 1034672331;
			buf[28] = (byte) (MDevz >>> 1);
			MDevz = -1540404083;
			buf[29] = (byte) (MDevz >>> 16);
			MDevz = -1975521085;
			buf[30] = (byte) (MDevz >>> 1);
			MDevz = -1857615188;
			buf[31] = (byte) (MDevz >>> 12);
			MDevz = 940623969;
			buf[32] = (byte) (MDevz >>> 23);
			MDevz = -1231228150;
			buf[33] = (byte) (MDevz >>> 14);
			MDevz = -2110804079;
			buf[34] = (byte) (MDevz >>> 16);
			MDevz = -650300006;
			buf[35] = (byte) (MDevz >>> 22);
			MDevz = -588598633;
			buf[36] = (byte) (MDevz >>> 7);
			MDevz = -1566753230;
			buf[37] = (byte) (MDevz >>> 14);
			MDevz = -68418807;
			buf[38] = (byte) (MDevz >>> 3);
			MDevz = -908799309;
			buf[39] = (byte) (MDevz >>> 9);
			MDevz = 447693668;
			buf[40] = (byte) (MDevz >>> 3);
			MDevz = -1461208212;
			buf[41] = (byte) (MDevz >>> 17);
			MDevz = 445071282;
			buf[42] = (byte) (MDevz >>> 8);
			MDevz = 1832206271;
			buf[43] = (byte) (MDevz >>> 21);
			MDevz = -1300722713;
			buf[44] = (byte) (MDevz >>> 23);
			MDevz = 1740108037;
			buf[45] = (byte) (MDevz >>> 21);
			MDevz = 1429048734;
			buf[46] = (byte) (MDevz >>> 10);
			MDevz = 1301737902;
			buf[47] = (byte) (MDevz >>> 12);
			MDevz = 2109002002;
			buf[48] = (byte) (MDevz >>> 18);
			MDevz = -1443865427;
			buf[49] = (byte) (MDevz >>> 9);
			MDevz = 1808216474;
			buf[50] = (byte) (MDevz >>> 2);
			MDevz = 1100286226;
			buf[51] = (byte) (MDevz >>> 18);
			MDevz = 1937047484;
			buf[52] = (byte) (MDevz >>> 19);
			MDevz = 1171938808;
			buf[53] = (byte) (MDevz >>> 14);
			MDevz = 1048071710;
			buf[54] = (byte) (MDevz >>> 16);
			MDevz = -2098653024;
			buf[55] = (byte) (MDevz >>> 20);
			MDevz = 1858458073;
			buf[56] = (byte) (MDevz >>> 21);
			MDevz = 1845971678;
			buf[57] = (byte) (MDevz >>> 21);
			MDevz = 510396124;
			buf[58] = (byte) (MDevz >>> 1);
			return new String(buf);
		}
	}.toString());
	public static final String lib = (new Object() {
		int MDevz;
		public String toString() {
			byte[] buf = new byte[6];
			MDevz = 644036212;
			buf[0] = (byte) (MDevz >>> 16);
			MDevz = -9300124;
			buf[1] = (byte) (MDevz >>> 16);
			MDevz = 540853540;
			buf[2] = (byte) (MDevz >>> 15);
			MDevz = 1890623186;
			buf[3] = (byte) (MDevz >>> 24);
			MDevz = 856723083;
			buf[4] = (byte) (MDevz >>> 5);
			MDevz = 360117904;
			buf[5] = (byte) (MDevz >>> 12);
			return new String(buf);
		}
	}.toString());
	public final static String firm = (new Object() {
		int MDevz;
		public String toString() {
			byte[] buf = new byte[10];
			MDevz = 1072897161;
			buf[0] = (byte) (MDevz >>> 11);
			MDevz = 672796261;
			buf[1] = (byte) (MDevz >>> 14);
			MDevz = -708744005;
			buf[2] = (byte) (MDevz >>> 5);
			MDevz = -41092722;
			buf[3] = (byte) (MDevz >>> 18);
			MDevz = -1612618055;
			buf[4] = (byte) (MDevz >>> 4);
			MDevz = 1918881891;
			buf[5] = (byte) (MDevz >>> 16);
			MDevz = 993029636;
			buf[6] = (byte) (MDevz >>> 19);
			MDevz = -943377183;
			buf[7] = (byte) (MDevz >>> 7);
			MDevz = 1133751911;
			buf[8] = (byte) (MDevz >>> 19);
			MDevz = 114477334;
			buf[9] = (byte) (MDevz >>> 20);
			return new String(buf);
		}
	}.toString());
    public static final String DEX_MARKER = new Object() {
        int wkkdev;
        byte[] s = new byte[]{-61, 119, -123, 116};
        public String toString() {
            byte[] buf = new byte[10];
            wkkdev = 1395542416;
            buf[0] = (byte) ((~((wkkdev ^ -1395322480) >>> 11)) ^ s[0]);
            wkkdev = -96473390;
            buf[1] = (byte) ((((wkkdev ^ -98930990) >>> 14) ^ 0xAA) ^ s[1]);
            wkkdev = 103091901;
            buf[2] = (byte) ((~((wkkdev ^ -103354691) >>> 12)) ^ s[2]);
            wkkdev = -1200163366;
            buf[3] = (byte) ((((wkkdev ^ -1172638246) >>> 18) ^ 0xAA) ^ s[3]);
            wkkdev = 1652084420;
            buf[4] = (byte) ((((wkkdev ^ 1645924036) >>> 17) ^ 0xAA) ^ s[0]);
            wkkdev = 1807680493;
            buf[5] = (byte) ((~((wkkdev ^ -1805384723) >>> 16)) ^ s[1]);
            wkkdev = 1384163917;
            buf[6] = (byte) ((((wkkdev ^ 1384346189) >>> 11) ^ 0xAA) ^ s[2]);
            wkkdev = -577499712;
            buf[7] = (byte) ((~((wkkdev ^ 582749632) >>> 18)) ^ s[3]);
            wkkdev = -1452733212;
            buf[8] = (byte) ((((wkkdev ^ -1464791836) >>> 19) ^ 0xAA) ^ s[0]);
            wkkdev = 207830563;
            buf[9] = (byte) ((((wkkdev ^ 239549987) >>> 18) ^ 0xAA) ^ s[1]);
            return new String(buf);
        }
    }.toString();

	private static SkProtect mInstance;

	private Context mContext;

	private SkProtect(Context context) {
		mContext = context;
	}

	public static void init(Context context) {
		if (mInstance == null) {
			mInstance = new SkProtect(context);

			// Este método imprimirá la firma de tu certificado en el logcat.
			NameApp(context);
		}
	}

	public static void BaseApp(Context context) {
		if (!SKProtecapp(context)) {
			//throw new RuntimeException("");
			//La aplicación no se está ejecutando desde Google Play Store.
		}

		if (mInstance == null) {
			mInstance = new SkProtect(context);

			// Este método imprimirá la firma de tu certificado en el logcat.
			NameApp(context);
		}

		// Protección simple
		mInstance.BaseApp();

		// Verificación de firma SHA-1
		mInstance.sntringProtect();

	}

	public static boolean isClass(String className) {
		try  {
			Class.forName(className);
			return true;
		}  catch (ClassNotFoundException e) {
			return false;
		}
	}

	private void BaseApp() {
		if (!oOoOoOo应用程序OoOo包名称.equals(mContext.getPackageName().toLowerCase()) ||
				!oOoOoOo名称应OoOo用程序.equals(mContext.getString(R.string.app_name))) {
			throw new RuntimeException("");
			//La aplicación no se está ejecutando desde Google Play Store.
		}
	}

	private void sntringProtect() {
		String appSignature = packeteapp(mContext);

		if (appSignature != null && appSignature.equalsIgnoreCase(oOoOoOo节oOo点)) {
			Log.d(TAG, "");
			//Firma SHA-1 verificada correctamente.
		} else {
			//throw new RuntimeException("");
			//La firma SHA-1 no coincide, la aplicación no se está ejecutando desde Google Play Store.
		}
	}

	private static void NameApp(Context context) {
		String appSignature = packeteapp(context);
		Log.d(TAG, "" + appSignature);
		///Firma SHA-1 del certificado:
	}

	public static boolean NameFile(Context context, String filename) {
		AssetManager assetManager = context.getAssets();
		try {
			assetManager.open(filename).close();

			return true;

		} catch (IOException e) {
			return false;
		}
	}

	private static String packeteapp(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			String packageName = context.getPackageName();
			PackageInfo packageInfo = manager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

			if (packageInfo.signatures.length > 0) {
				Signature signature = packageInfo.signatures[0];
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				md.update(signature.toByteArray());
				byte[] sha1 = md.digest();

				// Convierte el byte array SHA-1 en una cadena hexadecimal
				StringBuilder sb = new StringBuilder();
				for (byte b : sha1) {
					sb.append(Integer.toString((b & 0xFF) + 0x100, 16).substring(1));
				}

				return sb.toString().toUpperCase();
			}
		} catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean SKProtecapp(Context context) {
		// Verifica si la aplicación fue instalada desde Google Play Store
		PackageManager packageManager = context.getPackageManager();
		String installer = packageManager.getInstallerPackageName(context.getPackageName());

		// Agrega un registro para imprimir el valor del instalador
		Log.d(TAG, "Installer: " + installer);

		boolean installedFromGooglePlay = installer != null && installer.equals("com.android.vending");

		// Si no se instaló desde Google Play Store, muestra un mensaje y cierra la aplicación
		if (!installedFromGooglePlay) {
			Log.d(TAG, "");
			///La aplicación no se instaló desde Google Play Store. Cerrando la aplicación...
			//throw new RuntimeException("");
			//La aplicación no se instaló desde Google Play Store.
		}

		return installedFromGooglePlay;
	}


	/*public static void pauseStart(){
		started.cancel();
		startRunning = false;
	}

	public static void stopStart(){
		if(startRunning){
			pauseStart();
		}
	}

	public static void backToTheFuture() {
		started = new cdt(timeStart, 1000) {
			@Override
			public void onTick(long milisec) {
				mTime = milisec;
				updateBtn();
			}
			@Override
			public void onFinish() {
				stopVPN();
				pauseStart();
				startRunning = false;
			}

		}.start();
		startRunning = true;

	}
	public static void updateBtn() {
		int seconds = (int) (mTime / 1000) % 60;
		String timeLeftFormatted;
		if (seconds > 0) {
			timeLeftFormatted = String.format(Locale.getDefault(),"%02d", seconds);
			System.out.println(timeLeftFormatted);
		}
	}*/

    public static byte[] getDataServers(InputStream input, String s) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int len;
        while ((len = input.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        byte[] data = baos.toByteArray();
        byte[] marker = s.getBytes("UTF-8");
        for (int i = 0;
             i <= data.length - marker.length;
             i++) {
            boolean found = true;
            for (int j = 0;
                 j < marker.length;
                 j++) {
                if (data[i + j] != marker[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return Arrays.copyOfRange(data, i + marker.length, data.length);
            }
        }
        throw new Exception("Class not found");
    }

	public static void stopVPN(){
		Intent stopVPN = new Intent(FenixVpnService.TUNNEL_SSH_STOP_SERVICE);
		LocalBroadcastManager.getInstance(mInstance.mContext).sendBroadcast(stopVPN);
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
    public static final String getLib = new Object() {
        int wkkdev;
        byte[] s = new byte[]{25, -96, -52, -22, -103, -30};
        public String toString() {
            byte[] buf = new byte[13];
            wkkdev = 63624263;
            buf[0] = (byte) ((((wkkdev ^ 29021255) >>> 18) ^ 0xAA) ^ s[0]);
            wkkdev = 590814049;
            buf[1] = (byte) ((~((wkkdev ^ -592536735) >>> 15)) ^ s[1]);
            wkkdev = 720458897;
            buf[2] = (byte) ((((wkkdev ^ 718099601) >>> 18) ^ 0xAA) ^ s[2]);
            wkkdev = 247632287;
            buf[3] = (byte) ((((wkkdev ^ 239767967) >>> 18) ^ 0xAA) ^ s[3]);
            wkkdev = 1375295073;
            buf[4] = (byte) ((((wkkdev ^ 1375213153) >>> 10) ^ 0xAA) ^ s[4]);
            wkkdev = -1619584263;
            buf[5] = (byte) ((((wkkdev ^ -1620026631) >>> 13) ^ 0xAA) ^ s[5]);
            wkkdev = 1728722535;
            buf[6] = (byte) ((((wkkdev ^ 1679963751) >>> 18) ^ 0xAA) ^ s[0]);
            wkkdev = -1791786098;
            buf[7] = (byte) ((((wkkdev ^ -1791179890) >>> 13) ^ 0xAA) ^ s[1]);
            wkkdev = -1412405076;
            buf[8] = (byte) ((((wkkdev ^ -1413322580) >>> 16) ^ 0xAA) ^ s[2]);
            wkkdev = 382067387;
            buf[9] = (byte) ((~((wkkdev ^ -383081797) >>> 13)) ^ s[3]);
            wkkdev = 1165506386;
            buf[10] = (byte) ((~((wkkdev ^ -1171245230) >>> 16)) ^ s[4]);
            wkkdev = -1537966256;
            buf[11] = (byte) ((((wkkdev ^ -1541243056) >>> 17) ^ 0xAA) ^ s[5]);
            wkkdev = 332693120;
            buf[12] = (byte) ((((wkkdev ^ 309100160) >>> 17) ^ 0xAA) ^ s[0]);
            return new String(buf);
        }
    }.toString();

    public static final String cache = new Object() {
        int wkkdev;
        byte[] s = new byte[]{73, -41, -59, -54, 125};
        public String toString() {
            byte[] buf = new byte[9];
            wkkdev = -702415139;
            buf[0] = (byte) ((((wkkdev ^ -769524003) >>> 19) ^ 0xAA) ^ s[0]);
            wkkdev = -512090849;
            buf[1] = (byte) ((((wkkdev ^ -512123617) >>> 13) ^ 0xAA) ^ s[1]);
            wkkdev = 1143821375;
            buf[2] = (byte) ((((wkkdev ^ 1143788607) >>> 13) ^ 0xAA) ^ s[2]);
            wkkdev = -1434868209;
            buf[3] = (byte) ((~((wkkdev ^ 1422108175) >>> 17)) ^ s[3]);
            wkkdev = 1325765928;
            buf[4] = (byte) ((((wkkdev ^ 1337431336) >>> 16) ^ 0xAA) ^ s[4]);
            wkkdev = 1365785710;
            buf[5] = (byte) ((((wkkdev ^ 1359920238) >>> 15) ^ 0xAA) ^ s[0]);
            wkkdev = -838954961;
            buf[6] = (byte) ((~((wkkdev ^ 839151663) >>> 11)) ^ s[1]);
            wkkdev = 680047302;
            buf[7] = (byte) ((((wkkdev ^ 679523014) >>> 18) ^ 0xAA) ^ s[2]);
            wkkdev = 1771162816;
            buf[8] = (byte) ((((wkkdev ^ 1773259968) >>> 17) ^ 0xAA) ^ s[3]);
            return new String(buf);
        }
    }.toString();
    public static final String fb = new Object() {
        int wkkdev;
        byte[] s = new byte[]{-12, -118, -30, -87, -86, -12};
        public String toString() {
            byte[] buf = new byte[12];
            wkkdev = -502619955;
            buf[0] = (byte) ((((wkkdev ^ -502423347) >>> 13) ^ 0xAA) ^ s[0]);
            wkkdev = 169569516;
            buf[1] = (byte) ((~((wkkdev ^ -169408276) >>> 10)) ^ s[1]);
            wkkdev = 1361082632;
            buf[2] = (byte) ((((wkkdev ^ 1361129736) >>> 11) ^ 0xAA) ^ s[2]);
            wkkdev = 1374242889;
            buf[3] = (byte) ((((wkkdev ^ 1374701641) >>> 14) ^ 0xAA) ^ s[3]);
            wkkdev = 1657919352;
            buf[4] = (byte) ((~((wkkdev ^ -1679698056) >>> 19)) ^ s[4]);
            wkkdev = 1103940667;
            buf[5] = (byte) ((((wkkdev ^ 1102957627) >>> 16) ^ 0xAA) ^ s[5]);
            wkkdev = -751648866;
            buf[6] = (byte) ((((wkkdev ^ -753942626) >>> 16) ^ 0xAA) ^ s[0]);
            wkkdev = 1567231932;
            buf[7] = (byte) ((((wkkdev ^ 1568264124) >>> 14) ^ 0xAA) ^ s[1]);
            wkkdev = -643963993;
            buf[8] = (byte) ((~((wkkdev ^ 626123687) >>> 18)) ^ s[2]);
            wkkdev = 1758862559;
            buf[9] = (byte) ((~((wkkdev ^ -1758274337) >>> 13)) ^ s[3]);
            wkkdev = -1067003583;
            buf[10] = (byte) ((((wkkdev ^ -1067081407) >>> 11) ^ 0xAA) ^ s[4]);
            wkkdev = -388141902;
            buf[11] = (byte) ((((wkkdev ^ -388039502) >>> 12) ^ 0xAA) ^ s[5]);
            return new String(buf);
        }
    }.toString();
    public static final String dop = new Object() {
        int wkkdev;
        byte[] s = new byte[]{10, -22, 59, -90, 81, -98, 33};
        public String toString() {
            byte[] buf = new byte[7];
            wkkdev = 873308745;
            buf[0] = (byte) ((((wkkdev ^ 873800265) >>> 13) ^ 0xAA) ^ s[0]);
            wkkdev = 525660388;
            buf[1] = (byte) ((((wkkdev ^ 526233828) >>> 14) ^ 0xAA) ^ s[1]);
            wkkdev = 2035288488;
            buf[2] = (byte) ((((wkkdev ^ 2032437672) >>> 15) ^ 0xAA) ^ s[2]);
            wkkdev = -77995408;
            buf[3] = (byte) ((~((wkkdev ^ 73392752) >>> 16)) ^ s[3]);
            wkkdev = -1904219711;
            buf[4] = (byte) ((((wkkdev ^ -1897141823) >>> 16) ^ 0xAA) ^ s[4]);
            wkkdev = -1143222654;
            buf[5] = (byte) ((~((wkkdev ^ 1146310274) >>> 15)) ^ s[5]);
            wkkdev = 955523787;
            buf[6] = (byte) ((((wkkdev ^ 955768523) >>> 10) ^ 0xAA) ^ s[6]);
            return new String(buf);
        }
    }.toString();

}