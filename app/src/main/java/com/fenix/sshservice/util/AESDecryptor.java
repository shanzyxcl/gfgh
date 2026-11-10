package com.fenix.sshservice.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class AESDecryptor {
    public static byte[] decryptOpenSSL(byte[] encryptedData, String password) throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(encryptedData);

        // Leer encabezado personalizado: "WakkoDev" (8 bytes)
        byte[] saltHeader = new byte[8];
        buffer.get(saltHeader);
        if (!Arrays.equals(saltHeader, "WakkoDev".getBytes(StandardCharsets.US_ASCII))) {
            throw new IllegalArgumentException("Encabezado inválido (se esperaba WakkoDev)");
        }

        byte[] salt = new byte[8];
        buffer.get(salt);

        // Derivar clave y IV usando PBKDF2 con HMAC-SHA256
        int iterations = 100000;
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 48 * 8); // 48 bytes = 32 key + 16 IV
        SecretKey tmp = factory.generateSecret(spec);
        byte[] keyAndIv = tmp.getEncoded();

        byte[] key = Arrays.copyOfRange(keyAndIv, 0, 32);
        byte[] iv = Arrays.copyOfRange(keyAndIv, 32, 48);

        // Cifrado AES-256-CBC
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));

        byte[] ciphertext = new byte[buffer.remaining()];
        buffer.get(ciphertext);

        return cipher.doFinal(ciphertext);
    }
}