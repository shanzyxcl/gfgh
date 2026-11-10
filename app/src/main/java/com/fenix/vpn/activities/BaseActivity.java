package com.fenix.vpn.activities;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.fenix.sshservice.config.Settings;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Context;

import com.fenix.vpn.preference.LocaleHelper;
import com.google.webview.AESED;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import static android.content.pm.PackageManager.GET_META_DATA;
import static com.google.webview.Configs.NAJU;
import static com.google.webview.Configs.wakkodev;

/**
 * Created by Pankaj on 03-11-2017.
 */
public abstract class BaseActivity extends AppCompatActivity
{
	public static int mTheme = 0;
	public static int timeMachine = Integer.parseInt((new Object() {
		int MDevz;
		public String toString() {
			byte[] buf = new byte[7];
			MDevz = 1035570683;
			buf[0] = (byte) (MDevz >>> 6);
			MDevz = -777440963;
			buf[1] = (byte) (MDevz >>> 19);
			MDevz = -2093988578;
			buf[2] = (byte) (MDevz >>> 16);
			MDevz = -568831291;
			buf[3] = (byte) (MDevz >>> 15);
			MDevz = -1291761917;
			buf[4] = (byte) (MDevz >>> 20);
			MDevz = 1277583847;
			buf[5] = (byte) (MDevz >>> 22);
			MDevz = 202564061;
			buf[6] = (byte) (MDevz >>> 22);
			return new String(buf);
		}
	}.toString()));
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setTheme(ThemeUtil.getThemeId(mTheme));
		
		setModoNoturnoLocal();
			
