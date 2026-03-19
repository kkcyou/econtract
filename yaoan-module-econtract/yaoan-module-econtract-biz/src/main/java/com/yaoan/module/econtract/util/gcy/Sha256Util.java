package com.yaoan.module.econtract.util.gcy;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

/**
 * @author
 * @create2020-03-1013:57
 **/
@Slf4j
public class Sha256Util {
    public static String encodeBySHA256(String str) {

        if (str == null) {

            return null;
        }

        try {

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.reset();
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());

        } catch (Exception e) {

            throw new RuntimeException(e);
        }

    }

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Takestherawbytesfromthedigestandformatsthemcorrect.
     *
     * @parambytestherawbytesfromthedigest.
     * @returntheformattedbytes.
     */
    private static String getFormattedText(byte[] bytes) {

        int len = bytes.length;

        StringBuilder buf = new StringBuilder(len * 2);

//把密文转换成十六进制的字符串形式

        for (int j = 0; j < len; j++) {

            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);

            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);

        }

        return buf.toString();
    }
    public static void main(String [] args){
        String a = "测试";
        log.info(encodeBySHA256(a));
    }
}
