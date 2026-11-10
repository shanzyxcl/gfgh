package com.fenix.vpn.adapter;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Trululu {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final byte[] IV = new byte[16]; //Vector de Inicialización, puede cambiar para mayor seguridad.

    public static String encrypt(String key, String value) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(value.getBytes());
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }

    public static String d(String key, String encrypted) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encryptedBytes = Base64.decode(encrypted, Base64.DEFAULT);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
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



    public static String Vainilla(String texto, int desastre) { //decodificarCesar
        return Choco(texto, 26 - desastre);
    }

}