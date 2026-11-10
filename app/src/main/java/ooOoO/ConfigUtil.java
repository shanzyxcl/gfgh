package ooOoO;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ConfigUtil {

    Context context;

    public static final String PASSWORD = (new Object() {
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

    public ConfigUtil(Context context) {
        this.context = context;
    }



    private String readStream(InputStream in)
    {
        StringBuilder sb = new StringBuilder();
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in));
            char[] buff = new char[1024];
            while (true) {
                int read = reader.read(buff, 0, buff.length);
                if (read <= 0) {
                    break;
                }
                sb.append(buff, 0, read);
            }
        } catch (Exception e) {

        }
        return sb.toString();
    }

    public JSONObject ConfigAnuncios() {
        try {
            File file = new File(context.getFilesDir(), "anuncios.png");
            if (file.exists()) {
                String json_file = readStream(new FileInputStream(file));
                String json = AESCrypt.decrypt(PASSWORD, json_file);
                return new JSONObject(json);
            } else {
                InputStream inputStream = context.getAssets().open("flags/anuncios.png");
                String json = AESCrypt.decrypt(PASSWORD, readStream(inputStream));
                return new JSONObject(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String Version() {
        try {
            return ConfigAnuncios().getString("Version");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String Banner() {
        try {
            return ConfigAnuncios().getString("Banner");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String Intersticial() {
        try {
            return ConfigAnuncios().getString("Intersticial");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String Recompensado() {
        try {
            return ConfigAnuncios().getString("Recompensado");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String OpenApp() {
        try {
            return ConfigAnuncios().getString("OpenApp");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