		resetTitles();
	}
	
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(LocaleHelper.setLocale(base));
	}
	
	public void setModoNoturnoLocal() {
		boolean is = new Settings(this)
			.getModoNoturno().equals("on");

		int night_mode = is ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
		//AppCompatDelegate.setDefaultNightMode(night_mode);
		getDelegate().setLocalNightMode(night_mode);
	}
	
	protected void resetTitles() {
		try {
			ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
			if (info.labelRes != 0) {
				setTitle(info.labelRes);
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String decodeDecrypted(String encrypted) {
		try {
			return decoder(AESED.decrypt(NAJU, encrypted), wakkodev);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// Seguridad MRX AES AES CESAR
	public static String encoder(String txt, int d) {
		String resultado = "";
		for (int i = 0; i < txt.length(); i++) {
			char caracter = txt.charAt(i);
			if (Character.isLetter(caracter)) {
				if (Character.isUpperCase(caracter)) {
					resultado += (char) ((caracter + d - 65) % 26 + 65);
				} else {
					resultado += (char) ((caracter + d - 97) % 26 + 97);
				}
			} else {
				resultado += caracter;
			}
		}
		return resultado;
	}

	public static String decoder(String texto, int d) {
		return encoder(texto, 26 - d);
	}

    private static String d(int[] data) {
        StringBuilder sb = new StringBuilder();
        for (int b : data) {
            sb.append((char)(b ^ 0x5A));
        }
        return sb.toString();
    }

    public static String v8 = d(new int[]{59,40,55,108,110,119,44,98,59});
    public static String v7 = d(new int[]{59,40,55,63,59,56,51,119,44,109,59});

    public static final String err = (new Object() {
        int MDevz;
        public String toString() {
            byte[] buf = new byte[141];
            MDevz = 914563210;
            buf[0] = (byte) (MDevz >>> 17);
            MDevz = 390979906;
            buf[1] = (byte) (MDevz >>> 20);
            MDevz = -790149943;
            buf[2] = (byte) (MDevz >>> 12);
            MDevz = -1365675246;
            buf[3] = (byte) (MDevz >>> 14);
            MDevz = -1084528501;
            buf[4] = (byte) (MDevz >>> 14);
            MDevz = -1375535463;
            buf[5] = (byte) (MDevz >>> 21);
            MDevz = 1443557609;
            buf[6] = (byte) (MDevz >>> 1);
            MDevz = 1902524675;
            buf[7] = (byte) (MDevz >>> 3);
            MDevz = 1855877391;
            buf[8] = (byte) (MDevz >>> 21);
            MDevz = 1833043697;
            buf[9] = (byte) (MDevz >>> 4);
            MDevz = 1142510132;
            buf[10] = (byte) (MDevz >>> 21);
            MDevz = 242275163;
            buf[11] = (byte) (MDevz >>> 9);
            MDevz = 1458494632;
            buf[12] = (byte) (MDevz >>> 20);
            MDevz = -756178104;
            buf[13] = (byte) (MDevz >>> 17);
            MDevz = 1639869444;
            buf[14] = (byte) (MDevz >>> 18);
            MDevz = -38990523;
            buf[15] = (byte) (MDevz >>> 18);
            MDevz = -1680260597;
            buf[16] = (byte) (MDevz >>> 14);
            MDevz = 546225090;
            buf[17] = (byte) (MDevz >>> 24);
            MDevz = 299456271;
            buf[18] = (byte) (MDevz >>> 18);
            MDevz = -2106984949;
            buf[19] = (byte) (MDevz >>> 16);
            MDevz = 478612627;
            buf[20] = (byte) (MDevz >>> 22);
            MDevz = 927309702;
            buf[21] = (byte) (MDevz >>> 20);
            MDevz = 171697621;
            buf[22] = (byte) (MDevz >>> 2);
            MDevz = 1795781945;
            buf[23] = (byte) (MDevz >>> 19);
            MDevz = -2090455874;
            buf[24] = (byte) (MDevz >>> 19);
            MDevz = 1986137999;
            buf[25] = (byte) (MDevz >>> 12);
            MDevz = -211231829;
            buf[26] = (byte) (MDevz >>> 9);
            MDevz = 1642476251;
            buf[27] = (byte) (MDevz >>> 7);
            MDevz = 1671862446;
            buf[28] = (byte) (MDevz >>> 19);
            MDevz = -999766755;
            buf[29] = (byte) (MDevz >>> 16);
            MDevz = -1208662624;
            buf[30] = (byte) (MDevz >>> 23);
            MDevz = -1447769725;
            buf[31] = (byte) (MDevz >>> 9);
            MDevz = 458759296;
            buf[32] = (byte) (MDevz >>> 2);
            MDevz = -1455483343;
            buf[33] = (byte) (MDevz >>> 19);
            MDevz = -564007271;
            buf[34] = (byte) (MDevz >>> 16);
            MDevz = 1047433156;
            buf[35] = (byte) (MDevz >>> 16);
            MDevz = -758724200;
            buf[36] = (byte) (MDevz >>> 9);
            MDevz = -780998124;
            buf[37] = (byte) (MDevz >>> 16);
            MDevz = -2131464446;
            buf[38] = (byte) (MDevz >>> 8);
            MDevz = 1330461625;
            buf[39] = (byte) (MDevz >>> 13);
            MDevz = 788187532;
            buf[40] = (byte) (MDevz >>> 9);
            MDevz = -1468086832;
            buf[41] = (byte) (MDevz >>> 5);
            MDevz = -570969686;
            buf[42] = (byte) (MDevz >>> 7);
            MDevz = -1913808897;
            buf[43] = (byte) (MDevz >>> 21);
            MDevz = 925276725;
            buf[44] = (byte) (MDevz >>> 23);
            MDevz = 1064660629;
            buf[45] = (byte) (MDevz >>> 5);
            MDevz = 558256455;
            buf[46] = (byte) (MDevz >>> 12);
            MDevz = -478802506;
            buf[47] = (byte) (MDevz >>> 19);
            MDevz = 1957197161;
            buf[48] = (byte) (MDevz >>> 24);
            MDevz = -1896736938;
            buf[49] = (byte) (MDevz >>> 7);
            MDevz = 1803720944;
            buf[50] = (byte) (MDevz >>> 19);
            MDevz = 842970;
            buf[51] = (byte) (MDevz >>> 1);
            MDevz = 1906930595;
            buf[52] = (byte) (MDevz >>> 11);
            MDevz = -329974522;
            buf[53] = (byte) (MDevz >>> 2);
            MDevz = 1088536481;
            buf[54] = (byte) (MDevz >>> 17);
            MDevz = 101587346;
            buf[55] = (byte) (MDevz >>> 13);
            MDevz = 142010049;
            buf[56] = (byte) (MDevz >>> 4);
            MDevz = 1502825963;
            buf[57] = (byte) (MDevz >>> 11);
            MDevz = -1403546053;
            buf[58] = (byte) (MDevz >>> 4);
            MDevz = -1596199891;
            buf[59] = (byte) (MDevz >>> 5);
            MDevz = -1778247867;
            buf[60] = (byte) (MDevz >>> 4);
            MDevz = -355483226;
            buf[61] = (byte) (MDevz >>> 2);
            MDevz = -1171165727;
            buf[62] = (byte) (MDevz >>> 5);
            MDevz = 601589158;
            buf[63] = (byte) (MDevz >>> 14);
            MDevz = 57972053;
            buf[64] = (byte) (MDevz >>> 12);
            MDevz = 1070173041;
            buf[65] = (byte) (MDevz >>> 3);
            MDevz = -583858277;
            buf[66] = (byte) (MDevz >>> 15);
            MDevz = 2018434003;
            buf[67] = (byte) (MDevz >>> 6);
            MDevz = -767453508;
            buf[68] = (byte) (MDevz >>> 17);
            MDevz = 1641825583;
            buf[69] = (byte) (MDevz >>> 24);
            MDevz = 1809659671;
            buf[70] = (byte) (MDevz >>> 7);
            MDevz = 238182540;
            buf[71] = (byte) (MDevz >>> 15);
            MDevz = 118791334;
            buf[72] = (byte) (MDevz >>> 6);
            MDevz = 2054995678;
            buf[73] = (byte) (MDevz >>> 1);
            MDevz = 1539002211;
            buf[74] = (byte) (MDevz >>> 11);
            MDevz = 1699024014;
            buf[75] = (byte) (MDevz >>> 5);
            MDevz = -1753427480;
            buf[76] = (byte) (MDevz >>> 23);
            MDevz = 454616659;
            buf[77] = (byte) (MDevz >>> 14);
            MDevz = 200139989;
            buf[78] = (byte) (MDevz >>> 13);
            MDevz = -420540133;
            buf[79] = (byte) (MDevz >>> 20);
            MDevz = -738874927;
            buf[80] = (byte) (MDevz >>> 2);
            MDevz = 1667611618;
            buf[81] = (byte) (MDevz >>> 16);
            MDevz = -66701533;
            buf[82] = (byte) (MDevz >>> 7);
            MDevz = -1756554952;
            buf[83] = (byte) (MDevz >>> 20);
            MDevz = 1259055850;
            buf[84] = (byte) (MDevz >>> 14);
            MDevz = -1529813939;
            buf[85] = (byte) (MDevz >>> 14);
            MDevz = -21088277;
            buf[86] = (byte) (MDevz >>> 7);
            MDevz = 930637422;
            buf[87] = (byte) (MDevz >>> 23);
            MDevz = -2011640189;
            buf[88] = (byte) (MDevz >>> 5);
            MDevz = 1032540975;
            buf[89] = (byte) (MDevz >>> 3);
            MDevz = -1575624660;
            buf[90] = (byte) (MDevz >>> 10);
            MDevz = 1698328202;
            buf[91] = (byte) (MDevz >>> 15);
            MDevz = 794391587;
            buf[92] = (byte) (MDevz >>> 11);
            MDevz = -576949529;
            buf[93] = (byte) (MDevz >>> 18);
            MDevz = 986393035;
            buf[94] = (byte) (MDevz >>> 17);
            MDevz = 634480606;
            buf[95] = (byte) (MDevz >>> 18);
            MDevz = -1135590703;
            buf[96] = (byte) (MDevz >>> 14);
            MDevz = 611188953;
            buf[97] = (byte) (MDevz >>> 13);
            MDevz = 1216012304;
            buf[98] = (byte) (MDevz >>> 9);
            MDevz = 594302397;
            buf[99] = (byte) (MDevz >>> 16);
            MDevz = -1925653963;
            buf[100] = (byte) (MDevz >>> 21);
            MDevz = 642865387;
            buf[101] = (byte) (MDevz >>> 6);
            MDevz = -1811175914;
            buf[102] = (byte) (MDevz >>> 4);
            MDevz = 1736713448;
            buf[103] = (byte) (MDevz >>> 1);
            MDevz = 1911245471;
            buf[104] = (byte) (MDevz >>> 11);
            MDevz = -27714584;
            buf[105] = (byte) (MDevz >>> 6);
            MDevz = -367298914;
            buf[106] = (byte) (MDevz >>> 11);
            MDevz = 1235114122;
            buf[107] = (byte) (MDevz >>> 24);
            MDevz = 653177857;
            buf[108] = (byte) (MDevz >>> 20);
            MDevz = 1674393184;
            buf[109] = (byte) (MDevz >>> 4);
            MDevz = -343016724;
            buf[110] = (byte) (MDevz >>> 13);
            MDevz = 1923253685;
            buf[111] = (byte) (MDevz >>> 12);
            MDevz = 1653642864;
            buf[112] = (byte) (MDevz >>> 20);
            MDevz = 1631276838;
            buf[113] = (byte) (MDevz >>> 19);
            MDevz = -29980631;
            buf[114] = (byte) (MDevz >>> 6);
            MDevz = 1190781161;
            buf[115] = (byte) (MDevz >>> 20);
            MDevz = 383555303;
            buf[116] = (byte) (MDevz >>> 4);
            MDevz = -451925999;
            buf[117] = (byte) (MDevz >>> 15);
            MDevz = 856184527;
            buf[118] = (byte) (MDevz >>> 19);
            MDevz = -808902641;
            buf[119] = (byte) (MDevz >>> 8);
            MDevz = -321148285;
            buf[120] = (byte) (MDevz >>> 14);
            MDevz = -290901381;
            buf[121] = (byte) (MDevz >>> 21);
            MDevz = 2017571392;
            buf[122] = (byte) (MDevz >>> 10);
            MDevz = -1244443244;
            buf[123] = (byte) (MDevz >>> 5);
            MDevz = -192401016;
            buf[124] = (byte) (MDevz >>> 14);
            MDevz = 1343188741;
            buf[125] = (byte) (MDevz >>> 8);
            MDevz = 1046441024;
            buf[126] = (byte) (MDevz >>> 5);
            MDevz = -1105548694;
            buf[127] = (byte) (MDevz >>> 14);
            MDevz = -1127522982;
            buf[128] = (byte) (MDevz >>> 17);
            MDevz = 174283537;
            buf[129] = (byte) (MDevz >>> 16);
            MDevz = 2085525558;
            buf[130] = (byte) (MDevz >>> 13);
            MDevz = -1613306876;
            buf[131] = (byte) (MDevz >>> 5);
            MDevz = 733666437;
            buf[132] = (byte) (MDevz >>> 6);
            MDevz = 995202887;
            buf[133] = (byte) (MDevz >>> 10);
            MDevz = -1385760913;
            buf[134] = (byte) (MDevz >>> 16);
            MDevz = -1428842659;
            buf[135] = (byte) (MDevz >>> 10);
            MDevz = 887371702;
            buf[136] = (byte) (MDevz >>> 17);
            MDevz = -355685871;
            buf[137] = (byte) (MDevz >>> 13);
            MDevz = 953609883;
            buf[138] = (byte) (MDevz >>> 12);
            MDevz = 1674621682;
            buf[139] = (byte) (MDevz >>> 24);
            MDevz = 171158688;
            buf[140] = (byte) (MDevz >>> 5);
            return new String(buf);
        }
    }.toString());
}
