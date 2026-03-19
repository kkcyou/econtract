package com.yaoan.framework.banner.core;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


@Component
@Log4j2
//初始化顺序改为第一
@Order(1)
public class LicenseValidator implements CommandLineRunner {

    @Value("${validate.license}")
    private String validate;

    @Override
    public void run(String... args) {
        log.info("=== 应用启动时License校验 ===");

        boolean isValid = checkLicense(validate);
        if (!isValid) {
            log.error("License校验失败");
        }
        log.info("License校验成功，应用继续运行");
    }


    public static boolean checkLicense(String str) {
        if (str != null && !str.trim().isEmpty()) {
            String dataAndMac;
            try {
                dataAndMac = decryptPassword(str);
            } catch (Exception var7) {
                log.info("validate为{}", new Object[]{str});
                log.info("--->validate解析错误");
                return false;
            }
            String[] split = dataAndMac.split(",");
            if (split.length != 2) {
                return false;
            } else {
                List<String> macList = getMacList();
                LocalDate validateDate = LocalDate.parse(split[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                boolean macCheck = macList.contains(split[1]);
                if (!macCheck) {
                    log.info("MAC地址验证结果为{}", new Object[]{macCheck});
                }

                boolean timeCheck = LocalDate.now().isBefore(validateDate);
                if (!timeCheck) {
                    log.info("有效期验证结果为{}", new Object[]{timeCheck});
                }

                return macCheck && timeCheck;
            }
        } else {
            if (ObjectUtil.isEmpty(str)) {
                log.info("----------------------------------------no validate date------------------------------------");
            }

            return false;
        }
    }


    public static final String decryptPassword(String encrypted) {
        if (encrypted == null) {
            return "";
        } else if (encrypted.isEmpty()) {
            return "";
        } else {
            BigInteger bi_confuse = new BigInteger("0933910847463829827159347601486730416058");
            BigInteger bi_r1 = new BigInteger(encrypted, 16);
            BigInteger bi_r0 = bi_r1.xor(bi_confuse);
            return new String(bi_r0.toByteArray());
        }
    }


    public static List<String> getMacList() {
        ArrayList<String> list = new ArrayList<>();

        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface anInterface = (NetworkInterface) networkInterfaces.nextElement();
                byte[] hardwareAddress = anInterface.getHardwareAddress();
                if (hardwareAddress != null) {
                    list.add(getMac(hardwareAddress));
                }
            }

            return list;
        } catch (SocketException var4) {
            return list;
        }
    }

    private static String getMac(byte[] hardwareAddress) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < hardwareAddress.length; ++i) {
            if (i != 0) {
                sb.append("-");
            }

            String s = Integer.toHexString(hardwareAddress[i] & 255);
            sb.append(s.length() == 1 ? 0 + s : s);
        }

        return sb.toString().toUpperCase();
    }
}
