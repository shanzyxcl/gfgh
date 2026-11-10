package ooOoO;

import static ooOoO.oOoOoOO.hex;
import android.util.Base64;

public class o0oo0o00 {

    static String CP = new Object() {
        int wkkdev;
        byte[] s = new byte[]{100, -99, -111, -64, 43};
        public String toString() {
            byte[] buf = new byte[9];
            wkkdev = -229140890;
            buf[0] = (byte) ((((wkkdev ^ -253520282) >>> 18) ^ 0xAA) ^ s[0]);
            wkkdev = -1533097911;
            buf[1] = (byte) ((~((wkkdev ^ 1528318025) >>> 15)) ^ s[1]);
            wkkdev = 890978756;
            buf[2] = (byte) ((((wkkdev ^ 890901956) >>> 10) ^ 0xAA) ^ s[2]);
            wkkdev = 991732478;
            buf[3] = (byte) ((((wkkdev ^ 991741694) >>> 10) ^ 0xAA) ^ s[3]);
            wkkdev = -1294573280;
            buf[4] = (byte) ((((wkkdev ^ -1292967648) >>> 14) ^ 0xAA) ^ s[4]);
            wkkdev = -1788718887;
            buf[5] = (byte) ((~((wkkdev ^ 1789939929) >>> 17)) ^ s[0]);
            wkkdev = 603012258;
            buf[6] = (byte) ((~((wkkdev ^ -603943774) >>> 12)) ^ s[1]);
            wkkdev = -705945670;
            buf[7] = (byte) ((((wkkdev ^ -705804358) >>> 11) ^ 0xAA) ^ s[2]);
            wkkdev = 691801745;
            buf[8] = (byte) ((~((wkkdev ^ -748417391) >>> 19)) ^ s[3]);
            return new String(buf);
        }
    }.toString();

    static {
        System.loadLibrary(CP);
    }
    public static native byte[] o0oooOoO(byte[] data);
    public static native byte[] o0oooo00(byte[] data);

    private static String o0oo00o(String in) {
        try {
            if (in != null && in.startsWith(hex)) {
                in = in.substring(hex.length());
            }
            byte[] encryptedBytes = Base64.decode(in, Base64.NO_WRAP | Base64.URL_SAFE);
            byte[] decryptedBytes = o0oooo00(encryptedBytes);
            return new String(decryptedBytes, "UTF-8").trim();
        } catch (Exception e) {
            e.printStackTrace();
            return in;
        }
    }
}