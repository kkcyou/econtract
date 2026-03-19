package com.yaoan.module.system.util;

import java.io.FileWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class CreateLicense {
    public static void main(String[] args) throws Exception {
        //根据 Mac 地址生成License
        byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        StringBuilder macAddress = new StringBuilder();
        if (mac == null) {
            byte[] macFrom = getMacByList();
            for (byte b : macFrom) {
                macAddress.append(String.format("%02X-", b));
            }
        } else {
            for (byte b : mac) {
                macAddress.append(String.format("%02X-", b));
            }

        }
        // 移除最后一个-
        macAddress = new StringBuilder(macAddress.substring(0, macAddress.length() - 1));
        System.out.println(macAddress);
        String licenseStr = printLicense("2025-12-30", new String[]{macAddress.toString()});
        System.out.println(licenseStr);

        // 将许可证写入文件
        try (FileWriter writer = new FileWriter("ecms-license.txt")) {
            writer.write(licenseStr);
        }
    }

    /**
     * 生成licese方法
     *
     * @param expireDate 超时日期
     * @param macStrs    网卡MAC
     */
    public static String printLicense(String expireDate, String[] macStrs) {
        List<String> licenseList = new ArrayList<>();
        Arrays.asList(macStrs).forEach((mac) -> {
            String macAddress = mac.toUpperCase().replace(":", "-");
            String license = encryptPassword(String.join(",", expireDate, macAddress));
            licenseList.add(license);
            System.out.println("=====================================License授权信息============================================");
            System.out.println("项目：XXX项目");
            System.out.println("授权机器MAC地址：" + macAddress);
            System.out.println("授权失效日期：" + expireDate);
            System.out.println("License授权码：" + license);
            System.out.println("授权码设置说明：修改application.properties文件中validate配置项值为上边授权码，然后重启服务。");
            System.out.println("==============================================================================================");
        });
        return licenseList.get(0);
    }


    public static String encryptPassword(String password) {
        if (password == null) {
            return "";
        } else if (password.isEmpty()) {
            return "";
        } else {
            BigInteger bi_passwd = new BigInteger(password.getBytes());
            BigInteger bi_r0 = new BigInteger("0933910847463829827159347601486730416058");
            BigInteger bi_r1 = bi_r0.xor(bi_passwd);
            return bi_r1.toString(16);
        }
    }


    public static byte[] getMacByList() {
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface anInterface = (NetworkInterface) networkInterfaces.nextElement();
                if ("ens5".equals(anInterface.getName())) {
                    return anInterface.getHardwareAddress();
                }
            }

            return null;
        } catch (SocketException var4) {
            return null;
        }
    }
}
