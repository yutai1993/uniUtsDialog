package com.qishi.utsdialog;

import android.util.Base64;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class EncryptionUtil {
    private static final int MAX_SEGMENT_SIZE = 100; // 每段最大大小

    // 将 Base64 编码的公钥字符串转换为 PublicKey 对象
    public static PublicKey getPublicKeyFromString(String key) throws Exception {
        byte[] keyBytes = Base64.decode(key, Base64.DEFAULT);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    // 使用公钥分段加密数据
    public static String rsaEncrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] dataBytes = data.getBytes("UTF-8");
        int dataLength = dataBytes.length;
        StringBuilder encryptedData = new StringBuilder();

        for (int i = 0; i < dataLength; i += MAX_SEGMENT_SIZE) {
            int segmentSize = Math.min(MAX_SEGMENT_SIZE, dataLength - i);
            byte[] segment = new byte[segmentSize];
            System.arraycopy(dataBytes, i, segment, 0, segmentSize);

            byte[] encryptedSegment = cipher.doFinal(segment);
            encryptedData.append(Base64.encodeToString(encryptedSegment, Base64.DEFAULT)).append("|");
        }

        // 移除最后一个多余的分隔符
        encryptedData.setLength(encryptedData.length() - 1);

        return encryptedData.toString();
    }

    public static String rsaEncryptMain(String data, String publicKeyString) throws Exception {
        // 将公钥字符串转换为 PublicKey 对象
        PublicKey publicKey = getPublicKeyFromString(publicKeyString);
        // 使用公钥分段加密数据
        return rsaEncrypt(data, publicKey);
    }
}
