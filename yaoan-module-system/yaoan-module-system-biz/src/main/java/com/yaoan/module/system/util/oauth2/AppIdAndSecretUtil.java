package com.yaoan.module.system.util.oauth2;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class AppIdAndSecretUtil {
    private static final String ALGORITHM = "SHA-256";

//    public static void main(String[] args) {
//        String appId = generateAppId();
//        String appSecret = generateAppSecret();
//        System.out.println("AppId: " + appId);
//        System.out.println("AppSecret: " + appSecret);
//    }

    public static String generateAppIdV2() {
        // 生成一个随机的 AppId
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
    public static String generateAppId() {
        // 生成一个随机的 AppId
        UUID uuid = UUID.randomUUID();
        return uuid.toString(); }


    public static String generateAppSecret() {
        try {
            // 生成一个随机的 AppSecret
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            SecureRandom random = new SecureRandom();
            byte[] bytes = new BigInteger(130, random).toString(32).getBytes();
            byte[] hash = digest.digest(bytes);
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
