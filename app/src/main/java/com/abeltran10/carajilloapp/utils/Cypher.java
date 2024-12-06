package com.abeltran10.carajilloapp.utils;

import android.util.Base64;

import com.abeltran10.carajilloapp.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Cypher {

    private static String SECRET_KEY = BuildConfig.SECRETKEY;
    private static String INIT_VECTOR = BuildConfig.INITVECTOR;


    public static String encrypt(String value) {
        String encrypt = null;

        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            encrypt = Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return encrypt;
    }

    public static String decrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decode(value, Base64.DEFAULT));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
