package com.yaoan.module.system.service.auth.utils;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
@Slf4j
public class Sm4Utils {
    public static final String ALGORIGTHM_NAME = "SM4";
    public static final String ALGORITHM_NAME_ECB_PADDING =
            "SM4/ECB/PKCS7Padding";
    public static final String ENCODING = "UTF-8";
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    /**
     * @Description:生成ecb暗号
     */
    private static Cipher generateEcbCipher(String algorithmName, int mode,
                                            byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException,
            NoSuchProviderException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(algorithmName,
                BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORIGTHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }
    /**
     * @Description:加密
     */
    public static String encryptEcb(String hexKey, String paramStr, String
            charset) throws Exception {
        String cipherText = "";
        if (null != paramStr && !"".equals(paramStr)) {
            byte[] keyData = ByteUtils.fromHexString(hexKey);
            charset = charset.trim();
            if (charset.length() <= 0) {
                charset = ENCODING;
            }
            byte[] srcData = paramStr.getBytes(charset);
            byte[] cipherArray = encrypt_Ecb_Padding(keyData, srcData);
            cipherText = ByteUtils.toHexString(cipherArray);
        }
        return cipherText;
    }
    /**
     * @Description:加密模式之ecb
     */
    public static byte[] encrypt_Ecb_Padding(byte[] key, byte[] data) throws
            NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING,
                Cipher.ENCRYPT_MODE, key);
        byte[] bs = cipher.doFinal(data);
        return bs;
    }
    /**
     * @Description:sm4解密
     */
    public static String decryptEcb(String hexKey, String cipherText, String
            charset) throws Exception {
        String decryptStr = "";
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        byte[] srcData = decrypt_Ecb_Padding(keyData, cipherData);
        charset = charset.trim();
        if (charset.length() <= 0) {
            charset = ENCODING;
        }
        decryptStr = new String(srcData, charset);
        return decryptStr;
    }
    /**
     * @Description:解密
     */
    public static byte[] decrypt_Ecb_Padding(byte[] key, byte[] cipherText)
            throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING,
                Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }
    /**
     * @Description:测试类
     */
    public static void main(String[] args) {
        try {
            String json = "{\"name\":\"color\",\"sex\":\"man\"}";
// 自定义的32位16进制密钥
            String key = "42503218C1C2954FFD370CE4205B1B62";
//加密
            String cipher = Sm4Utils.encryptEcb(key, json, ENCODING);
            log.info(cipher);
//解密
            json = Sm4Utils.decryptEcb(key, cipher, ENCODING);
            log.info(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}